package robaertschi.minetech.api.util;

/**
 * Tier used for most machines.
 * @author RoBaertschi
 * @since 0.1.0-1.19.4
 * @version 1
 */
public enum Tier {
    TIER_1(8, 1, 1),
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
}
