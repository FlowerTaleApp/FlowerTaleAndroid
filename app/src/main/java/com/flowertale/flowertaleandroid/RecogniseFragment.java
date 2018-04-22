package com.flowertale.flowertaleandroid;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.aip.imageclassify.AipImageClassify;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class RecogniseFragment extends Fragment {

    private static final String TAG = "RecogniseFragment";

    private static final int TAKE_PHOTO = 0;
    private static final int CHOOSE_PHOTO=1;
    public static final String API_RECOGNITION_RESULT ="ApiRecognitionResult";

    private ImageView mCameraImage;
    private ImageView mPhotosImage;
    private ImageView mHistoryImage;
    private File mPhotoFile;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recognise, container, false);
        mCameraImage = view.findViewById(R.id.camera_image);
        mPhotosImage = view.findViewById(R.id.photos_image);
        mHistoryImage = view.findViewById(R.id.history_image);
        requestPermissions();
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager packageManager = getActivity().getPackageManager();
        boolean canTakePhoto = captureImage.resolveActivity(packageManager) != null;
        mCameraImage.setEnabled(canTakePhoto);
        mCameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoFile = new File(getContext().getFilesDir(), "IMG_" + System.currentTimeMillis());
                Uri uri = FileProvider.getUriForFile(getActivity(),
                        "com.flowertale.flowertaleandroid.fileprovider", mPhotoFile);
                //Log.i(TAG,uri.toString());
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                List<ResolveInfo> cameraActivities = getActivity().getPackageManager().queryIntentActivities(captureImage,
                        PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activity : cameraActivities) {
                    getActivity().grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                startActivityForResult(captureImage, TAKE_PHOTO);
            }
        });
        mPhotosImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent,CHOOSE_PHOTO);
            }
        });
        return view;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case TAKE_PHOTO:
                //Log.i(TAG, mPhotoFile.getPath());
                if (resultCode == RESULT_OK) {
                    // 将图片保存到系统相册
                    try {
                        MediaStore.Images.Media.insertImage(getContext().getContentResolver(), mPhotoFile.getPath(), mPhotoFile.getName(), null);
                    } catch (FileNotFoundException e) {
                        Log.e(TAG, e.getMessage());
                    }
                    Intent mediaScan = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    Uri uri = Uri.fromFile(mPhotoFile);
                    mediaScan.setData(uri);
                    getActivity().sendBroadcast(mediaScan);
                    // 调用百度API进行识别
                    new ClassifyTask().execute(mPhotoFile.getPath());
                }
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    String imagePath=null;
                    Uri uri=data.getData();
                    if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
                        String docId = DocumentsContract.getDocumentId(uri);
                        if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                            String id=docId.split(":")[1];
                            String selection=MediaStore.Images.Media._ID+"="+id;
                            imagePath = getImagePath(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                        } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                            Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://download/public_downloads"),
                                    Long.valueOf(docId));
                            imagePath=getImagePath(contentUri,null);
                        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                            imagePath = getImagePath(uri, null);
                        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                            imagePath=uri.getPath();
                        }
                    } else {
                        imagePath=getImagePath(uri,null);
                    }
                    if (imagePath == null) {
                        Toast.makeText(getActivity(),"选择图片失败",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mPhotoFile=new File(imagePath);
                    new ClassifyTask().execute(mPhotoFile.getPath());
                }
            default:
        }
    }

    private void requestPermissions() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(getActivity(), "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            getActivity().finish();
                            return;
                        }
                    }
                } else {
                    Toast.makeText(getActivity(), "发生未知错误", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                break;
            default:
        }
    }

    private class ClassifyTask extends AsyncTask<String, Void, List<ApiRecResultItem>> {
        @Override
        protected List<ApiRecResultItem> doInBackground(String... strings) {
            return classify(AipImageClassifyClient.get(getActivity()),strings[0]);
        }

        @Override
        protected void onPostExecute(List<ApiRecResultItem> items) {
            if (items == null) {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(),"未能识别",Toast.LENGTH_SHORT).show();
                    }
                });
                return;
            }
            Intent intent=new Intent(getActivity(),RecognitionResultActivity.class);
            intent.putExtra(API_RECOGNITION_RESULT,(Serializable)items);
            startActivity(intent);
        }

        private List<ApiRecResultItem> classify(AipImageClassify client,String imagePath) {
            qualityCompress(BitmapFactory.decodeFile(imagePath),new File(imagePath));
            HashMap<String,String> options=new HashMap<>();
            JSONObject response=client.plantDetect(imagePath,options);
            Log.i(TAG,response.toString());
            try {
                return parseItems(response);
            } catch (JSONException e) {
                Log.e(TAG,e.getMessage());
                return null;
            }
        }
    }

    private static void qualityCompress(Bitmap bitmap, File file) {
        // size of the image to be uploaded restricted less than 3M
        final int size=3*1024*1024;
        ByteArrayOutputStream baos=new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100,baos);
        int quality=90;
        while (baos.toByteArray().length > size) {
            baos.reset();
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, baos);
            quality -= 10;
        }
        try {
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(baos.toByteArray());
            fos.flush();
            fos.close();
        } catch (Exception e) {
            Log.e(TAG,e.getMessage());
        }
    }

    private String getImagePath(Uri uri,String selection) {
        String path=null;
        Cursor cursor = getActivity().getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    private List<ApiRecResultItem> parseItems(JSONObject jsonBody) throws JSONException{
        List<ApiRecResultItem> items=new ArrayList<>();
        JSONArray jsonArray = jsonBody.getJSONArray("result");
        for (int i=0;i<jsonArray.length();i++) {
            JSONObject object=jsonArray.getJSONObject(i);
            ApiRecResultItem item=new ApiRecResultItem();
            item.setCred(object.getDouble("score"));
            item.setName(object.getString("name"));
            items.add(item);
        }
        return items;
    }
}
