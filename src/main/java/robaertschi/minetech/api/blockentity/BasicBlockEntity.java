package robaertschi.minetech.api.blockentity;

import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import robaertschi.minetech.api.util.Tier;
import robaertschi.minetech.api.util.WrappedHandler;

import javax.annotation.ParametersAreNonnullByDefault;
import java.util.Map;

/**
 * @author RoBaertschi
 * @since 0.1.0-1.19.4
 * @version 2
 */

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@FieldsAreNonnullByDefault
public abstract class BasicBlockEntity extends BlockEntity implements MenuProvider {
    public Tier tier;

    public BasicBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state,
                     Tier tier
                     ) {
        super(type, pos, state);
        this.tier = tier;
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        return super.getCapability(cap, side);
    }

    public void basicSetHandler(ItemStackHandler itemHandler, ItemStackHandler thisHandler) {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            thisHandler.setStackInSlot(i, itemHandler.getStackInSlot(i));
        }
    }


}
