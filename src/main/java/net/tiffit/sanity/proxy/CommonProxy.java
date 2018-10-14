package net.tiffit.sanity.proxy;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootTableList;
import net.minecraftforge.common.capabilities.CapabilityManager;
import net.minecraftforge.common.util.EnumHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.EntityRegistry;
import net.minecraftforge.oredict.OreDictionary;
import net.tiffit.sanity.Sanity;
import net.tiffit.sanity.SanityCapability;
import net.tiffit.sanity.SanityCapability.SanityCapabilityFactory;
import net.tiffit.sanity.SanityCapability.SanityCapabilityStorage;
import net.tiffit.sanity.Singletons;
import net.tiffit.sanity.entity.GhostZombieEntity;
import net.tiffit.sanity.entity.LightSeekerEntity;
import net.tiffit.sanity.items.CheckerItem;
import net.tiffit.sanity.items.PreciseCheckerItem;
import net.tiffit.sanity.items.SpecterBreadItem;
import net.tiffit.sanity.items.SpectergooItem;
import net.tiffit.tiffitlib.RegistryHelper;
import net.tiffit.tiffitlib.utils.ParticleUtils;

@Mod.EventBusSubscriber
public class CommonProxy {
	
	public static RegistryHelper helper = new RegistryHelper(Sanity.MODID);
	public static EnumParticleTypes EYES_PARTICLE;
	
	
    public void preInit(FMLPreInitializationEvent e) {
    	registerEntity("ghost_zombie", GhostZombieEntity.class, 30, 2, false);
    	registerEntity("light_seeker", LightSeekerEntity.class, 30, 2, false);
    	EYES_PARTICLE = ParticleUtils.createParticle("EYES", "eyes", 134, true, 0);
    }
    
    private int entityId = 0;
    
    private void registerEntity(String name, Class<? extends Entity> clazz,  int trackingRange, int updateFrequency, boolean sendsVelocityUpdates){
    	EntityRegistry.registerModEntity(new ResourceLocation(Sanity.MODID, name), clazz, name, entityId++, Sanity.INSTANCE, trackingRange, updateFrequency, sendsVelocityUpdates);
    }

    public void init(FMLInitializationEvent e) {
    	CapabilityManager.INSTANCE.register(SanityCapability.class, new SanityCapabilityStorage(), new SanityCapabilityFactory());
    	OreDictionary.registerOre("slimeball", Singletons.specterGoo);
    }

    public void postInit(FMLPostInitializationEvent e) {
    	LootTableList.register(new ResourceLocation(Sanity.MODID, "entities/ghost_zombie"));
    }
    
    @SubscribeEvent
    public static void blockRegistry(RegistryEvent.Register<Block> e) {
    	helper.setBlockRegistry(e.getRegistry());
    }

    @SubscribeEvent
    public static void itemRegistry(RegistryEvent.Register<Item> e) {
    	helper.setItemRegistry(e.getRegistry());
    	
    	helper.registerItemBlocks();
    	helper.register(new CheckerItem());
    	helper.register(new PreciseCheckerItem());
    	helper.register(new SpectergooItem());
    	helper.register(new SpecterBreadItem());
    }
}

