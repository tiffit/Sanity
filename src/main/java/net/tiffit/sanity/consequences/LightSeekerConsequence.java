package net.tiffit.sanity.consequences;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.RandomUtils;

import net.minecraft.block.BlockLiquid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tiffit.sanity.ConfigHelper;
import net.tiffit.sanity.SanityCapability.SanityLevel;
import net.tiffit.sanity.entity.LightSeekerEntity;

public class LightSeekerConsequence implements IConsequence {
	@Override
	public void run(EntityPlayerMP player, SanityLevel level) {
		BlockPos pos = findLight(player);
		if (pos != null && !player.world.isDaytime()) {
			LightSeekerEntity ent = new LightSeekerEntity(player.world);
			ent.setTarget(pos);
			BlockPos spawn = pos.add(getOffset());
			ent.setPosition(spawn.getX() + .5, spawn.getY() + .5, spawn.getZ() + .5);
			player.world.spawnEntity(ent);
		}
	}
	
	private BlockPos getOffset(){
		int xzMax = 5;
		int x = 0;
		int y = 0;
		int z = 0;
		y = RandomUtils.nextInt(0, 7) - 3;
		boolean useX = RandomUtils.nextBoolean();
		if(useX){
			if(RandomUtils.nextBoolean())z = xzMax;
			else z = -xzMax;
			x = RandomUtils.nextInt(0, xzMax*2 + 1) - xzMax;
		}else{
			if(RandomUtils.nextBoolean())x = xzMax;
			else x = -xzMax;
			z = RandomUtils.nextInt(0, xzMax*2 + 1) - xzMax;
		}
		return new BlockPos(x, y, z);
	}
	
	private BlockPos findLight(EntityPlayerMP p) {
		int searchRadius = 3;
		World w = p.getEntityWorld();
		List<BlockPos> allowed = new ArrayList<BlockPos>();
		for (int x = -searchRadius; x <= searchRadius; x++) {
			for (int y = -2; y <= 2; y++) {
				for (int z = -searchRadius; z <= searchRadius; z++) {
					BlockPos pos = new BlockPos(x, y, z).add(p.getPosition());
					IBlockState state = w.getBlockState(pos);
					if(w.getTileEntity(pos) == null && !(state.getBlock() instanceof BlockLiquid) && state.getLightValue(w, pos) > .2){
						allowed.add(pos);
					}
				}
			}
		}
		if (!allowed.isEmpty()) {
			Random rand = new Random();
			return allowed.get(rand.nextInt(allowed.size()));
		} else
			return null;
	}

	@Override
	public double getChance(SanityLevel level) {
		switch (level) {
		case VERY_HEALTHY:
			return ConfigHelper.spawn_rate_lightseeker_veryhealthy;
		case HEALTHY:
			return ConfigHelper.spawn_rate_lightseeker_healthy;
		case DAMAGED:
			return ConfigHelper.spawn_rate_lightseeker_damaged;
		case VERY_DAMAGED:
			return ConfigHelper.spawn_rate_lightseeker_verydamaged;
		case INSANE:
			return ConfigHelper.spawn_rate_lightseeker_insane;
		default:
			return 0;
		}
	}
	
	@Override
	public int getCooldown() {
		return ConfigHelper.spawn_rate_lightseeker_cooldown;
	}

}
