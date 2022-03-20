package com.kaden.clayconversion;


import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

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
      enderPearlFullStackEnabled = cfgBuilder.comment("\nEnable Full Stack of Ender Pearls [Default: false]")
        .define("ender_pearl_stacks", false);
      snowballFullStackEnabled = cfgBuilder.comment("\nEnable Full Stack of Snow Balls [Default: false]")
        .define("snowball_stacks", false);
      emptyBucketsFullStackEnabled = cfgBuilder.comment("\nEnable Full Stack of Empty Buckets [Default: false]")
        .define("empty_bucket_stacks", false);

      cfgBuilder.comment("Recipes settings").push(recipesCategory);
      {
        clayRecipeEnabled = cfgBuilder.comment("Enable Clay Recipe [Default: true]").define("clay_recipe", true);
        snowRecipeEnabled = cfgBuilder.comment("\nEnable Snow Recipe [Default: true]").define("snowball_recipe", true);
        quartzRecipeEnabled = cfgBuilder.comment("\nEnable Quartz Recipe [Default: true]").define("quartz_recipe",
          true);
        glowstoneRecipeEnabled = cfgBuilder.comment("\nEnable Glowstone Recipe [Default: true]")
          .define("glowstone_recipe", true);
      }
      cfgBuilder.pop();
    }
    cfgBuilder.pop();

    cfg = cfgBuilder.build();
  }

  static void loadConfig(ModConfig.Type type) {
    CommentedFileConfig file = CommentedFileConfig
      .builder(
        FMLPaths.CONFIGDIR.get().resolve(ClayConversion.modid + "-" + type.toString().toLowerCase() + ".toml").toFile())
      .sync().autosave().writingMode(WritingMode.REPLACE).build();
    file.load();
    cfg.setConfig(file);
  }
}
