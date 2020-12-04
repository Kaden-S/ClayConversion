package com.Kaden.clayconversion;

import java.io.File;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Logger;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid ="clayconversion", version = "1.2", name = "Clay Conversion")
public class ClayConversion
{   
	public static Configuration config;
	
	private static final String CATEGORY_GENERAL = "general";
	private static final String CATEGORY_RECIPES = "recipes";

    public static boolean enderPearlFullStack = false;
    public static boolean clayballRecipe = true;
    public static boolean snowballRecipe = true;
    public static boolean glowstonedustRecipe = true;
    
    public static void readConfig() {
        Configuration cfg = config;
        try {
            cfg.load();
            initGeneral(cfg);
            initRecipes(cfg);
        } catch (Exception e1) {
            logger.log(Level.ERROR, "Problem loading config file!", e1);
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
    }

    private static void initGeneral(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
        enderPearlFullStack = cfg.getBoolean("Enable Full Stack Of Ender Pearls", CATEGORY_GENERAL, false, "Set to true if you want to ender pearls to stack to 64 instead of 16.");
    }
    private static void initRecipes(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_RECIPES, "Recipes configuration");
        clayballRecipe = cfg.getBoolean("Enable Clay Recipe", CATEGORY_RECIPES, true, "Set to true if you want to enable the clayball recipe.");
        snowballRecipe = cfg.getBoolean("Enable Snow Recipe", CATEGORY_RECIPES, true, "Set to true if you want to enable the snowball recipe.");
        glowstonedustRecipe = cfg.getBoolean("Enable Glowstone Recipe", CATEGORY_RECIPES, true, "Set to true if you want to enable the glowstone dust recipe.");
    }
    
    public static Logger logger;
    
    @EventHandler
    public void init(FMLPreInitializationEvent e)
    {
    	logger = e.getModLog();
    	File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "clayconversion.cfg"));
        readConfig();
        
        if(enderPearlFullStack == true) {
        Items.ender_pearl.setMaxStackSize(64);
        }
        if(clayballRecipe == true) {
        GameRegistry.addShapelessRecipe(new ItemStack(Items.clay_ball, 4), Blocks.clay);
        }
        if(snowballRecipe == true) {
        GameRegistry.addShapelessRecipe(new ItemStack(Items.snowball, 4), Blocks.snow);
        }
        if(glowstonedustRecipe == true) {
        GameRegistry.addShapelessRecipe(new ItemStack(Items.glowstone_dust, 4), Blocks.glowstone);
        }
    }
    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {
    	if (config.hasChanged()) {
            config.save();
    	}
    }
}
