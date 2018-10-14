package net.tiffit.sanity.items;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.tiffit.sanity.Sanity;

public class SpectergooItem extends Item {

	public SpectergooItem(){
		setUnlocalizedName(Sanity.MODID + ".spectergoo");
		setRegistryName("spectergoo");
		setCreativeTab(Sanity.CTAB);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("A ghastly slimeball!");
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
	
}
