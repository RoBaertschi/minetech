package robaertschi.minetech.screen;

import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.SimpleContainerData;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.SlotItemHandler;
import robaertschi.minetech.api.screen.BasicMenu;
import robaertschi.minetech.api.util.IItemContainer;
import robaertschi.minetech.block.ModBlocks;
import robaertschi.minetech.blockentity.FuelGeneratorBlockEntity;

public class FuelGeneratorMenu extends BasicMenu<FuelGeneratorBlockEntity> {
    public FuelGeneratorMenu(int id, Inventory inv, FriendlyByteBuf buf) {
        this(id, inv, buf.readBlockPos());
    }

    private FuelGeneratorMenu(int id, Inventory inv, BlockPos pos) {
        this(id, inv, inv.player.level.getBlockEntity(pos), new SimpleContainerData(2));
    }

    public FuelGeneratorMenu(int id, Inventory inv, BlockEntity blockEntity, ContainerData data) {
        super(ModMenuTypes.FUEL_GENERATOR_MENU.get(), id, (FuelGeneratorBlockEntity) blockEntity, ModBlocks.FUEL_GENERATOR, inv.player.level, data, ((IItemContainer) blockEntity).getItemHandler().getSlots(), inv);
        checkContainerSize(inv, ((IItemContainer) blockEntity).getItemHandler().getSlots());

        this.blockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(handler -> {
            this.addSlot(new SlotItemHandler(handler, 0, 86, 30));
        });
    }

    public boolean isGenerating() {
        return data.get(0) > 0;
    }

    public ContainerData getData() {
        return data;
    }
}
