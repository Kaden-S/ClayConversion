package com.kaden.clayconversion;


import com.kaden.clayconversion.Config.ConfigType;

import net.minecraft.world.item.BucketItem;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.EnderpearlItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Item.Properties;
import net.minecraft.world.item.SnowballItem;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.ConfigGuiHandler.ConfigGuiFactory;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.crafting.CraftingHelper;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.event.lifecycle.FMLClientSetupEvent;
import net.minecraftforge.fml.event.lifecycle.FMLCommonSetupEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;


@Mod("clayconversion")
public class ClayConversion {

  public static final String MODID = "clayconversion";
  public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, "minecraft");

  public ClayConversion() {
    ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, Config.cfg);
    Config.loadConfig(ModConfig.Type.COMMON);

    IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
    modEventBus.addListener(this::commonSetup);
    modEventBus.addListener(this::clientSetup);
    registerItems(modEventBus);

    MinecraftForge.EVENT_BUS.register(this);
  }

  private void commonSetup(final FMLCommonSetupEvent e) {
    CraftingHelper.register(new EnabledCondition(null).new Serializer());
  }

  private void clientSetup(final FMLClientSetupEvent event) {
    DistExecutor.safeCallWhenOn(Dist.CLIENT, () -> ClientSide::new);
  }

  private static void registerItems(IEventBus modEventBus) {
    Properties properties = new Properties().stacksTo(64).tab(CreativeModeTab.TAB_MISC);

    if (ConfigType.PEARL_STACK.enabled()) ITEMS.register("ender_pearl", () -> new EnderpearlItem(properties));
    if (ConfigType.SNOW_STACK.enabled()) ITEMS.register("snowball", () -> new SnowballItem(properties));
    if (ConfigType.BUCKET_STACK.enabled())
      ITEMS.register("bucket", () -> new BucketItem(() -> Fluids.EMPTY, properties));

    ITEMS.register(modEventBus);
  }

  public class ClientSide {

    public ClientSide() {
      ModLoadingContext.get().registerExtensionPoint(ConfigGuiFactory.class,
        () -> new ConfigGuiFactory((mc, screen) -> new ConfigScreen(screen)));
    }
  }
}
