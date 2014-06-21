/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.android.glass.sample.compass.model;

import com.google.android.glass.sample.compass.R;
import com.google.android.glass.sample.compass.ShowFacts;
import com.google.android.glass.sample.compass.util.MathUtils;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * This class provides access to a list of hard-coded landmarks (located in
 * {@code res/raw/landmarks.json}) that will appear on the compass when the user is near them.
 */
public class Landmarks {
	
	public static Context mContext;
	
    private static final String TAG = Landmarks.class.getSimpleName();

    /**
     * The url of the server for getting the landmarks list.
     */
    private static String SERVER_URL = "http://54.187.0.241:3000/targets.json";
    
    /**
     * The threshold used to display a landmark on the compass.
     */
    private static final double MAX_DISTANCE_KM = 100000;

    /**
     * The list of landmarks loaded from resources.
     */
    private final ArrayList<Place> mPlaces;

    private LocationManager mLocationManager;
    /**
     * Initializes a new {@code Landmarks} object by loading the landmarks from the resource
     * bundle.
     */
    public Landmarks(Context context) {
        mPlaces = new ArrayList<Place>();
        mLocationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
        mContext = context;
        // This class will be instantiated on the service's main thread, and doing I/O on the
        // main thread can be dangerous if it will block for a noticeable amount of time. In
        // this case, we assume that the landmark data will be small enough that there is not
        // a significant penalty to the application. If the landmark data were much larger,
        // we may want to load it in the background instead.
        new ProgressTask().execute();
    }

    /**
     * Gets a list of landmarks that are within ten kilometers of the specified coordinates. This
     * function will never return null; if there are no locations within that threshold, then an
     * empty list will be returned.
     */
    public List<Place> getNearbyLandmarks(double latitude, double longitude) {
        ArrayList<Place> nearbyPlaces = new ArrayList<Place>();

        for (Place knownPlace : mPlaces) {
            if (MathUtils.getDistance(latitude, longitude,
                    knownPlace.getLatitude(), knownPlace.getLongitude()) <= MAX_DISTANCE_KM) {
                nearbyPlaces.add(knownPlace);
            }
        }

        return nearbyPlaces;
    }

    /**
     * Populates the internal places list from places found in a JSON string. This string should
     * contain a root object with a "landmarks" property that is an array of objects that represent
     * places. A place has three properties: name, latitude, and longitude.
     */
    private void populatePlaceList(String jsonString) {
        try {
            JSONObject json = new JSONObject(jsonString);
            JSONArray array = json.optJSONArray("landmarks");

            if (array != null) {
                for (int i = 0; i < array.length(); i++) {
                    JSONObject object = array.optJSONObject(i);
                    Place place = jsonObjectToPlace(object);
                    if (place != null) {
                        mPlaces.add(place);
                    }
                }
            }
        } catch (JSONException e) {
            Log.e(TAG, "Could not parse landmarks JSON string", e);
        }
    }

    /**
     * Converts a JSON object that represents a place into a {@link Place} object.
     * @throws JSONException 
     */
    private Place jsonObjectToPlace(JSONObject object) throws JSONException {
    	//android.os.Debug.waitForDebugger();
        String name = object.optString("title");
        double latitude = object.optDouble("latitude", Double.NaN);
        double longitude = object.optDouble("longitude", Double.NaN);
        String question = object.optString("question");
        String fact = object.optString("fact");
        String correctAnswer = "";
        
        JSONArray arrayAnswerChoices = object.getJSONArray("answers");
        List<String> answerChoices = new ArrayList<String>();
        for (int i = 0; i < arrayAnswerChoices.length(); i = i + 1) {
        	JSONObject answerObject = arrayAnswerChoices.optJSONObject(i);
        	answerChoices.add(answerObject.optString("content"));
        	if (answerObject.optBoolean("correct") ) {
        		correctAnswer = object.optString("content");
        	}
        }
        Log.i(TAG, name + " " + correctAnswer);
        if (!name.isEmpty() && !Double.isNaN(latitude) && !Double.isNaN(longitude)) {
             Place place = new Place(latitude, longitude, name, fact, question,
            		correctAnswer, answerChoices);
             Intent intent = new Intent(mContext, ShowFacts.class);
             Bundle bundle = new Bundle();
             bundle.putSerializable("place", place);
             intent.putExtras(bundle);
             mLocationManager.addProximityAlert(latitude, longitude, 100, -1, PendingIntent.getActivity(mContext, 0, intent, 0));
             return place;
        } else {
            return null;
        }
    }

    /**
     * Reads the text from {@code res/raw/landmarks.json} and returns it as a string.
     */
    private static String readLandmarksResource() {
    	String jsonResponse = connect(SERVER_URL);
    	Log.i(TAG, jsonResponse);
    	return jsonResponse;
    }
    
    /**
     * Converts an InputStream to a String
     * 
     * @param is the input stream to convert
     * @return the input stream as a String
     */
    private static String convertStreamToString(InputStream is) {
        
        BufferedReader reader = new BufferedReader(new InputStreamReader(is));
        StringBuilder sb = new StringBuilder();

        String line = null;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                is.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        return sb.toString();
    }
    
    /**
     * Connects to the given url and returns a JSONResponse as a String.
     * 
     * @param url the url to connect to
     * @return a JSONResponse as a String
     */
    public static String connect(String url)
    {
    	//android.os.Debug.waitForDebugger();
        HttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet(url); 
        HttpResponse response;
        
        try {
            response = httpclient.execute(httpget);
            HttpEntity entity = response.getEntity();
            if (entity != null) {
                InputStream instream = entity.getContent();
                String result= convertStreamToString(instream);
                instream.close();
                return result;
            }
        } catch (ClientProtocolException e) {
        } catch (IOException e) {
        	Log.e(TAG, "Connect: ", e);
        }
        return null;
    }
    
    private class ProgressTask extends AsyncTask<String, Void, Boolean> {

		@Override
		protected Boolean doInBackground(String... arg0) {
			Log.i(TAG, "Start... ");
			String jsonString = readLandmarksResource();
	        populatePlaceList(jsonString);
	        return null;
		}
    	
    }
    
}
