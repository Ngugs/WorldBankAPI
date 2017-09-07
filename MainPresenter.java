package com.example.redman.wbank;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;

import static java.lang.Double.parseDouble;

/**
 * Created by redman on 8/29/17.
 */

public class MainPresenter implements MVP_Main.ViewToPresenter, MVP_Main.ModelToPresenter {

    // View reference. We use as a WeakReference
    // because the Activity could be destroyed at any time
    // and we don't want to create a memory leak
    private WeakReference<MVP_Main.PresenterToView> mView;
    // Model reference
    private MVP_Main.PresenterToModel mModel;

    /**
     * Presenter Constructor
     * @param view  MainActivity
     */
    public MainPresenter(MVP_Main.PresenterToView view) {
        mView = new WeakReference<>(view);
    }

    /**
     * Called by View every time it is destroyed.
     * @param isChangingConfiguration   true: is changing configuration
     *                                  and will be recreated
     */
    @Override
    public void onDestroy(boolean isChangingConfiguration) {
        // View should be null every time onDestroy is called
        mView = null;
        // Inform Model about the event
        mModel.onDestroy(isChangingConfiguration);
        // Activity destroyed
        if ( !isChangingConfiguration ) {
            // Nulls Model when the Activity destruction is permanent
            mModel = null;
        }
    }
    /**
     * Return the View reference.
     * Could throw an exception if the View is unavailable.
     *
     * @throws NullPointerException when View is unavailable
     */
    private MVP_Main.PresenterToView getView() throws NullPointerException{
        if ( mView != null )
            return mView.get();
        else
            throw new NullPointerException("View is unavailable");
    }

    /**
     * Called by View during the reconstruction events
     * @param view  Activity instance
     */
    @Override
    public void setView(MVP_Main.PresenterToView view) {
        mView = new WeakReference<>(view);
    }

    /**
     * Called by Activity during MVP setup. Only called once.
     * @param model Model instance
     */
    public void setModel(MVP_Main.PresenterToModel model) {
        mModel = model;
    }
    /**
     * Load data from Model in a AsyncTask
     */
    @Override
    public void readWBAPI(final GoogleMap mMap) {
        try {
            //getView().showProgress();
            new AsyncTask<Void, Void, String>() {
                @Override
                protected String doInBackground(Void... params) {
                    // Load data from Model
                    return mModel.loadData();
                }

                @Override
                protected void onPostExecute(String result) {
                    try {
                        //getView().hideProgress();
                        if (result.equals("")) // Loading error
                            getView().showToast(makeToast("Error loading data."));
                        else {
                            try {
                                JSONArray allEntries = new JSONArray(result).getJSONArray(1);
                                for (int i = 0; i < allEntries.length(); i++) {
                                    final JSONObject oneEntry = allEntries.getJSONObject(i);
                                    mMap.addMarker(new MarkerOptions().position(new LatLng(parseDouble(oneEntry.getString("latitude")),parseDouble(oneEntry.getString("longitude")))).title(oneEntry.getString("name")));

                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                           //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
                        }
                    } catch (NullPointerException e) {
                        e.printStackTrace();
                    }
                }
            }.execute();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }
    /**
     * Create a Toast object with given message
     * @param msg   Toast message
     * @return      A Toast object
     */
    private Toast makeToast(String msg) {
        return Toast.makeText(getView().getAppContext(), msg, Toast.LENGTH_SHORT);
    }
    /**
     * Retrieve Application Context
     * @return  Application context
     */
    @Override
    public Context getAppContext() {
        try {
            return getView().getAppContext();
        } catch (NullPointerException e) {
            return null;
        }
    }

    /**
     * Retrieves Activity context
     * @return  Activity context
     */
    @Override
    public Context getActivityContext() {
        try {
            return getView().getActivityContext();
        } catch (NullPointerException e) {
            return null;
        }
    }
}
