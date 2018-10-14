package net.tiffit.sanity.items;

import java.util.List;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.tiffit.sanity.Sanity;

public class SpecterBreadItem extends ItemFood {

	public SpecterBreadItem(){
		super(8, 2F, false);
		setUnlocalizedName(Sanity.MODID + ".specterbread");
		setRegistryName("specterbread");
		setCreativeTab(Sanity.CTAB);
	}
	
	@Override
	public void addInformation(ItemStack stack, World worldIn, List<String> tooltip, ITooltipFlag flagIn) {
		tooltip.add("Might not be the best for your sanity...");
		tooltip.add("but it sure does fill you up!");
		super.addInformation(stack, worldIn, tooltip, flagIn);
	}
	
}
