package flux.zoom.client;

import flux.zoom.FluxZoom;
import net.minecraft.client.settings.KeyBinding;
import org.lwjgl.input.Keyboard;
import cpw.mods.fml.client.registry.ClientRegistry;

/**
 * Keybinds mirroring Zume archaic defaults.
 */
public class KeyHandler {

    public static final String CATEGORY = "FluxZoom";

    public static KeyBinding keyZoom;
    public static KeyBinding keyZoomIn;
    public static KeyBinding keyZoomOut;

    public static void init() {
        // Match Zume defaults: Z / = / -
        keyZoom = new KeyBinding(FluxZoom.PREFIX + "keybind.zoom", Keyboard.KEY_Z, CATEGORY);
        keyZoomIn = new KeyBinding(FluxZoom.PREFIX + "keybind.zoom_in", Keyboard.KEY_EQUALS, CATEGORY);
        keyZoomOut = new KeyBinding(FluxZoom.PREFIX + "keybind.zoom_out", Keyboard.KEY_MINUS, CATEGORY);

        ClientRegistry.registerKeyBinding(keyZoom);
        ClientRegistry.registerKeyBinding(keyZoomIn);
        ClientRegistry.registerKeyBinding(keyZoomOut);
    }
}
