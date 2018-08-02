package crazypants.enderio.base.capacitor;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import crazypants.enderio.api.capacitor.Scaler;
import net.minecraft.util.math.MathHelper;

/**
 * This factory allows us to write scalers to the config file. All scalers used within Ender IO must be supported by the factors (i.e. either be an Enum
 * constant or an IndexedScaler), but addons may use their own scalers.
 *
 */
public enum ScalerFactory implements Scaler {
  INVALID(new Scaler() { // 0-0-0-...
    @Override
    public float scaleValue(float idx) {
      return 0;
    }
  }),
  IDENTITY(new Scaler() { // 1-2-3-...
    @Override
    public float scaleValue(float idx) {
      return Math.max(idx, 0);
    }
  }),
  LINEAR_0_8(new Scaler() { // 1-2-3-4-5-6-7-8-8-8
    @Override
    public float scaleValue(float idx) {
      return MathHelper.clamp(idx, 0, 8);
    }
  }),
  QUADRATIC(new Scaler() { // 1-2-4-8-16-...
    @Override
    public float scaleValue(float idx) {
      return (float) Math.pow(2, idx - 1);
    }
  }),
  QUADRATIC_1_8(new Scaler() { // 1-2-4-8-8-8
    @Override
    public float scaleValue(float idx) {
      return (float) MathHelper.clamp(Math.pow(2, idx - 1), 1, 8);
    }
  }),
  CUBIC(new Scaler() { // 1-3-9-...
    @Override
    public float scaleValue(float idx) {
      return (float) Math.pow(3, idx - 1);
    }
  }),
  OCTADIC_1_8(new IndexedScaler(.5f, 0, .5f, 1, 3, 2, 4, 8, 10, 16)),
  POWER(new IndexedScaler(1f, 0, 1, 3, 5, 8, 13, 18)),
  CHARGE(new IndexedScaler(1f, 1000, 100, 60, 20, 10, 1)),
  SPEED(new IndexedScaler(1f, 100, 20, 10, 2, 1)),
  POWER10(new IndexedScaler(1f, 0, 1, 2, 10, 20, 40)),
  RANGE(new IndexedScaler(1f, 0, 4, 6, 10, 17, 24)),
  FIXED(new Scaler() { // 1-1-1
    @Override
    public float scaleValue(float idx) {
      return 1;
    }
  }),
  SPAWNER(new IndexedScaler(1f, 0, 1, 5, 10, 20, 40)),
  BURNTIME(new IndexedScaler(1f, 0.8f, 1f, 1.25f, 1.5f, 1.5f, 2f, 2.5f) {
    @Override
    public float scaleValue(float idx) {
      return super.scaleValue(idx) / 100f; // Convert from percentage
    }
  }),
  CHEMICAL(new Scaler() { // (.75)-1-1.25-1.5-1.75-2...
    @Override
    public float scaleValue(float idx) {
      return 1 + (idx - 1f) * 0.25f;
    }
  }),
  DROPOFF(new IndexedScaler(1f, 1, 1, 4 / 3f, 2, 2.5f, 3f, 3.25f)), // Special case for stirling gen
  CENT(new Scaler() { // 0.01-0.01-0.01 (used for power loss)
    @Override
    public float scaleValue(float idx) {
      return 0.01f;
    }
  }),

  ;

  private final @Nonnull Scaler scaler;

  private ScalerFactory(@Nonnull Scaler scaler) {
    this.scaler = scaler;
  }

  @Override
  public float scaleValue(float idx) {
    return scaler.scaleValue(idx);
  }

  public static @Nullable String toString(@Nonnull Scaler scaler) {
    if (scaler instanceof ScalerFactory) {
      return ((ScalerFactory) scaler).name();
    }
    if (scaler instanceof IndexedScaler) {
      return ((IndexedScaler) scaler).store();
    }
    return null;
  }

  public static @Nullable Scaler fromString(@Nullable String s) {
    if (s == null) {
      return null;
    }
    if (s.startsWith("idx(")) {
      s = s.replace('(', ':').replace(')', ':').replaceAll("::", ":");
    }
    if (s.startsWith("idx:")) {
      try {
        String[] split = s.split(":");
        float scale = 0;
        float[] values = new float[split.length - 2];
        int i = -2;
        for (String sub : split) {
          if (i >= -1) {
            Float value = Float.valueOf(sub);
            if (i == -1) {
              scale = value;
            } else {
              values[i] = value;
            }
          }
          i++;
        }
        return new IndexedScaler(scale, values);
      } catch (NumberFormatException e) {
        return null;
      }
    }

    try {
      return ScalerFactory.valueOf(s);
    } catch (Exception e) {
      return null;
    }
  }

}