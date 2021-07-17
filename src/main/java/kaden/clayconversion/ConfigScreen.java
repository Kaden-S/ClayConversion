package kaden.clayconversion;

import java.util.Arrays;
import java.util.List;

import com.mojang.blaze3d.matrix.MatrixStack;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.MainMenuScreen;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.gui.widget.list.OptionsRowList;
import net.minecraft.client.settings.BooleanOption;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextComponentUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.client.gui.screen.ModListScreen;
import net.minecraftforge.fml.config.ModConfig;

@OnlyIn(Dist.CLIENT)
public class ConfigScreen extends Screen {

	private OptionsRowList stackingList;
	private Button doneButton = null, allOnButton, allOffButton, resetButton;

	private static ConfigScreen cfgScreen;

	protected ConfigScreen(ITextComponent titleIn) {
		super(titleIn);
		cfgScreen = this;
	}

	@Override
	public void render(MatrixStack matrix, int mouseX, int mouseY, float partialTicks) {
		renderBackground(matrix);
		stackingList.render(matrix, mouseX, mouseY, partialTicks);
		drawCenteredString(matrix, font, title.getString(), width / 2, 8, 0xFFFFFFFF);
		drawCenteredString(matrix, font, "Changes apply on restart.", width / 2, height - 12, 0xFFFFFFFF);
		super.render(matrix, mouseX, mouseY, partialTicks);
	}

	@Override
	protected void init() {
		if (doneButton == null) {
			doneButton = new Button(2, height - 22, 50, 20, TextComponentUtils.fromMessage(() -> "Done"), b -> {
				Minecraft.getInstance().setScreen(new ModListScreen(new MainMenuScreen()));
				Config.loadConfig(ModConfig.Type.COMMON);
			});
			allOnButton = new Button(width-104, height - 22, 50, 20, TextComponentUtils.fromMessage(() -> "All on"), b -> {
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
			allOffButton = new Button(width-52, height - 22, 50, 20, TextComponentUtils.fromMessage(() -> "All off"), b -> {
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
			resetButton = new Button(54, height - 22, 70, 20, TextComponentUtils.fromMessage(() -> "Defaults"), b -> {
				Config.emptyBucketsFullStackEnabled.set(false);;
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
		stackingList = new OptionsRowList(minecraft, width, height, 24, height - 25, 25);
		stackingList.addBig(new BooleanOption("Snowballs stack to 64", a -> {
			return Config.snowballFullStackEnabled.get();
		}, (a, b) -> {
			Config.snowballFullStackEnabled.set(b);
			testValues();
		}));
		stackingList.addBig(new BooleanOption("Ender Pearls stack to 64", a -> {
			return Config.enderPearlFullStackEnabled.get();
		}, (a, b) -> {
			Config.enderPearlFullStackEnabled.set(b);
			testValues();
		}));
		stackingList.addBig(new BooleanOption("Empty Buckets stack to 64", a -> {
			return Config.emptyBucketsFullStackEnabled.get();
		}, (a, b) -> {
			Config.emptyBucketsFullStackEnabled.set(b);
			testValues();
		}));
		stackingList.addBig(new BooleanOption("Clay block to clay ball recipe", a -> {
			return Config.clayRecipeEnabled.get();
		}, (a, b) -> {
			Config.clayRecipeEnabled.set(b);
			testValues();
		}));
		stackingList.addBig(new BooleanOption("Glowstone block to glowstone dust recipe", a -> {
			return Config.glowstoneRecipeEnabled.get();
		}, (a, b) -> {
			Config.glowstoneRecipeEnabled.set(b);
			testValues();
		}));
		stackingList.addBig(new BooleanOption("Snow block to snow ball recipe", a -> {
			return Config.snowRecipeEnabled.get();
		}, (a, b) -> {
			Config.snowRecipeEnabled.set(b);
			testValues();
		}));
		stackingList.addBig(new BooleanOption("Quartz block to quartz recipe", a -> {
			return Config.quartzRecipeEnabled.get();
		}, (a, b) -> {
			Config.quartzRecipeEnabled.set(b);
			testValues();
		}));
		addButton(doneButton);
		addButton(resetButton);
		this.children.add(this.stackingList);
		super.init();
	}

	private static void testValues() {
		List<Boolean> cfgValues = Arrays.asList(new Boolean[] { Config.emptyBucketsFullStackEnabled.get(),
				Config.enderPearlFullStackEnabled.get(), Config.snowRecipeEnabled.get(),
				Config.clayRecipeEnabled.get(), Config.glowstoneRecipeEnabled.get(),
				Config.snowballFullStackEnabled.get(), Config.quartzRecipeEnabled.get() });
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
		cfgScreen.addButton(cfgScreen.allOnButton);
		cfgScreen.addButton(cfgScreen.allOffButton);
	}
}