package com.kaden.clayconversion;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.SnowballItem;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigGuiHandler.ConfigGuiFactory;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLDedicatedServerSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;

@Mod("clayconversion")
public class ClayConversion {

	public static final String modid = "clayconversion";

	public ClayConversion() {
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.cfg);
		Config.loadConfig(ModConfig.Type.COMMON);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::commonSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::clientSetup);
		FMLJavaModLoadingContext.get().getModEventBus().addListener(this::serverSetup);
	}

	private void commonSetup(final FMLCommonSetupEvent e) {
		CraftingHelper.register(new EnabledCondition(null).new Serializer());
	}

	private void clientSetup(final FMLClientSetupEvent event) {
		DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientSide::new);
	}

	private void serverSetup(final FMLDedicatedServerSetupEvent event) {
		DistExecutor.safeCallWhenOn(Dist.DEDICATED_SERVER, () -> ServerSide::new);
	}

	public class ClientSide {
		public ClientSide() {
//			LogManager.getLogger().debug("Client side");
			BiFunction<Minecraft, Screen, Screen> factorySupplier = (mc,
					screen) -> new ConfigScreen(new TextComponent("Clay Conversion Config"), screen);

			ModLoadingContext.get().registerExtensionPoint(ConfigGuiFactory.class,
					() -> new ConfigGuiFactory(factorySupplier));
		}
	}

	public class ServerSide {
		public ServerSide() {
//			LogManager.getLogger().debug("Server side");
		}
	}

	@Mod.EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {

		@SubscribeEvent
		public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
			Properties properties = new Properties().stacksTo(64).tab(CreativeModeTab.TAB_MISC);
			List<Item> items = new ArrayList<>();

			if (Config.enderPearlFullStackEnabled.get())
				items.add(new EnderpearlItem(properties).setRegistryName("minecraft", "ender_pearl"));

			if (Config.snowballFullStackEnabled.get())
				items.add(new SnowballItem(properties).setRegistryName("minecraft", "snowball"));

			if (Config.emptyBucketsFullStackEnabled.get())
				items.add(new BucketItem(() -> Fluids.EMPTY, properties).setRegistryName("minecraft", "bucket"));

			if (items.size() > 0)
				event.getRegistry().registerAll(items.toArray(new Item[] {}));
		}
	}
}
