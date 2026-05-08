package flux.zoom;

import java.util.Locale;

import net.minecraftforge.common.config.Configuration;

public final class FluxZoomConfig {

    public static final String BAUBLES_MODID = "Baubles|Expanded";
    public static final String ZOOM_BAUBLE_SLOT_TYPE = "zoom";

    private static final String CATEGORY_GENERAL = "general";
    private static final String CATEGORY_BAUBLES = "baubles";

    public static boolean allowZoomWithoutItem = false;
    public static boolean enableMouseSmoothingWhenZooming = true;
    public static boolean enablePunchToToggleMouseSmoothing = false;
    public static boolean savePunchMouseSmoothingToggle = false;
    public static boolean enableBaublesSupport = true;
    public static boolean addZoomBaublesSlot = true;
    private static String[] baubleSlotTypes = new String[] { ZOOM_BAUBLE_SLOT_TYPE };
    private static Configuration loadedConfig;

    private FluxZoomConfig() {}

    public static void load(Configuration config) {
        config.load();
        loadedConfig = config;

        allowZoomWithoutItem = config.getBoolean(
                "allowZoomWithoutItem",
                CATEGORY_GENERAL,
                allowZoomWithoutItem,
                "Allows the zoom keybind to zoom without requiring a FluxZoom item in the player's inventory.");

        enableMouseSmoothingWhenZooming = config.getBoolean(
                "enableMouseSmoothingWhenZooming",
                CATEGORY_GENERAL,
                enableMouseSmoothingWhenZooming,
                "Enables cinematic mouse smoothing while zooming.");

        enablePunchToToggleMouseSmoothing = config.getBoolean(
                "enablePunchToToggleMouseSmoothing",
                CATEGORY_GENERAL,
                enablePunchToToggleMouseSmoothing,
                "Allows left-click/punch while zooming to toggle cinematic mouse smoothing on or off. "
                        + "The punch click is consumed when it toggles smoothing.");

        savePunchMouseSmoothingToggle = config.getBoolean(
                "savePunchMouseSmoothingToggle",
                CATEGORY_GENERAL,
                savePunchMouseSmoothingToggle,
                "Saves the punch smoothing toggle between zooms and game sessions. "
                        + "When disabled, each new zoom starts from enableMouseSmoothingWhenZooming.");

        enableBaublesSupport = config.getBoolean(
                "enableBaublesSupport",
                CATEGORY_BAUBLES,
                enableBaublesSupport,
                "Enables FluxZoom integration with Baubles Expanded when Baubles Expanded is installed.");

        addZoomBaublesSlot = config.getBoolean(
                "addZoomBaublesSlot",
                CATEGORY_BAUBLES,
                addZoomBaublesSlot,
                "Adds one extra Baubles Expanded slot of type 'zoom' for FluxZoom items.");

        baubleSlotTypes = normalizeSlotTypes(config.getStringList(
                "baubleSlotTypes",
                CATEGORY_BAUBLES,
                baubleSlotTypes,
                "Baubles Expanded slot types that FluxZoom items can equip into and zoom from. "
                        + "Use 'universal' to allow any Baubles slot."));

        if (baubleSlotTypes.length == 0) {
            baubleSlotTypes = new String[] { ZOOM_BAUBLE_SLOT_TYPE };
        }

        if (config.hasChanged()) {
            config.save();
        }
    }

    public static String[] getBaubleSlotTypes() {
        String[] copy = new String[baubleSlotTypes.length];
        System.arraycopy(baubleSlotTypes, 0, copy, 0, baubleSlotTypes.length);
        return copy;
    }

    public static void setMouseSmoothingWhenZooming(boolean enabled) {
        enableMouseSmoothingWhenZooming = enabled;
        if (loadedConfig != null) {
            loadedConfig.get(CATEGORY_GENERAL, "enableMouseSmoothingWhenZooming", enabled).set(enabled);
            loadedConfig.save();
        }
    }

    public static boolean isAllowedBaubleSlotType(String slotType) {
        if (slotType == null) {
            return false;
        }

        String normalized = normalizeSlotType(slotType);
        for (String allowedType : baubleSlotTypes) {
            if ("universal".equals(allowedType) || allowedType.equals(normalized)) {
                return true;
            }
        }
        return false;
    }

    public static String normalizeSlotType(String slotType) {
        if (slotType == null) {
            return "";
        }
        return slotType.trim().toLowerCase(Locale.ENGLISH);
    }

    private static String[] normalizeSlotTypes(String[] slotTypes) {
        if (slotTypes == null) {
            return new String[0];
        }

        String[] normalized = new String[slotTypes.length];
        int count = 0;
        for (String slotType : slotTypes) {
            String value = normalizeSlotType(slotType);
            if (value.length() > 0 && !contains(normalized, count, value)) {
                normalized[count++] = value;
            }
        }

        String[] result = new String[count];
        System.arraycopy(normalized, 0, result, 0, count);
        return result;
    }

    private static boolean contains(String[] values, int length, String needle) {
        for (int i = 0; i < length; i++) {
            if (values[i].equals(needle)) {
                return true;
            }
        }
        return false;
    }
}
