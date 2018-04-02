package top.wifistar.activity

import android.annotation.SuppressLint
import android.graphics.Color
import android.icu.lang.UCharacter.GraphemeClusterBreak.T
import android.support.v7.app.AlertDialog
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.*
import android.widget.*
import com.flyco.dialog.entity.DialogMenuItem
import com.flyco.dialog.listener.OnOperItemClickL
import com.flyco.dialog.widget.NormalListDialog


import kotlinx.android.synthetic.main.activity_edit_info.*
import top.wifistar.R
import top.wifistar.R.id.*
import top.wifistar.R.string.email
import top.wifistar.adapter.SingleChoiceAdapter
import top.wifistar.app.App
import top.wifistar.app.ToolbarActivity
import top.wifistar.bean.BUser
import top.wifistar.bean.bmob.BmobUtils
import top.wifistar.bean.bmob.User
import top.wifistar.utils.Utils
import java.util.ArrayList


class EditProfileActivity : ToolbarActivity() {
//    lateinit var rlNick: RelativeLayout
//    lateinit var rlSex: RelativeLayout
//    lateinit var rlBirth: RelativeLayout
//    lateinit var rlEmail: RelativeLayout
//    lateinit var rlSignature1: RelativeLayout
//    lateinit var rlSignature2: RelativeLayout
//    lateinit var rlSignature3: RelativeLayout
//    lateinit var rlSelfIntro: RelativeLayout

    lateinit var mUser: User

    override fun initTopBar() {
        mToolbar.setNavigationIcon(R.drawable.back)
    }

    override fun initUI() {
        super.setContentView(R.layout.activity_edit_info)
        setCenterTitle("编辑个人资料")

        mUser = Utils.getCurrentShortUser()
        if (mUser == null) {
            return
        }
        initText()
        setClickListener()

    }

    private fun setClickListener() {
        mToolbar.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when (item?.getItemId()) {
                    R.id.done -> commitData()
                }
                return true
            }
        })

        rlNick.setOnClickListener {
            showEditDialog("修改昵称", mUser.name, tvNickName, object : EditDialogCallBack {
                override fun onFinish(result: String) {
                    mUser.name = result
                    tvNickName.text = result
                }
            })
        }
        rlSex.setOnClickListener {
            showSingleChoiceDialog("修改性别", arrayOf("女", "男"), mUser.sex, object : SingleChoiceDialogCallBack {
                override fun onFinish(result: Int) {
                    mUser.sex = result
                    tvSex.text = if (mUser.sex == 0) "女" else "男"
                }
            })
        }
        rlBirth.setOnClickListener {
            //TODO  Date picker
        }
        rlEmail.setOnClickListener {
            Utils.makeSysToast("注册邮箱暂时不支持修改")
        }
        rlSignature1.setOnClickListener {
            showEditDialog("修改签名1", mUser.startWord1, tvNickName, object : EditDialogCallBack {
                override fun onFinish(result: String) {
                    mUser.startWord1 = result
                    tvSignature1.text = result
                }
            })
        }
        rlSignature2.setOnClickListener {
            showEditDialog("修改签名2", mUser.startWord2, tvNickName, object : EditDialogCallBack {
                override fun onFinish(result: String) {
                    mUser.startWord2 = result
                    tvSignature2.text = result
                }
            })
        }
        rlSignature3.setOnClickListener {
            showEditDialog("修改签名3", mUser.startWord3, tvNickName, object : EditDialogCallBack {
                override fun onFinish(result: String) {
                    mUser.startWord3 = result
                    tvSignature3.text = result
                }
            })
        }
        rlSelfIntro.setOnClickListener {
            showEditDialog("修改个人介绍", mUser.selfIntroduce, tvNickName, object : EditDialogCallBack {
                override fun onFinish(result: String) {
                    mUser.selfIntroduce = result
                    tvSelfIntro.text = result
                }
            })
        }
    }

    private fun commitData() {
        BmobUtils.updateUser(mUser)
        finish()
    }

    @SuppressLint("RestrictedApi")
    private fun showEditDialog(title: String, content: String?, tvNickName: TextView?, editDialogCallBack: EditProfileActivity.EditDialogCallBack) {
        var editText = EditText(this)
        editText.setBackgroundResource(R.drawable.et_underline_selector)
        editText.setText(if (TextUtils.isEmpty(content)) "" else content)
        try {
            var f = TextView::class.java.getDeclaredField("mCursorDrawableRes")
            f.setAccessible(true)
            f.set(editText, R.drawable.text_cursor)
        } catch (e: Exception) {
        }
        editText.setSelection(if (TextUtils.isEmpty(content)) 0 else content!!.length)

        var alertDialogBuilder = AlertDialog.Builder(this, R.style.DialogTheme)
        editText.setTextColor(Color.BLACK)
        var dialog = alertDialogBuilder.setTitle(title).setView(editText, 15, 30, 15, 0).setNegativeButton("取消") { dialog, which -> dialog?.cancel() }.setPositiveButton("确定") { dialog, which -> editDialogCallBack.onFinish(editText.text.toString()) }.create()
        dialog.show()
        var dialogWindow = dialog.getWindow()
        var lp = dialogWindow.getAttributes()
        var d = this.getResources().getDisplayMetrics() // 获取屏幕宽、高用
        lp.width = (d.widthPixels * 0.8).toInt() // 宽度设置为屏幕的0.8
        dialogWindow.setAttributes(lp)
    }

    private fun showSingleChoiceDialog(title: String, strings: Array<String>, sex: Int, singleChoiceDialogCallBack: EditProfileActivity.SingleChoiceDialogCallBack) {
//        var alertDialogBuilder = AlertDialog.Builder(this, R.style.DialogTheme)
//        var tempSex = 0
//        var dialog = alertDialogBuilder.setTitle(title).setSingleChoiceItems(strings, sex) { dialog, which -> tempSex = which }.setNegativeButton("取消") { dialog, which -> dialog?.cancel() }.setPositiveButton("确定") { dialog, which -> singleChoiceDialogCallBack.onFinish(tempSex) }.create()
//        dialog.show()
//        var dialogWindow = dialog.getWindow()
//        var lp = dialogWindow.getAttributes()
//        var d = this.getResources().getDisplayMetrics() // 获取屏幕宽、高用
//        lp.width = (d.widthPixels * 0.8).toInt() // 宽度设置为屏幕的0.8
//        dialogWindow.setAttributes(lp)

         var mMenuItems = ArrayList<DialogMenuItem>()
        for(item in strings){
            mMenuItems.add(DialogMenuItem(item,0))
        }
        val singleChoiceDialog = NormalListDialog(mContext, SingleChoiceAdapter(mContext, mMenuItems)).titleBgColor(0x88FF571D2.toInt()).titleTextColor(0xFFFFFFFFF.toInt()).layoutAnimation(null)
        singleChoiceDialog.title(title).show()
        singleChoiceDialog.setOnOperItemClickL({ parent, view, position, id ->
            singleChoiceDialogCallBack.onFinish(position)
            singleChoiceDialog.dismiss()
        })
    }

    interface EditDialogCallBack {
        fun onFinish(result: String)
    }

    interface SingleChoiceDialogCallBack {
        fun onFinish(result: Int)
    }

    private fun initText() {
        tvNickName.text = mUser.name
        tvSex.text = if (mUser.sex == 0) "女" else "男"
        tvBirth.text = mUser.birth
        tvEmail.text = BUser.getCurrentUser().email
        tvSignature1.text = mUser.startWord1
        tvSignature2.text = mUser.startWord2
        tvSignature3.text = mUser.startWord3
        tvSelfIntro.text = mUser.selfIntroduce

    }
    //        @Override
    //        protected boolean isNeedHeadLayout()
    //        {
    //                return true;
    //        }
    //
    //        @Override
    //        protected boolean isNeedEmptyLayout() {
    //                return false;
    //        }
    //
    //        @Override
    //        protected int getContentLayout() {
    //                return R.layout.activity_edit_info;
    //        }
    //
    //
    //        @Override
    //        public void initView() {
    //                avatarLayout = (RelativeLayout) findViewById(R.id.rl_edit_user_info_avatar);
    //                nickLayout = (RelativeLayout) findViewById(R.id.rl_edit_user_info_nick);
    //                sexLayout = (RelativeLayout) findViewById(R.id.rl_edit_user_info_sex);
    //                birthLayout = (RelativeLayout) findViewById(R.id.rl_edit_user_info_birth);
    //                phoneLayout = (RelativeLayout) findViewById(R.id.rl_edit_user_info_phone);
    //                emailLayout = (RelativeLayout) findViewById(R.id.rl_edit_user_info_email);
    //                signatureLayout = (RelativeLayout) findViewById(R.id.rl_edit_user_info_signature);
    //                addressLayout = (RelativeLayout) findViewById(R.id.rl_edit_user_info_address);
    //                avatar = (CircleImageView) findViewById(R.id.riv_edit_user_info_avatar);
    //                nick = (TextView) findViewById(R.id.tv_edit_user_info_nick);
    //                sex = (TextView) findViewById(R.id.tv_edit_user_info_sex);
    //                birth = (TextView) findViewById(R.id.tv_edit_user_info_birth);
    //                phone = (TextView) findViewById(R.id.tv_edit_user_info_phone);
    //                email = (TextView) findViewById(R.id.tv_edit_user_info_email);
    //                signature = (TextView) findViewById(R.id.tv_edit_user_info_signature);
    //                address = (TextView) findViewById(R.id.tv_edit_user_info_address);
    //                avatarLayout.setOnClickListener(this);
    //                nickLayout.setOnClickListener(this);
    //                sexLayout.setOnClickListener(this);
    //                birthLayout.setOnClickListener(this);
    //                phoneLayout.setOnClickListener(this);
    //                emailLayout.setOnClickListener(this);
    //                signatureLayout.setOnClickListener(this);
    //                addressLayout.setOnClickListener(this);
    //        }
    //
    //
    //
    //
    //        @Override
    //        public void initData() {
    //                mUser = UserManager.getInstance().getCurrentUser();
    //                nick.setText(mUser.getNick());
    //                birth.setText(mUser.getBirthDay());
    //                phone.setText(mUser.getMobilePhoneNumber());
    //                sex.setText(mUser.isSex() ? "男" : "女");
    //                email.setText(mUser.getEmail());
    //                signature.setText(mUser.getSignature());
    //                address.setText(mUser.getAddress());
    //                Glide.with(this).load(mUser.getAvatar()).into(avatar);
    //                ToolBarOption toolBarOption = new ToolBarOption();
    //                toolBarOption.setAvatar(null);
    //                toolBarOption.setRightResId(R.drawable.ic_file_upload_blue_grey_900_24dp);
    //                toolBarOption.setTitle("编辑个人资料");
    //                toolBarOption.setRightListener(new View.OnClickListener() {
    //                        @Override
    //                        public void onClick(View v) {
    //                                if (mUser.getAvatar() == null) {
    //                                        ToastUtils.showShortToast("请设置个人头像拉^_^");
    //                                        return;
    //                                }
    //                                Intent intent = new Intent();
    //                                intent.putExtra("user", mUser);
    //                                setResult(Activity.RESULT_OK, intent);
    //                                finish();
    //                        }
    //                });
    //                getBack().setOnClickListener(new View.OnClickListener() {
    //                        @Override
    //                        public void onClick(View v) {
    //                                if (mUser.getAvatar() == null) {
    //                                        ToastUtils.showShortToast("请设置个人头像拉^_^");
    //                                        return;
    //                                }
    //                                finish();
    //                        }
    //                });
    //                toolBarOption.setNeedNavigation(true);
    //                setToolBar(toolBarOption);
    //        }
    //
    //        @Override
    //
    //        public void onClick(View v) {
    //                Intent intent = new Intent();
    //                switch (v.getId()) {
    //                        case R.id.rl_edit_user_info_avatar:
    //                                CommonImageLoader.getInstance().initStanderConfig(1);
    //                                intent.setClass(this, SelectedPictureActivity.class);
    //                                startActivityForResult(intent, Constant.REQUEST_CODE_SELECT_PICTURE);
    //                                break;
    //                        case R.id.rl_edit_user_info_nick:
    //                                intent.putExtra("from", "nick");
    //                                intent.putExtra("message", mUser.getNick());
    //                                intent.setClass(this, EditUserInfoDetailActivity.class);
    //                                startActivityForResult(intent, Constant.REQUEST_CODE_NICK);
    //                                break;
    //                        case R.id.rl_edit_user_info_sex:
    //                                intent.putExtra("from", "gender");
    //                                intent.putExtra("message", mUser.isSex() ? "男" : "女");
    //                                intent.setClass(this, EditUserInfoDetailActivity.class);
    //                                startActivityForResult(intent, Constant.REQUEST_CODE_SEX);
    //                                break;
    //                        case R.id.rl_edit_user_info_birth:
    //                                intent.putExtra("from", "birth");
    //                                intent.putExtra("message", mUser.getBirthDay());
    //                                intent.setClass(this, EditUserInfoDetailActivity.class);
    //                                startActivityForResult(intent, Constant.REQUEST_CODE_BIRTH);
    //                                break;
    //                        case R.id.rl_edit_user_info_phone:
    //                                intent.putExtra("from", "phone");
    //                                intent.putExtra("message", mUser.getMobilePhoneNumber());
    //                                intent.setClass(this, EditUserInfoDetailActivity.class);
    //                                startActivityForResult(intent, Constant.REQUEST_CODE_PHONE);
    //                                break;
    //                        case R.id.rl_edit_user_info_email:
    //                                intent.putExtra("from", "email");
    //                                intent.putExtra("message", mUser.getEmail());
    //                                intent.setClass(this, EditUserInfoDetailActivity.class);
    //                                startActivityForResult(intent, Constant.REQUEST_CODE_EMAIL);
    //                                break;
    //                        case R.id.rl_edit_user_info_signature:
    //                                intent.putExtra("from", "signature");
    //                                intent.putExtra("message", mUser.getSignature());
    //                                intent.setClass(this, EditUserInfoDetailActivity.class);
    //                                startActivityForResult(intent, Constant.REQUEST_CODE_SIGNATURE);
    //                                break;
    //                        case R.id.rl_edit_user_info_address:
    //                                intent.putExtra("from", "address");
    //                                intent.putExtra("message", mUser.getAddress());
    //                                intent.setClass(this, EditUserInfoDetailActivity.class);
    //                                startActivityForResult(intent, Constant.REQUEST_CODE_ADDRESS);
    //                }
    //        }
    //
    //
    //        @Override
    //        protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    //                super.onActivityResult(requestCode, resultCode, data);
    //                if (resultCode == Activity.RESULT_OK) {
    //                        String message = null;
    //                        if (data != null) {
    //                                message = data.getStringExtra("message");
    //                        }
    //                        switch (requestCode) {
    //                                case Constant.REQUEST_CODE_SELECT_PICTURE:
    ////                                        这里进入编辑图片界面
    //                                        if (CommonImageLoader.getInstance().getSelectedImages().get(0) != null) {
    //                                                String path = CommonImageLoader.getInstance().getSelectedImages().get(0).getPath();
    //                                                cropPhoto(path);
    //                                        }
    //                                        break;
    //                                case Constant.REQUEST_CODE_CROP:
    //                                        LogUtil.e("裁剪完成");
    //                                        try {
    //                                                showLoadDialog("正在上传头像，请稍候........");
    //                                                final BmobFile bmobFile = new BmobFile(new File(new URI(PhotoUtil.buildUri(this).toString())));
    //                                                bmobFile.uploadblock(CustomApplication.getInstance(), new UploadFileListener() {
    //                                                        @Override
    //                                                        public void onSuccess() {
    ////                                                                这里更新用户信息头像
    //                                                                UserManager.getInstance().updateUserInfo("avatar", bmobFile.getFileUrl(CustomApplication.getInstance()), new UpdateListener() {
    //                                                                        @Override
    //                                                                        public void onSuccess() {
    //                                                                                dismissLoadDialog();
    //                                                                                LogUtil.e("更新用户头像成功");
    //                                                                                Glide.with(EditProfileActivity.this).load(bmobFile.getFileUrl(CustomApplication.getInstance())).diskCacheStrategy(DiskCacheStrategy.ALL).into(avatar);
    //                                                                                mUser.setAvatar(bmobFile.getFileUrl(EditProfileActivity.this));
    ////                                                                                更新数据库中消息的头像
    //                                                                                ChatDB.create().updateMessageAvatar(UserManager.getInstance().getCurrentUserObjectId(),bmobFile.getFileUrl(EditProfileActivity.this));
    //                                                                        }
    //
    //                                                                        @Override
    //                                                                        public void onFailure(int i, String s) {
    //                                                                                dismissLoadDialog();
    //                                                                                LogUtil.e("更新用户头像失败" + s + i);
    //                                                                        }
    //                                                                });
    //                                                        }
    //
    //                                                        @Override
    //                                                        public void onFailure(int i, String s) {
    //                                                                dismissLoadDialog();
    //                                                                LogUtil.e("加载失败");
    //                                                        }
    //                                                });
    //                                        } catch (URISyntaxException e) {
    //                                                e.printStackTrace();
    //                                        }
    //                                        break;
    //                                case Constant.REQUEST_CODE_SEX:
    //                                        sex.setText(message);
    //                                        if (message != null) {
    //                                                mUser.setSex(message.equals("男"));
    //                                        }
    //                                        break;
    //                                case Constant.REQUEST_CODE_BIRTH:
    //                                        birth.setText(message);
    //                                        mUser.setBirthDay(message);
    //                                        break;
    //                                case Constant.REQUEST_CODE_SIGNATURE:
    //                                        signature.setText(message);
    //                                        mUser.setSignature(message);
    //                                        break;
    //                                case Constant.REQUEST_CODE_EMAIL:
    //                                        email.setText(message);
    //                                        mUser.setEmail(message);
    //                                        break;
    //                                case Constant.REQUEST_CODE_NICK:
    //                                        nick.setText(message);
    //                                        mUser.setNick(message);
    //                                        break;
    //                                case Constant.REQUEST_CODE_ADDRESS:
    //                                        address.setText(message);
    //                                        mUser.setAddress(message);
    //                                case Constant.REQUEST_CODE_PHONE:
    //                                        phone.setText(message);
    //                                        mUser.setMobilePhoneNumber(message);
    //                                default:
    //                                        break;
    //                        }
    //                }
    //        }
    //
    //        private void cropPhoto(String path) {
    //                Uri uri = Uri.fromFile(new File(path));
    //                Intent cropIntent = new Intent("com.android.camera.action.CROP");
    //                cropIntent.setDataAndType(uri, "image/*");
    //                cropIntent.putExtra("crop", "true");
    //                cropIntent.putExtra("aspectX", 1);
    //                cropIntent.putExtra("aspectY", 1);
    //                cropIntent.putExtra("outputX", 200);
    //                cropIntent.putExtra("outputY", 200);
    //                cropIntent.putExtra("return-data", false);
    //                cropIntent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
    //                Uri cropUri = PhotoUtil.buildUri(this);
    //                cropIntent.putExtra(MediaStore.EXTRA_OUTPUT, cropUri);
    //
    //                if (cropIntent.resolveActivity(getPackageManager()) != null) {
    //                        startActivityForResult(cropIntent, Constant.REQUEST_CODE_CROP);
    //                }
    //        }
    //
    //        @Override
    //        protected void onDestroy() {
    //                super.onDestroy();
    //                CommonImageLoader.getInstance().clearAllData();
    //        }
    //
    //
    //        @Override
    //        public void finish() {
    //                LogUtil.e("editUserInfo_finish");
    //                Intent intent = new Intent();
    //                intent.putExtra("user", mUser);
    //                setResult(Activity.RESULT_OK, intent);
    //                super.finish();
    //        }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.done, menu)
        return true
    }
}
