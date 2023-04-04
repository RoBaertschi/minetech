package robaertschi.minetech.block.custom;

import net.minecraft.client.gui.components.AccessibilityOnboardingTextWidget;
import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Containers;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParam;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import robaertschi.minetech.Minetech;
import robaertschi.minetech.api.block.BasicBlockWithEntity;
import robaertschi.minetech.api.capabilities.CapabilityNotPresentException;
import robaertschi.minetech.api.util.Tier;
import robaertschi.minetech.blockentity.FuelGeneratorBlockEntity;
import robaertschi.minetech.blockentity.ModBlockEntities;
import robaertschi.minetech.blockentity.ModularArmorWorkspaceBlockEntity;
import robaertschi.minetech.item.ModItems;

import java.util.List;
import java.util.stream.Stream;

public class ModularArmorWorkspaceBlock extends BasicBlockWithEntity {
    public ModularArmorWorkspaceBlock(Properties properties) {
        super(properties, Block.box(0, 0, 0, 16, 16, 16));
    }

    @Override
    public InteractionResult use(BlockState state, Level level, BlockPos pos, Player player, InteractionHand hand, BlockHitResult blockHitResult) {
        if (!level.isClientSide()) {
            BlockEntity entity = level.getBlockEntity(pos);
            if (entity instanceof ModularArmorWorkspaceBlockEntity) {

                if (player.getItemInHand(hand).getItem() == ModItems.TIER_2_UPGRADE.get() && Tier.isTierHigher(((ModularArmorWorkspaceBlockEntity) entity).tier, Tier.TIER_2)) {
                    player.getItemInHand(hand).shrink(1);
                    ((ModularArmorWorkspaceBlockEntity) entity).tier = Tier.TIER_2;
                    return InteractionResult.CONSUME;
                }

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
