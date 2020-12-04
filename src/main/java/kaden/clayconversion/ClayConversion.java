package kaden.clayconversion;

import java.io.File;

import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;

@Mod(modid ="clayconversion", version = "1.4", name = "Clay Conversion")
public class ClayConversion
{   
	public static Configuration config;
	
	private static final String CATEGORY_GENERAL = "general";
	private static final String CATEGORY_RECIPES = "recipes";

	public static final String modid = "clayconversion";
    public static boolean enderPearlFullStack = false;
    public static boolean snowballFullStack = false;
    public static boolean clayballRecipe = true;
    public static boolean snowballRecipe = true;
    public static boolean glowstonedustRecipe = true;
    public static boolean quartzRecipe = true;
    
    public static void readConfig() {
        Configuration cfg = config;
        try {
            cfg.load();
            initGeneralCFG(cfg);
            initRecipeCFG(cfg);
        } catch (Exception e) {
        } finally {
            if (cfg.hasChanged()) {
                cfg.save();
            }
        }
    }

    private static void initGeneralCFG(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_GENERAL, "General configuration");
        enderPearlFullStack = cfg.getBoolean("Enable Full Stack Of Ender Pearls", CATEGORY_GENERAL, false, "Set to true if you want ender pearls to stack to 64 instead of 16.");
        snowballFullStack = cfg.getBoolean("Enable Full Stack Of Snowballs", CATEGORY_GENERAL, false, "Set to true if you want snow balls to stack to 64 instead of 16.");
    }
    private static void initRecipeCFG(Configuration cfg) {
        cfg.addCustomCategoryComment(CATEGORY_RECIPES, "Recipes configuration");
        clayballRecipe = cfg.getBoolean("Enable Clay Recipe", CATEGORY_RECIPES, true, "Set to true if you want to enable the clayball recipe.");
        snowballRecipe = cfg.getBoolean("Enable Snow Recipe", CATEGORY_RECIPES, true, "Set to true if you want to enable the snowball recipe.");
        glowstonedustRecipe = cfg.getBoolean("Enable Glowstone Recipe", CATEGORY_RECIPES, true, "Set to true if you want to enable the glowstone dust recipe.");
		quartzRecipe = cfg.getBoolean("Enable Quartz Recipe", CATEGORY_RECIPES, true, "Set to true if you want to enable the quartz recipe.");
    }
    
    @EventHandler
    public void init(FMLInitializationEvent e) {
    	if(clayballRecipe)
    		GameRegistry.addShapelessRecipe(new ResourceLocation("minecraft:CLAY"), new ResourceLocation("minecraft:CLAY_BALL"), new ItemStack(Items.CLAY_BALL, 4), Ingredient.fromItems(Item.getItemFromBlock(Blocks.CLAY)));
    	if(snowballRecipe)
    		GameRegistry.addShapelessRecipe(new ResourceLocation("minecraft:SNOW"), new ResourceLocation("minecraft:SNOWBALL"), new ItemStack(Items.SNOWBALL, 4), Ingredient.fromItems(Item.getItemFromBlock(Blocks.SNOW)));
    	if(glowstonedustRecipe)
    		GameRegistry.addShapelessRecipe(new ResourceLocation("minecraft:GLOWSTONE"), new ResourceLocation("minecraft:GLOWSTONE_DUST"), new ItemStack(Items.GLOWSTONE_DUST, 4), Ingredient.fromItems(Item.getItemFromBlock(Blocks.GLOWSTONE)));
    	if(quartzRecipe)
    		GameRegistry.addShapelessRecipe(new ResourceLocation("minecraft:QUARTZ_BLOCK"), new ResourceLocation("minecraft:QUARTZ"), new ItemStack(Items.QUARTZ, 4), Ingredient.fromItems(Item.getItemFromBlock(Blocks.QUARTZ_BLOCK)));
    }
    
    @EventHandler
    public void preInit(FMLPreInitializationEvent e) {
    	File directory = e.getModConfigurationDirectory();
        config = new Configuration(new File(directory.getPath(), "clayconversion.cfg"));
        readConfig();

        if(enderPearlFullStack) 
        	Items.ENDER_PEARL.setMaxStackSize(64);
        if(snowballFullStack) 
        	Items.SNOWBALL.setMaxStackSize(64);
    }
    @EventHandler
    public void postInit(FMLPostInitializationEvent e) {	
    	if (config.hasChanged()) {
            config.save();
    	}
    }
}    