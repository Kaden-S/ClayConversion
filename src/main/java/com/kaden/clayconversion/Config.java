package com.kaden.clayconversion;


import com.electronwill.nightconfig.core.file.CommentedFileConfig;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraftforge.fml.loading.FMLPaths;


public class Config {

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
