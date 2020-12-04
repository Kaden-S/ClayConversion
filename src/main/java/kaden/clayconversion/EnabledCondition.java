package kaden.clayconversion;

import javax.annotation.Nullable;

import com.google.gson.JsonObject;

import net.minecraft.util.JSONUtils;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.common.crafting.conditions.IConditionSerializer;

public class EnabledCondition implements ICondition{
	
	private static final ResourceLocation name = new ResourceLocation(ClayConversion.modid, "enabled");
	private boolean enabled;
	@Nullable private String recipe;

	public EnabledCondition(@Nullable String recipe) {
		this.recipe = recipe;
	}
	
	@Override
	public ResourceLocation getID() {
		return name;
	}

	@Override
	public boolean test() {
		switch(recipe) {
		case("clay"):
			return (enabled = Config.clayRecipeEnabled.get());
		case("snow"):
			return (enabled = Config.snowRecipeEnabled.get());
		case("quartz"):
			return (enabled = Config.quartzRecipeEnabled.get());
		case("glowstone"):
			return (enabled = Config.glowstoneRecipeEnabled.get());
		default:
			return false;
		}
	}
	
	public class Serializer implements IConditionSerializer<EnabledCondition> {

		@Override
		public void write(JsonObject json, EnabledCondition value) {
			json.addProperty("recipe", value.recipe);
			json.addProperty("enabled", value.enabled);
		}

		@Override
		public EnabledCondition read(JsonObject json) {
			return new EnabledCondition(JSONUtils.getString(json, "recipe"));
		}

		@Override
		public ResourceLocation getID() {
			return EnabledCondition.name;
		}
	}
}