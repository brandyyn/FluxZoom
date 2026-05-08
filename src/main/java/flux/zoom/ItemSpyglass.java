package flux.zoom;

import java.util.List;

import baubles.api.BaubleType;
import baubles.api.IBauble;
import baubles.api.expanded.IBaubleExpanded;
import cpw.mods.fml.common.Optional;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import flux.zoom.client.KeyHandler;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;

/**
 * Spyglass item that behaves exactly like the binoculars (same zoom rules + keybind support).
 */
@Optional.InterfaceList({
        @Optional.Interface(iface = "baubles.api.IBauble", modid = BaublesSupport.MODID),
        @Optional.Interface(iface = "baubles.api.expanded.IBaubleExpanded", modid = BaublesSupport.MODID)
})
public class ItemSpyglass extends Item implements IBauble, IBaubleExpanded {

    public ItemSpyglass() {
        this.setMaxStackSize(1);
        this.setUnlocalizedName(FluxZoom.PREFIX + "spyglass");
        this.setTextureName(FluxZoom.RESOURCE_PREFIX + "spyglass");
        this.setCreativeTab(CreativeTabs.tabTools);

        GameRegistry.registerItem(this, "spyglass");
    }

    @Override
    public ItemStack onItemRightClick(ItemStack itemStack, World world, EntityPlayer player) {
        // Do NOT call setItemInUse() to avoid vanilla "item use" slow-down.
        // Zoom is handled client-side (key state + held item) in the ZumeIntegration.
        return itemStack;
    }

    @Override
    public int getMaxItemUseDuration(ItemStack itemStack) {
        return Integer.MAX_VALUE;
    }

    @Override
    @SideOnly(Side.CLIENT)
    @SuppressWarnings({ "rawtypes", "unchecked" })
    public void addInformation(ItemStack itemStack, EntityPlayer player, List list, boolean advanced) {
        list.add(StatCollector.translateToLocal("item.fluxzoom.spyglass.desc.1"));
        if (KeyHandler.keyZoom != null && KeyHandler.keyZoom.getKeyCode() != 0) {
            list.add(StatCollector.translateToLocalFormatted(
                    "item.fluxzoom.spyglass.desc.2",
                    EnumChatFormatting.AQUA + GameSettings.getKeyDisplayString(KeyHandler.keyZoom.getKeyCode())
                            + EnumChatFormatting.GRAY));
        }
    }

    @Override
    @Optional.Method(modid = BaublesSupport.MODID)
    public BaubleType getBaubleType(ItemStack itemStack) {
        return BaublesSupport.getFallbackBaubleType();
    }

    @Override
    @Optional.Method(modid = BaublesSupport.MODID)
    public String[] getBaubleTypes(ItemStack itemStack) {
        return BaublesSupport.getItemBaubleTypes();
    }

    @Override
    @Optional.Method(modid = BaublesSupport.MODID)
    public void onWornTick(ItemStack itemStack, EntityLivingBase entity) {}

    @Override
    @Optional.Method(modid = BaublesSupport.MODID)
    public void onEquipped(ItemStack itemStack, EntityLivingBase entity) {}

    @Override
    @Optional.Method(modid = BaublesSupport.MODID)
    public void onUnequipped(ItemStack itemStack, EntityLivingBase entity) {}

    @Override
    @Optional.Method(modid = BaublesSupport.MODID)
    public boolean canEquip(ItemStack itemStack, EntityLivingBase entity) {
        return true;
    }

    @Override
    @Optional.Method(modid = BaublesSupport.MODID)
    public boolean canUnequip(ItemStack itemStack, EntityLivingBase entity) {
        return true;
    }

    @Override
    @Optional.Method(modid = BaublesSupport.MODID)
    public void onPlayerLoad(ItemStack itemStack, EntityLivingBase entity) {}
}
