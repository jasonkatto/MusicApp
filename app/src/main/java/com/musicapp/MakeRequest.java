package com.musicapp;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Request.Method;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;



import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public abstract class MakeRequest implements GetJsonObject {


    private  ProgressDialog dialog = null;

    public MakeRequest(final Map<String, String> params, final Activity context, final String url, int method, final boolean isShow) {
        Log.e("url",url.toString());
        Log.e("Params",params.toString());
        String tag_json_obj = "json_obj_req";
        dialog = new ProgressDialog(context);
        dialog.setTitle("Please Wait");
        dialog.setMessage("Loading..");
        if (isShow) {
            dialog.show();
        }
        dialog.setCanceledOnTouchOutside(false);

        StringRequest jsonObjRequest = new StringRequest(method,
                url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if (isShow) {
                            dialog.dismiss();
                        }
                        Log.e("Response", response);
                        // PreferencesService.instance().saveResponse(response);

                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            getJsonResponse(jsonObject);
                        } catch (JSONException e) {
                            Log.e("Exception", e.toString());

                        }
                    }
                }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                if (isShow) {
                    dialog.dismiss();
                }
                Log.e("Value", "" + error.getMessage());
                getError(error);
                VolleyLog.d("", "Error: " + error);
                if (error instanceof NetworkError) {
                    if (context != null)
                        Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
                } else if (error instanceof ServerError) {
                    if (context != null)
                        Toast.makeText(context, "Server not responding", Toast.LENGTH_LONG).show();
                } else if (error instanceof AuthFailureError) {
                    if (context != null)
                        Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
                } else if (error instanceof ParseError) {
                    if (context != null)
                        Toast.makeText(context, "Server not responding", Toast.LENGTH_LONG).show();
                } else if (error instanceof NoConnectionError) {
                    if (context != null)
                        Toast.makeText(context, "No internet connection", Toast.LENGTH_LONG).show();
                } else if (error instanceof TimeoutError) {
                    if (context != null)
                        Toast.makeText(context, "Connection TimeOut! Check your internet connection", Toast.LENGTH_LONG).show();
                } else if (error.networkResponse == null) {
                    if (error.getClass().equals(TimeoutError.class)) {
                        if (context != null)
                            if (!context.isRestricted()) {
                                Toast.makeText(context, "Server not responding", Toast.LENGTH_LONG).show();
                            }

                    }
                }
            }
        }) ;
        jsonObjRequest.setRetryPolicy(new DefaultRetryPolicy(
                0,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(jsonObjRequest, tag_json_obj);

        // Cancelling request
        // ApplicationController.getInstance().getRequestQueue().cancelAll(tag_json_obj);
    }

}