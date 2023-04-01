package robaertschi.minetech.item.custom;

import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.IntTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.*;
import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.ItemStackHandler;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import robaertschi.minetech.api.util.IEnergyContainer;
import robaertschi.minetech.api.util.ModEnergyStorage;
import robaertschi.minetech.util.Constants;

import javax.swing.plaf.basic.BasicComboBoxUI;
import java.util.List;

public class ModularArmorItem extends ArmorItem {
    public ModularArmorItem(ArmorMaterial material, Type type, Properties properties) {
        super(material, type, properties);
    }

    private ModEnergyStorage newStorage() {
        return new ModEnergyStorage(30000, 32) {
            @Override
            public void onEnergyChanged() {

            }
        };
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        return new ICapabilitySerializable<CompoundTag>() {
            private ModEnergyStorage energyStorage = newStorage();
            private ItemStackHandler itemHandler = new ItemStackHandler(4);
            private LazyOptional<IEnergyStorage> energyStorageHolder = LazyOptional.of(() -> energyStorage);
            private LazyOptional<IItemHandler> itemHandlerHolder = LazyOptional.of(() -> itemHandler);


            @Override
            public CompoundTag serializeNBT() {
                var tag = new CompoundTag();
                tag.putInt("modular_armor.energy", energyStorage.getEnergyStored());
                tag.put("inventory", itemHandler.serializeNBT());
                return tag;
            }

            @Override
            public void deserializeNBT(CompoundTag nbt) {
                energyStorage.setEnergy(nbt.getInt("modular_armor.energy"));
                itemHandler.deserializeNBT(nbt.getCompound("inventory"));
            }

            @Override
            public @NotNull <T> LazyOptional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
                if (cap == ForgeCapabilities.ENERGY) {
                    return energyStorageHolder.cast();
                }
                if (cap == ForgeCapabilities.ITEM_HANDLER) {
                    return itemHandlerHolder.cast();
                }
                return LazyOptional.empty();
            }
        };
    }

    @Override
    public @Nullable CompoundTag getShareTag(ItemStack stack) {
        CompoundTag tag = stack.getOrCreateTag();
        stack.getCapability(ForgeCapabilities.ENERGY, null).ifPresent(iEnergyStorage -> {
            tag.putInt("modular_armor.energy", iEnergyStorage.getEnergyStored());
        });
        stack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            tag.put("modular_armor.inventory", ((ItemStackHandler) iItemHandler).serializeNBT());
        });
        return tag;
    }

    @Override
    public void readShareTag(ItemStack stack, @Nullable CompoundTag nbt) {
        stack.getCapability(ForgeCapabilities.ENERGY, null).ifPresent(iEnergyStorage -> {
            iEnergyStorage.receiveEnergy(nbt.getInt("modular_armor.energy")-iEnergyStorage.getEnergyStored(), false);
        });

        stack.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(iItemHandler -> {
            ((ItemStackHandler) iItemHandler).deserializeNBT(nbt.getCompound("modular_armor.inventory"));
        });
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag flags) {
        stack.getCapability(ForgeCapabilities.ENERGY, null).ifPresent(cap -> {
            String charge = String.valueOf(cap.getEnergyStored());
            String capacity = String.valueOf(cap.getMaxEnergyStored());
            components.add(Component.literal("Energy: " + charge + "/" + capacity));
        });
    }
}
