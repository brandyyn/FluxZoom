package flux.zoom.client;

import flux.zoom.CommonProxy;
import flux.zoom.FluxZoom;
import flux.zoom.client.model.ModelPlayerCustom;
import net.minecraftforge.client.MinecraftForgeClient;
import api.player.model.ModelPlayerAPI;
import cpw.mods.fml.common.Loader;

public class ClientProxy extends CommonProxy {
    
    @Override
    public void registerHandlers() {
        super.registerHandlers();
        EventHandler.init();
        KeyHandler.init();
        ItemRenderer renderer = new ItemRenderer();
        MinecraftForgeClient.registerItemRenderer(FluxZoom.itemBinoculars, renderer);
        if (FluxZoom.itemSpyglass != null) {
            MinecraftForgeClient.registerItemRenderer(FluxZoom.itemSpyglass, renderer);
        }
        if (FluxZoom.itemExplorersScope != null) {
            MinecraftForgeClient.registerItemRenderer(FluxZoom.itemExplorersScope, renderer);
        }
        
        if (Loader.isModLoaded("RenderPlayerAPI")) {
            ModelPlayerAPI.register(FluxZoom.MODID, ModelPlayerCustom.class);
        }
    }
    
}
