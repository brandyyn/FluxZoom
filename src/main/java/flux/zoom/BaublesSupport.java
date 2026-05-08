package flux.zoom;

import baubles.api.BaubleType;
import baubles.api.BaublesApi;
import baubles.api.expanded.BaubleExpandedSlots;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;

public final class BaublesSupport {

    private BaublesSupport() {}

    public static void registerConfiguredSlot() {
        registerConfiguredTypes();

        if (FluxZoomConfig.addZoomBaublesSlot) {
            if (BaubleExpandedSlots.tryAssignSlotOfType(FluxZoomConfig.ZOOM_BAUBLE_SLOT_TYPE)) {
                FluxZoom.logger.info("Added FluxZoom Baubles slot of type '{}'", FluxZoomConfig.ZOOM_BAUBLE_SLOT_TYPE);
            } else {
                FluxZoom.logger.warn("Could not add FluxZoom Baubles slot of type '{}'", FluxZoomConfig.ZOOM_BAUBLE_SLOT_TYPE);
            }
        }
    }

    public static String[] getItemBaubleTypes() {
        if (!FluxZoomConfig.enableBaublesSupport) {
            return new String[0];
        }
        return FluxZoomConfig.getBaubleSlotTypes();
    }

    public static BaubleType getFallbackBaubleType() {
        String[] slotTypes = FluxZoomConfig.getBaubleSlotTypes();
        for (String slotType : slotTypes) {
            if ("ring".equals(slotType)) {
                return BaubleType.RING;
            }
            if ("amulet".equals(slotType)) {
                return BaubleType.AMULET;
            }
            if ("belt".equals(slotType)) {
                return BaubleType.BELT;
            }
            if ("universal".equals(slotType)) {
                return BaubleType.UNIVERSAL;
            }
        }
        return BaubleType.UNIVERSAL;
    }

    public static boolean hasZoomItemInBaubles(EntityPlayer player) {
        if (player == null) {
            return false;
        }

        IInventory baubles = BaublesApi.getBaubles(player);
        if (baubles == null) {
            return false;
        }

        for (int slot = 0; slot < baubles.getSizeInventory(); slot++) {
            ItemStack stack = baubles.getStackInSlot(slot);
            if (stack != null && isZoomItem(stack) && isConfiguredZoomSlot(slot)) {
                return true;
            }
        }
        return false;
    }

    private static void registerConfiguredTypes() {
        registerSlotType(FluxZoomConfig.ZOOM_BAUBLE_SLOT_TYPE);

        String[] slotTypes = FluxZoomConfig.getBaubleSlotTypes();
        for (String slotType : slotTypes) {
            registerSlotType(slotType);
        }
    }

    private static void registerSlotType(String slotType) {
        String normalized = FluxZoomConfig.normalizeSlotType(slotType);
        if (normalized.length() > 0 && !BaubleExpandedSlots.isTypeRegistered(normalized)) {
            BaubleExpandedSlots.tryRegisterType(normalized);
        }
    }

    private static boolean isConfiguredZoomSlot(int slot) {
        return FluxZoomConfig.isAllowedBaubleSlotType(BaubleExpandedSlots.getSlotType(slot));
    }

    private static boolean isZoomItem(ItemStack stack) {
        return stack.getItem() instanceof ItemBinoculars || stack.getItem() instanceof ItemSpyglass
                || stack.getItem() instanceof ItemGoggles;
    }
}
