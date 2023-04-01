package robaertschi.minetech.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import robaertschi.minetech.api.blockentity.BasicBlockEntity;
import robaertschi.minetech.api.networking.packet.EnergySyncS2CPacket;
import robaertschi.minetech.api.networking.packet.ItemStackSyncS2CPacket;
import robaertschi.minetech.api.util.IEnergyContainer;
import robaertschi.minetech.api.util.IItemContainer;
import robaertschi.minetech.api.util.ModEnergyStorage;
import robaertschi.minetech.api.util.Tier;
import robaertschi.minetech.item.custom.ModularArmorItem;
import robaertschi.minetech.networking.ModMessages;
import robaertschi.minetech.screen.FuelGeneratorMenu;
import robaertschi.minetech.screen.ModularArmorWorkspaceMenu;
import robaertschi.minetech.util.Constants;

public class ModularArmorWorkspaceBlockEntity extends BasicBlockEntity implements IEnergyContainer, IItemContainer {

    private final ItemStackHandler itemHandler = new ItemStackHandler(1 + Constants.MODULAR_ARMOR_UPGRADES) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (!level.isClientSide()) {
                ModMessages.sendToClients(new ItemStackSyncS2CPacket(this, worldPosition));
            }
        }

        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0 -> stack.getItem() instanceof ModularArmorItem;
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    private final ModEnergyStorage ENERGY_STORAGE = new ModEnergyStorage(50000, 128) {
        @Override
        public void onEnergyChanged() {
            setChanged();
            ModMessages.sendToClients(new EnergySyncS2CPacket(energy, worldPosition));
        }
    };

    public static final int BASE_ENERGY_USAGE = 8;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> lazyEnergyStorage = LazyOptional.empty();

    public ModularArmorWorkspaceBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.MODULAR_ARMOR_WORKSPACE.get(), pos, state, Tier.TIER_1);
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatableWithFallback("blockentity.minetech.modular_armor_workspace.name", "Modular Armor Workspace");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player p_39956_) {
        ModMessages.sendToClients(new EnergySyncS2CPacket(this.ENERGY_STORAGE.getEnergyStored(), getBlockPos()));
        return new ModularArmorWorkspaceMenu(id, inv, this);
    }

    @Override
    public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
        if (cap == ForgeCapabilities.ITEM_HANDLER) {
            return lazyItemHandler.cast();
        }

        if (cap == ForgeCapabilities.ENERGY) {
            return lazyEnergyStorage.cast();
        }
        return super.getCapability(cap, side);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyEnergyStorage = LazyOptional.of(() -> ENERGY_STORAGE);
        lazyItemHandler = LazyOptional.of(() -> itemHandler);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyItemHandler.invalidate();
        lazyEnergyStorage.invalidate();
    }

    @Override
    protected void saveAdditional(CompoundTag nbt) {
        nbt.put("inventory", itemHandler.serializeNBT());
        nbt.putInt("modular_armor_workspace.energy", this.ENERGY_STORAGE.getEnergyStored());

        super.saveAdditional(nbt);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);

        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        ENERGY_STORAGE.setEnergy(nbt.getInt("modular_armor_workspace.energy"));
    }

    @Override
    public IEnergyStorage getEnergyStorage() {
        return ENERGY_STORAGE;
    }

    @Override
    public LazyOptional<IEnergyStorage> getLazyEnergyStorage() {
        return lazyEnergyStorage;
    }

    @Override
    public void setEnergyLevel(int energy) {
        ENERGY_STORAGE.setEnergy(energy);
    }

    @Override
    public ItemStackHandler getItemHandler() {
        return itemHandler;
    }

    @Override
    public IItemHandler getLazyItemHandler() {
        return itemHandler;
    }

    @Override
    public void setHandler(ItemStackHandler itemStackHandler) {
        basicSetHandler(itemStackHandler, itemHandler);
    }

    public static void tick(Level level, BlockPos pos, BlockState state, ModularArmorWorkspaceBlockEntity blockEntity) {
        if (!blockEntity.itemHandler.getStackInSlot(0).isEmpty()) {

            var stack = blockEntity.itemHandler.getStackInSlot(0);
            stack.getCapability(ForgeCapabilities.ENERGY).ifPresent(iEnergyStorage -> {
                blockEntity.ENERGY_STORAGE.extractEnergy(iEnergyStorage.receiveEnergy(blockEntity.tier.getBaseEnergyMultiplier() * 8, false), false);
            });
        }
    }
}