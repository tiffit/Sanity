package net.tiffit.sanity;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.logging.log4j.Logger;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.Mod.Instance;
import net.minecraftforge.fml.common.SidedProxy;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.event.FMLServerStartingEvent;
import net.tiffit.sanity.commands.SetSanityCommand;
import net.tiffit.sanity.proxy.CommonProxy;

@Mod(modid = Sanity.MODID, name = Sanity.NAME, version = Sanity.VERSION, dependencies = Sanity.DEPENDENCIES)
public class Sanity {
	public static final String MODID = "sanity";
	public static final String NAME = "Sanity";
	public static final String VERSION = "1.0.3";
	public static final String DEPENDENCIES = "required-after:tiffitlib;";

	public static Logger logger;

	@SidedProxy(clientSide = "net.tiffit.sanity.proxy.ClientProxy", serverSide = "net.tiffit.sanity.proxy.ServerProxy")
	public static CommonProxy proxy;

	public static HashMap<String, List<SanityModifier>> SANITY_MODIFIERS = new HashMap<>();
	public static final String MOD_KILL = "kill";
	public static final String MOD_EAT = "eat";
	public static final String MOD_MISC = "misc";
	public static final String MOD_DIMENSION = "dimension";
	
	@Instance(MODID)
	public static Sanity INSTANCE;
	
	public static File CONFIG_DIR;
	
	public static CreativeTabs CTAB = new CreativeTabs("sanity") {

		@Override
		public ItemStack getTabIconItem() {
			return new ItemStack(Singletons.specterGoo);
		}

	};
	
	@EventHandler
	public void preInit(FMLPreInitializationEvent e) {
		logger = e.getModLog();
		proxy.preInit(e);
		File configs = e.getModConfigurationDirectory();
		CONFIG_DIR = new File(configs, MODID);
		if(!CONFIG_DIR.exists())CONFIG_DIR.mkdir();
	}

	@EventHandler
	public void init(FMLInitializationEvent e) {
		proxy.init(e);
	}
	
	@EventHandler
	public void postInit(FMLPostInitializationEvent e) {
		proxy.postInit(e);
		addModifier(MOD_KILL, new SanityModifier("minecraft:chicken", -.75f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:bat", -.2f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:villager", -7.5f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:zombie_villager", -3f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:pig", -1f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:sheep", -1f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:cow", -1f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:mooshroom", -2f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:horse", -2f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:donkey", -2f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:llama", -1.5f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:mule", -2f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:ocelot", -2f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:wolf", -3f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:parrot", -2f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:rabbit", -.75f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:squid", -.2f));
		
		addModifier(MOD_EAT, new SanityModifier("minecraft:mushroom_stew", -.6f));
		addModifier(MOD_EAT, new SanityModifier("minecraft:golden_apple", 4f));
		addModifier(MOD_EAT, new SanityModifier("minecraft:cookie", .15f));
		addModifier(MOD_EAT, new SanityModifier("minecraft:pumpkin_pie", .6f));
		addModifier(MOD_EAT, new SanityModifier("minecraft:beetroot_soup", .6f));
		addModifier(MOD_EAT, new SanityModifier("minecraft:rotten_flesh", -7f));
		addModifier(MOD_EAT, new SanityModifier("minecraft:spider_eye", -10f));
		addModifier(MOD_EAT, new SanityModifier("sanity:specterbread", -20f));
		
		addModifier(MOD_MISC, new SanityModifier("sleep", 10));
		addModifier(MOD_MISC, new SanityModifier("fish", 0.25f));
		
		addModifier(MOD_DIMENSION, new SanityModifier("nether", -(1f/(20*60)*0.1f)));
		addModifier(MOD_DIMENSION, new SanityModifier("midnight", -(1f/(20*60)*0.2f)));
		ConfigHelper.loadModifierMap(SANITY_MODIFIERS);
	}
	
	public static void addModifier(String type, SanityModifier value){
		SANITY_MODIFIERS.putIfAbsent(type, new ArrayList<SanityModifier>());
		SANITY_MODIFIERS.get(type).add(value);
	}

	public static List<SanityModifier> getModifierValues(String type){
		return SANITY_MODIFIERS.getOrDefault(type, new ArrayList<SanityModifier>());
	}
	
	@EventHandler
	public void serverStart(FMLServerStartingEvent e) {
		e.registerServerCommand(new SetSanityCommand());

	}
}
