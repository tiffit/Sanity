package net.tiffit.sanity;

import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.ObjectHolder;
import net.tiffit.sanity.items.CheckerItem;
import net.tiffit.sanity.items.PreciseCheckerItem;
import net.tiffit.sanity.items.SpecterBreadItem;
import net.tiffit.sanity.items.SpectergooItem;

@GameRegistry.ObjectHolder(Sanity.MODID)
public class Singletons {
	
	@ObjectHolder("checker")
	public static CheckerItem checker;
	
	@ObjectHolder("precisechecker")
	public static PreciseCheckerItem preciseChecker;
	
	@ObjectHolder("spectergoo")
	public static SpectergooItem specterGoo;
	
	@ObjectHolder("specterbread")
	public static SpecterBreadItem specterBread;
	
}
