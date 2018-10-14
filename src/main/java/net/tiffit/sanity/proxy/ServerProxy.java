package net.tiffit.sanity.proxy;

import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.relauncher.Side;

@Mod.EventBusSubscriber(Side.SERVER)
public class ServerProxy extends CommonProxy {
	
    @Override
    public void preInit(FMLPreInitializationEvent e) {
        super.preInit(e);
    }
    
}
