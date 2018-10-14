package net.tiffit.sanity.entity;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.material.EnumPushReaction;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EnumCreatureAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.SoundEvents;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class LightSeekerEntity extends EntityLiving {
	//summon sanity:light_seeker ~ ~ ~ {tx:1324, ty:67, tz:-786}
	//kill @e[type=sanity:light_seeker]
	private long sound_timer = 0;
	private BlockPos target;
	private int break_progress = 60;
	private IBlockState state;
	
	public LightSeekerEntity(World w) {
		super(w);
		setSize(0, 0);
		noClip = true;
	}
	
	@Override
	protected void initEntityAI() {
	}

	public void setTarget(BlockPos pos){
		target = pos;
		state = world.getBlockState(pos);
	}
	
	@Override
	public boolean canBeHitWithPotion() {
		return false;
	}
	
	@Override
	public boolean getIsInvulnerable() {
		return true;
	}
	
	private double distanceFromTarget(){
		if(target == null)return 1000;
		return Math.sqrt(target.distanceSq(posX, posY, posZ));
	}
	
	@Override
	public void onLivingUpdate() {
		if(!world.isRemote){
			if(!hasNoGravity())setNoGravity(true);
			WorldServer ws = (WorldServer) world;
			if(state != null && ws.getBlockState(target) != state)dieWithPassion();
			double offDiv = 2;
			double xoff = (Math.random() - 0.5)/offDiv;
			double yoff = (Math.random() - 0.5)/offDiv;
			double zoff = (Math.random() - 0.5)/offDiv;
			ws.spawnParticle(EnumParticleTypes.SMOKE_NORMAL, posX, posY + 1, posZ, 3, xoff, yoff, zoff, 0, new int[] {});
			updateSound();
			updateMove();
			
			if(getPosition().equals(target)){
				break_progress--;
			}
			if(break_progress <= 0){
				ws.destroyBlock(getPosition(), true);
				setDead();
				playSound(SoundEvents.ENTITY_ELDER_GUARDIAN_CURSE, 1f, 1.3f);
				ws.spawnParticle(EnumParticleTypes.EXPLOSION_LARGE, posX, posY + 1, posZ, 3, 0, 0, 0, 0, new int[] {});
			}else searchArea();
		}
		super.onLivingUpdate();
	}
	
	private void updateMove(){
		if(target != null){
			double maxMove = 0.02;
			double dx = target.getX() + 0.5 - posX;
			double dy = target.getY() - 0.5 - posY;
			double dz = target.getZ() + 0.5 - posZ;
			double newX = moveDir(dx, maxMove);
			double newY = moveDir(dy, maxMove);
			double newZ = moveDir(dz, maxMove);
			setPosition(newX + posX, newY + posY, newZ + posZ);
		}
	}
	
	private void searchArea(){
		AxisAlignedBB aabb = new AxisAlignedBB(getPosition());
		double expand = .5;
		aabb = aabb.expand(expand, expand, expand).expand(-expand, -expand, -expand);
		List<Entity> ents = world.getEntitiesInAABBexcluding(this, aabb, (ent) -> {return ent instanceof EntityPlayer;});
		if(!ents.isEmpty()){
			dieWithPassion();
		}
	}
	
	private void dieWithPassion(){
		setDead();
		playSound(SoundEvents.BLOCK_FIRE_EXTINGUISH, 1f, 2f);
		playSound(SoundEvents.ENTITY_GHAST_DEATH, 1f, .5f);
	}
	
	@Override
	public SoundCategory getSoundCategory() {
		return SoundCategory.HOSTILE;
	}
	
	private double moveDir(double dis, double maxmove){
		if(Math.abs(maxmove) > Math.abs(dis))return dis;
		if(dis > 0)return maxmove;
		else return -maxmove;
	}
	
	private void updateSound(){
		sound_timer++;
		if(sound_timer % 30 == 0){
			playSound(SoundEvents.BLOCK_NOTE_BASS, 2f, .7f);
			playSound(SoundEvents.BLOCK_NOTE_GUITAR, 1f, .8f);
		}
		if(sound_timer % 30 == 5)playSound(SoundEvents.BLOCK_NOTE_BASS, 2f, .8f);
		if(sound_timer % 30 == 10)playSound(SoundEvents.BLOCK_NOTE_BASS, 2f, .9f);
		if(sound_timer % 30 == 15)playSound(SoundEvents.BLOCK_NOTE_BASS, 2f, 1f);
		if(sound_timer % 30 == 20)playSound(SoundEvents.BLOCK_NOTE_BASS, 2f, .85f);
		if(getPosition().equals(target)){
			if(sound_timer % 4 == 0){
				playSound(SoundEvents.BLOCK_REDSTONE_TORCH_BURNOUT, 1f, (break_progress / 60f) * 0.7f + 0.3f);
			}
		}else if(distanceFromTarget() < 3){
			if(sound_timer % 20 == 0)playSound(SoundEvents.BLOCK_NOTE_CHIME, 2f, .7f);
			if(sound_timer % 20 == 8)playSound(SoundEvents.BLOCK_NOTE_CHIME, 2f, .5f);
		}
	}
	
	@Override
	public EnumPushReaction getPushReaction() {
		return EnumPushReaction.IGNORE;
	}

	@Override
	public boolean canTrample(World world, Block block, BlockPos pos, float fallDistance) {
		return false;
	}
	
	public EnumCreatureAttribute getCreatureAttribute() {
		return EnumCreatureAttribute.UNDEFINED;
	}
	
	@Override
	public void readFromNBT(NBTTagCompound compound) {
		super.readFromNBT(compound);
		if(compound.hasKey("targetblock")){
			target = BlockPos.fromLong(compound.getLong("targetblock"));
		}else if(compound.hasKey("tx") && compound.hasKey("ty") && compound.hasKey("tz")){
			target = new BlockPos(compound.getInteger("tx"), compound.getInteger("ty"), compound.getInteger("tz"));
		}
	}

	@Override
	public NBTTagCompound writeToNBT(NBTTagCompound compound) {
		compound = super.writeToNBT(compound);
		if(target != null)compound.setLong("targetblock", target.toLong());
		return compound;
	}

}
