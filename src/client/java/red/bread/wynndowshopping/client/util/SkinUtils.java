/*
 * Copyright © Wynntils 2023-2024.
 * This file is released under LGPLv3. See LICENSE for full license details.
 */
package red.bread.wynndowshopping.client.util;

import com.google.gson.JsonObject;
import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ProfileComponent;
import net.minecraft.item.ItemStack;

import java.nio.charset.Charset;
import java.util.Base64;
import java.util.UUID;

public final class SkinUtils {
    public static void setPlayerHeadFromUUID(ItemStack itemStack, String uuid) {
        JsonObject skinObject = new JsonObject();
        skinObject.addProperty("url", "https://textures.minecraft.net/texture/" + uuid);

        JsonObject texturesObject = new JsonObject();
        texturesObject.add("SKIN", skinObject);

        JsonObject jsonObject = new JsonObject();
        jsonObject.add("textures", texturesObject);

        // Encode the jsonObject into a base64 string.
        String textureString =
                Base64.getEncoder().encodeToString(jsonObject.toString().getBytes(Charset.defaultCharset()));

        setPlayerHeadSkin(itemStack, textureString);
    }

    public static void setPlayerHeadSkin(ItemStack itemStack, String textureString) {
        // If this starts being done repeatedly for the same texture string,
        // we should cache the UUID.
        GameProfile gameProfile = new GameProfile(UUID.randomUUID(), "");
        gameProfile.getProperties().put("textures", new Property("textures", textureString, null));

        itemStack.set(DataComponentTypes.PROFILE, new ProfileComponent(gameProfile));
    }

}
