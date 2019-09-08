package top.wifistar.utils;

import android.Manifest;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import com.greysonparrelli.permiso.Permiso;
import top.wifistar.photopicker.ImageCaptureManager;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

import top.wifistar.R;

/**
 * Created by boyla on 2018/7/5.
 */

public class IntentUtils {

    public static void openCamera(Activity activity){
        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
            @Override
            public void onPermissionResult(Permiso.ResultSet resultSet) {
                if (resultSet.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) && resultSet.isPermissionGranted(Manifest.permission.CAMERA)) {
                    showCameraAction(activity);
                }else{
                    Toast.makeText(activity,"拍照需要获取相机和文件存储权限，请到应用权限中进行设置",Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                Permiso.getInstance().showRationaleInDialog("拍照", "需要使用相机和图片文件保存权限", null, callback);
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
    }

    private static void showCameraAction(Activity activity){
        try {
            Intent intent = dispatchTakePictureIntent(activity);
            activity.startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
        } catch (IOException e) {
            Toast.makeText(activity, R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    static Intent takePictureIntent;
    public static File photoFile;

    private static Intent dispatchTakePictureIntent(Activity activity) throws IOException {
        takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(activity.getPackageManager()) != null) {
            // Create the File where the photo should go
            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
            String imageFileName = "JPEG_" + timeStamp + "_";
            File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
            if (!storageDir.exists()) {
                if (!storageDir.mkdir()) {
                    throw new IOException();
                }
            }
            photoFile = new File(storageDir, imageFileName + ".jpg");
            // Continue only if the File was successfully created
            if (photoFile != null) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                } else {
                    ContentValues contentValues = new ContentValues(1);
                    contentValues.put(MediaStore.Images.Media.DATA, photoFile.getAbsolutePath());
                    Uri uri = activity.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                }
            }
        }
        return takePictureIntent;
    }
}
