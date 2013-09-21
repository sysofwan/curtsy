package com.example.curtsy;

import com.android.volley.*;
import com.android.volley.Response.*;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSyntaxException;

import java.io.UnsupportedEncodingException;
import java.util.Map;

/**
 * Created by SySofwan
 * Date: 8/3/13
 * Time: 12:08 PM
 */

public class GsonRequestHandler<T> extends Request<T> {

    public static final String BASE_URL = "http://sayyidsofwan.com/curtsy/";

    private final Gson gson;
    private final Class<T> clazz;
    private final Map<String, String> headers;
    private final Listener<T> listener;

    public GsonRequestHandler(String path, Class<T> clazz, JsonDeserializer deserializer, Map<String, String> headers,
                              Listener<T> listener, ErrorListener errorListener) {
        super(Method.GET, BASE_URL + path, errorListener);
        this.clazz = clazz;
        this.headers = headers;
        this.listener = listener;

        if (deserializer != null) {
            gson = new GsonBuilder().registerTypeAdapter(clazz, deserializer).create();
        }
        else {
            gson = new Gson();
        }
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {
        return headers != null ? headers : super.getHeaders();
    }

    @Override
    protected void deliverResponse(T response) {
        listener.onResponse(response);
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
            return Response.success(
                    gson.fromJson(json, clazz), HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        }
    }

}