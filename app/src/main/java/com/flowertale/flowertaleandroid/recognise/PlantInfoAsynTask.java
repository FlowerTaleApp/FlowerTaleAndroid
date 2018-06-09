package com.flowertale.flowertaleandroid.recognise;

import android.os.AsyncTask;

import com.flowertale.flowertaleandroid.bean.PlantInfoResult;
import com.flowertale.flowertaleandroid.bean.Response;
import com.flowertale.flowertaleandroid.http.ResponseData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;

public class PlantInfoAsynTask extends AsyncTask<String, Void, ResponseData> {

    OnRecogniseListener<PlantInfoResult> mOnRecogniseListener;

    @Override
    protected ResponseData doInBackground(String... strings) {
        return HblPlantApiService.info(strings[0]);
    }

    @Override
    protected void onPostExecute(ResponseData responseData) {
        if (responseData != null && responseData.getStatusCode() == 200) {
            Type type = new TypeToken<Response<PlantInfoResult>>() {
            }.getType();
            Response<PlantInfoResult> response = new Gson().fromJson(responseData.getBody(), type);
            if (response != null && response.getStatus() == 0) {
                PlantInfoResult plantInfoResult = response.getResult();
                if (plantInfoResult != null) {
                    mOnRecogniseListener.onSuccess(plantInfoResult);
                    return;
                }
            }
            mOnRecogniseListener.onFailure();
        }
    }

    public void setOnRecogniseListener(OnRecogniseListener<PlantInfoResult> onRecogniseListener) {
        mOnRecogniseListener = onRecogniseListener;
    }
}
