package robaertschi.minetech.item.custom.modulararmor;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.util.Mth;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.energy.IEnergyStorage;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import robaertschi.minetech.api.capabilities.CapabilityNotPresentException;
import robaertschi.minetech.api.util.CapabilityEnergyProvider;

import java.util.List;
import java.util.function.Consumer;

public class ModularArmorItem extends ArmorItem {
    public ModularArmorItem(ArmorMaterial material, Type type) {
        super(material, type, new Properties().stacksTo(1));
    }

    @Override
    public @Nullable ICapabilityProvider initCapabilities(ItemStack stack, @Nullable CompoundTag nbt) {
        if (stack.getItem() instanceof ModularArmorItem item) {
            return new CapabilityEnergyProvider(stack, switch (item.getType()) {
                case HELMET, BOOTS -> 250000;
                case CHESTPLATE -> 500000;
                case LEGGINGS -> 400000;
            });

        } else {
            throw new IllegalArgumentException("Stack is not the required item. Report this issue on the github with any needed infos.");
        }
    }

    @Override
    public int getMaxDamage(ItemStack stack) {
        return 30000;
    }

    @Override
    public boolean isBarVisible(ItemStack stack) {
        IEnergyStorage energyStorage = stack.getCapability(ForgeCapabilities.ENERGY, null).orElseThrow(CapabilityNotPresentException::new);
        return (energyStorage.getEnergyStored() < energyStorage.getMaxEnergyStored());
    }

    @Override
    public int getBarWidth(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.ENERGY, null)
                .map(e -> Math.min(13 * e.getEnergyStored() / e.getMaxEnergyStored(), 13))
                .orElse(0);
    }

    @Override
    public int getBarColor(ItemStack stack) {
        return stack.getCapability(ForgeCapabilities.ENERGY)
                .map(e -> Mth.hsvToRgb(Math.max(0.0F, (float) e.getEnergyStored() / (float) e.getMaxEnergyStored()) / 3.0F, 1.0F, 1.0F))
                .orElse(super.getBarColor(stack));
    }

    @Override
    public void appendHoverText(@NotNull ItemStack stack, @Nullable Level level, @NotNull List<Component> components, @NotNull TooltipFlag flags) {
        super.appendHoverText(stack, level, components, flags);

        stack.getCapability(ForgeCapabilities.ENERGY, null).ifPresent(energy -> components.add(Component.literal("Energy: " + energy.getEnergyStored() + "/" + energy.getMaxEnergyStored())));
    }

    @Override
    public void onArmorTick(ItemStack stack, Level level, Player player) {
        var item = (ModularArmorItem) stack.getItem();

        var energyStorage = stack.getCapability(ForgeCapabilities.ENERGY).orElseThrow(CapabilityNotPresentException::new);
        if (energyStorage.getEnergyStored() <= 0) {
            player.getArmorSlots().forEach(itemStack -> {
                if (itemStack.getItem() instanceof ModularArmorItem armorItem) {
                    if (armorItem.type.equals(item.type)) {
                        player.drop(stack, false);
                        player.setItemSlot(item.getEquipmentSlot(), ItemStack.EMPTY);
                    }
                }
            });
        }

        switch (item.type) {
            case BOOTS -> onBootsTick(stack, level, player);
            case HELMET -> onHelmetTick(stack, level, player);
            case LEGGINGS -> onLeggingsTick(stack, level, player);
            case CHESTPLATE -> onChestplateTick(stack, level, player);
        }

        if (player.getAbilities().flying && ((ModularArmorItem) stack.getItem()).getType() == Type.CHESTPLATE) {
            energyStorage.extractEnergy(5, false);
            if (energyStorage.getEnergyStored() < 5 && !(energyStorage.getEnergyStored() <= 0)) {
                energyStorage.extractEnergy(energyStorage.getEnergyStored(), false);
            }
        }
    }

    @Override
    @SuppressWarnings({"DataFlowIssue"})
    public <T extends LivingEntity> int damageItem(ItemStack stack, int amount, T entity, Consumer<T> onBroken) {
        var energyStorage = stack.getCapability(ForgeCapabilities.ENERGY).orElseThrow(CapabilityNotPresentException::new);
        if (energyStorage.getEnergyStored() <= 0) {
            if (entity instanceof Player player) {
                player.drop(stack, false);
            } else {
                entity.getArmorSlots().forEach(itemStack -> {
                    if (ItemStack.isSame(itemStack, stack)) {
                        entity.setItemSlot(stack.getEquipmentSlot(), ItemStack.EMPTY);
                    }
                });
            }

        }

        return energyStorage.extractEnergy(amount * 10, false);
    }

    @Override
    public boolean isDamageable(ItemStack stack) {
        return true;
    }


    @SuppressWarnings({"unused", "DataFlowIssue"})
    protected void onHelmetTick(ItemStack stack, Level level, Player player) {
        var energyStorage = stack.getCapability(ForgeCapabilities.ENERGY).orElseThrow(CapabilityNotPresentException::new);

        if (player.getEffect(MobEffects.NIGHT_VISION) == null || player.getEffect(MobEffects.NIGHT_VISION).getDuration() < 210) {
            player.addEffect(new MobEffectInstance(MobEffects.NIGHT_VISION, 300, 2));
            energyStorage.extractEnergy(1, false);
        }

    }
    @SuppressWarnings({"unused"})
    protected void onChestplateTick(ItemStack stack, Level level, Player player) {
        var energyStorage = stack.getCapability(ForgeCapabilities.ENERGY).orElseThrow(CapabilityNotPresentException::new);

        if (player.getAbilities().flying) {
            energyStorage.extractEnergy(1, false);
            if (energyStorage.getEnergyStored() < 1 && !(energyStorage.getEnergyStored() <= 0)) {
                energyStorage.extractEnergy(energyStorage.getEnergyStored(), false);
            }
        }
    }
    @SuppressWarnings({"unused"})
    protected void onLeggingsTick(ItemStack stack, Level level, Player player) {
        var energyStorage = stack.getCapability(ForgeCapabilities.ENERGY).orElseThrow(CapabilityNotPresentException::new);

        if(player.getEffect(MobEffects.MOVEMENT_SPEED) == null) {
            player.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SPEED, 60, 2));
            energyStorage.extractEnergy(1, false);
        }
    }
    @SuppressWarnings({"unused"})
    protected void onBootsTick(ItemStack stack, Level level, Player player) {
        var energyStorage = stack.getCapability(ForgeCapabilities.ENERGY).orElseThrow(CapabilityNotPresentException::new);

        if (player.getEffect(MobEffects.JUMP) == null) {
            player.addEffect(new MobEffectInstance(MobEffects.JUMP, 60, 1));
            energyStorage.extractEnergy(1, false);
        }
    }

    public void onUnequip(ItemStack stack, Player player) {
        if (stack.getItem() instanceof ModularArmorItem item && item.getType() == Type.CHESTPLATE){
            player.getAbilities().mayfly = false;
            player.getAbilities().flying = false;
            player.onUpdateAbilities();
        }
    }

    public void onEquip(ItemStack stack, Player player) {
        if (stack.getItem() instanceof ModularArmorItem item && item.getType() == Type.CHESTPLATE){
            player.getAbilities().mayfly = true;
            player.onUpdateAbilities();
        }
    }
}
