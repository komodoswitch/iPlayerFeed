package com.linxmap.iplayerfeed.feed.parser;

import com.linxmap.iplayerfeed.common.L;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by linxmap on 16/09/14.
 */
public class JsonParser {

    private final String LOG_TAG = "JsonParserManager";
    private static final boolean LOG_ENABLED = true;
    private final String JSON_VAR_MOVIES = "movies";
    private final String JSON_VAR_RATINGS = "ratings";
    private final String JSON_VAR_AUDIENCE_SCORE = "audience_score";

    public int getAudienceRating(String httpResponseBody) {
        int rating = -1;
        try {
            JSONObject jsonObject = new JSONObject(httpResponseBody);
            JSONArray arrayList = jsonObject.getJSONArray(JSON_VAR_MOVIES);
            if (arrayList != null && arrayList.length() > 0) {
                for (int i = 0; i < arrayList.length(); i++) {
                    JSONObject listItem = arrayList.getJSONObject(i);
                    JSONObject ratingsJsonObject = listItem.getJSONObject(JSON_VAR_RATINGS);
                    rating = ratingsJsonObject.getInt(JSON_VAR_AUDIENCE_SCORE);
                    L.log(LOG_TAG,LOG_ENABLED,"JSON RATING :"+rating);
                    break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return rating;

    }
}
