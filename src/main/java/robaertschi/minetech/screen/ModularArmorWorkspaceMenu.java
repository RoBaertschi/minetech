package robaertschi.minetech.screen;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.registries.RegistryObject;
import org.jetbrains.annotations.Nullable;
import robaertschi.minetech.api.screen.BasicMenu;
import robaertschi.minetech.api.util.IItemContainer;
import robaertschi.minetech.block.ModBlocks;
import robaertschi.minetech.blockentity.FuelGeneratorBlockEntity;
import robaertschi.minetech.blockentity.ModularArmorWorkspaceBlockEntity;
import robaertschi.minetech.util.Constants;

import java.util.ArrayList;

public class ModularArmorWorkspaceMenu extends BasicMenu<ModularArmorWorkspaceBlockEntity> {
    public ModularArmorWorkspaceMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, buf.readBlockPos());
    }

    private ModularArmorWorkspaceMenu(int id, Inventory inv, BlockPos pos) {
        this(id, inv, inv.player.level.getBlockEntity(pos));
    }

    private ArrayList<Slot> customSlots =
            new ArrayList<>();

    public ModularArmorWorkspaceMenu(int id, Inventory inv, BlockEntity blockEntity) {
        super(ModMenuTypes.MODULAR_ARMOR_WORKSPACE_MENU.get(),
                id,
                (ModularArmorWorkspaceBlockEntity) blockEntity,
                ModBlocks.MODULAR_ARMOR_WORKSPACE,
                inv.player.level,
                new SimpleContainerData(0),
                1 + 4,
                inv);
        checkContainerSize(inv, 1);



        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            customSlots.add(this.addSlot(new SlotItemHandler(handler, 0, 86, 30)));

            handler.getStackInSlot(0).getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(itemHandler -> {
                addCustomSlots(itemHandler);
            });

        });
    }

    @Override
    public void updateItemStacks() {
        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {

            handler.getStackInSlot(0).getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(itemHandler -> {
                addCustomSlots(itemHandler);
            });

            if (!handler.getStackInSlot(0).getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {
                removeCustomSlots();
            }

        });
    }

    protected void addCustomSlots(IItemHandler itemHandler) {
        customSlots.add(addSlot(new SlotItemHandler(itemHandler, 1, 20, 10)));
        customSlots.add(addSlot(new SlotItemHandler(itemHandler, 2, 2*20, 10)));
        customSlots.add(addSlot(new SlotItemHandler(itemHandler, 3, 20, 30)));
        customSlots.add(addSlot(new SlotItemHandler(itemHandler, 4, 2*20, 30)));
    }

    protected void removeCustomSlots() {
        for (Slot slot :
                customSlots) {
            slots.remove(slot);
            remoteSlots.remove(slot);
            lastSlots.remove(slot);
            customSlots.remove(slot);
        }
    }
}
