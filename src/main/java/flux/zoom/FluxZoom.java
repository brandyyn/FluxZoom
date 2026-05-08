package flux.zoom;

import net.minecraftforge.oredict.ShapedOreRecipe;
import net.minecraftforge.common.config.Configuration;

import org.apache.logging.log4j.Logger;

import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;

@Mod(modid = FluxZoom.MODID, version = FluxZoom.VERSION)
public class FluxZoom {
    
    public static final String MODID = "fluxzoom";
    public static final String PREFIX = MODID + ".";
    public static final String RESOURCE_PREFIX = MODID + ":";
    public static final String VERSION = "@VERSION@";
    
    @Instance(MODID)
    public static FluxZoom instance;
    @SidedProxy(clientSide = "flux.zoom.client.ClientProxy", serverSide = "flux.zoom.CommonProxy")
    public static CommonProxy proxy;
    public static Logger logger;
    
    public static ItemBinoculars itemBinoculars;
    public static ItemSpyglass itemSpyglass;
    public static ItemGoggles itemGoggles;
    
    @EventHandler
    public static void preInit(FMLPreInitializationEvent evt) {
        logger = evt.getModLog();
        logger.info("Starting FluxZoom");

        FluxZoomConfig.load(new Configuration(evt.getSuggestedConfigurationFile()));
        if (FluxZoomConfig.enableBaublesSupport && Loader.isModLoaded(FluxZoomConfig.BAUBLES_MODID)) {
            BaublesSupport.registerConfiguredSlot();
        }
        
        logger.info("Registering items");
        itemBinoculars = new ItemBinoculars();
        itemSpyglass = new ItemSpyglass();
        itemGoggles = new ItemGoggles();
    }
    
    @EventHandler
    public static void init(FMLInitializationEvent evt) {
        proxy.registerHandlers();
    }
    
    @EventHandler
    public static void postInit(FMLPostInitializationEvent evt) {
        logger.info("Registering recipes");
        GameRegistry.addRecipe(new ShapedOreRecipe(itemBinoculars, new Object[] { "B B", "LEL", "P P", 'B', "blockGlassColorless", 'L', "ingotIron", 'E', "stickWood", 'P', "paneGlassColorless" }));

        // Simple spyglass recipe (kept intentionally close to vanilla materials).
        GameRegistry.addRecipe(new ShapedOreRecipe(itemSpyglass, new Object[] { " G ", " I ", " S ", 'G', "paneGlassColorless", 'I', "ingotIron", 'S', "stickWood" }));
        GameRegistry.addRecipe(new ShapedOreRecipe(itemGoggles, new Object[] { "LGL", "S S", 'G', "paneGlassColorless", 'L', "leather", 'S', "string" }));
    }
    
}
