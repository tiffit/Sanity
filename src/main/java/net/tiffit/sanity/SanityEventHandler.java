package net.tiffit.sanity;

import java.util.List;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEntityUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.gameevent.TickEvent.PlayerTickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.ServerTickEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.tiffit.sanity.SanityCapability.SanityCapabilityProvider;
import net.tiffit.sanity.SanityCapability.SanityLevel;
import net.tiffit.sanity.consequences.ConsequenceManager;
import net.tiffit.sanity.consequences.IConsequence;

@Mod.EventBusSubscriber
public class SanityEventHandler {
	
    @SubscribeEvent
    public static void attachCap(AttachCapabilitiesEvent<Entity> e){
    	if(e.getObject() instanceof EntityPlayer){
    		e.addCapability(new ResourceLocation(Sanity.MODID, "sanity"), new SanityCapabilityProvider());
    	}
    }
    
    @SubscribeEvent
    public static void killEntity(LivingDeathEvent e){
    	if(e.getEntity().world.isRemote || e.getSource() == null)return;
    	Entity killer = e.getSource().getTrueSource();
    	if(killer != null && killer instanceof EntityPlayer){
    		EntityPlayer p = (EntityPlayer) killer;
    		SanityCapability cap = p.getCapability(SanityCapability.INSTANCE, null);
    		List<SanityModifier> mods = Sanity.getModifierValues(Sanity.MOD_KILL);
			ResourceLocation killRL = EntityList.getKey(e.getEntity());
    		for(SanityModifier mod : mods){
    			if(new ResourceLocation(mod.value).equals(killRL)){
    				cap.increaseSanity(mod.amount);
    			}
    		}
    	}
    }
    
    @SubscribeEvent
    public static void eatFood(LivingEntityUseItemEvent.Finish e){
    	if(e.getEntityLiving() instanceof EntityPlayerMP){
    		EntityPlayerMP p = (EntityPlayerMP) e.getEntityLiving();
    		ItemStack s = e.getItem();
    		int old_size = s.getCount();
    		s.setCount(1);
        	ResourceLocation rl = Item.REGISTRY.getNameForObject(s.getItem());
    		List<SanityModifier> mods = Sanity.getModifierValues(Sanity.MOD_EAT);
    		SanityCapability cap = p.getCapability(SanityCapability.INSTANCE, null);
    		for(SanityModifier mod : mods){
    			if(new ResourceLocation(mod.value).equals(rl)){
    				cap.increaseSanity(mod.amount);
    			}
    		}
        	s.setCount(old_size);
    	}
    }
    
    @SubscribeEvent
    public static void playerTick(PlayerTickEvent e){
    	if(e.side == Side.CLIENT || e.phase == Phase.START)return;
    	EntityPlayer p = e.player;
    	SanityCapability cap = p.getCapability(SanityCapability.INSTANCE, null);
    	SanityLevel level = cap.getSanity();
    	List<IConsequence> cons = ConsequenceManager.roll(p);
    	for(IConsequence con : cons)con.run((EntityPlayerMP) p, level);
    }
    
    @SubscribeEvent
    public static void gameTick(ServerTickEvent e){
    	if(e.side == Side.CLIENT || e.phase == Phase.START)return;
    	ConsequenceManager.tickCooldowns();
    }
    
    @SubscribeEvent
    public static void onWakeUp(PlayerWakeUpEvent e){
    	if(!e.getEntityPlayer().world.isRemote){
        	SanityCapability cap = e.getEntityPlayer().getCapability(SanityCapability.INSTANCE, null);
        	cap.increaseSanity(20);
    	}
    }
}

