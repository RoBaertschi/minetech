package robaertschi.minetech.api.util;

import net.minecraft.world.item.ItemStack;
import robaertschi.minetech.item.ModItems;
import robaertschi.minetech.item.custom.modulararmor.ModularArmorItem;

/**
 * Tier used for most machines.
 * @author RoBaertschi
 * @since 0.1.0-1.19.4
 * @version 1
 */
public enum Tier {
    TIER_1(8, 1, 1),
    TIER_2(32, 2, 4),
    ;
    final int baseEnergyMultiplier;
    final int baseEfficiencyMultiplier;
    final int upgrades;

    /**
     * Basic Tiers for Machines
     * @param baseEnergy The Base Energy. This is used for booth production and use. Multiply when needed.
     * @param baseSpeedMultiplier The Base Speed to produce something (Energy, Items, Fluids).
     * @param upgrades The amount of upgrades it accepts.
     */
    Tier(int baseEnergy, int baseSpeedMultiplier, int upgrades) {
        this.baseEnergyMultiplier = baseEnergy;
        this.baseEfficiencyMultiplier = baseSpeedMultiplier;
        this.upgrades = upgrades;
    }

    public int getBaseEnergyMultiplier() {
        return baseEnergyMultiplier;
    }

    public int getBaseEfficiencyMultiplier() {
        return baseEfficiencyMultiplier;
    }

    public int getUpgrades() {
        return upgrades;
    }

    public static Tier getTierFromItemStack(ItemStack stack) {
        if (stack.getItem() == ModItems.TIER_2_UPGRADE.get()) {
            return TIER_2;
        }

        return TIER_1;
    }

    public static boolean isTierHigher(Tier lower, Tier higher) {
        if (lower == higher) return false;
        return lower.compareTo(higher) < 0;
    }
}
