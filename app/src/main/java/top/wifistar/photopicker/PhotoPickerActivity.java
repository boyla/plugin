package top.wifistar.photopicker;

import android.Manifest;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;

import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.ListPopupWindow;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.greysonparrelli.permiso.Permiso;
import com.jaeger.library.StatusBarUtil;

import top.wifistar.R;
import top.wifistar.app.AppExecutor;
import top.wifistar.imagelib.ImageLibUtils;
import top.wifistar.photopicker.intent.PhotoPreviewIntent;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class PhotoPickerActivity extends AppCompatActivity {

    public static final String TAG = PhotoPickerActivity.class.getName();

    private Context mCxt;

    /**
     * 图片选择模式，int类型
     */
    public static final String EXTRA_SELECT_MODE = "select_count_mode";
    public static final int CODE_CAMERA = 1001;
    /**
     * 单选
     */
    public static final int MODE_SINGLE = 0;
    /**
     * 多选
     */
    public static final int MODE_MULTI = 1;
    /**
     * 最大图片选择次数，int类型
     */
    public static final String EXTRA_SELECT_COUNT = "max_select_count";
    /**
     * 是否根据url加载网络图片
     */
    public static final String EXTRA_IS_NET_IMG = "is_net_img";
    /**
     * 默认最大照片数量
     */
    public static final int DEFAULT_MAX_TOTAL = 9;
    /**
     * 是否显示相机，boolean类型
     */
    public static final String EXTRA_SHOW_CAMERA = "show_camera";
    /**
     * 默认选择的数据集
     */
    public static final String EXTRA_DEFAULT_SELECTED_LIST = "default_result";
    /**
     * 筛选照片配置信息
     */
    public static final String EXTRA_IMAGE_CONFIG = "image_config";
    /**
     * 选择结果，返回为 ArrayList&lt;String&gt; 图片路径集合
     */
    public static final String EXTRA_RESULT = "select_result";

    // 结果数据
    private ArrayList<String> pickList = new ArrayList<>();
    // 文件夹数据
    private ArrayList<Folder> allFolders = new ArrayList<>();

    // 不同loader定义
    private static final int LOADER_ALL = 0;
    private static final int LOADER_CATEGORY = 1;

    private MenuItem menuDoneItem;
    private GridView mGridView;
    private View mPopupAnchorView;
    private Button btnAlbum;
    private Button btnPreview;

    // 最大照片数量
    private ImageCaptureManager captureManager;
    private int mDesireImageCount;
    private boolean mIsNetImg;
    private ImageConfig imageConfig; // 照片配置

    private ImageGridAdapter mImageAdapter;
    private FolderAdapter mFolderAdapter;
    private ListPopupWindow mFolderPopupWindow;

    private boolean hasFolderGened = false;
    private boolean mIsShowCamera = false;
    private Folder netFolder = new Folder();
    List<Image> allImage = new ArrayList<>();

    private List<String> urls = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photopicker);
        Permiso.getInstance().setActivity(this);
        int mStatusBarColor = getResources().getColor(R.color.primary);
        StatusBarUtil.setColorNoTranslucent(this, mStatusBarColor);
        initViews();

        // 照片属性
        imageConfig = getIntent().getParcelableExtra(EXTRA_IMAGE_CONFIG);

        // 是否加载网络图片
        mIsNetImg = getIntent().getBooleanExtra(EXTRA_IS_NET_IMG, false);
        if (mIsNetImg) {

        } else {
            // 首次加载所有图片
            getSupportLoaderManager().initLoader(LOADER_ALL, null, mLoaderCallback);
        }

        // 选择图片数量
        mDesireImageCount = getIntent().getIntExtra(EXTRA_SELECT_COUNT, DEFAULT_MAX_TOTAL);

        // 图片选择模式
        final int mode = getIntent().getExtras().getInt(EXTRA_SELECT_MODE, MODE_SINGLE);

        // 默认选择
        if (mode == MODE_MULTI) {
            ArrayList<String> tmp = getIntent().getStringArrayListExtra(EXTRA_DEFAULT_SELECTED_LIST);
            if (tmp != null && tmp.size() > 0) {
                pickList.addAll(tmp);
            }
        }

        // 是否显示照相机
        mIsShowCamera = getIntent().getBooleanExtra(EXTRA_SHOW_CAMERA, false);
        mImageAdapter = new ImageGridAdapter(mCxt, mIsShowCamera, getItemImageWidth());
        // 是否显示选择指示器
        mImageAdapter.showSelectIndicator(mode == MODE_MULTI);
        mGridView.setAdapter(mImageAdapter);

        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                if (mImageAdapter.isShowCamera()) {
                    // 如果显示照相机，则第一个Grid显示为照相机，处理特殊逻辑
                    if (i == 0) {
                        if (mode == MODE_MULTI) {
                            // 判断选择数量问题
                            if (mDesireImageCount == pickList.size() - 1) {
                                Toast.makeText(mCxt, R.string.msg_amount_limit, Toast.LENGTH_SHORT).show();
                                return;
                            }
                        }
                        Permiso.getInstance().requestPermissions(new Permiso.IOnPermissionResult() {
                            @Override
                            public void onPermissionResult(Permiso.ResultSet resultSet) {
                                if (resultSet.isPermissionGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) && resultSet.isPermissionGranted(Manifest.permission.CAMERA)) {
                                    showCameraAction();
                                } else {
                                    Toast.makeText(PhotoPickerActivity.this, "拍照需要获取相机和文件存储权限，请到应用权限中进行设置", Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onRationaleRequested(Permiso.IOnRationaleProvided callback, String... permissions) {
                                Permiso.getInstance().showRationaleInDialog("拍照", "需要使用相机和图片文件保存权限", null, callback);
                            }
                        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA);
                    } else {
                        // 正常操作
                        Image image = (Image) adapterView.getAdapter().getItem(i);
                        selectImageFromGrid(image, mode);
                    }
                } else {
                    // 正常操作
                    Image image = (Image) adapterView.getAdapter().getItem(i);
                    selectImageFromGrid(image, mode);
                }
            }
        });

        mFolderAdapter = new FolderAdapter(mCxt);

        // 打开相册列表
        btnAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mFolderPopupWindow == null) {
                    createPopupFolderList();
                }

                if (mFolderPopupWindow.isShowing()) {
                    mFolderPopupWindow.dismiss();
                } else {
                    mFolderPopupWindow.show();
                    int index = mFolderAdapter.getSelectIndex();
                    index = index == 0 ? index : index - 1;
                    mFolderPopupWindow.getListView().setSelection(index);
                }
            }
        });

        // 预览
        btnPreview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PhotoPreviewIntent intent = new PhotoPreviewIntent(mCxt);
                intent.setCurrentItem(0);
                intent.setPhotoPaths(pickList);
                startActivityForResult(intent, PhotoPreviewActivity.REQUEST_PREVIEW);
            }
        });
        netFolder.name = "网络图库";
        netFolder.isNet = true;
        AppExecutor.getInstance().postWork(new Runnable() {
            @Override
            public void run() {
                getNetImgs();
            }
        });
    }

    private void getNetImgs() {
        urls = ImageLibUtils.Companion.getUpsplashUrls();
        if (urls == null || urls.size() == 0) {
            ImageLibUtils.Companion.getBmobImageUrls(urls, new ImageLibUtils.LoadedNext() {
                @Override
                public void onNext() {
                    generateImgs();
                }
            });
        } else {
            generateImgs();
        }
    }

    private void generateImgs() {
        if (urls.size() > 0) {
            List<Image> list = new ArrayList<>();
            for (String str : urls) {
                Image image = new Image(str, "", System.currentTimeMillis());
                list.add(image);
            }
            netFolder.cover = list.get(0);
            netFolder.images = list;
            allFolders.add(0, netFolder);
            mFolderAdapter.setData(allFolders);
        }
    }

    private void initViews() {
        mCxt = this;
        captureManager = new ImageCaptureManager(mCxt);
        // ActionBar Setting
        Toolbar toolbar = findViewById(R.id.pickerToolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(getResources().getString(R.string.image));
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        mGridView = findViewById(R.id.grid);
        mGridView.setNumColumns(getNumColnums());

        mPopupAnchorView = findViewById(R.id.photo_picker_footer);
        btnAlbum = findViewById(R.id.btnAlbum);
        btnPreview = findViewById(R.id.btnPreview);
    }

    @Override
    protected void onResume() {
        super.onResume();
        Permiso.getInstance().setActivity(this);
    }

    private void createPopupFolderList() {

        mFolderPopupWindow = new ListPopupWindow(mCxt);
        mFolderPopupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        mFolderPopupWindow.setAdapter(mFolderAdapter);
        mFolderPopupWindow.setContentWidth(ListPopupWindow.MATCH_PARENT);
        mFolderPopupWindow.setWidth(ListPopupWindow.MATCH_PARENT);

        // 计算ListPopupWindow内容的高度(忽略mPopupAnchorView.height)，R.layout.item_foloer
        int folderItemViewHeight =
                // 图片高度
                getResources().getDimensionPixelOffset(R.dimen.folder_cover_size) +
                        // Padding Top
                        getResources().getDimensionPixelOffset(R.dimen.folder_padding) +
                        // Padding Bottom
                        getResources().getDimensionPixelOffset(R.dimen.folder_padding);
        int folderViewHeight = mFolderAdapter.getCount() * folderItemViewHeight;

        int screenHeigh = getResources().getDisplayMetrics().heightPixels;
        if (folderViewHeight >= screenHeigh) {
            mFolderPopupWindow.setHeight(Math.round(screenHeigh * 0.6f));
        } else {
            mFolderPopupWindow.setHeight(ListPopupWindow.WRAP_CONTENT);
        }

        mFolderPopupWindow.setAnchorView(mPopupAnchorView);
        mFolderPopupWindow.setModal(true);
        mFolderPopupWindow.setAnimationStyle(R.style.Animation_AppCompat_DropDownUp);
        mFolderPopupWindow.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                mFolderAdapter.setSelectIndex(position);

                final int index = position;
                final AdapterView v = parent;

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mFolderPopupWindow.dismiss();

                        if (index == 0) {
                            getSupportLoaderManager().restartLoader(LOADER_ALL, null, mLoaderCallback);
                            btnAlbum.setText(R.string.all_image);
                            mImageAdapter.setShowCamera(mIsShowCamera);
                        } else {
                            Folder folder = (Folder) v.getAdapter().getItem(index);
                            if (null != folder) {
                                mImageAdapter.setData(folder.images);
                                btnAlbum.setText(folder.name);
                                // 设定默认选择
                                if (pickList != null && pickList.size() > 0) {
                                    mImageAdapter.setDefaultSelected(pickList);
                                }
                            }
                            mImageAdapter.setShowCamera(false);
                        }

                        // 滑动到最初始位置
                        mGridView.smoothScrollToPosition(0);
                    }
                }, 100);
            }
        });
    }

    public void onSingleImageSelected(String path) {
        Intent data = new Intent();
        pickList.add(path);
        data.putStringArrayListExtra(EXTRA_RESULT, pickList);
        setResult(RESULT_OK, data);
        finish();
    }

    public void onImageSelected(String path) {
        if (!pickList.contains(path)) {
            pickList.add(path);
        }
        refreshActionStatus();
    }

    public void onImageUnselected(String path) {
        if (pickList.contains(path)) {
            pickList.remove(path);
        }
        refreshActionStatus();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                // 相机拍照完成后，返回图片路径
                case ImageCaptureManager.REQUEST_TAKE_PHOTO:
                    if (captureManager.getCurrentPhotoPath() != null) {
                        captureManager.galleryAddPic();
                        pickList.add(captureManager.getCurrentPhotoPath());
                    }
                    complete();
                    break;
                // 预览照片
                case PhotoPreviewActivity.REQUEST_PREVIEW:
                    ArrayList<String> pathArr = data.getStringArrayListExtra(PhotoPreviewActivity.EXTRA_RESULT);
                    // 刷新页面
                    if (pathArr != null && pathArr.size() != pickList.size()) {
                        pickList = pathArr;
                        refreshActionStatus();
                        mImageAdapter.setDefaultSelected(pickList);
                    }
                    break;
            }
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        Log.d(TAG, "on change");

        // 重置列数
        mGridView.setNumColumns(getNumColnums());
        // 重置Item宽度
        mImageAdapter.setItemSize(getItemImageWidth());

        if (mFolderPopupWindow != null) {
            if (mFolderPopupWindow.isShowing()) {
                mFolderPopupWindow.dismiss();
            }

            // 重置PopupWindow高度
            int screenHeigh = getResources().getDisplayMetrics().heightPixels;
            mFolderPopupWindow.setHeight(Math.round(screenHeigh * 0.6f));
        }

        super.onConfigurationChanged(newConfig);
    }

    /**
     * 选择相机
     */
    private void showCameraAction() {
        try {
            Intent intent = dispatchTakePictureIntent();
            startActivityForResult(intent, ImageCaptureManager.REQUEST_TAKE_PHOTO);
        } catch (IOException e) {
            Toast.makeText(mCxt, R.string.msg_no_camera, Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }

    Intent takePictureIntent;
    File photoFile;

    public Intent dispatchTakePictureIntent() throws IOException {
        takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(this.getPackageManager()) != null) {
            // Create the File where the photo should go
            photoFile = captureManager.createImageFile();
            // Continue only if the File was successfully created
            if (photoFile != null) {
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
                            Uri.fromFile(photoFile));
                } else {
                    ContentValues contentValues = new ContentValues(1);
                    contentValues.put(MediaStore.Images.Media.DATA, photoFile.getAbsolutePath());
                    Uri uri = this.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);
                    takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                }
            }
        }
        return takePictureIntent;
    }


    /**
     * 选择图片操作
     *
     * @param image
     */
    private void selectImageFromGrid(Image image, int mode) {
        if (image != null) {
            // 多选模式
            if (mode == MODE_MULTI) {
                if (pickList.contains(image.path)) {
                    pickList.remove(image.path);
                    onImageUnselected(image.path);
                } else {
                    // 判断选择数量问题
                    if (mDesireImageCount == pickList.size()) {
                        Toast.makeText(mCxt, R.string.msg_amount_limit, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    pickList.add(image.path);
                    onImageSelected(image.path);
                }
                mImageAdapter.select(image);
            } else if (mode == MODE_SINGLE) {
                // 单选模式
                onSingleImageSelected(image.path);
            }
        }
    }

    /**
     * 刷新操作按钮状态
     */
    private void refreshActionStatus() {
        if (pickList.contains("000000")) {
            pickList.remove("000000");
        }
        String text = getString(R.string.done_with_count, pickList.size(), mDesireImageCount);
        menuDoneItem.setTitle(text);
        boolean hasSelected = pickList.size() > 0;
        menuDoneItem.setVisible(hasSelected);
        btnPreview.setEnabled(hasSelected);
        if (hasSelected) {
            btnPreview.setText(getResources().getString(R.string.preview) + "(" + (pickList.size()) + ")");
        } else {
            btnPreview.setText(getResources().getString(R.string.preview));
        }
    }

    private LoaderManager.LoaderCallbacks<Cursor> mLoaderCallback = new LoaderManager.LoaderCallbacks<Cursor>() {

        private final String[] IMAGE_PROJECTION = {
                MediaStore.Images.Media.DATA,
                MediaStore.Images.Media.DISPLAY_NAME,
                MediaStore.Images.Media.DATE_ADDED,
                MediaStore.Images.Media._ID};

        @Override
        public Loader<Cursor> onCreateLoader(int id, Bundle args) {
            // 根据图片设置参数新增验证条件
            StringBuilder selectionArgs = new StringBuilder();
            if (imageConfig != null) {
                if (imageConfig.minWidth != 0) {
                    selectionArgs.append(MediaStore.Images.Media.WIDTH + " >= " + imageConfig.minWidth);
                }
                if (imageConfig.minHeight != 0) {
                    selectionArgs.append("".equals(selectionArgs.toString()) ? "" : " and ");
                    selectionArgs.append(MediaStore.Images.Media.HEIGHT + " >= " + imageConfig.minHeight);
                }
                if (imageConfig.minSize != 0f) {
                    selectionArgs.append("".equals(selectionArgs.toString()) ? "" : " and ");
                    selectionArgs.append(MediaStore.Images.Media.SIZE + " >= " + imageConfig.minSize);
                }
                if (imageConfig.mimeType != null) {
                    selectionArgs.append(" and (");
                    for (int i = 0, len = imageConfig.mimeType.length; i < len; i++) {
                        if (i != 0) {
                            selectionArgs.append(" or ");
                        }
                        selectionArgs.append(MediaStore.Images.Media.MIME_TYPE + " = '" + imageConfig.mimeType[i] + "'");
                    }
                    selectionArgs.append(")");
                }
            }
            if (id == LOADER_ALL) {
                CursorLoader cursorLoader = new CursorLoader(mCxt,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        selectionArgs.toString(), null, IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            } else if (id == LOADER_CATEGORY) {
                String selectionStr = selectionArgs.toString();
                if (!"".equals(selectionStr)) {
                    selectionStr += " and" + selectionStr;
                }
                CursorLoader cursorLoader = new CursorLoader(mCxt,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                        IMAGE_PROJECTION[0] + " like '%" + args.getString("path") + "%'" + selectionStr, null,
                        IMAGE_PROJECTION[2] + " DESC");
                return cursorLoader;
            }

            return null;
        }

        @Override
        public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
            if (data != null && allImage.size() == 0) {
                int count = data.getCount();
                if (count > 0) {
                    data.moveToFirst();
                    do {
                        String path = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[0]));
                        String name = data.getString(data.getColumnIndexOrThrow(IMAGE_PROJECTION[1]));
                        long dateTime = data.getLong(data.getColumnIndexOrThrow(IMAGE_PROJECTION[2]));
                        Image image = new Image(path, name, dateTime);
                        allImage.add(image);
                        if (!hasFolderGened) {
                            // 获取文件夹名称
                            File imageFile = new File(path);
                            File folderFile = imageFile.getParentFile();
                            Folder folder = new Folder();
                            folder.name = folderFile.getName();
                            folder.path = folderFile.getAbsolutePath();
                            folder.cover = image;
                            if (!allFolders.contains(folder)) {
                                List<Image> imageList = new ArrayList<>();
                                imageList.add(image);
                                folder.images = imageList;
                                allFolders.add(folder);
                            } else {
                                // 更新
                                Folder f = allFolders.get(allFolders.indexOf(folder));
                                f.images.add(image);
                            }
                        }
                    } while (data.moveToNext());

                    mImageAdapter.setData(allImage);

                    // 设定默认选择
                    if (pickList != null && pickList.size() > 0) {
                        mImageAdapter.setDefaultSelected(pickList);
                    }
                    mFolderAdapter.setData(allFolders);
                    hasFolderGened = true;

                }
            }
        }

        @Override
        public void onLoaderReset(Loader<Cursor> loader) {

        }
    };

    /**
     * 获取GridView Item宽度
     *
     * @return
     */
    private int getItemImageWidth() {
        int cols = getNumColnums();
        int screenWidth = getResources().getDisplayMetrics().widthPixels;
        int columnSpace = getResources().getDimensionPixelOffset(R.dimen.space_size);
        return (screenWidth - columnSpace * (cols - 1)) / cols;
    }

    /**
     * 根据屏幕宽度与密度计算GridView显示的列数， 最少为三列
     *
     * @return
     */
    private int getNumColnums() {
        int cols = getResources().getDisplayMetrics().widthPixels / getResources().getDisplayMetrics().densityDpi;
        return cols < 3 ? 3 : cols;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_picker, menu);
        menuDoneItem = menu.findItem(R.id.action_picker_done);
        menuDoneItem.setVisible(false);
        refreshActionStatus();
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        if (item.getItemId() == R.id.action_picker_done) {
            complete();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    // 返回已选择的图片数据
    private void complete() {
        Intent data = new Intent();
        data.putStringArrayListExtra(EXTRA_RESULT, pickList);
        setResult(RESULT_OK, data);
        finish();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        captureManager.onSaveInstanceState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        captureManager.onRestoreInstanceState(savedInstanceState);
        super.onRestoreInstanceState(savedInstanceState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        Permiso.getInstance().onRequestPermissionResult(requestCode, permissions, grantResults);
    }

}
