package net.tiffit.sanity.proxy;

import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.fml.client.registry.RenderingRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.relauncher.Side;
import net.tiffit.sanity.Sanity;
import net.tiffit.sanity.SanityModifier;
import net.tiffit.sanity.entity.GhostZombieEntity;
import net.tiffit.sanity.render.ParticleEyes;
import net.tiffit.sanity.render.RenderGhostZombie;

@Mod.EventBusSubscriber(Side.CLIENT)
public class ClientProxy extends CommonProxy {

	public static TextureAtlasSprite EYES_TAS = null;
	
	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
		RenderingRegistry.registerEntityRenderingHandler(GhostZombieEntity.class, new RenderGhostZombie.RenderGhostZombieFactory());
	}

	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
		Minecraft.getMinecraft().effectRenderer.registerParticle(CommonProxy.EYES_PARTICLE.getParticleID(), (id, w, x, y, z, sx, sy, sz, args) -> {
			if (id == CommonProxy.EYES_PARTICLE.getParticleID())
				return new ParticleEyes(w, x, y, z);
			else
				return null;
		});
	}

	@SubscribeEvent
	public static void registerModels(ModelRegistryEvent e) {
		helper.registerModels();
	}
	
	@SubscribeEvent
	public static void registerSprites(TextureStitchEvent e) {
		EYES_TAS = e.getMap().registerSprite(new ResourceLocation(Sanity.MODID, "eyes"));
	}
	
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent e){
    	ResourceLocation rl = Item.REGISTRY.getNameForObject(e.getItemStack().getItem());
		List<SanityModifier> mods = Sanity.getModifierValues(Sanity.MOD_EAT);
		for(SanityModifier mod : mods){
			if(new ResourceLocation(mod.value).equals(rl)){
				String type = null;
				float amount = mod.amount;
				if(amount == 0)continue;
				float abs = Math.abs(amount);
				if(abs >= 10f) type = "immensely";
				else if(abs >= 5f) type = "extremely";
				else if(abs >= 1f) type = "very";
				else if(abs >= .5f)type = "";
				else type = "slightly";
				type += " ";
				if(amount < 0)type += "bad";
				else type += "good";
				type = type.trim();
				e.getToolTip().add(TextFormatting.GOLD + "This item is " + type +" for your sanity! (" + amount + ")");
			}
		}
    }

}
