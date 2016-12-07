/*
 * Decompiled with CFR 0_118.
 * 
 * Could not load the following classes:
 *  com.google.gson.Gson
 *  com.google.gson.GsonBuilder
 *  com.google.gson.JsonElement
 *  com.google.gson.JsonObject
 *  com.google.gson.JsonParseException
 *  com.google.gson.JsonParser
 *  com.google.gson.JsonPrimitive
 *  org.apache.logging.log4j.Logger
 */
package com.matt.forgehax.util.container;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.matt.forgehax.ForgeHax;
import com.matt.forgehax.ForgeHaxBase;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.Set;
import org.apache.logging.log4j.Logger;

public class ContainerList
extends ForgeHaxBase {
    private final File file;
    private final String name;
    private JsonObject root;

    public ContainerList(String name, File file) {
        this.name = name;
        this.file = file;
        this.root = new JsonObject();
    }

    public String getName() {
        return this.name;
    }

    protected File getFile() {
        return this.file;
    }

    protected <V extends JsonElement> void add(String keyName, JsonElement value) {
        this.root.add(keyName, value);
    }

    protected void add(String keyName, Integer value) {
        this.add(keyName, new JsonPrimitive((Number)value));
    }

    protected void add(String keyName, Long value) {
        this.add(keyName, new JsonPrimitive((Number)value));
    }

    protected void add(String keyName, Short value) {
        this.add(keyName, new JsonPrimitive((Number)value));
    }

    protected void add(String keyName, String value) {
        this.add(keyName, new JsonPrimitive(value));
    }

    protected void add(String keyName, Float value) {
        this.add(keyName, new JsonPrimitive((Number)value));
    }

    protected void add(String keyName, Double value) {
        this.add(keyName, new JsonPrimitive((Number)value));
    }

    protected void add(String keyName, Byte value) {
        this.add(keyName, new JsonPrimitive((Number)value));
    }

    protected void add(String keyName, Boolean value) {
        this.add(keyName, new JsonPrimitive(value));
    }

    protected void add(String keyName, Character value) {
        this.add(keyName, new JsonPrimitive(value));
    }

    protected boolean remove(String keyName) {
        return this.root.remove(keyName) != null;
    }

    protected JsonElement get(String keyName) {
        return this.root.get(keyName);
    }

    protected boolean contains(String keyName) {
        return this.root.has(keyName);
    }

    public Set<Map.Entry<String, JsonElement>> entrySet() {
        return this.root.entrySet();
    }

    /*
     * WARNING - Removed try catching itself - possible behaviour change.
     */
    public void read() {
        if (this.file.exists() && this.file.isFile() && this.file.length() > 0) {
            BufferedReader buffer = null;
            try {
                FileReader reader = new FileReader(this.file);
                buffer = new BufferedReader(reader);
                try {
                    this.root = new JsonObject();
                    this.parseJsonObject(new JsonParser().parse((Reader)buffer).getAsJsonObject());
                }
                catch (JsonParseException e) {
                    MOD.getLog().error(String.format("Failed to read file %s: %s", this.file.getName(), e.getMessage()));
                }
            }
            catch (IOException e) {
                MOD.printStackTrace(e);
            }
            finally {
                try {
                    if (buffer != null) {
                        buffer.close();
                    }
                }
                catch (IOException e) {
                    MOD.printStackTrace(e);
                }
            }
        }
    }

    public void save() {
        try {
            FileWriter writer = new FileWriter(this.file);
            BufferedWriter buffer = new BufferedWriter(writer);
            Gson gson = new GsonBuilder().setPrettyPrinting().create();
            buffer.write(gson.toJson((JsonElement)this.root));
            buffer.close();
        }
        catch (IOException e) {
            MOD.printStackTrace(e);
        }
    }

    public boolean delete() {
        boolean deletedFile = false;
        try {
            deletedFile = Files.deleteIfExists(this.file.toPath());
        }
        catch (IOException e) {
            MOD.printStackTrace(e);
        }
        return deletedFile;
    }

    public int size() {
        return this.root.entrySet().size();
    }

    protected void parseJsonObject(JsonObject root) {
        for (Map.Entry entry : root.entrySet()) {
            this.add((String)entry.getKey(), ((JsonElement)entry.getValue()));
        }
    }

    protected JsonObject toJsonObject(JsonObject json) {
        for (Map.Entry entry : this.root.entrySet()) {
            json.add((String)entry.getKey(), (JsonElement)entry.getValue());
        }
        return json;
    }

    protected JsonObject toJsonObject() {
        return this.toJsonObject(new JsonObject());
    }
}

