package robaertschi.minetech.api.inventory;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class VariableSlotItemHandler extends SlotItemHandler {
    public boolean enabled = true;
    public IItemHandler variableItemHandler;
    public final int itemHandlerIndex;
    public final int index;

    @SuppressWarnings("unused")
    public VariableSlotItemHandler(IItemHandler itemHandler, int itemAndSlotIndex, int xPosition, int yPosition) {
        super(itemHandler, itemAndSlotIndex, xPosition, yPosition);

        variableItemHandler = itemHandler;
        this.itemHandlerIndex = itemAndSlotIndex;
        this.index = itemAndSlotIndex;
    }

    public VariableSlotItemHandler(IItemHandler itemHandler, int itemHandlerIndex, int index, int xPosition, int yPosition) {
        super(itemHandler, itemHandlerIndex, xPosition, yPosition);

        variableItemHandler = itemHandler;
        this.itemHandlerIndex = itemHandlerIndex;
        this.index = index;
    }
    @Override
    public boolean mayPlace(@NotNull ItemStack stack)
    {
        if (stack.isEmpty())
            return false;
        return getItemHandler().isItemValid(itemHandlerIndex, stack);
    }

    @Override
    @NotNull
    public ItemStack getItem()
    {
        return this.getItemHandler().getStackInSlot(itemHandlerIndex);
    }

    // Override if your IItemHandler does not implement IItemHandlerModifiable
    @Override
    public void set(@NotNull ItemStack stack)
    {
        ((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(itemHandlerIndex, stack);
        this.setChanged();
    }

    // Override if your IItemHandler does not implement IItemHandlerModifiable
    @Override
    public void initialize(ItemStack stack)
    {
        ((IItemHandlerModifiable) this.getItemHandler()).setStackInSlot(itemHandlerIndex, stack);
        this.setChanged();
    }

    @Override
    public int getMaxStackSize()
    {
        return this.getItemHandler().getSlotLimit(this.itemHandlerIndex);
    }

    @Override
    public int getMaxStackSize(@NotNull ItemStack stack)
    {
        ItemStack maxAdd = stack.copy();
        int maxInput = stack.getMaxStackSize();
        maxAdd.setCount(maxInput);

        IItemHandler handler = this.getItemHandler();
        ItemStack currentStack = handler.getStackInSlot(itemHandlerIndex);
        if (handler instanceof IItemHandlerModifiable handlerModifiable) {

            handlerModifiable.setStackInSlot(itemHandlerIndex, ItemStack.EMPTY);

            ItemStack remainder = handlerModifiable.insertItem(itemHandlerIndex, maxAdd, true);

            handlerModifiable.setStackInSlot(itemHandlerIndex, currentStack);

            return maxInput - remainder.getCount();
        }
        else
        {
            ItemStack remainder = handler.insertItem(itemHandlerIndex, maxAdd, true);

            int current = currentStack.getCount();
            int added = maxInput - remainder.getCount();
            return current + added;
        }
    }

    @Override
    public boolean mayPickup(Player playerIn)
    {
        return !this.getItemHandler().extractItem(itemHandlerIndex, 1, true).isEmpty();
    }

    @Override
    @NotNull
    public ItemStack remove(int amount)
    {
        return this.getItemHandler().extractItem(itemHandlerIndex, amount, false);
    }


    @Override
    public boolean isActive() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    @Override
    public IItemHandler getItemHandler() {
        return variableItemHandler;
    }

    public void setItemHandler(IItemHandler itemHandler) {
        this.variableItemHandler = itemHandler;
    }
}
