package top.wifistar.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.lidong.photopicker.ImageCaptureManager;
import com.lidong.photopicker.PhotoPickerActivity;
import com.lidong.photopicker.PhotoPreviewActivity;
import com.lidong.photopicker.SelectModel;
import com.lidong.photopicker.intent.PhotoPickerIntent;
import com.lidong.photopicker.intent.PhotoPreviewIntent;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UploadBatchListener;
import top.wifistar.R;
import top.wifistar.app.ToolbarActivity;
import top.wifistar.bean.BUser;
import top.wifistar.bean.demo.Moment;
import top.wifistar.bean.demo.User;
import top.wifistar.event.PublishMomentEvent;
import top.wifistar.manager.FileUploadManager;
import top.wifistar.utils.ACache;
import top.wifistar.utils.EventUtils;
import top.wifistar.utils.Utils;


public class PublishMomentActivity extends ToolbarActivity {

    private static final int REQUEST_CAMERA_CODE = 10;
    private static final int REQUEST_PREVIEW_CODE = 20;
    private ArrayList<String> imagePaths = new ArrayList<>();
    private ImageCaptureManager captureManager; // 相机拍照处理类

    private GridView gridView;
    private GridAdapter gridAdapter;
    private Button mButton;
    private String depp;
    private EditText textView;
    private String TAG = PublishMomentActivity.class.getSimpleName();
    private static final int PERMISSION_REQUEST_CODE = 1;

    @Override
    protected void initTopBar() {

    }

    @Override
    protected void initUI() {
        super.setContentView(R.layout.activity_publish_moment);
        setToolbarTitle();
        gridView = (GridView) findViewById(R.id.gridView);
        mButton = (Button) findViewById(R.id.button);
        textView = (EditText) findViewById(R.id.et_context);

        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        cols = cols < 3 ? 3 : cols;
        gridView.setNumColumns(cols);

        // preview
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String imgs = (String) parent.getItemAtPosition(position);
                //when click add item to pick a picture
                if ("000000".equals(imgs)) {
                    //check permission
                    if (ContextCompat.checkSelfPermission(PublishMomentActivity.this,
                            Manifest.permission.READ_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        //ask for permission
                        ActivityCompat.requestPermissions(PublishMomentActivity.this,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                PERMISSION_REQUEST_CODE);
                    } else {
                        //show pictures select page
                        jumpToPicSelectPage();
                    }
                } else {
                    PhotoPreviewIntent intent = new PhotoPreviewIntent(PublishMomentActivity.this);
                    intent.setCurrentItem(position);
                    intent.setPhotoPaths(imagePaths);
                    startActivityForResult(intent, REQUEST_PREVIEW_CODE);
                }
            }
        });
        imagePaths.add("000000");
        gridAdapter = new GridAdapter(imagePaths);
        gridView.setAdapter(gridAdapter);
        mButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                depp = textView.getText().toString().trim() != null ? textView.getText().toString().trim() : "woowoeo";
                new Thread() {
                    @Override
                    public void run() {
                        super.run();
                        FileUploadManager.uploadMany(imagePaths, depp);
//                        FileUploadManager.upload(imagePaths,depp);
                    }
                }.start();
            }
        });
//        EventUtils.registerEventBus(this);

    }

    private void jumpToPicSelectPage() {
        PhotoPickerIntent intent = new PhotoPickerIntent(PublishMomentActivity.this);
        intent.setSelectModel(SelectModel.MULTI);
        intent.setShowCarema(true); // 是否显示拍照
        intent.setMaxTotal(9); // 最多选择照片数量，默认为6
        intent.setSelectedPaths(imagePaths); // 已选中的照片地址， 用于回显选中状态
        startActivityForResult(intent, REQUEST_CAMERA_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.
                    jumpToPicSelectPage();
                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.GINGERBREAD)
    protected void setToolbarTitle() {
        mToolbar.setNavigationIcon(R.drawable.back);
        setCenterTitle("新动态");
        tool_bar_frame.setVisibility(View.VISIBLE);
        mCustomLogo.setVisibility(View.GONE);
        invalidateOptionsMenu();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // TODO Auto-generated method stub
        getMenuInflater().inflate(R.menu.publish_moment, menu);
        return super.onCreateOptionsMenu(menu);
    }

    String momentType = "4";// text type default

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
        } else if (item.getItemId() == R.id.publish) {
            //first get moment type
            if (imagePaths.size() > 1) {
                momentType = "2";//pic type
            }

            //post moment
            switch (momentType) {
                case "2":
                    uploadPics();
                    break;
                case "4":
                    saveMoment();
                    break;
            }
            finish();
        }

        return super.onOptionsItemSelected(item);
    }

    private void uploadPics() {
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
        String[] filePaths = new String[imagePaths.size() - 1];
        String[] src = new String[imagePaths.size()];
        imagePaths.toArray(src);
        System.arraycopy(src, 0, filePaths, 0,
                imagePaths.size() - 1);
        BmobFile.uploadBatch(filePaths, new UploadBatchListener() {

            @Override
            public void onSuccess(List<BmobFile> files, List<String> urls) {
                //1、files-上传完成后的BmobFile集合，是为了方便大家对其上传后的数据进行操作，例如你可以将该文件保存到表中
                //2、urls-上传文件的完整url地址
                if (urls.size() == filePaths.length) {//如果数量相等，则代表文件全部上传完成
                    saveMoment(urls);
                }
            }

            @Override
            public void onError(int statuscode, String errormsg) {
                //ShowToast("错误码"+statuscode +",错误描述："+errormsg);
            }

            @Override
            public void onProgress(int curIndex, int curPercent, int total, int totalPercent) {
                //1、curIndex--表示当前第几个文件正在上传
                //2、curPercent--表示当前上传文件的进度值（百分比）
                //3、total--表示总的上传文件数
                //4、totalPercent--表示总的上传进度（百分比）
            }
        });
//            }
//        }).start();
    }

    private void saveMoment() {
        saveMoment(null);
    }

    private void saveMoment(List<String> urls) {
        Moment momentToPost = new Moment();
        momentToPost.setType(momentType);
        momentToPost.setContent(textView.getText().toString());
        if (urls != null) {
            momentToPost.setPhotos(TextUtils.join(",", urls));
        }
        //set user
        String id = ACache.get(this).getAsString("SHORT_USER_ID_" + BUser.getCurrentUser().getObjectId());
        User user = new User();
        user.setObjectId(id);
        momentToPost.setUser(user);
        momentToPost.save(new SaveListener<String>() {

            @Override
            public void done(String objectId, BmobException e) {
                if (e == null) {
                    Utils.showToast("发布动态成功");
                    PublishMomentEvent event = new PublishMomentEvent();
                    event.moment = momentToPost;
                    EventUtils.post(event);
                } else {
                    Utils.showToast("发布动态失败：" + e.getMessage());
                }
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 选择照片
                case REQUEST_CAMERA_CODE:
                    ArrayList<String> list = data.getStringArrayListExtra(PhotoPickerActivity.EXTRA_RESULT);
                    Log.d(TAG, "list: " + "list = [" + list.size());
                    loadAdpater(list);
                    break;
                // 预览
                case REQUEST_PREVIEW_CODE:
                    ArrayList<String> ListExtra = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                    Log.d(TAG, "ListExtra: " + "ListExtra = [" + ListExtra.size());
                    loadAdpater(ListExtra);
                    break;
            }
        }
    }

    private void loadAdpater(ArrayList<String> paths) {
        if (imagePaths != null && imagePaths.size() > 0) {
            imagePaths.clear();
        }
        if (paths.contains("000000")) {
            paths.remove("000000");
        }
        paths.add("000000");
        imagePaths.addAll(paths);
        gridAdapter = new GridAdapter(imagePaths);
        gridView.setAdapter(gridAdapter);
        try {
            JSONArray obj = new JSONArray(imagePaths);
            Log.e("--", obj.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private class GridAdapter extends BaseAdapter {
        private ArrayList<String> listUrls;
        private LayoutInflater inflater;

        public GridAdapter(ArrayList<String> listUrls) {
            this.listUrls = listUrls;
            if (listUrls.size() == 10) {
                listUrls.remove(listUrls.size() - 1);
            }
            inflater = LayoutInflater.from(PublishMomentActivity.this);
        }

        public int getCount() {
            return listUrls.size();
        }

        @Override
        public String getItem(int position) {
            return listUrls.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            ViewHolder holder = null;
            if (convertView == null) {
                holder = new ViewHolder();
                convertView = inflater.inflate(R.layout.item_moment_image, parent, false);
                holder.image = (ImageView) convertView.findViewById(R.id.imageView);
                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }

            final String path = listUrls.get(position);
            if (path.equals("000000")) {
                holder.image.setImageResource(R.drawable.add_pic);
            } else {
                Glide.with(PublishMomentActivity.this)
                        .load(path)
                        .placeholder(R.mipmap.default_error)
                        .error(R.mipmap.default_error)
                        .centerCrop()
                        .crossFade()
                        .into(holder.image);
            }
            return convertView;
        }

        class ViewHolder {
            ImageView image;
        }
    }

    @Override
    protected void onDestroy() {
//        EventUtils.unregisterEventBus(this);
        super.onDestroy();
    }
}
