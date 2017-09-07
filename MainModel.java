package com.example.redman.wbank;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by redman on 8/29/17.
 */

public class MainModel implements MVP_Main.PresenterToModel {
    private MVP_Main.ModelToPresenter mPresenter;
    public MainModel(MVP_Main.ModelToPresenter presenter) {
        this.mPresenter = presenter;
    }
    /**
     * Unit Testing
     */
    public MainModel(MVP_Main.ModelToPresenter presenter,String response) {
        this.mPresenter = presenter;
    }
    /**
     * Called by Presenter when View is destroyed
     * @param isChangingConfiguration   true configuration is changing
     */
    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        if (!isChangingConfiguration) {
            mPresenter = null;
        }
    }
    // ---Connects using HTTP GET---
    private static InputStream OpenHttpGETConnection(String url) {
        InputStream inputStream = null;
        try {
            inputStream = ((HttpURLConnection)new URL(url).openConnection()).getInputStream();
        } catch (Exception e) {
            //Log.d("InputStream", e.getLocalizedMessage());
        }
        return inputStream;
    }

    /**
     * Loads all data from DB
     * @return  true with success
     */
    @Override
    public String loadData() {
        int BUFFER_SIZE = 2000;
        InputStream in = null;
        try {
            in = OpenHttpGETConnection("http://api.worldbank.org/incomeLevels/LIC/countries?format=json");
        } catch (Exception e) {
            //Log.d("DownloadText", e.getLocalizedMessage());
            return "";
        }
        InputStreamReader isr = new InputStreamReader(in);
        int charRead;
        String str = "";
        char[] inputBuffer = new char[BUFFER_SIZE];
        try {
            while ((charRead = isr.read(inputBuffer)) > 0) {
                String readString = String
                        .copyValueOf(inputBuffer, 0, charRead);
                str += readString;
                inputBuffer = new char[BUFFER_SIZE];
            }
            in.close();
        } catch (IOException e) {
            //Log.d("DownloadText", e.getLocalizedMessage());
            return "";
        }
        return str;
    }

}
