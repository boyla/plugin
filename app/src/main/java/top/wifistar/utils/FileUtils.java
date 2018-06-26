package top.wifistar.utils;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by boyla on 2018/6/26.
 */

public class FileUtils {

    public static final int REQUEST_CODE_FILES = 0;
    public static final int REQUEST_CODE_PIC = 1;
    public static final int REQUEST_CODE_VIDEO = 2;
    public static final int REQUEST_CODE_PIC_VIDEO = 3;
    public static final int REQUEST_CODE_ADIUO = 4;

    static Map<Integer,String> typeMap;

    public static void startFileChooser(Activity activity,int requestCode){
        if(typeMap==null){
            initTypeMap();
        }
        String type = typeMap.get(requestCode);
        String action = Build.VERSION.SDK_INT < 19 ? Intent.ACTION_GET_CONTENT : Intent.ACTION_OPEN_DOCUMENT;
        Intent intent = new Intent(action);
        intent.setType(type);//无类型限制
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
        activity.startActivityForResult(Intent.createChooser(intent, "Select Files"), 1);
    }



    private static void initTypeMap() {
        typeMap = new HashMap<>();
        typeMap.put(REQUEST_CODE_FILES,"*/*");
        typeMap.put(REQUEST_CODE_PIC,"image/*");
        typeMap.put(REQUEST_CODE_VIDEO,"video/*");
        typeMap.put(REQUEST_CODE_PIC_VIDEO,"video/*;image/*");
        typeMap.put(REQUEST_CODE_ADIUO,"audio/*");
    }

}
