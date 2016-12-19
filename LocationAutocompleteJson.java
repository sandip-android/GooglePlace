package com.webzino.runmile.googleplace;

import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;

public class LocationAutocompleteJson extends AsyncTask<String, Void, String> {
    Context context;
    String googlePlacesAutoCompleteServerAPIKey;
    String place;
    String parameters;
    String placeUrl;

    HttpURLConnection httpURLConnection;

    ArrayList<PredictionModel> predictionList;
    LocationAutocompleteInterface locationAutocompleteInterface;

    public LocationAutocompleteJson(Context context, String googlePlacesAutoCompleteServerAPIKey, String place) {
        this.context = context;
        this.googlePlacesAutoCompleteServerAPIKey = googlePlacesAutoCompleteServerAPIKey;
        this.place = place;
        locationAutocompleteInterface = (LocationAutocompleteInterface) context;
    }

    public LocationAutocompleteJson(Context context, Fragment fragment, String googlePlacesAutoCompleteServerAPIKey, String place) {
        this.context = context;
        this.googlePlacesAutoCompleteServerAPIKey = googlePlacesAutoCompleteServerAPIKey;
        this.place = place;
        locationAutocompleteInterface = (LocationAutocompleteInterface) fragment;
    }

    @Override
    protected void onPreExecute() {
        // TODO Auto-generated method stub
        super.onPreExecute();

        String input = "";
        try {
            input = URLEncoder.encode(place, "utf-8");
        } catch (UnsupportedEncodingException e1) {
            Log.e("Error", e1.getMessage());
            e1.printStackTrace();
        }
        parameters = "input=" + input + "&types=geocode&sensor=false&key=" + googlePlacesAutoCompleteServerAPIKey;
        placeUrl = "https://maps.googleapis.com/maps/api/place/autocomplete/json?" + parameters;

        predictionList = new ArrayList<>();
    }

    @Override
    protected String doInBackground(String... params) {
        // TODO Auto-generated method stub
        String result = "";
        try {
            URL url = new URL(placeUrl);
            httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setRequestProperty("Content-Type", "application/json");
            httpURLConnection.setRequestProperty("Accept", "application/json");
            httpURLConnection.setReadTimeout(10000);
            httpURLConnection.setConnectTimeout(15000);
            httpURLConnection.setRequestMethod("GET");
            httpURLConnection.setDoInput(true);
            httpURLConnection.setDoOutput(false);

            int responseCode = httpURLConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream iStream = httpURLConnection.getInputStream();
                InputStreamReader isReader = new InputStreamReader(iStream);
                BufferedReader br = new BufferedReader(isReader);
                String line;
                while ((line = br.readLine()) != null) {
                    result += line;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    protected void onPostExecute(String result) {
        // TODO Auto-generated method stub
        super.onPostExecute(result);
        //Log.e("Place Json", result);
        try {
            JSONObject locationObj = new JSONObject(result);
            String status = locationObj.getString("status");
            if (status.equals("OK")) {
                JSONArray locationList = locationObj.getJSONArray("predictions");
                for (int i = 0; i < locationList.length(); i++) {
                    JSONObject locationDetails = locationList.getJSONObject(i);
                    PredictionModel prediction = new PredictionModel();
                    prediction.setDescription(locationDetails.getString("description"));
                    prediction.setId(locationDetails.getString("id"));
                    prediction.setPlace_id(locationDetails.getString("place_id"));
                    prediction.setReference(locationDetails.getString("reference"));

                    predictionList.add(prediction);
                }
            } else {
                Toast.makeText(context, locationObj.getString("error_message"), Toast.LENGTH_SHORT).show();
            }
            locationAutocompleteInterface.getLocationAutocompleteDataList(status, predictionList);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
