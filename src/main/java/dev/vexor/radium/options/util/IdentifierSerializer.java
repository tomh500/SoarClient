package dev.vexor.radium.options.util;

import com.google.gson.*;
import java.lang.reflect.Type;
import net.minecraft.util.ResourceLocation;

public class IdentifierSerializer implements JsonSerializer<ResourceLocation>, JsonDeserializer<ResourceLocation> {
    @Override
    public JsonElement serialize(ResourceLocation src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("namespace", src.getResourceDomain());
        jsonObject.addProperty("path", src.getResourcePath());
        return jsonObject;
    }

    @Override
    public ResourceLocation deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return new ResourceLocation(json.getAsJsonObject().get("namespace").getAsString(), json.getAsJsonObject().get("path").getAsString());
    }
}
