package flux.zoom;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.simpleimpl.IMessage;
import cpw.mods.fml.common.network.simpleimpl.IMessageHandler;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import io.netty.buffer.ByteBuf;

public class SyncZoomConfigMessage implements IMessage {

    private boolean allowZoomWithoutItem;
    private boolean enableBaublesSupport;
    private String[] baubleSlotTypes;

    public SyncZoomConfigMessage() {}

    public SyncZoomConfigMessage(boolean allowZoomWithoutItem, boolean enableBaublesSupport, String[] baubleSlotTypes) {
        this.allowZoomWithoutItem = allowZoomWithoutItem;
        this.enableBaublesSupport = enableBaublesSupport;
        this.baubleSlotTypes = baubleSlotTypes;
    }

    @Override
    public void fromBytes(ByteBuf buf) {
        this.allowZoomWithoutItem = buf.readBoolean();
        this.enableBaublesSupport = buf.readBoolean();
        int slotTypeCount = buf.readByte() & 255;
        this.baubleSlotTypes = new String[slotTypeCount];
        for (int i = 0; i < slotTypeCount; i++) {
            this.baubleSlotTypes[i] = ByteBufUtils.readUTF8String(buf);
        }
    }

    @Override
    public void toBytes(ByteBuf buf) {
        buf.writeBoolean(this.allowZoomWithoutItem);
        buf.writeBoolean(this.enableBaublesSupport);
        int slotTypeCount = this.baubleSlotTypes == null ? 0 : Math.min(this.baubleSlotTypes.length, 255);
        buf.writeByte(slotTypeCount);
        for (int i = 0; i < slotTypeCount; i++) {
            ByteBufUtils.writeUTF8String(buf, this.baubleSlotTypes[i]);
        }
    }

    public static class Handler implements IMessageHandler<SyncZoomConfigMessage, IMessage> {

        @Override
        public IMessage onMessage(SyncZoomConfigMessage message, MessageContext ctx) {
            FluxZoomConfig.setServerAllowZoomWithoutItem(message.allowZoomWithoutItem);
            FluxZoomConfig.setServerBaublesConfig(message.enableBaublesSupport, message.baubleSlotTypes);
            return null;
        }
    }
}
