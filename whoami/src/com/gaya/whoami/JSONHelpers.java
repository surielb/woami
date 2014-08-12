package com.gaya.whoami;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author suriel
 *         Date: 6/22/14
 *         Time: 1:34 PM
 */
public class JSONHelpers {

    public interface Factory<Tin, Tout> {
        Tout build(Tin item);
    }

    public static <T> List<T> map(JSONArray array, Factory<Object, T> mapper) {
        if (array == null) return Collections.emptyList();
        List<T> items = new ArrayList<T>(array.length());
        for (int i = 0; i < array.length(); i++) {
            T res = mapper.build(array.opt(i));
            if (res != null)
                items.add(res);
        }
        return items;
    }

    public static JSONObject getJson(InputStream is) throws IOException, JSONException {

        if (is == null)
            return null;
        StringBuilder builder = new StringBuilder();
        BufferedReader reader = new BufferedReader(
                new InputStreamReader(is));
        String line;

        while ((line = reader.readLine()) != null) {
            builder.append(line);
        }

        return new JSONObject(removeBOM(builder));
    }

    private static String removeBOM(StringBuilder inString) {
        char firstChar = inString.charAt(0);

        if (firstChar == 0xFEFF) {
            return inString.substring(1);
        }

        return inString.toString();
    }
}