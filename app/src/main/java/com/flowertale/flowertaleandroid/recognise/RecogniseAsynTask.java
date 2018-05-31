package com.flowertale.flowertaleandroid.recognise;

import android.os.AsyncTask;

import com.flowertale.flowertaleandroid.bean.RecognitionResult;
import com.flowertale.flowertaleandroid.bean.Response;
import com.flowertale.flowertaleandroid.http.ResponseData;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class RecogniseAsynTask extends AsyncTask<String, Void, ResponseData> {

    private OnRecogniseListener<List<RecognitionResult>> mOnRecogniseListener;

    @Override
    protected ResponseData doInBackground(String... strings) {
//        return HblPlantApiService.recognise(strings[0]);
        return HblPlantApiService.testRecognise();
    }

    @Override
    protected void onPostExecute(ResponseData responseData) {
        if (responseData != null && responseData.getStatusCode() == 200) {
            Type type = new TypeToken<Response<List<RecognitionResult>>>() {
            }.getType();
            Response<List<RecognitionResult>> response = new Gson().fromJson(responseData.getBody(), type);
            if (response != null && response.getStatus() == 0) {
                List<RecognitionResult> recognitionResultList = response.getResult();
                if (recognitionResultList != null && recognitionResultList.size() > 0) {
                    mOnRecogniseListener.onSuccess(recognitionResultList);
                    return;
                }
            }
            mOnRecogniseListener.onFailure();
        }
    }

    public void setOnRecogniseListener(OnRecogniseListener<List<RecognitionResult>> onRecogniseListener) {
        mOnRecogniseListener = onRecogniseListener;
    }
}
