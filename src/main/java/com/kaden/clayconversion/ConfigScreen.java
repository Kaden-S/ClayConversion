package com.kaden.clayconversion;


import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;

import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.CycleOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.ForgeConfigSpec.BooleanValue;


@OnlyIn(Dist.CLIENT)
public class ConfigScreen extends Screen {

  public enum ConfigType {
    CLAY_RECIPE, GLOWSTONE_RECIPE, QUARTZ_RECIPE, SNOW_RECIPE, BUCKET_STACK, PEARL_STACK, SNOW_STACK;
  }

  private OptionsList stackingList;
  private Screen previousScreen;
  private Button allOnButton = null, allOffButton, resetButton;
  private Map<ConfigType, Boolean> configValues = new EnumMap<>(ConfigType.class) {

    private static final long serialVersionUID = 1L;
    {
      put(ConfigType.CLAY_RECIPE, getBooleanValue(ConfigType.CLAY_RECIPE).get());
      put(ConfigType.GLOWSTONE_RECIPE, getBooleanValue(ConfigType.GLOWSTONE_RECIPE).get());
      put(ConfigType.QUARTZ_RECIPE, getBooleanValue(ConfigType.QUARTZ_RECIPE).get());
      put(ConfigType.SNOW_RECIPE, getBooleanValue(ConfigType.SNOW_RECIPE).get());
      put(ConfigType.BUCKET_STACK, getBooleanValue(ConfigType.BUCKET_STACK).get());
      put(ConfigType.PEARL_STACK, getBooleanValue(ConfigType.PEARL_STACK).get());
      put(ConfigType.SNOW_STACK, getBooleanValue(ConfigType.SNOW_STACK).get());
    }
  };

  public ConfigScreen(Screen prevScreen) {
    super(new TextComponent("Clay Conversion Config"));

    this.previousScreen = prevScreen;
    configValues.putAll(configValues);
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
    if (allOnButton == null) createButtons();
    else removeWidget(stackingList);

    stackingList = new OptionsList(minecraft, width, height, 24, height - 25, 25);
    addOption("Snowballs stack to 64", ConfigType.SNOW_STACK);
    addOption("Ender Pearls stack to 64", ConfigType.PEARL_STACK);
    addOption("Empty Buckets stack to 64", ConfigType.BUCKET_STACK);
    addOption("Clay block to clay ball recipe", ConfigType.CLAY_RECIPE);
    addOption("Glowstone block to glowstone dust recipe", ConfigType.GLOWSTONE_RECIPE);
    addOption("Quartz block to quartz recipe", ConfigType.QUARTZ_RECIPE);
    addOption("Snow block to snow ball recipe", ConfigType.SNOW_RECIPE);
    addWidget(stackingList);

    super.init();
  }

  private void createButtons() {
    Button doneButton = new Button(2, height - 22, 50, 20, new TextComponent("Save"), b -> {
      saveConfig();
      Minecraft.getInstance().setScreen(this.previousScreen);
    });

    allOnButton = new Button(width - 104, height - 22, 50, 20, new TextComponent("All on"), b -> {
      setAll(true);
      b.active = false;
      init();
    });

    allOffButton = new Button(width - 52, height - 22, 50, 20, new TextComponent("All off"), b -> {
      setAll(false);
      b.active = false;
      init();
    });

    resetButton = new Button(54, height - 22, 70, 20, new TextComponent("Revert"), b -> {
      // TODO: this could be better
      setValue(ConfigType.SNOW_STACK, false);
      setValue(ConfigType.PEARL_STACK, false);
      setValue(ConfigType.BUCKET_STACK, false);
      setValue(ConfigType.CLAY_RECIPE, true);
      setValue(ConfigType.GLOWSTONE_RECIPE, true);
      setValue(ConfigType.QUARTZ_RECIPE, true);
      setValue(ConfigType.SNOW_RECIPE, true);
      b.active = false;
      init();
    });

    addRenderableWidget(doneButton);
    addRenderableWidget(resetButton);
    addRenderableWidget(allOnButton);
    addRenderableWidget(allOffButton);
  }

  private void addOption(String label, ConfigType cfg) {
    CycleOption.OptionSetter<Boolean> setter = (a, b, c) -> setValue(cfg, c);
    Function<net.minecraft.client.Options, Boolean> getter = a -> getValue(cfg);
    this.stackingList.addBig(CycleOption.createOnOff(label, getter, setter));
  }

  private boolean getValue(ConfigType cfg) {
    return this.configValues.get(cfg);
  }

  private void setValue(ConfigType cfg, boolean value) {
    this.configValues.replace(cfg, value);

    if (allValuesAre(value)) (value ? allOnButton : allOffButton).active = false;
    else(value ? allOffButton : allOnButton).active = true;

    resetButton.active = true;
  }

  private void setAll(boolean value) {
    this.configValues.replaceAll((a, b) -> value);

    resetButton.active = true;
  }

  private boolean allValuesAre(boolean value) {
    return !this.configValues.values().contains(!value);
  }

  private BooleanValue getBooleanValue(ConfigType cfg) {
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

  private void saveConfig() {
    this.configValues.forEach((cfg, b) -> {
      BooleanValue value = getBooleanValue(cfg);
      if (value.get() != b) value.set(b);
    });
  }
}
