package robaertschi.minetech.api.screen;

import net.minecraft.FieldsAreNonnullByDefault;
import net.minecraft.MethodsReturnNonnullByDefault;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.*;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import robaertschi.minetech.api.blockentity.BasicBlockEntity;
import robaertschi.minetech.api.util.IFluidContainer;

import javax.annotation.ParametersAreNonnullByDefault;

/**
 * An Basic Menu implementation that adds the default Player Inventory Slots and some Quick move stuff for the slots.
 * @param <T> T stands for Tile Entity. Because B made more sense for Block than Block Entity
 *
 * Some Code is from DieSieben07 and the {@link BasicMenu#getScaledProgress(int, int, int)} is from Kaupenjoe (modified by me).
 * @since 0.1.0-1.19.4
 * @version 1
 * @author RoBaertschi
 * @author DieSieben07
 * @author Kaupenjoe
 */

@MethodsReturnNonnullByDefault
@ParametersAreNonnullByDefault
@FieldsAreNonnullByDefault
public abstract class BasicMenu<T extends BasicBlockEntity> extends AbstractContainerMenu {
    public final T blockEntity;
    public final RegistryObject<Block> block;
    protected final Level level;
    protected  final ContainerData data;
    protected @Nullable FluidStack fluidStack;

    int customSlotsCount;


    /**
     * Basic Constructor
     * @param menuType The Menu Type
     * @param id The id
     * @param blockEntity The Block Entity for THIS Menu
     * @param block RegistryObject of the Block
     * @param level The level
     * @param data The Container Data of the Block Entity.
     * @param customSlotsCount The amount of custom Slots added by the Block Entity (for Quick move)
     * @param inv The inventory of the Player
     */
    protected BasicMenu(@Nullable MenuType<?> menuType,
                        int id,
                        T blockEntity,
                        RegistryObject<Block> block,
                        Level level,
                        ContainerData data,
                        int customSlotsCount,
                        Inventory inv) {
        super(menuType, id);
        this.blockEntity = blockEntity;
        this.block = block;
        this.level = level;
        this.data = data;
        this.fluidStack = blockEntity instanceof IFluidContainer fluidContainer ? fluidContainer.getFluidStack() : null;
        this.customSlotsCount = customSlotsCount;

        addPlayerInventory(inv);
        addPlayerHotbar(inv);

        addDataSlots(data);

    }

    public @Nullable FluidStack getFluidStack() {
        return fluidStack;
    }

    public void setFluid(FluidStack fluidStack) {
        this.fluidStack = fluidStack;
    }

    public T getBlockEntity() {
        return this.blockEntity;
    }

    /**
     * Get a nice value for drawing something like an progress Arrow. The only line of code is from Kaupenjoe. I reduced the Function to something more usefull in this context.
     *
     * @param progress Current Progress
     * @param maxProgress Max Progress
     * @param progressArrowSize The Size of the Arrow
     * @return An int that is not bigger then progressArrowSize and in Pixels.
     * @apiNote Don't give minus values.
     */
    public static int getScaledProgress(int progress, int maxProgress, int progressArrowSize) {
        return maxProgress != 0 && progress != 0 ? progress * progressArrowSize / maxProgress : 0;
    }

    public void updateItemStacks() {}

    // CREDIT GOES TO: diesieben07 | https://github.com/diesieben07/SevenCommons with mini minor adjustments by me (RoBaertschi).
    // must assign a slot number to each of the slots used by the GUI.
    // For this container, we can see both the tile inventory's slots as well as the player inventory slots and the hotbar.
    // Each time we add a Slot to the container, it automatically increases the slotIndex, which means
    //  0 - 8 = hotbar slots (which will map to the InventoryPlayer slot numbers 0 - 8)
    //  9 - 35 = player inventory slots (which map to the InventoryPlayer slot numbers 9 - 35)
    //  36 - 44 = TileInventory slots, which map to our TileEntity slot numbers 0 - 8)
    protected static final int HOTBAR_SLOT_COUNT = 9;
    protected static final int PLAYER_INVENTORY_ROW_COUNT = 3;
    protected static final int PLAYER_INVENTORY_COLUMN_COUNT = 9;
    protected static final int PLAYER_INVENTORY_SLOT_COUNT = PLAYER_INVENTORY_COLUMN_COUNT * PLAYER_INVENTORY_ROW_COUNT;
    protected static final int VANILLA_SLOT_COUNT = HOTBAR_SLOT_COUNT + PLAYER_INVENTORY_SLOT_COUNT;
    protected static final int VANILLA_FIRST_SLOT_INDEX = 0;
    protected static final int TE_INVENTORY_FIRST_SLOT_INDEX = VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT;


    @Override
    public ItemStack quickMoveStack(Player playerIn, int index) {
        Slot sourceSlot = slots.get(index);
        if (sourceSlot == null || !sourceSlot.hasItem()) return ItemStack.EMPTY;  //EMPTY_ITEM
        ItemStack sourceStack = sourceSlot.getItem();
        ItemStack copyOfSourceStack = sourceStack.copy();

        // Check if the slot clicked is one of the vanilla container slots
        if (index < VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT) {
            // This is a vanilla container slot so merge the stack into the tile inventory
            if (!moveItemStackTo(sourceStack, TE_INVENTORY_FIRST_SLOT_INDEX, TE_INVENTORY_FIRST_SLOT_INDEX
                    + customSlotsCount, false)) {
                return ItemStack.EMPTY;  // EMPTY_ITEM
            }
        } else if (index < TE_INVENTORY_FIRST_SLOT_INDEX + customSlotsCount) {
            // This is a TE slot so merge the stack into the players inventory
            if (!moveItemStackTo(sourceStack, VANILLA_FIRST_SLOT_INDEX, VANILLA_FIRST_SLOT_INDEX + VANILLA_SLOT_COUNT, false)) {
                return ItemStack.EMPTY;
            }
        } else {
            System.out.println("Invalid slotIndex:" + index);
            return ItemStack.EMPTY;
        }
        // If stack size == 0 (the entire stack was moved) set slot contents to null
        if (sourceStack.getCount() == 0) {
            sourceSlot.set(ItemStack.EMPTY);
        } else {
            sourceSlot.setChanged();
        }
        sourceSlot.onTake(playerIn, sourceStack);
        return copyOfSourceStack;
    }

    @Override
    public boolean stillValid(Player player) {
        return stillValid(ContainerLevelAccess.create(level, blockEntity.getBlockPos()),
                player, block.get());
    }

    private void addPlayerInventory(Inventory playerInventory) {
        for (int i = 0; i < 3; ++i) {
            for (int l = 0; l < 9; ++l) {
                this.addSlot(new Slot(playerInventory, l + i * 9 + 9, 8 + l * 18, 86 + i * 18));
            }
        }
    }

    private void addPlayerHotbar(Inventory playerInventory) {
        for (int i = 0; i < 9; ++i) {
            this.addSlot(new Slot(playerInventory, i, 8 + i * 18, 144));
        }
    }
}
