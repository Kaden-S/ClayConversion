package com.kaden.clayconversion;


import java.util.EnumMap;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;

import com.kaden.clayconversion.Config.ConfigType;
import com.mojang.blaze3d.vertex.PoseStack;

import net.minecraft.client.CycleOption;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.components.OptionsList;
import net.minecraft.client.gui.screens.Screen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


@OnlyIn(Dist.CLIENT)
public class ConfigScreen extends Screen {

  private OptionsList stackingList;
  private Screen previousScreen;
  private Button allOnButton, allOffButton, resetButton;
  private Button.OnPress allOnFn = onPress(() -> setAll(true)), allOffFn = onPress(() -> setAll(false)),
    resetFn = onPress(() -> setAll((cfg) -> cfg.enabledByDefault)), doneFn = b -> {
      save();
      exit();
    };
  private Map<ConfigType, Boolean> configValues = new EnumMap<>(ConfigType.class) {

    private static final long serialVersionUID = 1L;
    {
      for (var cfg : ConfigType.values())
        put(cfg, cfg.enabled());
    }
  };

  public ConfigScreen(Screen prevScreen) {
    super(Text.configScreen);

    this.previousScreen = prevScreen;
  }

  @Override
  public void render(PoseStack matrix, int mouseX, int mouseY, float partialTicks) {
    int white = 0xFFFFFFFF;

    renderBackground(matrix);
    stackingList.render(matrix, mouseX, mouseY, partialTicks);
    drawCenteredString(matrix, font, title.getString(), width / 2, 8, white);
    drawCenteredString(matrix, font, "Changes apply on restart.", width / 2, height - 12, white);
    super.render(matrix, mouseX, mouseY, partialTicks);
  }

  @Override
  protected void init() {
    if (allOnButton == null) createButtons();
    else removeWidget(stackingList);

    stackingList = new OptionsList(minecraft, width, height, 24, height - 25, 25);
    for (var cfg : ConfigType.values())
      addOption(cfg.desc, cfg);
    addWidget(stackingList);

    super.init();
  }

  private void createButtons() {
    Button doneButton = new Button(2, height - 22, 50, 20, Text.saveButton, doneFn);
    allOnButton = new Button(width - 104, height - 22, 50, 20, Text.allOnButton, allOnFn);
    allOffButton = new Button(width - 52, height - 22, 50, 20, Text.allOffButton, allOffFn);
    resetButton = new Button(54, height - 22, 70, 20, Text.revertButton, resetFn);

    if (matchAll(true)) allOnButton.active = false;
    if (matchAll(false)) allOffButton.active = false;
    if (matchAll(cfg -> cfg.enabledByDefault)) resetButton.active = false;

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

    boolean resetButtonActive = true;

    if (matchAll(value)) {
      (value ? allOnButton : allOffButton).active = false;
    } else {
      (value ? allOffButton : allOnButton).active = true;

      if (matchAll(c -> c.enabledByDefault)) resetButtonActive = false;
    }

    resetButton.active = resetButtonActive;
  }

  private void setAll(Predicate<ConfigType> val) {
    for (var cfg : ConfigType.values())
      setValue(cfg, val.test(cfg));
  }

  private void setAll(boolean val) {
    for (var cfg : ConfigType.values())
      setValue(cfg, val);
  }

  private boolean matchAll(Predicate<ConfigType> val) {
    for (var cfg : ConfigType.values()) {
      if (getValue(cfg) != val.test(cfg)) return false;
    }
    return true;
  }

  private boolean matchAll(boolean value) {
    return !this.configValues.values().contains(!value);
  }

  private Button.OnPress onPress(Runnable fn) {
    return b -> {
      fn.run();
      b.active = false;
      init();
    };
  }

  private void exit() {
    Minecraft.getInstance().setScreen(this.previousScreen);
  }

  private void save() {
    this.configValues.forEach((cfg, enabled) -> {
      if (cfg.enabled() != enabled) cfg.booleanValue().set(enabled);
    });
  }
}
