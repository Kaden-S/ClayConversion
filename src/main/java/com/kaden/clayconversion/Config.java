package com.kaden.clayconversion;


import com.electronwill.nightconfig.core.file.CommentedFileConfig;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;


public class Config {

  public static enum ConfigType {

    CLAY_RECIPE("Clay Recipe", "Clay block to clay ball recipe", true),
    GLOWSTONE_RECIPE("Glowstone Recipe", "Glowstone block to glowstone dust recipe", true),
    QUARTZ_RECIPE("Quartz Recipe", "Quartz block to quartz recipe", true),
    SNOW_RECIPE("Snow Recipe", "Snow block to snow ball recipe", true),
    BUCKET_STACK("Fullstack Buckets", "Empty Buckets stack to 64", false),
    PEARL_STACK("Fullstack Ender Pearls", "Ender Pearls stack to 64", false),
    SNOW_STACK("Fullstack Snowball", "Snowballs stack to 64", false);

    public final String name;
    public final String desc;
    public final boolean enabledByDefault;

    private ConfigType(String name, String desc, boolean defaultEnabled) {
      this.name = name;
      this.desc = desc;
      this.enabledByDefault = defaultEnabled;
    }

    public boolean enabled() {
      return this.booleanValue().get();
    }

    public BooleanValue booleanValue() {
      return getBooleanValue(this);
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

  static final BooleanValue clayRecipeEnabled;
  static final BooleanValue snowRecipeEnabled;
  static final BooleanValue quartzRecipeEnabled;
  static final BooleanValue glowstoneRecipeEnabled;
  static final BooleanValue enderPearlFullStackEnabled;
  static final BooleanValue snowballFullStackEnabled;
  static final BooleanValue emptyBucketsFullStackEnabled;

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
        FMLPaths.CONFIGDIR.get().resolve(ClayConversion.modid + "-" + type.toString().toLowerCase() + ".toml").toFile())
      .build();
    config.load();
    cfg.setConfig(config);
  }
}
