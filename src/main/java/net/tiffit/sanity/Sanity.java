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
	public static final String VERSION = "1.0.1";
	public static final String DEPENDENCIES = "required-after:tiffitlib;";

	public static Logger logger;

	@SidedProxy(clientSide = "net.tiffit.sanity.proxy.ClientProxy", serverSide = "net.tiffit.sanity.proxy.ServerProxy")
	public static CommonProxy proxy;

	public static HashMap<String, List<SanityModifier>> SANITY_MODIFIERS = new HashMap<>();
	public static final String MOD_KILL = "kill";
	public static final String MOD_EAT = "eat";
	public static final String MOD_MISC = "misc";
	
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
		addModifier(MOD_KILL, new SanityModifier("minecraft:chicken", -.4f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:bat", -.25f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:villager", -5));
		addModifier(MOD_KILL, new SanityModifier("minecraft:zombie_villager", -2.5f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:pig", -.5f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:sheep", -.5f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:cow", -.5f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:mooshroom", -1f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:horse", -1f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:donkey", -1f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:llama", -.8f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:mule", -1.2f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:ocelot", -1.5f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:wolf", -2f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:parrot", -1f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:rabbit", -.7f));
		addModifier(MOD_KILL, new SanityModifier("minecraft:squid", -.1f));
		
		addModifier(MOD_EAT, new SanityModifier("minecraft:mushroom_stew", .2f));
		addModifier(MOD_EAT, new SanityModifier("minecraft:golden_apple", 3f));
		addModifier(MOD_EAT, new SanityModifier("minecraft:cookie", .15f));
		addModifier(MOD_EAT, new SanityModifier("minecraft:pumpkin_pie", .75f));
		addModifier(MOD_EAT, new SanityModifier("minecraft:beetroot_soup", 1f));
		addModifier(MOD_EAT, new SanityModifier("minecraft:rotten_flesh", -3f));
		addModifier(MOD_EAT, new SanityModifier("minecraft:spider_eye", -5f));
		addModifier(MOD_EAT, new SanityModifier("sanity:specterbread", -20f));
		
		addModifier(MOD_MISC, new SanityModifier("sleep", 20f));
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
