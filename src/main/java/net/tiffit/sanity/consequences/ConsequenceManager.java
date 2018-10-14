package net.tiffit.sanity.consequences;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.UUID;

import net.minecraft.entity.player.EntityPlayer;
import net.tiffit.sanity.SanityCapability;
import net.tiffit.sanity.SanityCapability.SanityLevel;

public class ConsequenceManager {

	public static List<IConsequence> CONSEQUENCES = new ArrayList<IConsequence>();
	private static HashMap<IConsequence, HashMap<UUID, Integer>> COOLDOWNS = new HashMap<>();

	static {
		CONSEQUENCES.add(new EyesConsequence());
		CONSEQUENCES.add(new GhostZombieConsequence());
		CONSEQUENCES.add(new LightSeekerConsequence());
	}

	public static List<IConsequence> roll(EntityPlayer p) {
		List<IConsequence> cons = new ArrayList<>();
		SanityCapability cap = p.getCapability(SanityCapability.INSTANCE, null);
		SanityLevel level = cap.getSanity();
		for (IConsequence con : CONSEQUENCES) {
			if (con.worksInDimension(p.dimension) && getCooldown(con, p) == 0) {
				if (Math.random() <= con.getChance(level)) {
					reset(con, p);
					cons.add(con);
				}
			}
		}
		return cons;
	}

	public static void tickCooldowns() {
		for (Entry<IConsequence, HashMap<UUID, Integer>> conentry : COOLDOWNS.entrySet()) {
			for (Entry<UUID, Integer> pentry : conentry.getValue().entrySet()) {
				if (pentry.getValue() > 0)
					pentry.setValue(pentry.getValue() - 1);
			}
		}
	}

	public static int getCooldown(IConsequence con, EntityPlayer p) {
		COOLDOWNS.putIfAbsent(con, new HashMap<UUID, Integer>());
		HashMap<UUID, Integer> map = COOLDOWNS.get(con);
		map.putIfAbsent(p.getUniqueID(), con.getCooldown());
		return map.get(p.getUniqueID());
	}

	public static void reset(IConsequence con, EntityPlayer p) {
		COOLDOWNS.putIfAbsent(con, new HashMap<UUID, Integer>());
		HashMap<UUID, Integer> map = COOLDOWNS.get(con);
		map.put(p.getUniqueID(), con.getCooldown());
	}
}
