package com.kaden.clayconversion;


import javax.annotation.Nullable;

import com.google.gson.JsonObject;
import com.kaden.clayconversion.Config.ConfigType;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;


public class EnabledCondition implements ICondition {

  private static final ResourceLocation name = new ResourceLocation(ClayConversion.MODID, "enabled");
  private boolean enabled;
  @Nullable
  private ConfigType recipe;

  public EnabledCondition(@Nullable ConfigType recipe) {
    this.recipe = recipe;
  }

  @Override
  public ResourceLocation getID() {
    return name;
  }

  @Override
  public boolean test(IContext context) {
    if (recipe == null) return false;

    return recipe.enabled();
  }

  public class Serializer implements IConditionSerializer<EnabledCondition> {

    @Override
    public void write(JsonObject json, EnabledCondition value) {
      json.addProperty("recipe", value.recipe.id);
      json.addProperty("enabled", value.enabled);
    }

    @Override
    public EnabledCondition read(JsonObject json) {
      String id = json.get("recipe").getAsString();
      return new EnabledCondition(ConfigType.getById(id));
    }

    @Override
    public ResourceLocation getID() {
      return EnabledCondition.name;
    }
  }
}
