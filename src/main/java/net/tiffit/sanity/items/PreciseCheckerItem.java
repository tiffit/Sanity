package net.tiffit.sanity.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumHand;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.tiffit.sanity.Sanity;
import net.tiffit.sanity.SanityCapability;

public class PreciseCheckerItem extends Item {

	public PreciseCheckerItem(){
		setUnlocalizedName(Sanity.MODID + ".precisechecker");
		setRegistryName("precisechecker");
		setCreativeTab(Sanity.CTAB);
		setMaxStackSize(1);
	}
	
	@Override
	public ActionResult<ItemStack> onItemRightClick(World w, EntityPlayer p, EnumHand h) {
		if(!w.isRemote){
			SanityCapability cap = p.getCapability(SanityCapability.INSTANCE, null);
			p.sendMessage(new TextComponentString(TextFormatting.GOLD + "Mental State: " + cap.getSanity().toColoredString() + " [" + ItemStack.DECIMALFORMAT.format(cap.getSanityExact()) + "]"));
		}
		return new ActionResult<ItemStack>(EnumActionResult.SUCCESS, p.getHeldItem(h));
	}
	
}
