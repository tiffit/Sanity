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
import net.tiffit.sanity.entity.GhostZombieEntity;

public class GhostZombieConsequence implements IConsequence {

	@Override
	public void run(EntityPlayerMP player, SanityLevel level) {
		BlockPos pos = findBestSpot(player);
		if (pos != null) {
			GhostZombieEntity ent = new GhostZombieEntity(player.world);
			ent.setTarget(player, pos);
			if (ent.getCanSpawnHere()) {
				player.world.spawnEntity(ent);
			}
		}
	}

	private BlockPos findBestSpot(EntityPlayerMP p) {
		int searchRadius = 10;
		World w = p.getEntityWorld();
		List<BlockPos> allowed = new ArrayList<BlockPos>();
		for (int x = -searchRadius; x <= searchRadius; x++) {
			for (int y = -2; y <= 2; y++) {
				for (int z = -searchRadius; z <= searchRadius; z++) {
					BlockPos pos = new BlockPos(x, y, z).add(p.getPosition());
					if (pos.distanceSq(p.getPosition()) < Math.pow(2.5, 2))
						continue;
					IBlockState state = w.getBlockState(pos);
					AxisAlignedBB aabb = state.getCollisionBoundingBox(w, pos);
					if (aabb == null) {
						state = w.getBlockState(pos.down());
						aabb = state.getCollisionBoundingBox(w, pos.down());
						if (aabb != null)
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
			return 1 / (20 * 60 * 20.0);
		case VERY_DAMAGED:
			return 1 / (20 * 60 * 7.0);
		case INSANE:
			return 1 / (20 * 60 * 2.0);
		default:
			return 0;
		}
	}

	@Override
	public int getCooldown() {
		return 20*10;
	}
	
}
