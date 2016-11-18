package com.doublesp.coherence.converters;

import com.raizlabs.android.dbflow.converter.TypeConverter;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by pinyaoting on 11/18/16.
 */

@com.raizlabs.android.dbflow.annotation.TypeConverter
public class StringListConverter extends TypeConverter<String, JSONObject> {

    @Override
    public String getDBValue(JSONObject model) {
        return model == null ? null : model.toString();
    }

    @Override
    public JSONObject getModelValue(String data) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject(data);
        } catch (JSONException e) {

        } finally {
            return jsonObject;
        }
    }
}
