package net.tiffit.sanity.entity;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIAttackMelee;
import net.minecraft.entity.ai.EntityAISwimming;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.datasync.DataParameter;
import net.minecraft.network.datasync.DataSerializers;
import net.minecraft.network.datasync.EntityDataManager;
import net.minecraft.util.DamageSource;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.SoundEvent;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;
import net.tiffit.sanity.Sanity;

public class GhostZombieEntity extends EntityMob {

	private boolean disable_despawn;
	private static final DataParameter<String> TARGET = EntityDataManager.<String>createKey(GhostZombieEntity.class, DataSerializers.STRING);

	public GhostZombieEntity(World w) {
		super(w);
		this.setSize(0.6F, 1.95F);
	}
	
	@Override
	protected void entityInit() {
		super.entityInit();
		dataManager.register(TARGET, "");
	}

	protected void initEntityAI() {
		this.tasks.addTask(0, new EntityAISwimming(this));
		this.tasks.addTask(2, new EntityAIAttackMelee(this, 1.0D, false));
	}
	
	public void setTarget(EntityPlayerMP p, BlockPos pos){
		setAttackTarget(p);
		setLocationAndAngles(pos.getX(), pos.getY(), pos.getZ(), 0, 0);
	}
	
	public String getTargetDataParam(){
		return dataManager.get(TARGET);
	}

	protected void applyEntityAttributes() {
		super.applyEntityAttributes();
		this.getEntityAttribute(SharedMonsterAttributes.FOLLOW_RANGE).setBaseValue(50.0D);
		this.getEntityAttribute(SharedMonsterAttributes.MOVEMENT_SPEED).setBaseValue(0.3D);
		this.getEntityAttribute(SharedMonsterAttributes.ATTACK_DAMAGE).setBaseValue(6.0D);
		this.getEntityAttribute(SharedMonsterAttributes.ARMOR).setBaseValue(3.0D);
	}

	@Override
	protected void damageEntity(DamageSource src, float amount) {
		if (!src.isCreativePlayer() && src.getTrueSource() != getAttackTarget())
			return;
		super.damageEntity(src, amount);
		if (getHealth() <= 0) {
			triggerDeathAnimation();
		}
	}

	@Override
	public boolean canBeHitWithPotion() {
		return false;
	}

	@Override
	public EnumPushReaction getPushReaction() {
		return EnumPushReaction.IGNORE;
	}

	@Override
	public boolean canTrample(World world, Block block, BlockPos pos, float fallDistance) {
		return false;
	}
	
	@Override
	protected ResourceLocation getLootTable() {
		return new ResourceLocation(Sanity.MODID, "entities/ghost_zombie");
	}

	@Override
	protected SoundEvent getAmbientSound() {
		return SoundEvents.ENTITY_ZOMBIE_AMBIENT;
	}

	@Override
	protected SoundEvent getHurtSound(DamageSource damageSourceIn) {
		return SoundEvents.ENTITY_ZOMBIE_HURT;
	}

	@Override
	protected SoundEvent getDeathSound() {
		return SoundEvents.BLOCK_GLASS_BREAK;
	}

	protected void playStepSound(BlockPos pos, Block blockIn) {
		this.playSound(SoundEvents.ENTITY_ZOMBIE_STEP, 0.15F, 1.0F);
	}

	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEAD;
	}

	@Override
	protected float getSoundPitch() {
		return 0;
	}

	private float brightnessThreshold(){
		return 0.15f;
	}
	
	@Override
	public void onLivingUpdate() {
		super.onLivingUpdate();
		if (!world.isRemote) {
			if ((ticksExisted > 20 * 20 || getAttackTarget() == null) && !disable_despawn) {
				setDead();
				triggerDeathAnimation();
				playSound(getDeathSound(), 1, 0);
			}
			if (getBrightness() > brightnessThreshold()) {
				setDead();
				triggerDeathAnimation();
				playSound(getDeathSound(), 1, 0);
			}
			if(getAttackTarget() != null){
				dataManager.set(TARGET, getAttackTarget().getUniqueID().toString());
			}
		}
	}
	
	@Override
	public boolean getCanSpawnHere() {
		return world.getDifficulty() != EnumDifficulty.PEACEFUL && getBrightness() < brightnessThreshold() * 0.9;
	}

	private void triggerDeathAnimation() {
		if (world.isRemote)
			return;
		WorldServer w = (WorldServer) world;
		w.spawnParticle(EnumParticleTypes.CLOUD, posX, posY + 1, posZ, 30, 0, 0, 0, .2, new int[] {});
	}

	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		disable_despawn = compound.getBoolean("ddespawn");
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		compound.setBoolean("ddespawn", disable_despawn);
		return compound;
	}
	
}
