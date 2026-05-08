package flux.zoom.client.zume;

import flux.zoom.FluxZoomConfig;
import flux.zoom.client.zume.impl.EasingUtil;
import flux.zoom.client.zume.impl.MathUtil;
import flux.zoom.client.zume.impl.ZumeConfig;

/**
 * Zume (archaic) zoom core (logic + math) adapted for FluxZoom.
 *
 * This intentionally mirrors Zume's behavior at its default config values.
 */
public final class ZumeEngine {

    private ZumeEngine() {}

    // Public state
    public static IZumeImplementation implementation;
    public static ZumeConfig config;
    public static boolean disabled = false;

    // Internal state (matches Zume)
    private static final EasedDouble zoom = new EasedDouble(1D);
    private static int scrollDelta = 0;
    private static boolean wasHeld = false;
    private static boolean zooming = false;
    private static boolean wasZooming = false;
    private static long prevRenderTimestamp;

    /** Register implementation & initialize defaults. */
    public static void registerImplementation(final IZumeImplementation impl) {
        if (implementation != null) {
            throw new AssertionError("ZumeEngine already initialized!");
        }
        implementation = impl;
        config = new ZumeConfig();
        config.enableCinematicZoom = FluxZoomConfig.enableMouseSmoothingWhenZooming;
        zoom.update(config.zoomSmoothnessMs, config.animationEasingExponent);
        disabled = config.disable;
    }

    private static double getZoom() {
        return zoom.getEased();
    }

    private static void setZoom(final double targetZoom) {
        zoom.set(MathUtil.clamp(targetZoom, 0D, 1D));
    }

    private static void setZoom(final double fromZoom, final double targetZoom) {
        zoom.set(MathUtil.clamp(fromZoom, 0D, 1D), MathUtil.clamp(targetZoom, 0D, 1D));
    }

    private static double getThirdPersonStartZoom() {
        return EasingUtil.inverseOut(
                config.minThirdPersonZoomDistance,
                config.maxThirdPersonZoomDistance,
                4D,
                config.zoomEasingExponent
        );
    }

    private static void onZoomActivate() {
        if (!FluxZoomConfig.savePunchMouseSmoothingToggle) {
            config.enableCinematicZoom = FluxZoomConfig.enableMouseSmoothingWhenZooming;
        }

        implementation.onZoomActivate();

        if (shouldUseFirstPersonZoom()) {
            setZoom(1D, 1D - config.defaultZoom);
        } else {
            final double from = getThirdPersonStartZoom();
            final double target;

            if (implementation.getCameraPerspective() == CameraPerspective.THIRD_PERSON) {
                target = EasingUtil.linear(1D, from, config.defaultZoom);
            } else {
                target = EasingUtil.linear(from, 0D, config.defaultZoom);
            }

            setZoom(from, target);
        }
    }

    private static void onZoomDeactivate() {
        if (shouldUseFirstPersonZoom()) {
            setZoom(1D);
        } else {
            setZoom(getThirdPersonStartZoom());
        }
    }

    public static double fovHook(final double original) {
        return EasingUtil.out(config.minFOV, original, getZoom(), config.zoomEasingExponent);
    }

    public static double thirdPersonCameraHook(final double original) {
        if (shouldUseFirstPersonZoom() || !shouldHook()) {
            return original;
        }

        return original * 0.25D * EasingUtil.out(
                config.minThirdPersonZoomDistance,
                config.maxThirdPersonZoomDistance,
                getZoom(),
                config.zoomEasingExponent
        );
    }

    public static boolean cinematicCameraEnabledHook(final boolean original) {
        if (config.enableCinematicZoom && isActive() && shouldUseFirstPersonZoom()) {
            return true;
        }
        return original;
    }

    public static double mouseSensitivityHook(final double original) {
        if (!isActive() || !shouldUseFirstPersonZoom()) {
            return original;
        }
        return original * EasingUtil.linear(config.mouseSensitivityFloor, 1D, getZoom());
    }

    public static boolean isMouseScrollHookActive() {
        return config.enableZoomScrolling && isActive();
    }

    public static boolean mouseScrollHook(final int wheelDelta) {
        if (!isMouseScrollHookActive() || wheelDelta == 0) {
            return false;
        }
        scrollDelta += MathUtil.sign(wheelDelta);
        return true;
    }

    public static boolean attackMouseHook(final int button, final boolean buttonState) {
        if (!FluxZoomConfig.enablePunchToToggleMouseSmoothing || button != 0 || !buttonState || !isActive()) {
            return false;
        }

        config.enableCinematicZoom = !config.enableCinematicZoom;
        if (FluxZoomConfig.savePunchMouseSmoothingToggle) {
            FluxZoomConfig.setMouseSmoothingWhenZooming(config.enableCinematicZoom);
        }

        if (config.enableCinematicZoom && shouldUseFirstPersonZoom()) {
            implementation.onZoomActivate();
        }
        return true;
    }

    private static boolean getToggleMode() {
        return shouldUseFirstPersonZoom() ? config.toggleMode : config.thirdPersonToggleMode;
    }

    public static boolean isActive() {
        if (disabled || implementation == null) {
            return false;
        }
        return zooming;
    }

    private static boolean shouldHook() {
        if (disabled || implementation == null) {
            return false;
        }
        return isActive() || zoom.isEasing();
    }

    private static boolean shouldUseFirstPersonZoom() {
        return config.maxThirdPersonZoomDistance == 0D ||
                implementation.getCameraPerspective() == CameraPerspective.FIRST_PERSON;
    }

    public static boolean isFOVHookActive() {
        return shouldHook() && shouldUseFirstPersonZoom();
    }

    /** Called every frame from EntityRendererMixin. */
    public static void renderHook() {
        if (disabled || implementation == null) {
            return;
        }

        final long timestamp = System.currentTimeMillis();
        final boolean held = implementation.isZoomPressed();
        final boolean toggleMode = getToggleMode();

        if (toggleMode && held && !wasHeld) {
            zooming = !zooming;
        } else if (!toggleMode) {
            zooming = held;
        }

        if (zooming) {
            if (!wasZooming) {
                onZoomActivate();
            }

            final long timeDelta = timestamp - prevRenderTimestamp;

            if (config.enableZoomScrolling && scrollDelta != 0) {
                setZoom(zoom.getTarget() - (scrollDelta * config.zoomSpeed * 4E-3D));
            } else if (implementation.isZoomInPressed() ^ implementation.isZoomOutPressed()) {
                final double interpolatedIncrement = config.zoomSpeed * 1E-4D * timeDelta;

                if (implementation.isZoomInPressed()) {
                    setZoom(zoom.getTarget() - interpolatedIncrement);
                } else if (implementation.isZoomOutPressed()) {
                    setZoom(zoom.getTarget() + interpolatedIncrement);
                }
            }
        } else if (wasZooming) {
            onZoomDeactivate();
        }

        scrollDelta = 0;
        prevRenderTimestamp = timestamp;
        wasHeld = held;
        wasZooming = zooming;
    }
}
