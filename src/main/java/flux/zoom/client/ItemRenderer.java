package flux.zoom.client;

import flux.zoom.FluxZoom;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;

import org.lwjgl.opengl.GL11;

import flux.zoom.ItemBinoculars;
import flux.zoom.ItemExplorersScope;
import flux.zoom.ItemSpyglass;
import flux.zoom.client.model.ModelBinoculars;
import flux.zoom.client.model.ModelExplorersScope;
import flux.zoom.client.model.ModelSpyGlass;

public class ItemRenderer implements IItemRenderer {
    
    private static final Minecraft mc = Minecraft.getMinecraft();
    private final ModelBinoculars binoculars = new ModelBinoculars();
    private final ModelSpyGlass spyglass = new ModelSpyGlass();
    private final ModelExplorersScope explorersScope = new ModelExplorersScope();
    
    @Override
    public boolean handleRenderType(ItemStack item, ItemRenderType type) {
        if (item != null && item.getItem() instanceof ItemExplorersScope
                && (type == ItemRenderType.INVENTORY || type == ItemRenderType.ENTITY)) {
            return false;
        }
        return true;
    }
    
    @Override
    public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
        return true;
    }
    
    @Override
    public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
        final boolean renderSpyglass = item != null && item.getItem() instanceof ItemSpyglass;
        final boolean renderExplorersScope = item != null && item.getItem() instanceof ItemExplorersScope;
        final boolean renderBinoculars = item != null && item.getItem() instanceof ItemBinoculars;

        if (renderExplorersScope) {
            this.renderExplorersScopeOldHeldPosition(type);
            return;
        }

        if (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }

        if (renderSpyglass) {
            mc.renderEngine.bindTexture(new ResourceLocation(FluxZoom.MODID, "textures/models/spyglass.png"));
        } else if (renderBinoculars) {
            mc.renderEngine.bindTexture(new ResourceLocation(FluxZoom.MODID, "textures/models/binoculars.png"));
        } else {
            // Fallback (should never happen because this renderer is only registered for our items).
            mc.renderEngine.bindTexture(new ResourceLocation(FluxZoom.MODID, "textures/models/binoculars.png"));
        }
        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        GL11.glScalef(0.09F, 0.09F, 0.09F);
        // The spyglass model's geometry is positioned around Y=12..21, so translate it down into view.
        if (renderSpyglass) {
            GL11.glScalef(1.15F, 1.15F, 1.15F);
            GL11.glTranslatef(2.25F, -16F, -1.8F);
            GL11.glRotatef(-115.0F, 0.0F, 1.0F, 0.0F);
        } else {
            GL11.glTranslatef(0.0F, 0.0F, -1.8F);
        }
        
        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(3.0F, -7.0F, -0.0F);
            if (renderSpyglass) {
                GL11.glRotatef(30F, 0.0F, 1.0F, 0.0F);
               GL11.glRotatef(16.5F, 0.0F, 0.0F, 1.0F);
               // GL11.glRotatef(2.5F, 0.0F, 0.0F, 1.0F);
               GL11.glTranslatef(3.13F, 0.0F, -2F);
            }
        }
        
        if (renderSpyglass) {
            this.spyglass.render(1.0F);
        } else {
            this.binoculars.render(1.0F);
        }
    }

    private void renderExplorersScopeOldHeldPosition(ItemRenderType type) {
        mc.renderEngine.bindTexture(new ResourceLocation(FluxZoom.MODID, "textures/models/explorers_scope.png"));

        if (type == ItemRenderType.EQUIPPED || type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glTranslatef(0.5F, 0.5F, 0.5F);
        }

        GL11.glRotatef(180F, 0.0F, 0.0F, 1.0F);
        GL11.glScalef(0.09F, 0.09F, 0.09F);
        GL11.glScalef(1.25F, 1.25F, 1.25F);
        GL11.glTranslatef(0.0F, 0.0F, -2.0F);
        GL11.glRotatef(-90.0F, 0.0F, 1.0F, 0.0F);

        if (type == ItemRenderType.EQUIPPED_FIRST_PERSON) {
            GL11.glRotatef(45F, 0.0F, 1.0F, 0.0F);
            GL11.glTranslatef(3.0F, -7.0F, -0.0F);
            GL11.glRotatef(22.5F, 0.0F, 0.0F, 1.0F);
            GL11.glTranslatef(0.0F, 1.5F, -1.5F);
        }

        GL11.glRotatef(184.56F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(-4.56F, 1.0F, 0.0F, 0.0F);
        GL11.glTranslatef(-2.22F, 1.11F, -0.6F);
        this.explorersScope.render(1.0F);
    }
    
}
