package com.cream.sparkle.hero.unit;

import com.cream.sparkle.common.utils.json.JsonCustomLongCodecUtil;
import com.cream.sparkle.hero.game.logic.Role;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.jupiter.api.Test;

public class JsonCodecTest {

    @Test
    public void test() {
        Role role = new Role(111222L, 33333555L, "小黑");
        String jsonStr = JsonCustomLongCodecUtil.toJson(role);
        /*
        System.out.println(jsonStr);
        Gson gson = new Gson();
        JsonObject jsonObject = gson.fromJson(jsonStr, JsonObject.class);
        JsonElement name = jsonObject.get("basic");
        System.out.println(name);
        JsonObject asJsonObject = JsonParser.parseString(jsonStr).getAsJsonObject();
        System.out.println(asJsonObject);
        */
    }

    @Test
    public void test2() {
        JsonObject asJsonObject = JsonParser.parseString("{\"msgType\":0,\"data\":1745208110099}").getAsJsonObject();
        System.out.println(asJsonObject);
    }
}
