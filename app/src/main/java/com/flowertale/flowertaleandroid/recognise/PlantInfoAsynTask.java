package com.flowertale.flowertaleandroid.recognise;

import android.os.AsyncTask;

import com.flowertale.flowertaleandroid.bean.PlantInfoResult;
import com.flowertale.flowertaleandroid.bean.Response;
import com.flowertale.flowertaleandroid.http.ResponseData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class PlantInfoAsynTask extends AsyncTask<String, Void, ResponseData> {

    OnRecogniseListener<List<PlantInfoResult>> mOnRecogniseListener;

    @Override
    protected ResponseData doInBackground(String... strings) {
        return HblPlantApiService.info(strings[0]);
    }

    @Override
    protected void onPostExecute(ResponseData responseData) {
        if (responseData != null && responseData.getStatusCode() == 200) {
            Type type = new TypeToken<Response<List<PlantInfoResult>>>() {
            }.getType();
            Response<List<PlantInfoResult>> response = new Gson().fromJson(responseData.getBody(), type);
            if (response != null && response.getStatus() == 0) {
                List<PlantInfoResult> plantInfoResultList = response.getResult();
                if (plantInfoResultList != null && plantInfoResultList.size() > 0) {
                    mOnRecogniseListener.onSuccess(plantInfoResultList);
                    return;
                }
            }
            mOnRecogniseListener.onFailure();
        }
    }

    public void setOnRecogniseListener(OnRecogniseListener<List<PlantInfoResult>> onRecogniseListener) {
        mOnRecogniseListener = onRecogniseListener;
    }
}
