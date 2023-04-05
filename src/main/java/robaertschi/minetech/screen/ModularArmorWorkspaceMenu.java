package robaertschi.minetech.screen;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import robaertschi.minetech.api.screen.BasicMenu;
import robaertschi.minetech.block.ModBlocks;
import robaertschi.minetech.blockentity.ModularArmorWorkspaceBlockEntity;

public class ModularArmorWorkspaceMenu extends BasicMenu<ModularArmorWorkspaceBlockEntity> {
    protected final Player player;

    public ModularArmorWorkspaceMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, buf.readBlockPos());
    }

    private ModularArmorWorkspaceMenu(int id, Inventory inv, BlockPos pos) {
        this(id, inv, inv.player.level.getBlockEntity(pos));
    }

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

}
