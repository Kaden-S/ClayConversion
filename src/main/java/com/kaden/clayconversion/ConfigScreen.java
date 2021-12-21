package com.kaden.clayconversion;

import java.util.Arrays;
import java.util.List;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.config.ModConfig;
import net.minecraft.client.CycleOption;

@OnlyIn(Dist.CLIENT)
public class ConfigScreen extends Screen {

	private OptionsList stackingList;
	private Button doneButton = null, allOnButton, allOffButton, resetButton;

	private static ConfigScreen cfgScreen;

	protected ConfigScreen(TextComponent titleIn) {
		super(titleIn);
		cfgScreen = this;
	}

	@Override
	public void render(PoseStack matrix, int mouseX, int mouseY, float partialTicks) {
		renderBackground(matrix);
		stackingList.render(matrix, mouseX, mouseY, partialTicks);
		drawCenteredString(matrix, font, title.getString(), width / 2, 8, 0xFFFFFFFF);
		drawCenteredString(matrix, font, "Changes apply on restart.", width / 2, height - 12, 0xFFFFFFFF);
		super.render(matrix, mouseX, mouseY, partialTicks);
	}

	@Override
	protected void init() {
		if (doneButton == null) {
			doneButton = new Button(2, height - 22, 50, 20, new TextComponent("Done"), b -> {
				// TODO
//				Minecraft.getInstance().setScreen(new ModListScreen(new MainMenuScreen()));
//				Config.loadConfig(ModConfig.Type.COMMON);
			});
			allOnButton = new Button(width - 104, height - 22, 50, 20, new TextComponent("All on"), b -> {
				Config.emptyBucketsFullStackEnabled.set(true);
				Config.enderPearlFullStackEnabled.set(true);
				Config.snowballFullStackEnabled.set(true);
				Config.clayRecipeEnabled.set(true);
				Config.glowstoneRecipeEnabled.set(true);
				Config.snowRecipeEnabled.set(true);
				Config.quartzRecipeEnabled.set(true);
				testValues();
				init();
			});
			allOffButton = new Button(width - 52, height - 22, 50, 20, new TextComponent("All off"), b -> {
				Config.emptyBucketsFullStackEnabled.set(false);
				Config.enderPearlFullStackEnabled.set(false);
				Config.snowballFullStackEnabled.set(false);
				Config.clayRecipeEnabled.set(false);
				Config.glowstoneRecipeEnabled.set(false);
				Config.snowRecipeEnabled.set(false);
				Config.quartzRecipeEnabled.set(false);
				testValues();
				init();
			});
			resetButton = new Button(54, height - 22, 70, 20, new TextComponent("Defaults"), b -> {
				Config.emptyBucketsFullStackEnabled.set(false);
				;
				Config.enderPearlFullStackEnabled.set(false);
				Config.snowballFullStackEnabled.set(false);
				Config.clayRecipeEnabled.set(true);
				Config.glowstoneRecipeEnabled.set(true);
				Config.snowRecipeEnabled.set(true);
				Config.quartzRecipeEnabled.set(true);
				testValues();
				init();
			});
			testValues();
		}
		stackingList = new OptionsList(minecraft, width, height, 24, height - 25, 25);
		stackingList.addBig(CycleOption.createOnOff("Snowballs stack to 64", a -> {
			return Config.snowballFullStackEnabled.get();
		}, (a, b, c) -> {
			Config.snowballFullStackEnabled.set(c);
			testValues();
		}));
		stackingList.addBig(CycleOption.createOnOff("Ender Pearls stack to 64", a -> {
			return Config.enderPearlFullStackEnabled.get();
		}, (a, b, c) -> {
			Config.enderPearlFullStackEnabled.set(c);
			testValues();
		}));
		stackingList.addBig(CycleOption.createOnOff("Empty Buckets stack to 64", a -> {
			return Config.emptyBucketsFullStackEnabled.get();
		}, (a, b, c) -> {
			Config.emptyBucketsFullStackEnabled.set(c);
			testValues();
		}));
		stackingList.addBig(CycleOption.createOnOff("Clay block to clay ball recipe", a -> {
			return Config.clayRecipeEnabled.get();
		}, (a, b, c) -> {
			Config.clayRecipeEnabled.set(c);
			testValues();
		}));
		stackingList.addBig(CycleOption.createOnOff("Glowstone block to glowstone dust recipe", a -> {
			return Config.glowstoneRecipeEnabled.get();
		}, (a, b, c) -> {
			Config.glowstoneRecipeEnabled.set(c);
			testValues();
		}));
		stackingList.addBig(CycleOption.createOnOff("Snow block to snow ball recipe", a -> {
			return Config.snowRecipeEnabled.get();
		}, (a, b, c) -> {
			Config.snowRecipeEnabled.set(c);
			testValues();
		}));
		stackingList.addBig(CycleOption.createOnOff("Quartz block to quartz recipe", a -> {
			return Config.quartzRecipeEnabled.get();
		}, (a, b, c) -> {
			Config.quartzRecipeEnabled.set(c);
			testValues();
		}));
		addWidget(doneButton);
		addWidget(resetButton);
		//TODO
//		this.children.add(this.stackingList);
		super.init();
	}

	private static void testValues() {
		List<Boolean> cfgValues = Arrays.asList(new Boolean[] { Config.emptyBucketsFullStackEnabled.get(),
				Config.enderPearlFullStackEnabled.get(), Config.snowRecipeEnabled.get(), Config.clayRecipeEnabled.get(),
				Config.glowstoneRecipeEnabled.get(), Config.snowballFullStackEnabled.get(),
				Config.quartzRecipeEnabled.get() });
		if (!cfgValues.contains(false)) {
			cfgScreen.allOnButton.active = false;
			cfgScreen.allOffButton.active = true;
		} else if (!cfgValues.contains(true)) {
			cfgScreen.allOffButton.active = false;
			cfgScreen.allOnButton.active = true;
		} else {
			cfgScreen.allOnButton.active = true;
			cfgScreen.allOffButton.active = true;
		}
		cfgScreen.addWidget(cfgScreen.allOnButton);
		cfgScreen.addWidget(cfgScreen.allOffButton);
	}
}



