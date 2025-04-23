package com.cream.sparkle.common.utils.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.google.gson.stream.MalformedJsonException;

import java.io.IOException;
import java.util.Objects;

public class LongTypeAdapter extends TypeAdapter<Long> {

    private static final String IsLongFlagKey = "_isLong";
    private static final String LongStrKey = "_longStr";

    /**
     * 序列化：将Java的Long转为{_isLong: true, _longStr: "value"}格式
     */
    @Override
    public void write(JsonWriter out, Long value) throws IOException {
        if (value == null) {
            out.nullValue();
            return;
        }
        out.beginObject();
        out.name(IsLongFlagKey).value(true);
        out.name(LongStrKey).value(value.toString());
        out.endObject();
    }

    /**
     * 反序列化：将{_isLong: true, _longStr: "value"}或合法数字转为Long
     */
    @Override
    public Long read(JsonReader in) throws IOException {
        in.beginObject();
        String longStr = null;
        boolean isLong = false;

        while (in.hasNext()) {
            String fieldName = in.nextName();
            if (IsLongFlagKey.equals(fieldName)) {
                isLong = in.nextBoolean();
            } else if (LongStrKey.equals(fieldName)) {
                longStr = in.nextString();
            } else {
                in.skipValue(); // 跳过未知字段
            }
        }
        in.endObject();

        if (!isLong || Objects.isNull(longStr)) {
            throw new MalformedJsonException("Invalid Long object: missing _isLong or _longStr");
        }

        // 验证并转换字符串为Long
        try {
            return Long.parseLong(longStr);
        } catch (NumberFormatException e) {
            throw new MalformedJsonException("Invalid Long string: " + longStr, e);
        }
    }
}