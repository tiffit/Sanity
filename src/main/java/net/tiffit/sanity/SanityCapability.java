package net.tiffit.sanity;

import java.util.concurrent.Callable;

import com.google.common.primitives.Floats;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public class SanityCapability {

	@CapabilityInject(SanityCapability.class)
	public static Capability<SanityCapability> INSTANCE;
	
	private float sanity = 0;

	public float getSanityExact(){
		return sanity;
	}
	
	public void decreaseSanity(float amount){
		increaseSanity(-amount);
	}
	
	public void increaseSanity(float amount){
		setSanity(sanity + amount);
	}
	
	public void setSanity(float sanity){
		this.sanity = sanity;
		checkSanity();
	}
	
	private void checkSanity(){
		sanity = Floats.constrainToRange(sanity, -200, 100);
	}
	
	public SanityLevel getSanity(){
		if(sanity >= 50)return SanityLevel.VERY_HEALTHY; 	//50 to 100
		if(sanity >= -10)return SanityLevel.HEALTHY;		//-10 to 50
		if(sanity >= -75)return SanityLevel.DAMAGED;		//-75 to -10
		if(sanity >= -150)return SanityLevel.VERY_DAMAGED;	//-150 to -75
		return SanityLevel.INSANE;							//-200 to -150
	}
	
	public static enum SanityLevel{
		VERY_HEALTHY("Very Healthy", TextFormatting.DARK_GREEN),
		HEALTHY("Healthy", TextFormatting.GREEN),
		DAMAGED("Damaged", TextFormatting.RED),
		VERY_DAMAGED("Very Damaged", TextFormatting.DARK_RED),
		INSANE("Insane", TextFormatting.DARK_PURPLE);
		
		private String name;
		private TextFormatting color;
		private SanityLevel(String name, TextFormatting color) {
			this.name = name;
			this.color = color;
		}
		public String toString(){return name;}
		public String toColoredString(){return color + name;}
	}
	
	public static class SanityCapabilityStorage implements Capability.IStorage<SanityCapability> {

		@Override
		public NBTBase writeNBT(Capability<SanityCapability> capability, SanityCapability instance, EnumFacing side) {
			NBTTagCompound tag = new NBTTagCompound();
			tag.setFloat("sanity", instance.sanity);
			return tag;
		}

		@Override
		public void readNBT(Capability<SanityCapability> capability, SanityCapability instance, EnumFacing side, NBTBase nbt) {
			NBTTagCompound tag = (NBTTagCompound) nbt;
			instance.sanity = tag.getFloat("sanity");
		}

	}

	public static class SanityCapabilityFactory implements Callable<SanityCapability> {

		@Override
		public SanityCapability call() throws Exception {
			return new SanityCapability();
		}
	}
	
	public static class SanityCapabilityProvider implements ICapabilityProvider, ICapabilitySerializable<NBTTagCompound>{

		private SanityCapability cap;
		
		@Override
		public NBTTagCompound serializeNBT() {
			if(cap == null)return new NBTTagCompound();
			return (NBTTagCompound) INSTANCE.getStorage().writeNBT(INSTANCE, cap, null);
		}

		@Override
		public void deserializeNBT(NBTTagCompound nbt) {
			if(cap == null)cap = INSTANCE.getDefaultInstance();
			INSTANCE.getStorage().readNBT(INSTANCE, cap, null, nbt);
		}
		
		@Override
		public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
			if(capability == SanityCapability.INSTANCE)return true;
			return false;//implements ICapabilitySerializable<T extends NBTBase>
		}
		
		@SuppressWarnings("unchecked")
		@Override
		public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
			if(capability == SanityCapability.INSTANCE){
				if(cap == null)cap = INSTANCE.getDefaultInstance();
				return (T) cap;
			}
			return null;
		}
	}

}
