package net.tiffit.sanity.consequences;

import net.minecraft.entity.player.EntityPlayerMP;
import net.tiffit.sanity.SanityCapability.SanityLevel;

public interface IConsequence {

	public void run(EntityPlayerMP player, SanityLevel level);
	
	public double getChance(SanityLevel level);
	
	public int getCooldown();
	
	public default boolean worksInDimension(int dimension){
		return dimension == 0;
	}
}
