package com.example.walker.represent;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

import static com.android.volley.VolleyLog.TAG;

public class Representative {
    Map<String, String> repInfo;
    String url;

    Representative(Map<String, String> repInfo) {
        this.repInfo = repInfo;
        String id = repInfo.get("bioguide_id");
        this.url = "http://bioguide.congress.gov/bioguide/photo/" + id.substring(0, 1) + "/" + id + ".jpg";

    }
}

