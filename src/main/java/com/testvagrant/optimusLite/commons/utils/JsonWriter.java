package com.testvagrant.optimusLite.commons.utils;

import com.google.gson.Gson;

import java.io.File;

public class JsonWriter {
    public <T> void writeJson(T json) {
        String s = new Gson().toJson(json);

    }
}
