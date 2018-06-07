package com.flowertale.flowertaleandroid;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
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
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.flowertale.flowertaleandroid.bean.RecognitionResult;
import com.flowertale.flowertaleandroid.recognise.OnRecogniseListener;
import com.flowertale.flowertaleandroid.recognise.RecogniseAsynTask;
import com.flowertale.flowertaleandroid.util.BitmapUtil;
import com.flowertale.flowertaleandroid.util.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class RecogniseFragment extends Fragment {

    public static final String API_RECOGNITION_RESULT = "ApiRecognitionResult";
    private static final String TAG = "RecogniseFragment";
    private static final int TAKE_PHOTO = 0;
    private static final int CHOOSE_PHOTO = 1;
    private ImageView mCameraImage;
    private ImageView mPhotosImage;
    private ImageView mHistoryImage;
    private File mPhotoFile;
    private ProgressBar mProgressBar;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_recognise, container, false);
        mCameraImage = view.findViewById(R.id.camera_image);
        mPhotosImage = view.findViewById(R.id.photos_image);
        mHistoryImage = view.findViewById(R.id.history_image);
        mProgressBar = view.findViewById(R.id.progress_bar);
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
                Intent intent = new Intent("android.intent.action.GET_CONTENT");
                intent.setType("image/*");
                startActivityForResult(intent, CHOOSE_PHOTO);
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
                    doRecognise();
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    String imagePath = null;
                    Uri uri = data.getData();
                    if (DocumentsContract.isDocumentUri(getActivity(), uri)) {
                        String docId = DocumentsContract.getDocumentId(uri);
                        if ("com.android.providers.media.documents".equals(uri.getAuthority())) {
                            String id = docId.split(":")[1];
                            String selection = MediaStore.Images.Media._ID + "=" + id;
                            imagePath = FileUtil.getImagePath(getActivity(), MediaStore.Images.Media.EXTERNAL_CONTENT_URI, selection);
                        } else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())) {
                            Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://download/public_downloads"),
                                    Long.valueOf(docId));
                            imagePath = FileUtil.getImagePath(getActivity(), contentUri, null);
                        } else if ("content".equalsIgnoreCase(uri.getScheme())) {
                            imagePath = FileUtil.getImagePath(getActivity(), uri, null);
                        } else if ("file".equalsIgnoreCase(uri.getScheme())) {
                            imagePath = uri.getPath();
                        }
                    } else {
                        imagePath = FileUtil.getImagePath(getActivity(), uri, null);
                    }
                    if (imagePath == null) {
                        Toast.makeText(getActivity(), "选择图片失败", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mPhotoFile = new File(imagePath);
                    doRecognise();
                }
                break;
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

    public void doRecognise() {
        Bitmap bitmap = BitmapUtil.compress(mPhotoFile);
        String imageBase64 = FileUtil.convertToBase64(bitmap);
        RecogniseAsynTask recogniseAsynTask = new RecogniseAsynTask();
        recogniseAsynTask.setOnRecogniseListener(new OnRecogniseListener<List<RecognitionResult>>() {
            @Override
            public void onSuccess(List<RecognitionResult> result) {
                Intent intent = new Intent(getActivity(), RecognitionResultActivity.class);
                intent.putExtra(API_RECOGNITION_RESULT, (Serializable) result);
                startActivity(intent);
                hideProgressBar();
            }

            @Override
            public void onFailure() {
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(getActivity(), "未能识别", Toast.LENGTH_SHORT).show();
                        hideProgressBar();
                    }
                });
            }
        });
        recogniseAsynTask.execute(imageBase64);
        showProgressBar();
    }

    private void showProgressBar() {
        mProgressBar.setVisibility(View.VISIBLE);
        getActivity().getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }

    private void hideProgressBar() {
        mProgressBar.setVisibility(View.INVISIBLE);
        getActivity().getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
    }
}
