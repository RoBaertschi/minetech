package robaertschi.minetech.api.util;

import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.energy.IEnergyStorage;
import robaertschi.minetech.api.util.ModEnergyStorage;

public interface IEnergyContainer {
    IEnergyStorage getEnergyStorage();
    LazyOptional<IEnergyStorage> getLazyEnergyStorage();
    void setEnergyLevel(int energy);

}
