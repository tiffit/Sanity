package net.tiffit.sanity.render;

import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.client.registry.IRenderFactory;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.tiffit.sanity.entity.LightSeekerEntity;

@SideOnly(Side.CLIENT)
public class RenderLightSeeker extends Render<LightSeekerEntity> {

	public RenderLightSeeker(RenderManager rm) {
		super(rm);
	}


	@Override
	protected ResourceLocation getEntityTexture(LightSeekerEntity entity) {
		return null;
	}
	
	public static class RenderGhostZombieFactory implements IRenderFactory<LightSeekerEntity> {

		@Override
		public Render<? super LightSeekerEntity> createRenderFor(RenderManager manager) {
			return new RenderLightSeeker(manager);
		}

	}
}
