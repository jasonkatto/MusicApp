package com.musicapp;

/**
 * Created by Giresg grovar on 11/29/2017.
 */

import com.android.volley.VolleyError;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public interface GetJsonObject {

    JSONObject getJsonResponse(JSONObject j) throws JSONException;
    JSONArray getJSonArray(JSONArray jsonArray);
    VolleyError getError(VolleyError geterror);
}