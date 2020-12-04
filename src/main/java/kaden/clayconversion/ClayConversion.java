package kaden.clayconversion;

import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SnowballItem;
import net.minecraft.item.Item.Properties;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.fml.loading.FMLPaths;

@Mod(ClayConversion.modid)
public class ClayConversion{
	
	public static final String modid = "clayconversion";
	
	public ClayConversion() {
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.cfg);
        Config.loadConfig(Config.cfg, FMLPaths.CONFIGDIR.get().resolve(modid +"-common.toml"));
        
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
	}
	
	public void setup(FMLCommonSetupEvent e) {
    	CraftingHelper.register(new EnabledCondition(null).new Serializer());
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	    public static class RegistryEvents {
		
	        @SubscribeEvent
	        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
	        	if(Config.enderPearlFullStackEnabled.get())
	        		event.getRegistry().register(new EnderPearlItem((new Properties()).maxStackSize(64).group(ItemGroup.MISC)).setRegistryName("minecraft", "ender_pearl"));
	        	if(Config.snowballFullStackEnabled.get())
	        		event.getRegistry().register(new SnowballItem((new Properties()).maxStackSize(64).group(ItemGroup.MISC)).setRegistryName("minecraft", "snowball"));
	        }	        
	 }
}
	