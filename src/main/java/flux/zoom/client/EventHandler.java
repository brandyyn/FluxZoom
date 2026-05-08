package flux.zoom.client;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import flux.zoom.FluxZoomConfig;
import flux.zoom.client.zume.ZumeEngine;
import flux.zoom.client.zume.ZumeIntegration;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.common.MinecraftForge;

/**
 * Client events for Zume-style zoom.
 */
public class EventHandler {

    public static void init() {
        EventHandler handler = new EventHandler();
        FMLCommonHandler.instance().bus().register(handler);
        MinecraftForge.EVENT_BUS.register(handler);

        // Initialize the exact Zume zoom core with FluxZoom's binocular restriction layer.
        ZumeEngine.registerImplementation(new ZumeIntegration());
    }

    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onMouseScroll(MouseEvent evt) {
        if (ZumeEngine.attackMouseHook(evt.button, evt.buttonstate)) {
            evt.setCanceled(true);
            return;
        }

        if (ZumeEngine.mouseScrollHook(evt.dwheel)) {
            evt.setCanceled(true);
        }
    }

    @SubscribeEvent
    public void onClientConnected(ClientConnectedToServerEvent evt) {
        FluxZoomConfig.resetServerAuthoritativeConfig();
    }

    @SubscribeEvent
    public void onClientDisconnected(ClientDisconnectionFromServerEvent evt) {
        FluxZoomConfig.resetServerAuthoritativeConfig();
    }
}
