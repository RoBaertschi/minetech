package robaertschi.minetech.api.util;

import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;

public interface IItemContainer {
    ItemStackHandler getItemHandler();
    IItemHandler getLazyItemHandler();
    void setHandler(ItemStackHandler itemStackHandler);

    default void drops() {

    }
}
