package net.tiffit.sanity.commands;

import net.minecraft.command.CommandBase;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.text.TextComponentString;
import net.minecraft.util.text.TextFormatting;
import net.tiffit.sanity.SanityCapability;

public class SetSanityCommand extends CommandBase{

	@Override
	public String getName() {
		return "setsanity";
	}

	@Override
	public String getUsage(ICommandSender sender) {
		return "commands.sanity_set.usage";
	}

	@Override
	public int getRequiredPermissionLevel() {
		return 4;
	}
	
	@Override
	public void execute(MinecraftServer server, ICommandSender sender, String[] args) throws CommandException {
		if(sender instanceof EntityPlayer){
			if(args.length == 0)throw new WrongUsageException(getUsage(sender), new Object[0]);
			try{
				int sanity = Integer.valueOf(args[0]);
				EntityPlayer p = (EntityPlayer) sender;
				p.getCapability(SanityCapability.INSTANCE, null).setSanity(sanity);
				sender.sendMessage(new TextComponentString("Sanity changed to " + sanity));
			}catch(NumberFormatException e){
				throw new WrongUsageException(getUsage(sender), new Object[0]);
			}
		}else{
			sender.sendMessage(new TextComponentString(TextFormatting.RED + "This command is player only!"));
		}
	}
	
}
