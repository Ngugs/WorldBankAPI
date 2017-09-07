package com.example.redman.wbank;

import android.content.Context;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;

/**
 * Created by redman on 8/29/17.
 */

public interface MVP_Main {

    interface PresenterToView {
        Context getAppContext();
        Context getActivityContext();
        void showToast(Toast toast);
        void showProgress();
        void hideProgress();
    }
    interface ViewToPresenter {
        void onDestroy(boolean isChangingConfiguration);
        void setView(PresenterToView view);
        void readWBAPI(GoogleMap googleMap);
    }
    /**
     * Operations offered to Model to communicate with Presenter
     * Handles all data business logic.
     */

    interface ModelToPresenter {
        Context getAppContext();
        Context getActivityContext();
    }

    /**
     *      Presenter to Model
     */
    interface PresenterToModel {
        void onDestroy(boolean isChangingConfiguration);
        String loadData();
    }
}
