package flux.zoom.client.zume;

import cpw.mods.fml.common.Loader;
import flux.zoom.BaublesSupport;
import flux.zoom.FluxZoomConfig;
import flux.zoom.ItemBinoculars;
import flux.zoom.ItemGoggles;
import flux.zoom.ItemSpyglass;
import flux.zoom.client.KeyHandler;
import flux.zoom.client.mixin.EntityRendererAccessor;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MouseFilter;

/**
 * Bridges ZumeEngine to FluxZoom's controls and binocular restrictions.
 */
public final class ZumeIntegration implements IZumeImplementation {

    private final Minecraft mc = Minecraft.getMinecraft();

    @Override
    public boolean isZoomPressed() {
        if (mc.currentScreen != null || mc.thePlayer == null) {
            return false;
        }

        // Keybind zoom: either globally allowed, or gated by inventory/configured Baubles zoom items.
        if (KeyHandler.keyZoom != null && KeyHandler.keyZoom.getIsKeyPressed()
                && (FluxZoomConfig.allowZoomWithoutItem || hasZoomItemInInventory())) {
            return true;
        }

        // Right-click zoom: only if holding a zoom item and holding use-item.
        final ItemStack held = mc.thePlayer.getHeldItem();
        if (held != null && isZoomItem(held)) {
            return mc.gameSettings.keyBindUseItem.getIsKeyPressed();
        }

        return false;
    }

    @Override
    public boolean isZoomInPressed() {
        return mc.currentScreen == null && KeyHandler.keyZoomIn != null && KeyHandler.keyZoomIn.getIsKeyPressed();
    }

    @Override
    public boolean isZoomOutPressed() {
        return mc.currentScreen == null && KeyHandler.keyZoomOut != null && KeyHandler.keyZoomOut.getIsKeyPressed();
    }

    @Override
    public CameraPerspective getCameraPerspective() {
        return CameraPerspective.values()[mc.gameSettings.thirdPersonView];
    }

    @Override
    public void onZoomActivate() {
        if (ZumeEngine.config != null && ZumeEngine.config.enableCinematicZoom && !mc.gameSettings.smoothCamera) {
            final EntityRendererAccessor er = (EntityRendererAccessor) mc.entityRenderer;
            er.setMouseFilterXAxis(new MouseFilter());
            er.setMouseFilterYAxis(new MouseFilter());
            er.setSmoothCamYaw(0F);
            er.setSmoothCamPitch(0F);
            er.setSmoothCamFilterX(0F);
            er.setSmoothCamFilterY(0F);
            er.setSmoothCamPartialTicks(0F);
        }
    }

    private boolean hasZoomItemInInventory() {
        for (ItemStack stack : mc.thePlayer.inventory.mainInventory) {
            if (stack != null && isZoomItem(stack)) {
                return true;
            }
        }
        return FluxZoomConfig.enableBaublesSupport && Loader.isModLoaded(FluxZoomConfig.BAUBLES_MODID)
                && BaublesSupport.hasZoomItemInBaubles(mc.thePlayer);
    }

    private static boolean isZoomItem(ItemStack stack) {
        return stack != null && (stack.getItem() instanceof ItemBinoculars || stack.getItem() instanceof ItemSpyglass
                || stack.getItem() instanceof ItemGoggles);
    }
}
