package kaden.clayconversion;

import org.apache.logging.log4j.LogManager;

import net.minecraft.fluid.Fluids;
import net.minecraft.item.BucketItem;
import net.minecraft.item.EnderPearlItem;
import net.minecraft.item.Item;
import net.minecraft.item.Item.Properties;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.SnowballItem;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ExtensionPoint;
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
			LogManager.getLogger().debug("Client side");
			ModLoadingContext.get().registerExtensionPoint(ExtensionPoint.CONFIGGUIFACTORY, () -> (mc, screen) -> {
				return new ConfigScreen(TextComponentUtils.toTextComponent(() -> "Clay Conversion Config"));
			});
		}
	}

	public class ServerSide {
		public ServerSide() {
			LogManager.getLogger().debug("Server side");
		}
	}

	@Mod.EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
	public static class RegistryEvents {

		@SubscribeEvent
		public static void onItemsRegistry(final RegistryEvent.Register<Item> event) {
			LogManager.getLogger().debug("Item Registry:\n" + Config.enderPearlFullStackEnabled.get() + "\n"
					+ Config.snowballFullStackEnabled.get());
			if (Config.enderPearlFullStackEnabled.get())
				event.getRegistry()
						.register(new EnderPearlItem((new Properties()).maxStackSize(64).group(ItemGroup.MISC))
								.setRegistryName("minecraft", "ender_pearl"));
			if (Config.snowballFullStackEnabled.get())
				event.getRegistry().register(new SnowballItem((new Properties()).maxStackSize(64).group(ItemGroup.MISC))
						.setRegistryName("minecraft", "snowball"));
			if (Config.emptyBucketsFullStackEnabled.get())
				event.getRegistry().register(new BucketItem(() -> {
					return Fluids.EMPTY;
				}, (new Properties()).maxStackSize(64).group(ItemGroup.MISC)).setRegistryName("minecraft", "bucket"));
		}
	}
}