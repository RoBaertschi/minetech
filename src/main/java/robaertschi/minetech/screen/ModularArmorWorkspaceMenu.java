package robaertschi.minetech.screen;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import net.minecraftforge.items.SlotItemHandler;
import net.minecraftforge.network.NetworkHooks;
import org.jetbrains.annotations.NotNull;
import robaertschi.minetech.Minetech;
import robaertschi.minetech.api.inventory.VariableSlotItemHandler;
import robaertschi.minetech.api.screen.BasicMenu;
import robaertschi.minetech.block.ModBlocks;
import robaertschi.minetech.blockentity.ModularArmorWorkspaceBlockEntity;

import java.util.ArrayList;

public class ModularArmorWorkspaceMenu extends BasicMenu<ModularArmorWorkspaceBlockEntity> {
    protected final Player player;

    public ModularArmorWorkspaceMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, buf.readBlockPos());
    }

    private ModularArmorWorkspaceMenu(int id, Inventory inv, BlockPos pos) {
        this(id, inv, inv.player.level.getBlockEntity(pos));
    }

    private final ItemStackHandler emptyItemStackHandler = new ItemStackHandler(5) {
        @Override
        public boolean isItemValid(int slot, @NotNull ItemStack stack) {
            return false;
        }
    };

    private ArrayList<VariableSlotItemHandler> customSlots =
            new ArrayList<>();

    public ModularArmorWorkspaceMenu(int id, Inventory inv, BlockEntity blockEntity) {
        super(ModMenuTypes.MODULAR_ARMOR_WORKSPACE_MENU.get(),
                id,
                (ModularArmorWorkspaceBlockEntity) blockEntity,
                ModBlocks.MODULAR_ARMOR_WORKSPACE,
                inv.player.level,
                new SimpleContainerData(0),
                5,
                inv);
        checkContainerSize(inv, 1);



        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, 0, 86, 30));
            /*handler.getStackInSlot(0).getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(this::addCustomSlots);

            if (!handler.getStackInSlot(0).getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {
                addCustomSlots(emptyItemStackHandler);
                deactivateCustomSlots();
            }*/
        });

        this.player = inv.player;
    }

    @Override
    public void updateItemStacks() {
        return;

        /*this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            if (!handler.getStackInSlot(0).getCapability(ForgeCapabilities.ITEM_HANDLER).isPresent()) {
                deactivateCustomSlots();
            }
            handler.getStackInSlot(0).getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(this::activateCustomSlots);
        });

        for (VariableSlotItemHandler slot :
                customSlots) {
            slot.setChanged();
        }
        ((ServerPlayer) player).doCloseContainer();
        NetworkHooks.openScreen((ServerPlayer) player, blockEntity, blockEntity.getBlockPos());*/
    }

    protected void addCustomSlots(IItemHandler itemHandler) {
        customSlots.add((VariableSlotItemHandler) addSlot(new VariableSlotItemHandler(itemHandler, 0, 1, 20, 10)));
        customSlots.add((VariableSlotItemHandler) addSlot(new VariableSlotItemHandler(itemHandler, 1, 2, 2*20, 10)));
        customSlots.add((VariableSlotItemHandler) addSlot(new VariableSlotItemHandler(itemHandler, 2, 3, 20, 30)));
        customSlots.add((VariableSlotItemHandler) addSlot(new VariableSlotItemHandler(itemHandler, 3, 4, 2*20, 30)));
    }

    protected void activateCustomSlots(IItemHandler itemHandler) {
        for (VariableSlotItemHandler slot :
                customSlots) {
            slot.setItemHandler(itemHandler);
            slot.setEnabled(true);
            slot.setChanged();
        }

    }

    protected void deactivateCustomSlots() {
        for (VariableSlotItemHandler slot :
                customSlots) {
            slot.setEnabled(false);
            slot.setChanged();
        }
    }
}
