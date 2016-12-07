/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.JsonArray
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParser
 *  com.google.gson.JsonPrimitive
 *  net.minecraft.entity.player.EntityPlayer
 *  org.apache.logging.log4j.Logger
 */
package com.matt.forgehax.util.container.lists;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.matt.forgehax.ForgeHax;
import com.matt.forgehax.util.Utils;
import com.matt.forgehax.util.container.ContainerList;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Base64;
import java.util.UUID;
import net.minecraft.entity.player.EntityPlayer;
import org.apache.logging.log4j.Logger;

public class PlayerList
extends ContainerList {
    public PlayerList(String name, File file) {
        super(name, file);
        this.read();
    }

    public boolean isResponseError(JsonElement json) throws Exception {
        JsonObject root;
        if (json.isJsonObject() && (root = json.getAsJsonObject()).has("error")) {
            String errorMsg = root.get("error").getAsString();
            String reason = "";
            if (root.has("errorMessage")) {
                reason = ": " + root.get("errorMessage").getAsString();
            }
            throw new Exception(errorMsg + reason);
        }
        return false;
    }

    public boolean addPlayer(String name, String uuid, String nickName) throws Exception {
        if (name.isEmpty()) {
            throw new Exception("Empty player name");
        }
        if (uuid.isEmpty()) {
            throw new Exception("Empty UUID");
        }
        if (this.contains(uuid)) {
            throw new Exception("Already contains player in list");
        }
        PlayerData data = new PlayerData(this, new JsonObject()).setName(name).setNickName(nickName).setUuid(uuid);
        try {
            this.requestPlayerProfileData(data);
        }
        catch (Exception e) {
            MOD.getLog().error(e.getMessage());
        }
        data.save();
        return true;
    }

    public boolean addPlayerByUUID(UUID uuid) throws Exception {
        String name;
        block9 : {
            name = "";
            HttpURLConnection connection = null;
            try {
                String line;
                URL url = new URL(String.format("https://api.mojang.com/user/profiles/%s/names", uuid.toString().replace("-", "").toLowerCase()));
                connection = (HttpURLConnection)url.openConnection();
                connection.setDoOutput(true);
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                StringBuilder response = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                String json = response.toString();
                JsonParser parser = new JsonParser();
                JsonElement jsonElement = parser.parse(json);
                if (this.isResponseError(jsonElement)) break block9;
                JsonArray array = jsonElement.getAsJsonArray();
                int last = array.size() - 1;
                if (last > -1) {
                    name = array.get(last).getAsJsonObject().get("name").getAsString();
                    break block9;
                }
                throw new Exception("Empty response");
            }
            catch (MalformedURLException e) {
                MOD.printStackTrace(e);
                throw new Exception(e.getMessage());
            }
            catch (IOException e) {
                MOD.printStackTrace(e);
                throw new Exception(e.getMessage());
            }
            finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
        return this.addPlayer(name, uuid.toString(), "");
    }

    public boolean addPlayerByName(String name) throws Exception {
        String uuid;
        block10 : {
            uuid = "";
            HttpURLConnection connection = null;
            try {
                String line;
                Gson gson = new Gson();
                JsonArray postRequest = new JsonArray();
                postRequest.add((JsonElement)new JsonPrimitive(name));
                URL url = new URL("https://api.mojang.com/profiles/minecraft");
                connection = (HttpURLConnection)url.openConnection();
                connection.setDoOutput(true);
                connection.setRequestMethod("POST");
                connection.setRequestProperty("Content-Type", "application/json");
                DataOutputStream output = new DataOutputStream(connection.getOutputStream());
                output.writeBytes(gson.toJson((JsonElement)postRequest));
                output.close();
                InputStream is = connection.getInputStream();
                BufferedReader rd = new BufferedReader(new InputStreamReader(is));
                StringBuilder response = new StringBuilder();
                while ((line = rd.readLine()) != null) {
                    response.append(line);
                    response.append('\r');
                }
                rd.close();
                String json = response.toString();
                JsonParser parser = new JsonParser();
                JsonElement jsonElement = parser.parse(json);
                if (this.isResponseError(jsonElement)) break block10;
                JsonArray array = jsonElement.getAsJsonArray();
                if (array.size() > 0) {
                    JsonObject data = array.get(0).getAsJsonObject();
                    if (data.has("id")) {
                        uuid = Utils.stringToUUID(data.get("id").getAsString()).toString();
                    }
                    break block10;
                }
                throw new Exception("Empty response");
            }
            catch (MalformedURLException e) {
                MOD.printStackTrace(e);
                throw new Exception(e.getMessage());
            }
            catch (IOException e) {
                MOD.printStackTrace(e);
                throw new Exception(e.getMessage());
            }
            finally {
                if (connection != null) {
                    connection.disconnect();
                }
            }
        }
        return this.addPlayer(name, uuid, "");
    }

    public boolean addPlayerByEntity(EntityPlayer player, String nickName) throws Exception {
        return this.addPlayer(player.getName(), player.getUniqueID().toString(), nickName);
    }

    public boolean addPlayerByEntity(EntityPlayer player) throws Exception {
        return this.addPlayer(player.getName(), player.getUniqueID().toString(), "");
    }

    public boolean requestPlayerProfileData(PlayerData data) throws Exception {
        if (!data.getSkinURL().isEmpty()) {
            return false;
        }
        HttpURLConnection connection = null;
        try {
            String line;
            URL url = new URL(String.format(" https://sessionserver.mojang.com/session/minecraft/profile/%s", data.getUuid().toString().replace("-", "").toLowerCase()));
            connection = (HttpURLConnection)url.openConnection();
            connection.setDoOutput(true);
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            StringBuilder response = new StringBuilder();
            while ((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            String json = response.toString();
            JsonParser parser = new JsonParser();
            JsonElement jsonElement = parser.parse(json);
            if (!this.isResponseError(jsonElement)) {
                JsonObject root = jsonElement.getAsJsonObject();
                if (root.has("name")) {
                    data.setName(root.get("name").getAsString());
                }
                if (root.has("properties") && root.get("properties").isJsonArray()) {
                    JsonArray properties = root.get("properties").getAsJsonArray();
                    for (JsonElement value : properties) {
                        JsonObject skinRoot;
                        JsonElement skinElement;
                        JsonElement texturesJson;
                        JsonObject textureProfileJson;
                        JsonObject jsonObject;
                        if (!value.isJsonObject() || !(skinRoot = value.getAsJsonObject()).has("name") || !skinRoot.has("value") || !skinRoot.get("name").getAsString().equals("textures")) continue;
                        String valueEncoded = skinRoot.get("value").getAsString();
                        String decoded = new String(Base64.getDecoder().decode(valueEncoded), "ASCII");
                        JsonElement decodedJson = parser.parse(decoded);
                        if (!decodedJson.isJsonObject() || !(textureProfileJson = decodedJson.getAsJsonObject()).has("textures") || !(texturesJson = textureProfileJson.get("textures")).isJsonObject()) continue;
                        JsonObject textureDataJson = texturesJson.getAsJsonObject();
                        if (textureDataJson.has("SKIN") && (skinElement = textureDataJson.get("SKIN")).isJsonObject() && (jsonObject = skinElement.getAsJsonObject()).has("url")) {
                            data.setSkinURL(jsonObject.get("url").getAsString());
                        }
                        if (!textureDataJson.has("CAPE") || !(skinElement = textureDataJson.get("CAPE")).isJsonObject() || !(jsonObject = skinElement.getAsJsonObject()).has("url")) continue;
                        data.setCapeURL(jsonObject.get("url").getAsString());
                    }
                }
            }
        }
        catch (MalformedURLException e) {
            MOD.printStackTrace(e);
            throw new Exception(e.getMessage());
        }
        catch (IOException e) {
            MOD.printStackTrace(e);
            throw new Exception(e.getMessage());
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
        return true;
    }

    public boolean removePlayerByUUID(String uuid) {
        if (this.contains(uuid)) {
            boolean ret = this.remove(uuid);
            this.save();
            return ret;
        }
        return false;
    }

    public boolean removePlayerByUUID(UUID uuid) {
        return this.removePlayerByUUID(uuid.toString());
    }

    public boolean removePlayerByEntity(EntityPlayer player) {
        return this.removePlayerByUUID(player.getUniqueID().toString());
    }

    public boolean containsPlayer(String uuid) {
        return this.contains(uuid);
    }

    public boolean containsPlayer(EntityPlayer player) {
        return this.contains(player.getUniqueID().toString());
    }

    public PlayerData getPlayerData(String uuid) {
        if (this.contains(uuid)) {
            JsonElement root = this.get(uuid);
            return root.isJsonObject() ? new PlayerData(this, root.getAsJsonObject()).setUuid(uuid) : null;
        }
        return null;
    }

    public void savePlayerData(PlayerData data) {
        this.add(data.getUuid().toString(), data.getRoot());
    }

    public static class PlayerData {
        private final PlayerList parent;
        private final JsonObject root;
        private UUID uuid;

        public PlayerData(PlayerList parent, JsonObject root) {
            this.parent = parent;
            this.root = root;
        }

        public PlayerData(JsonObject root) {
            this.parent = null;
            this.root = root;
        }

        public JsonObject getRoot() {
            return this.root;
        }

        public PlayerData setUuid(UUID uuid) {
            this.uuid = uuid;
            return this;
        }

        public PlayerData setUuid(String uuid) {
            this.uuid = Utils.stringToUUID(uuid);
            return this;
        }

        public UUID getUuid() {
            return this.uuid;
        }

        public PlayerData setName(String name) {
            if (!name.isEmpty()) {
                this.root.addProperty("name", name);
            }
            return this;
        }

        public String getName() {
            JsonElement element = this.root.get("name");
            return element != null ? element.getAsString() : "";
        }

        public PlayerData setNickName(String nickName) {
            if (!nickName.isEmpty()) {
                this.root.addProperty("nick", nickName);
            }
            return this;
        }

        public String getNickName() {
            JsonElement element = this.root.get("nick");
            return element != null ? element.getAsString() : "";
        }

        public String getGuiName() {
            String nick = this.getNickName();
            return !nick.isEmpty() ? String.format("%s (%s)", this.getName(), nick) : this.getName();
        }

        public String getDisplayName() {
            String nick = this.getNickName();
            return nick.isEmpty() ? this.getName() : nick;
        }

        public PlayerData setSkinURL(String skinURL) {
            this.root.addProperty("skin_url", skinURL);
            return this;
        }

        public String getSkinURL() {
            return this.root.has("skin_url") ? this.root.get("skin_url").getAsString() : "";
        }

        public PlayerData setCapeURL(String capeURL) {
            this.root.addProperty("cape_url", capeURL);
            return this;
        }

        public String getCapeURL() {
            return this.root.has("cape_url") ? this.root.get("cape_url").getAsString() : "";
        }

        public boolean save() {
            if (this.parent != null) {
                this.parent.savePlayerData(this);
                this.parent.save();
            }
            return false;
        }
    }

}

