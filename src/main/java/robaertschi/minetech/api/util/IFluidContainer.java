package robaertschi.minetech.api.util;

import net.minecraftforge.common.util.LazyOptional;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

public interface IFluidContainer {
    FluidTank getFluidTank();
    LazyOptional<IFluidHandler> getLazyFluidHandler();
    void setFluid(FluidStack stack);
    FluidStack getFluidStack();
}
