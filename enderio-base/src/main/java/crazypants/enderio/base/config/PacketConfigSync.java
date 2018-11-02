package crazypants.enderio.base.config;

import io.netty.buffer.ByteBuf;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;

/**
 * Syncs some configs that are only used clientside, but must use the serverside value for balance purposes.
 */
public class PacketConfigSync implements IMessage {

  @Override
  public void toBytes(ByteBuf buf) {
    buf.writeBoolean(Config.travelStaffBlinkEnabled);
    buf.writeBoolean(Config.travelStaffBlinkThroughSolidBlocksEnabled);
    buf.writeBoolean(Config.travelStaffBlinkThroughClearBlocksEnabled);
    buf.writeInt(Config.travelStaffBlinkPauseTicks);
    buf.writeInt(Config.travelStaffMaxBlinkDistance);
  }

  @Override
  public void fromBytes(ByteBuf data) {
    Config.travelStaffBlinkEnabled = data.readBoolean();
    Config.travelStaffBlinkThroughSolidBlocksEnabled = data.readBoolean();
    Config.travelStaffBlinkThroughClearBlocksEnabled = data.readBoolean();
    Config.travelStaffBlinkPauseTicks = data.readInt();
    Config.travelStaffMaxBlinkDistance = data.readInt();
  }

  public static class Handler implements IMessageHandler<PacketConfigSync, IMessage> {

    @Override
    public IMessage onMessage(PacketConfigSync message, MessageContext ctx) {
      return null;
    }

  }

}
