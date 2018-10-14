package net.tiffit.sanity.consequences;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tiffit.sanity.SanityCapability.SanityLevel;
import net.tiffit.sanity.proxy.CommonProxy;

public class EyesConsequence implements IConsequence {

	@Override
	public void run(EntityPlayerMP player, SanityLevel level) {
		BlockPos pos = findBestSpot(player);
		if (pos != null) {
			player.getServerWorld().spawnParticle(player, CommonProxy.EYES_PARTICLE, true, pos.getX(), pos.getY(), pos.getZ(), 1, .5, .5, .5, 0, new int[]{});
		}
	}

	private BlockPos findBestSpot(EntityPlayerMP p) {
		int searchRadius = 20;
		World w = p.getEntityWorld();
		List<BlockPos> allowed = new ArrayList<BlockPos>();
		for (int x = -searchRadius; x <= searchRadius; x++) {
			for (int y = -5; y <= 5; y++) {
				for (int z = -searchRadius; z <= searchRadius; z++) {
					BlockPos pos = new BlockPos(x, y, z).add(p.getPosition());
					if (pos.distanceSq(p.getPosition()) < Math.pow(10, 2))
						continue;
					IBlockState state = w.getBlockState(pos);
					AxisAlignedBB aabb = state.getCollisionBoundingBox(w, pos);
					if (aabb == null && w.getLight(pos, true) == 0) {
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
		case DAMAGED:
			return 1 / (20 * 60 * 3.0);
		case VERY_DAMAGED:
			return 1 / (20 * 60 * 1.0);
		case INSANE:
			return 1 / (20 * 60 * 0.25);
		default:
			return 0;
		}
	}

	@Override
	public int getCooldown() {
		return 40;
	}
	
}
