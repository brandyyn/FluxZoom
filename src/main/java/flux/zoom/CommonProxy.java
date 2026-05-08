package flux.zoom;

import cpw.mods.fml.common.FMLCommonHandler;

public class CommonProxy {
    
    public void registerHandlers() {
        FluxZoom.logger.info("Registering handlers");
        FMLCommonHandler.instance().bus().register(new CommonEventHandler());
    }
    
}
