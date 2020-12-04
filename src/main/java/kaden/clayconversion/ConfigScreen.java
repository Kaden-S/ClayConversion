package kaden.clayconversion;

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

	protected ConfigScreen(ITextComponent titleIn) {
		super(titleIn);
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
		Button doneButton = new Button(2, height - 22, 50, 20, TextComponentUtils.toTextComponent(() -> "Done"), b -> {
			Minecraft.getInstance().displayGuiScreen(new ModListScreen(new MainMenuScreen()));
			Config.loadConfig(ModConfig.Type.COMMON);
		});
		stackingList = new OptionsRowList(minecraft, width, height, 24, height - 25, 25);
		stackingList.addOption(new BooleanOption("Snowballs stack to 64", null, (a) -> {
			return Config.snowballFullStackEnabled.get();
		}, (a, b) -> {
			Config.snowballFullStackEnabled.set(b);
		}));
		stackingList.addOption(new BooleanOption("Ender Pearls stack to 64", null, (a) -> {
			return Config.enderPearlFullStackEnabled.get();
		}, (a, b) -> {
			Config.enderPearlFullStackEnabled.set(b);
		}));
		stackingList.addOption(new BooleanOption("Clay block to clay ball recipe", null, (a) -> {
			return Config.clayRecipeEnabled.get();
		}, (a, b) -> {
			Config.clayRecipeEnabled.set(b);
		}));
		stackingList.addOption(new BooleanOption("Glowstone block to glowstone dust recipe", null, (a) -> {
			return Config.glowstoneRecipeEnabled.get();
		}, (a, b) -> {
			Config.glowstoneRecipeEnabled.set(b);
		}));
		stackingList.addOption(new BooleanOption("Snow block to snow ball recipe", null, (a) -> {
			return Config.snowRecipeEnabled.get();
		}, (a, b) -> {
			Config.snowRecipeEnabled.set(b);
		}));
		stackingList.addOption(new BooleanOption("Quartz block to quartz recipe", null, (a) -> {
			return Config.quartzRecipeEnabled.get();
		}, (a, b) -> {
			Config.quartzRecipeEnabled.set(b);
		}));
		addButton(doneButton);
		this.children.add(this.stackingList);
		super.init();
	}
}