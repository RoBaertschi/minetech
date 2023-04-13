package robaertschi.minetech.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.AbstractFurnaceBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ForgeHooks;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import robaertschi.minetech.api.blockentity.BasicBlockEntity;
import robaertschi.minetech.api.networking.packet.EnergySyncS2CPacket;
import robaertschi.minetech.api.networking.packet.ItemStackSyncS2CPacket;
import robaertschi.minetech.api.util.IEnergyContainer;
import robaertschi.minetech.api.util.IItemContainer;
import robaertschi.minetech.api.util.ModEnergyStorage;
import robaertschi.minetech.api.util.Tier;
import robaertschi.minetech.networking.ModMessages;
import robaertschi.minetech.screen.FuelGeneratorMenu;

public class FuelGeneratorBlockEntity extends BasicBlockEntity implements IEnergyContainer, IItemContainer {

    private final ItemStackHandler itemHandler = new ItemStackHandler(1) {
        @Override
        protected void onContentsChanged(int slot) {
            setChanged();
            if (level != null && !level.isClientSide()) {
                ModMessages.sendToClients(new ItemStackSyncS2CPacket(this, worldPosition));
            }
        }

        @SuppressWarnings("SwitchStatementWithTooFewBranches")
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return switch (slot) {
                case 0 -> AbstractFurnaceBlockEntity.isFuel(stack);
                default -> super.isItemValid(slot, stack);
            };
        }
    };

    private final ModEnergyStorage ENERGY_STORAGE = new ModEnergyStorage(100000, 256) {
        @Override
        public void onEnergyChanged() {
            setChanged();
            ModMessages.sendToClients(new EnergySyncS2CPacket(energy, worldPosition));
        }
    };

    public static final int BASE_ENERGY_PROD = 8;

    private LazyOptional<IItemHandler> lazyItemHandler = LazyOptional.empty();
    private LazyOptional<IEnergyStorage> lazyEnergyStorage = LazyOptional.empty();

    private final ContainerData data;
    private int progress = 0;
    private int maxProgress = 0;

    public FuelGeneratorBlockEntity(BlockPos pos, BlockState state) {
        super(ModBlockEntities.FUEL_GENERATOR.get(), pos, state, Tier.TIER_1);
        data = new ContainerData() {
            @Override
            public int get(int index) {
                return switch (index) {
                    case 0 -> FuelGeneratorBlockEntity.this.progress;
                    case 1 -> FuelGeneratorBlockEntity.this.maxProgress;
                    default -> 0;
                };
            }

            @Override
            public void set(int index, int value) {
                switch (index) {
                    case 0 -> FuelGeneratorBlockEntity.this.progress = value;
                    case 1 -> FuelGeneratorBlockEntity.this.maxProgress = value;
                }
            }

            @Override
            public int getCount() {
                return 2;
            }
        };
    }

    @Override
    public @NotNull Component getDisplayName() {
        return Component.translatableWithFallback("blockentity.minetech.fuel_generator.name", "Fuel Generator");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, @NotNull Inventory inv, @NotNull Player player) {
        ModMessages.sendToClients(new EnergySyncS2CPacket(this.ENERGY_STORAGE.getEnergyStored(), getBlockPos()));
        return new FuelGeneratorMenu(id, inv, this, this.data);
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
        nbt.putInt("fuel_generator.progress", this.progress);
        nbt.putInt("fuel_generator.energy", this.ENERGY_STORAGE.getEnergyStored());

        super.saveAdditional(nbt);
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);

        itemHandler.deserializeNBT(nbt.getCompound("inventory"));
        progress = nbt.getInt("fuel_generator.progress");
        ENERGY_STORAGE.setEnergy(nbt.getInt("fuel_generator.energy"));
    }

    @Override
    public IEnergyStorage getEnergyStorage() {
        return this.ENERGY_STORAGE;
    }

    @Override
    public LazyOptional<IEnergyStorage> getLazyEnergyStorage() {
        return this.lazyEnergyStorage;
    }

    @Override
    public void setEnergyLevel(int energy) {
        ENERGY_STORAGE.setEnergy(energy);
    }

    @Override
    public ItemStackHandler getItemHandler() {
        return this.itemHandler;
    }

    @Override
    public IItemHandler getLazyItemHandler() {
        return this.itemHandler;
    }

    @Override
    public void setHandler(ItemStackHandler itemStackHandler) {
        basicSetHandler(itemStackHandler, itemHandler);
    }

    @SuppressWarnings("unused")
    public static void tick(Level level, BlockPos pos, BlockState state, FuelGeneratorBlockEntity blockEntity) {
        if (blockEntity.maxProgress == 0) {
            blockEntity.progress = 0;
            if (!blockEntity.itemHandler.getStackInSlot(0).isEmpty()) {
                var burnTime = ForgeHooks.getBurnTime(
                        blockEntity.itemHandler.getStackInSlot(0),
                        RecipeType.SMELTING
                );

                blockEntity.maxProgress = burnTime * blockEntity.tier.getBaseEfficiencyMultiplier();
                blockEntity.itemHandler.extractItem(0, 1, false);
            }
        } else if(blockEntity.maxProgress <= blockEntity.progress) {
            blockEntity.maxProgress = 0;
            blockEntity.progress = 0;
        } else {
            blockEntity.progress++;
            blockEntity.ENERGY_STORAGE.receiveEnergy(
                    blockEntity.tier.getBaseEnergyMultiplier() * BASE_ENERGY_PROD,
                    false
            );
        }

        if (blockEntity.ENERGY_STORAGE.getEnergyStored() > 0) {
            Direction.stream().forEach(direction -> {
                // Get the block entity
                var directionBlockEntity = level.getBlockEntity(pos.relative(direction));
                // Check for null
                if (directionBlockEntity != null) {
                    directionBlockEntity.getCapability(ForgeCapabilities.ENERGY, direction.getOpposite()).ifPresent((handler) -> {
                        if (handler.canReceive()) {
                            // Get how much it can receive.
                            var simulatedValue = handler.receiveEnergy(256, true);
                            // Get how much it actually receive.
                            var actuallyEnergy = handler.receiveEnergy(blockEntity.ENERGY_STORAGE.extractEnergy(simulatedValue, false), false);

                            // If there is to much energy extracted, give that Energy back. THIS SHOULD NEVER HAPPEN EXPECT IN SOME WRONG IMPLEMENTATION OF IEnergyStorage
                            if (simulatedValue > actuallyEnergy) {
                                blockEntity.ENERGY_STORAGE.receiveEnergy(simulatedValue - actuallyEnergy, false);
                            }
                        }
                    });
                }
            });
        }
    }
}
