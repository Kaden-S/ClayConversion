package com.kaden.clayconversion;


import com.electronwill.nightconfig.core.file.CommentedFileConfig;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;


public class Config {

  public static enum ConfigType {

    CLAY_RECIPE("clay", "Clay Recipe", "Clay block to clay ball recipe", true),
    GLOWSTONE_RECIPE("glowstone", "Glowstone Recipe", "Glowstone block to glowstone dust recipe", true),
    QUARTZ_RECIPE("quartz", "Quartz Recipe", "Quartz block to quartz recipe", true),
    SNOW_RECIPE("snow", "Snow Recipe", "Snow block to snow ball recipe", true),
    BUCKET_STACK("bucket_stack", "Fullstack Buckets", "Empty Buckets stack to 64", false),
    PEARL_STACK("pearl_stack", "Fullstack Ender Pearls", "Ender Pearls stack to 64", false),
    SNOW_STACK("snow_stack", "Fullstack Snowball", "Snowballs stack to 64", false);

    public final String id;
    public final String name;
    public final String desc;
    public final boolean enabledByDefault;
    private final BooleanValue value;

    private ConfigType(String id, String name, String desc, boolean defaultEnabled) {
      this.id = id;
      this.name = name;
      this.desc = desc;
      this.enabledByDefault = defaultEnabled;

      this.value = getBooleanValue(this);
    }

    public boolean enabled() {
      return value.get();
    }

    public void set(boolean enabled) {
      value.set(enabled);
    }

    public static ConfigType getById(String id) {
      for (var cfg : ConfigType.values())
        if (cfg.id == id) return cfg;

      return null;
    }

  }

  private static BooleanValue getBooleanValue(ConfigType cfg) {
    switch (cfg) {
    case CLAY_RECIPE:
      return Config.clayRecipeEnabled;
    case GLOWSTONE_RECIPE:
      return Config.glowstoneRecipeEnabled;
    case QUARTZ_RECIPE:
      return Config.quartzRecipeEnabled;
    case SNOW_RECIPE:
      return Config.snowRecipeEnabled;
    case BUCKET_STACK:
      return Config.emptyBucketsFullStackEnabled;
    case PEARL_STACK:
      return Config.enderPearlFullStackEnabled;
    case SNOW_STACK:
      return Config.snowballFullStackEnabled;
    default:
      throw new Error("Unreachable code");
    }
  }

  private static final String generalCategory = "general";
  private static final String recipesCategory = "recipes";

  static ForgeConfigSpec cfg;

  private static final BooleanValue clayRecipeEnabled;
  private static final BooleanValue snowRecipeEnabled;
  private static final BooleanValue quartzRecipeEnabled;
  private static final BooleanValue glowstoneRecipeEnabled;
  private static final BooleanValue enderPearlFullStackEnabled;
  private static final BooleanValue snowballFullStackEnabled;
  private static final BooleanValue emptyBucketsFullStackEnabled;

  static {
    ForgeConfigSpec.Builder cfgBuilder = new ForgeConfigSpec.Builder();

    cfgBuilder.comment("General settings").push(generalCategory);
    {
      cfgBuilder.comment("Enable Full Stack of Ender Pearls [Default: false]");
      enderPearlFullStackEnabled = cfgBuilder.define("ender_pearl_stacks", false);
      cfgBuilder.comment("Enable Full Stack of Snow Balls [Default: false]");
      snowballFullStackEnabled = cfgBuilder.define("snowball_stacks", false);
      cfgBuilder.comment("Enable Full Stack of Empty Buckets [Default: false]");
      emptyBucketsFullStackEnabled = cfgBuilder.define("empty_bucket_stacks", false);

      cfgBuilder.comment("Recipes settings").push(recipesCategory);
      {
        cfgBuilder.comment("Enable Clay Recipe [Default: true]");
        clayRecipeEnabled = cfgBuilder.define("clay_recipe", true);
        cfgBuilder.comment("Enable Snow Recipe [Default: true]");
        snowRecipeEnabled = cfgBuilder.define("snowball_recipe", true);
        cfgBuilder.comment("Enable Quartz Recipe [Default: true]");
        quartzRecipeEnabled = cfgBuilder.define("quartz_recipe", true);
        cfgBuilder.comment("Enable Glowstone Recipe [Default: true]");
        glowstoneRecipeEnabled = cfgBuilder.define("glowstone_recipe", true);
      }
      cfgBuilder.pop();
    }
    cfgBuilder.pop();

    cfg = cfgBuilder.build();
  }

  static void loadConfig(ModConfig.Type type) {
    CommentedFileConfig config = CommentedFileConfig
      .builder(
        FMLPaths.CONFIGDIR.get().resolve(ClayConversion.MODID + "-" + type.toString().toLowerCase() + ".toml").toFile())
      .build();
    config.load();
    cfg.setConfig(config);
  }
}
