package robaertschi.minetech.block.custom;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import robaertschi.minetech.api.block.BasicBlockWithEntity;
import robaertschi.minetech.blockentity.FuelGeneratorBlockEntity;
import robaertschi.minetech.blockentity.ModBlockEntities;
import robaertschi.minetech.blockentity.ModularArmorWorkspaceBlockEntity;

public class ModularArmorWorkspaceBlock extends BasicBlockWithEntity {
    public ModularArmorWorkspaceBlock(Properties properties) {
        super(properties, Block.box(0, 0, 0, 16, 16, 16));
    }

    @Override
    public InteractionResult use(BlockState p_60503_, Level level, BlockPos pos, Player player, InteractionHand p_60507_, BlockHitResult p_60508_) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof ModularArmorWorkspaceBlockEntity) {
                NetworkHooks.openScreen((ServerPlayer) player, (ModularArmorWorkspaceBlockEntity) entity, pos);
            } else {
                throw new IllegalStateException("Our Container provider is missing!");
            }
        }

        return InteractionResult.sidedSuccess(level.isClientSide());
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new ModularArmorWorkspaceBlockEntity(pos, state);
    }

    @Nullable
    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(@NotNull Level level, @NotNull BlockState state,
                                                                  @NotNull BlockEntityType<T> type) {
        return createTickerHelper(type, ModBlockEntities.MODULAR_ARMOR_WORKSPACE.get(),
                ModularArmorWorkspaceBlockEntity::tick);
    }
}
