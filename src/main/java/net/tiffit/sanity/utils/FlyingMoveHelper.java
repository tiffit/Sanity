package net.tiffit.sanity.utils;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.math.MathHelper;

public class FlyingMoveHelper extends EntityMoveHelper {

	public FlyingMoveHelper(EntityLiving ent) {
		super(ent);
	}

	@Override
	public void onUpdateMoveHelper() {
		if (action == Action.MOVE_TO) {
			this.action = EntityMoveHelper.Action.WAIT;
			double dx = this.posX - this.entity.posX;
			double dz = this.posZ - this.entity.posZ;
			double dy = this.posY - this.entity.posY;
			double d = dx * dx + dy * dy + dz * dz;
			double dxz = Math.sqrt(dx*dx + dz*dz);

			if (d < 2.500000277905201E-7D) {
				this.entity.setMoveForward(0.0F);
				return;
			}
			float yaw = (float) (MathHelper.atan2(dz, dx) * (180D / Math.PI)) - 90.0F;
			float pitch = (float) (MathHelper.atan2(dy, dxz) * (180D / Math.PI)) - 90.0F;
			this.entity.rotationYaw = this.limitAngle(this.entity.rotationYaw, yaw, 90.0F);
			this.entity.rotationPitch = pitch;//this.limitAngle(this.entity.rotationPitch, pitch, 90.0F);
			this.entity.setAIMoveSpeed((float) (this.speed * this.entity.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).getAttributeValue()));
		} else
			super.onUpdateMoveHelper();
	}

}
