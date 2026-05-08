package flux.zoom;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import net.minecraft.entity.player.EntityPlayerMP;

public class CommonEventHandler {

    @SubscribeEvent
    public void onPlayerLoggedIn(PlayerLoggedInEvent evt) {
        if (evt.player instanceof EntityPlayerMP) {
            FluxZoomNetwork.CHANNEL.sendTo(
                    new SyncZoomConfigMessage(
                            FluxZoomConfig.allowZoomWithoutItem,
                            FluxZoomConfig.enableBaublesSupport,
                            FluxZoomConfig.getBaubleSlotTypes()),
                    (EntityPlayerMP) evt.player);
        }
    }
}
