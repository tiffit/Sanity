package net.tiffit.sanity.render;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.Particle;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.tiffit.sanity.proxy.ClientProxy;

public class ParticleEyes extends Particle {

	public ParticleEyes(World worldIn, double posXIn, double posYIn, double posZIn) {
		super(worldIn, posXIn, posYIn, posZIn);
		setParticleTexture(ClientProxy.EYES_TAS);
		setMaxAge(20*5);
	}
	
	@Override
	public void onUpdate() {
		super.onUpdate();
		if(Minecraft.getMinecraft().player.getDistanceSq(posX, posY, posZ) < 10*10){
			setExpired();
		}
		BlockPos blockpos = new BlockPos(this.posX, this.posY, this.posZ);
		if(this.world.getCombinedLight(blockpos, 0) > 0){
			setExpired();
		}
	}
	
	@Override
	public int getBrightnessForRender(float p_189214_1_) {
		return 200;
	}
	
	@Override
	public int getFXLayer() {
		return 1;
	}
	

}
