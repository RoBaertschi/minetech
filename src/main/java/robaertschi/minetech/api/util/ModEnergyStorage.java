package robaertschi.minetech.api.util;

import net.minecraftforge.energy.EnergyStorage;

/*
 * CREDIT:  https://github.com/Tutorials-By-Kaupenjoe/Forge-Tutorial-1.19 by Kaupenjoe
 * Under MIT-License: https://github.com/Tutorials-By-Kaupenjoe/Forge-Tutorial-1.19/blob/main/LICENSE
 */

public abstract class ModEnergyStorage extends EnergyStorage {
    public ModEnergyStorage(int capacity, int maxTransfer) {
        super(capacity, maxTransfer);
    }

    @Override
    public int extractEnergy(int maxExtract, boolean simulate) {
        int extractedEnergy = super.extractEnergy(maxExtract, simulate);
        if(extractedEnergy != 0) {
            onEnergyChanged();
        }

        return extractedEnergy;
    }

    @Override
    public int receiveEnergy(int maxReceive, boolean simulate) {
        int receiveEnergy = super.receiveEnergy(maxReceive, simulate);
        if(receiveEnergy != 0) {
            onEnergyChanged();
        }

        return receiveEnergy;
    }

    public int setEnergy(int energy) {
        this.energy = energy;
        return energy;
    }

    public abstract void onEnergyChanged();
}