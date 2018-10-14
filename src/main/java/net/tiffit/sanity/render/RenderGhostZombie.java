package net.tiffit.sanity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tiffit.sanity.entity.GhostZombieEntity;
import net.tiffit.sanity.model.ModelGhostZombie;

@SideOnly(Side.CLIENT)
public class RenderGhostZombie extends RenderBiped<GhostZombieEntity> {
	private static final ResourceLocation ZOMBIE_TEXTURES = new ResourceLocation("textures/entity/zombie/zombie.png");

	public RenderGhostZombie(RenderManager rm) {
		super(rm, new ModelGhostZombie(), 0.5F);
	}

	@Override
	public void doRender(GhostZombieEntity entity, double x, double y, double z, float entityYaw, float partialTicks) {
		if(Minecraft.getMinecraft().player.getUniqueID().toString().equals(entity.getTargetDataParam())){
			super.doRender(entity, x, y, z, entityYaw, partialTicks);
		}
	}
	
	protected void renderModel(GhostZombieEntity entitylivingbaseIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scaleFactor) {
		boolean flag = this.isVisible(entitylivingbaseIn);
		boolean flag1 = !flag && !entitylivingbaseIn.isInvisibleToPlayer(Minecraft.getMinecraft().player);

		if (flag || flag1) {
			if (!this.bindEntityTexture(entitylivingbaseIn)) {
				return;
			}
			GlStateManager.enableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
			GlStateManager.color(1, 1, 1, 0.4f);
			this.mainModel.render(entitylivingbaseIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scaleFactor);
			GlStateManager.disableBlendProfile(GlStateManager.Profile.TRANSPARENT_MODEL);
		}
	}

	protected ResourceLocation getEntityTexture(GhostZombieEntity entity) {
		return ZOMBIE_TEXTURES;
	}

	public static class RenderGhostZombieFactory implements IRenderFactory<GhostZombieEntity> {

		@Override
		public Render<? super GhostZombieEntity> createRenderFor(RenderManager manager) {
			return new RenderGhostZombie(manager);
		}

	}
}
