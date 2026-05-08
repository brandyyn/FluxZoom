package flux.zoom;

import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import cpw.mods.fml.relauncher.Side;

public final class FluxZoomNetwork {

    public static final SimpleNetworkWrapper CHANNEL = NetworkRegistry.INSTANCE.newSimpleChannel(FluxZoom.MODID);

    private FluxZoomNetwork() {}

    public static void init() {
        CHANNEL.registerMessage(SyncZoomConfigMessage.Handler.class, SyncZoomConfigMessage.class, 0, Side.CLIENT);
    }
}
