package kaden.clayconversion;

import java.nio.file.Path;

import com.electronwill.nightconfig.core.file.CommentedFileConfig;
import com.electronwill.nightconfig.core.io.WritingMode;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.config.ModConfig;

@Mod.EventBusSubscriber
public class ModConfigFile {
	
	public static final String generalCategory = "general";
    public static final String recipesCategory = "recipes";

    public static ForgeConfigSpec cfg;

    public static ForgeConfigSpec.BooleanValue clayRecipeEnabled;
    public static ForgeConfigSpec.BooleanValue snowRecipeEnabled;
    public static ForgeConfigSpec.BooleanValue quartzRecipeEnabled;
    public static ForgeConfigSpec.BooleanValue glowstoneRecipeEnabled;
    public static ForgeConfigSpec.BooleanValue enderPearlFullStackEnabled;
    public static ForgeConfigSpec.BooleanValue snowballFullStackEnabled;
    
    public static int a;

    static {

        ForgeConfigSpec.Builder cfgBuilder = new ForgeConfigSpec.Builder();
        
        cfgBuilder.comment("General settings").push(generalCategory);
        {
        	enderPearlFullStackEnabled = cfgBuilder.comment("\nEnable Full Stack of Ender Pearls [Default: false]").define("ender_pearl_stacks", false);
        	snowballFullStackEnabled = cfgBuilder.comment("\nEnable Full Stack of Snow Balls [Default: false]").define("snowball_stacks", false);
        
        	cfgBuilder.comment("Recipes settings").push(recipesCategory);
        	{
        		clayRecipeEnabled = cfgBuilder.comment("Enable Clay Recipe [Default: true]").define("clay_recipe", true);
        		snowRecipeEnabled = cfgBuilder.comment("\nEnable Snow Recipe [Default: true]").define("snowball_recipe", true);
        		quartzRecipeEnabled = cfgBuilder.comment("\nEnable Quartz Recipe [Default: true]").define("quartz_recipe", true);
        		glowstoneRecipeEnabled = cfgBuilder.comment("\nEnable Glowstone Recipe [Default: true]").define("glowstone_recipe", true);
        	}
        	cfgBuilder.pop();
        }
        cfgBuilder.pop();
        
        cfg = cfgBuilder.build();
    }

    @SubscribeEvent
    public static void onLoad(final ModConfig.Loading configEvent) {
    }

    @SubscribeEvent
    public static void onReload(final ModConfig.ConfigReloading configEvent) {
    }


	public static void loadConfig(ForgeConfigSpec config, Path path) {
		final CommentedFileConfig file = CommentedFileConfig.builder(path.toFile()).sync().autosave().writingMode(WritingMode.REPLACE).build();
		file.load();
		config.setConfig(file);
	}
}
