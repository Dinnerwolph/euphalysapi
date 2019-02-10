package net.euphalys.core.api.names;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.UUID;

/**
 * @author Dinnerwolph
 */

public class NameFetcher {

    private static final JsonParser parser = new JsonParser();

    public static List<String> nameHistoryFromUuid(UUID uuid)
    {
        URLConnection connection;
        try
        {
            connection = new URL("https://api.mojang.com/user/profiles/"
                    + uuid.toString().replace("-", "").toLowerCase() + "/names"
            ).openConnection();
            String text = new Scanner(connection.getInputStream(), "UTF-8").useDelimiter("\\Z").next();
            JsonArray list = (JsonArray) parser.parse(text);
            List<String> names = new ArrayList<>();
            for (int i = 0; i < list.size(); i++)
                names.add(((JsonObject) list.get(i)).get("name").getAsString());
            return names;
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        return null;
    }
}
