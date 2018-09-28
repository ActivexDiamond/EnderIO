package crazypants.enderio.conduits.conduit.redstone;

import java.util.List;

import javax.annotation.Nonnull;

import com.enderio.core.client.render.ColorUtil;
import com.enderio.core.common.util.DyeColor;

import crazypants.enderio.base.conduit.ConnectionMode;
import crazypants.enderio.base.conduit.IClientConduit;
import crazypants.enderio.base.conduit.IConduit;
import crazypants.enderio.base.conduit.IConduitBundle;
import crazypants.enderio.base.conduit.geom.CollidableComponent;
import crazypants.enderio.base.conduit.geom.Offset;
import crazypants.enderio.conduits.geom.ConnectionModeGeometry;
import crazypants.enderio.conduits.render.DefaultConduitRenderer;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;

public class InsulatedRedstoneConduitRenderer extends DefaultConduitRenderer {

  @Override
  public boolean isRendererForConduit(@Nonnull IConduit conduit) {
    return conduit instanceof IRedstoneConduit;
  }

  @Override
  protected void addConduitQuads(@Nonnull IConduitBundle bundle, @Nonnull IClientConduit conduit, @Nonnull TextureAtlasSprite tex,
      @Nonnull CollidableComponent component, float selfIllum, BlockRenderLayer layer, @Nonnull List<BakedQuad> quads) {
    super.addConduitQuads(bundle, conduit, tex, component, selfIllum, layer, quads);

    if (layer == null || component.isCore()) {
      return;
    }

    EnumFacing dir = component.getDirection();
    if (!conduit.getExternalConnections().contains(dir)) {
      return;
    }

    IRedstoneConduit pc = (IRedstoneConduit) conduit;
    DyeColor inChannel = null;
    DyeColor outChannel = null;
    TextureAtlasSprite inTex = null;
    TextureAtlasSprite outTex = null;
    boolean render = true;
    if (conduit.getConnectionMode(dir) == ConnectionMode.INPUT) {
      inTex = pc.getTextureForOutputMode();
      inChannel = pc.getOutputSignalColor(dir);
    } else if (conduit.getConnectionMode(dir) == ConnectionMode.OUTPUT) {
      outTex = pc.getTextureForInputMode();
      outChannel = pc.getInputSignalColor(dir);
    } else if (conduit.getConnectionMode(dir) == ConnectionMode.IN_OUT) {
      inTex = pc.getTextureForInOutMode(false);
      outTex = pc.getTextureForInOutMode(true);
      inChannel = pc.getOutputSignalColor(dir);
      outChannel = pc.getInputSignalColor(dir);
    } else {
      render = false;
    }

    if (render) {
      Offset offset = bundle.getOffset(IRedstoneConduit.class, dir);
      ConnectionModeGeometry.addModeConnectorQuads(dir, offset, pc.getTextureForInOutBackground(), null, quads);
      if (inChannel != null) {
        ConnectionModeGeometry.addModeConnectorQuads(dir, offset, inTex, ColorUtil.toFloat4(inChannel.getColor()), quads);
      }
      if (outChannel != null) {
        ConnectionModeGeometry.addModeConnectorQuads(dir, offset, outTex, ColorUtil.toFloat4(outChannel.getColor()), quads);
      }
    }
  }
}
