package crazypants.enderio.autosave.handlers.endercore;

import java.lang.reflect.Field;
import java.util.Set;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

import info.loenwind.autosave.util.NBTAction;
import com.enderio.core.common.util.UserIdent;

import info.loenwind.autosave.Registry;
import info.loenwind.autosave.exceptions.NoHandlerFoundException;
import info.loenwind.autosave.handlers.IHandler;
import net.minecraft.nbt.NBTTagCompound;

public class HandleUserIdent implements IHandler<UserIdent> {

  public HandleUserIdent() {
  }

  @Override
  public Class<?> getRootType() {
    return UserIdent.class;
  }

  @Override
  public boolean store(@Nonnull Registry registry, @Nonnull Set<NBTAction> phase, @Nonnull NBTTagCompound nbt, @Nonnull String name, @Nonnull UserIdent object)
      throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    object.saveToNbt(nbt, name);
    return true;
  }

  @Override
  public UserIdent read(@Nonnull Registry registry, @Nonnull Set<NBTAction> phase, @Nonnull NBTTagCompound nbt, @Nullable Field field, @Nonnull String name,
      @Nullable UserIdent object) throws IllegalArgumentException, IllegalAccessException, InstantiationException, NoHandlerFoundException {
    return UserIdent.readfromNbt(nbt, name);
  }
}
