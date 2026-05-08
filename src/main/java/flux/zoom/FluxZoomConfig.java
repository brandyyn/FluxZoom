package flux.zoom;

import java.util.Locale;

import net.minecraftforge.common.config.Configuration;

public final class FluxZoomConfig {

    private static final String CATEGORY_BAUBLES = "baubles";
    public static final String ZOOM_BAUBLE_SLOT_TYPE = "zoom";

    public static boolean addZoomBaublesSlot = true;
    private static String[] baubleSlotTypes = new String[] { ZOOM_BAUBLE_SLOT_TYPE };

    private FluxZoomConfig() {}

    public static void load(Configuration config) {
        config.load();

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
