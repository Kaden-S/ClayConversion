package kaden.clayconversion;

import net.minecraft.item.ItemEnderPearl;

import net.minecraft.item.Item;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemSnowball;
import net.minecraft.item.Item.Properties;
import net.minecraft.util.ResourceLocation;
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
        ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, ModConfigFile.cfg);
        ModConfigFile.loadConfig(ModConfigFile.cfg, FMLPaths.CONFIGDIR.get().resolve(modid +"-common.toml"));
        
        FMLJavaModLoadingContext.get().getModEventBus().addListener(this::setup);
	}
	
	public void setup(FMLCommonSetupEvent e) {
    	CraftingHelper.register(new ResourceLocation(modid, "clay_enabled"), json -> () -> ModConfigFile.clayRecipeEnabled.get());
    	CraftingHelper.register(new ResourceLocation(modid, "snow_enabled"), json -> () -> ModConfigFile.snowRecipeEnabled.get());
    	CraftingHelper.register(new ResourceLocation(modid, "quartz_enabled"), json -> () -> ModConfigFile.quartzRecipeEnabled.get());
    	CraftingHelper.register(new ResourceLocation(modid, "glowstone_enabled"), json -> () -> ModConfigFile.glowstoneRecipeEnabled.get());
	}

	@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD)
	    public static class RegistryEvents {
		
	        @SubscribeEvent
	        public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
	        	if(ModConfigFile.enderPearlFullStackEnabled.get())
	        		event.getRegistry().register(new ItemEnderPearl((new Properties()).maxStackSize(64).group(ItemGroup.MISC)).setRegistryName("minecraft", "ender_pearl"));
	        	if(ModConfigFile.snowballFullStackEnabled.get())
	        		event.getRegistry().register(new ItemSnowball((new Properties()).maxStackSize(64).group(ItemGroup.MISC)).setRegistryName("minecraft", "snowball"));
	        }	        
	 }
}
	