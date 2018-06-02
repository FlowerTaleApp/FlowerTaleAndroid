package com.flowertale.flowertaleandroid;

import android.Manifest;
import android.content.ContentUris;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.media.ExifInterface;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class RecordAddActivity extends AppCompatActivity {

    public static final int TAKE_PHOTO = 0;
    public static final int CHOOSE_PHOTO = 1;
    private ImageView flowerRecordImage;
    private File mPhotoFile;
    private String filePath;
    private String fileName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFinishOnTouchOutside(true);
        setContentView(R.layout.activity_record_add);
        initView();
    }

    private void initView(){
        Button button = (Button)findViewById(R.id.record_publish);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        flowerRecordImage = (ImageView)findViewById(R.id.flower_image);

        LinearLayout mCameraImage = (LinearLayout) findViewById(R.id.camera);
        LinearLayout mPhotosImage = (LinearLayout) findViewById(R.id.photos);
        requestPermissions();
        final Intent captureImage = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        PackageManager packageManager = getPackageManager();
        boolean canTakePhoto = captureImage.resolveActivity(packageManager) != null;
        mCameraImage.setEnabled(canTakePhoto);
        mCameraImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPhotoFile = new File(getFilesDir(), "IMG_" + System.currentTimeMillis());
                Uri uri = FileProvider.getUriForFile(RecordAddActivity.this,
                        "com.flowertale.flowertaleandroid.fileprovider", mPhotoFile);
                //Log.i(TAG,uri.toString());
                captureImage.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                List<ResolveInfo> cameraActivities = getPackageManager().queryIntentActivities(captureImage,
                        PackageManager.MATCH_DEFAULT_ONLY);
                for (ResolveInfo activity : cameraActivities) {
                    grantUriPermission(activity.activityInfo.packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                }
                filePath = mPhotoFile.getPath();
                fileName = mPhotoFile.getName();
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
                        MediaStore.Images.Media.insertImage(getContentResolver(), filePath, fileName, null);
                    } catch (FileNotFoundException e) {
                        Log.e("RecogniseFragment", e.getMessage());
                    }
                    Bitmap bitmap = BitmapFactory.decodeFile(filePath);
                    //解决三星手机获取系统照片自动横屏显示的问题
                    int degree = readPictureDegree(filePath);
                    bitmap = rotaingImageView(degree,bitmap);
                    Glide.with(this).load(bitmap).into(flowerRecordImage);
                }
                break;
            case CHOOSE_PHOTO:
                if (resultCode == RESULT_OK) {
                    String imagePath=null;
                    Uri uri=data.getData();
                    if (DocumentsContract.isDocumentUri(RecordAddActivity.this, uri)) {
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
                        Toast.makeText(RecordAddActivity.this,"选择图片失败",Toast.LENGTH_SHORT).show();
                        return;
                    }
                    mPhotoFile=new File(imagePath);
                    Glide.with(this).load(mPhotoFile).into(flowerRecordImage);
                }
                break;
            default:
                break;
        }
    }

    private void requestPermissions() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(RecordAddActivity.this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(RecordAddActivity.this, permissions, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            Toast.makeText(RecordAddActivity.this, "必须同意所有权限才能使用本程序", Toast.LENGTH_SHORT).show();
                            finish();
                            return;
                        }
                    }
                } else {
                    Toast.makeText(RecordAddActivity.this, "发生未知错误", Toast.LENGTH_SHORT).show();
                    finish();
                }
                break;
            default:
        }
    }

    private String getImagePath(Uri uri,String selection) {
        String path=null;
        Cursor cursor = getContentResolver().query(uri, null, selection, null, null);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                path = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }
            cursor.close();
        }
        return path;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {                       //避免在返回mPhotoFile被置为空字符串
        super.onSaveInstanceState(outState);
        if (mPhotoFile!=null) {
            outState.putString("filePath", mPhotoFile.getPath());
        }

        if (mPhotoFile!=null) {
            outState.putString("fileName", mPhotoFile.getName());
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if (TextUtils.isEmpty(filePath)) {
            filePath = savedInstanceState.getString("filePath");
        }
        if (TextUtils.isEmpty(fileName)) {
            fileName = savedInstanceState.getString("fileName");
        }
    }

    //获取图片的旋转角度
    public static int readPictureDegree(String path) {
        int degree  = 0;
        try {
            ExifInterface exifInterface = new ExifInterface(path);
            int orientation = exifInterface.getAttributeInt(ExifInterface.TAG_ORIENTATION, ExifInterface.ORIENTATION_NORMAL);
            switch (orientation) {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    degree = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    degree = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    degree = 270;
                    break;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return degree;
    }

    /**
     * 旋转图片
     * @param angle
     * @param bitmap
     * @return Bitmap
     */
    public static Bitmap rotaingImageView(int angle , Bitmap bitmap) {
        //旋转图片 动作
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        System.out.println("angle2=" + angle);
        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0,
                bitmap.getWidth(), bitmap.getHeight(), matrix, true);
        return resizedBitmap;
    }

}
