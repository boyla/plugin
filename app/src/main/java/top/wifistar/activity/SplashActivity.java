package top.wifistar.activity;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import org.json.JSONArray;

import cn.bmob.v3.BmobUser;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import cn.bmob.v3.listener.QueryListener;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import rx.Subscriber;
import rx.Subscription;
import rx.subscriptions.CompositeSubscription;
import top.wifistar.R;
import top.wifistar.app.App;
import top.wifistar.app.BaseActivity;
import top.wifistar.bean.BUser;
import top.wifistar.bean.bmob.UserProfile;
import top.wifistar.bean.bmob.User;
import top.wifistar.customview.TopReminder;
import top.wifistar.dialog.LoadingDialog;
import top.wifistar.realm.BaseRealmDao;
import top.wifistar.utils.ACache;
import top.wifistar.utils.ProgressDialogUtil;
import top.wifistar.utils.Utils;

import static top.wifistar.utils.ACache.CURRENT_USER_CACHE;
import static top.wifistar.utils.ACache.PROFILE_CACHE;
import static top.wifistar.utils.ACache.SHORT_USER_ID_CACHE;


public class SplashActivity extends BaseActivity {

    private Context context;
    private android.os.Handler handler = new android.os.Handler();
    private FrameLayout flStart;
    private FrameLayout flWords;
    private View loginView;
    private ImageView ivBackground;
    private View llRegister;
    private View forgetPsw;
    private LoadingDialog loadingDialog;
    private TopReminder topReminder;

    boolean clearRegister = false;
    boolean canLogin;
    boolean isFirstIn = true;

    private CompositeSubscription mCompositeSubscription;
    private TextView tvWelcome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_splash);
        flStart = findViewById(R.id.flStart);
        flWords = findViewById(R.id.flWords);
        tvWelcome = flWords.findViewById(R.id.tvWelcome);
        ivBackground = findViewById(R.id.ivBackground);
        topReminder = findViewById(R.id.topReminder);
        context = this;
        setSplashWord();
        String cacheToken = ACache.get(this).getAsString("token");
        String username = ACache.get(this).getAsString("username");
        String password = ACache.get(this).getAsString("password");
        String account_info = ACache.get(this).getAsString("account_info");

//        if (!TextUtils.isEmpty(account_info)) {
//            String[] items = account_info.split("__cache__");
//            if (items.length != 3) {
//                canLogin = false;
//            } else {
//                if (items[2].equals(Utils.getUniqueID())) {
//                    canLogin = true;
//                } else {
//                    canLogin = false;
//                }
//            }
//        } else {
//            canLogin = false;
//        }
        if (BmobUser.getCurrentUser() == null) {
            canLogin = false;
        } else {
            canLogin = true;
        }
    }

    private void setSplashWord() {
        String[] originStrs = getResources().getStringArray(R.array.splash_word);
        ArrayList<String> userStrs = new ArrayList();
        if (Utils.getCurrentShortUser() != null) {
            if (!TextUtils.isEmpty(Utils.getCurrentShortUser().startWord1)) {
                userStrs.add(Utils.getCurrentShortUser().startWord1);
            }
            if (!TextUtils.isEmpty(Utils.getCurrentShortUser().startWord2)) {
                userStrs.add(Utils.getCurrentShortUser().startWord2);
            }
            if (!TextUtils.isEmpty(Utils.getCurrentShortUser().startWord3)) {
                userStrs.add(Utils.getCurrentShortUser().startWord3);
            }
            if (userStrs.size() > 0) {
                originStrs = new String[userStrs.size()];
                originStrs = userStrs.toArray(originStrs);
            }
        }
        if (originStrs.length > 0) {
            tvWelcome.setText(originStrs[new Random().nextInt(originStrs.length)]);
        }
    }


    private void goToMain() {
        App.currentUserProfile = (UserProfile) ACache.get(context).getAsObject(PROFILE_CACHE + BUser.getCurrentUser().getObjectId());
        HomeActivity.isFirstLogin = true;
        startActivity(new Intent(context, HomeActivity.class));
        finish();
    }

    private void goToLogin(View currentView) {
        if (loginView == null) {
            loginView = LayoutInflater.from(this).inflate(R.layout.fl_login_content, null, false);
        }
        currentView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.left_out));
        //flWords.setAnimation(AnimationUtils.makeOutAnimation(this, false));
        flStart.removeAllViews();
        flStart.addView(loginView);
        loginView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.right_in));
        initLogin(loginView);
        //loginView.setAnimation(AnimationUtils.makeInAnimation(this, false));
//        startActivity(new Intent(context, LoginActivity.class));
//        finish();
    }

    private void backLogin(View currentView) {
        if (topReminder.getTheme() != TopReminder.THEME_SUCCESS) {
            topReminder.dismiss();
        }
        if (loginView == null) {
            loginView = LayoutInflater.from(this).inflate(R.layout.fl_login_content, null, false);
        }
        currentView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.right_out));
        //flWords.setAnimation(AnimationUtils.makeOutAnimation(this, false));
        flStart.removeAllViews();
        if (clearRegister) {
            llRegister = null;
        }
        flStart.addView(loginView);
        loginView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.left_in));
        initLogin(loginView);
        //loginView.setAnimation(AnimationUtils.makeInAnimation(this, false));
//        startActivity(new Intent(context, LoginActivity.class));
//        finish();
    }


    private boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        return ni != null && ni.isConnectedOrConnecting();
    }

    @Override
    protected void onResume() {
        super.onResume();
        ivBackground.clearAnimation();
        if (canLogin) {
            //RongIM.connect(cacheToken, RongIMListener.getInstance().getConnectCallback());
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToMain();
                }
            }, 2500);
        } else if (isFirstIn) {
            handler.postDelayed(() -> {
                Animation animation = AnimationUtils.loadAnimation(SplashActivity.this, R.anim.translate_anim);
                ivBackground.startAnimation(animation);
            }, 3300);
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    goToLogin(flWords);
                }
            }, 2500);
            isFirstIn = false;
        }

        //queryData();
    }

    public void queryData() {
        BmobQuery query = new BmobQuery("BombUser");
//        query.addWhereEqualTo("age", 25);
//        query.setLimit(2);
//        query.order("createdAt");
//        //v3.5.0版本提供`findObjectsByTable`方法查询自定义表名的数据
        query.findObjectsByTable(new QueryListener<JSONArray>() {
            @Override
            public void done(JSONArray ary, BmobException e) {
                if (e == null) {
                    Log.i("bmob", "查询成功：" + ary.toString());
                } else {
                    Log.i("bmob", "失败：" + e.getMessage() + "," + e.getErrorCode());
                }
            }
        });
    }

    private void goToRegister(View thisView) {
        topReminder.dismiss();
        if (llRegister == null) {
            llRegister = LayoutInflater.from(this).inflate(R.layout.fl_register, null, false);
        }
        thisView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.left_out));
        flStart.removeAllViews();
        flStart.addView(llRegister);
        llRegister.setAnimation(AnimationUtils.loadAnimation(this, R.anim.right_in));

        initRegister(llRegister);
    }

    private void resetPassword(View thisView) {
        if (forgetPsw == null) {
            forgetPsw = LayoutInflater.from(this).inflate(R.layout.fl_forget_psw, null, false);
        }
        thisView.setAnimation(AnimationUtils.loadAnimation(this, R.anim.left_out));
        flStart.removeAllViews();
        flStart.addView(forgetPsw);
        forgetPsw.setAnimation(AnimationUtils.loadAnimation(this, R.anim.right_in));
        initResetPassword(forgetPsw);
    }

    public String nickname;

    private void register(String email, String username, String password, String nickname) {
        Utils.closeKeyboard(SplashActivity.this);
        if (TextUtils.isEmpty(nickname)) {
            Utils.showToast(topReminder, "昵称不能为空");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            Utils.showToast(topReminder, getResources().getString(R.string.email_is_empty));
            return;
        } else {
            String regEx =
                    "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
            Matcher matcherObj = Pattern.compile(regEx).matcher(email);
            if (!matcherObj.matches()) {
                Utils.showToast(topReminder, getResources().getString(R.string.invalid_email) + email);
                return;
            }
        }

        if (TextUtils.isEmpty(username) || username.length() < 6) {
            Utils.showToast(topReminder, getResources().getString(R.string.username_too_short));
            return;
        }
        if (TextUtils.isEmpty(password) || username.length() < 6) {
            Utils.showToast(topReminder, getResources().getString(R.string.psw_too_short));
            return;
        }
        BUser user = new BUser();
        user.setUsername(username);
        user.setPassword(password);
        user.setEmail(email);

        addSubscription(user.signUp(new SaveListener<BUser>() {
            @Override
            public void done(final BUser resultUser, BmobException e) {
                if (e == null) {
                    Log.i("添加数据成功，返回objectId为：", resultUser.getObjectId());
                    clearRegister = true;
                    Utils.showToast(topReminder, getResources().getString(R.string.register_success), TopReminder.THEME_SUCCESS);
                    backLogin(llRegister);
                    //add profile by buser
                    addProfile(resultUser, nickname);
                } else {
                    Utils.showToast(topReminder, e.getMessage());
                }
            }
        }));

    }

    private void addProfile(BUser bmobUser, String nickname) {
        final UserProfile profile = new UserProfile();
        profile.setUserId(bmobUser.getObjectId());
        profile.setNickName(nickname);
        profile.save(new SaveListener<String>() {
            @Override
            public void done(String profileId, BmobException e) {
                if (e != null) {
                    Utils.showToast(topReminder, e.getMessage());
                } else {
                    profile.setObjectId(profileId);
                    bmobUser.setProfileId(profileId);
                    bmobUser.update(bmobUser.getObjectId(), new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            if (e != null) {
                                Utils.makeToast(SplashActivity.this, e.getMessage());
                            }
                        }
                    });
                    ACache.get(context).put(CURRENT_USER_CACHE + bmobUser.getObjectId(), bmobUser);
                    ACache.get(context).put(PROFILE_CACHE + bmobUser.getObjectId(), profile);
                    App.currentUserProfile = profile;
                    generateNewUserAndSave(profileId);
                }
            }
        });
    }

    private void resetPassword(final String email) {
        Utils.closeKeyboard(SplashActivity.this);
        if (TextUtils.isEmpty(email)) {
            Utils.showToast(topReminder, getResources().getString(R.string.email_is_empty));
            return;
        } else {
            String regEx =
                    "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                            + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                            + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                            + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                            + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
            Matcher matcherObj = Pattern.compile(regEx).matcher(email);
            if (!matcherObj.matches()) {
                Utils.showToast(topReminder, getResources().getString(R.string.invalid_email) + email);
                return;
            }
        }

        topReminder.dismiss();

        BmobQuery<BmobUser> query = new BmobQuery<BmobUser>();
        query.addWhereEqualTo("email", email);
        query.findObjects(new FindListener<BmobUser>() {
            @Override
            public void done(List<BmobUser> object, BmobException e) {
                if (e == null) {
                    BmobUser.resetPasswordByEmail(email, new UpdateListener() {

                        @Override
                        public void done(BmobException e) {
                            if (e == null) {
                                Utils.showToast(topReminder, "重置密码请求成功，请到" + email + "邮箱进行密码重置操作", TopReminder.THEME_SUCCESS);
                            } else {
                                Utils.showToast(topReminder, e.getMessage());
                            }
                        }
                    });
                } else {
                    Utils.showToast(topReminder, e.getMessage());
                }
            }
        });

    }

    private void initLogin(final View loginView) {
        final EditText etLoginAccount = (EditText) loginView.findViewById(R.id.etLoginAccount);
        final EditText etLoginPassword = (EditText) loginView.findViewById(R.id.etLoginPassword);
        etLoginPassword.setTransformationMethod(new PasswordTransformationMethod());
        Button btLoginSignIn = (Button) loginView.findViewById(R.id.btLoginSignIn);
        TextView tvForgetPsw = (TextView) loginView.findViewById(R.id.tvForgetPsw);
        TextView tvSignup = (TextView) loginView.findViewById(R.id.tvSignup);

        btLoginSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.closeKeyboard(SplashActivity.this);
                ivBackground.clearAnimation();
                login(etLoginAccount.getText().toString().trim(), etLoginPassword.getText().toString().trim());
            }
        });

        tvSignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRegister(loginView);
            }
        });
        tvForgetPsw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetPassword(loginView);
            }
        });
    }

    private void initRegister(final View llRegister) {
        final EditText etEmail = (EditText) llRegister.findViewById(R.id.etEmail);
        final EditText etUsername = (EditText) llRegister.findViewById(R.id.etUsername);
        final EditText etPassword = (EditText) llRegister.findViewById(R.id.etPassword);
        final EditText eNickname = (EditText) llRegister.findViewById(R.id.eNickname);

        etPassword.setTransformationMethod(new PasswordTransformationMethod());
        Button btCancel = (Button) llRegister.findViewById(R.id.btCancel);
        Button btRegisterDone = (Button) llRegister.findViewById(R.id.btRegisterDone);
        btCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clearRegister = false;
                backLogin(llRegister);
            }
        });
        btRegisterDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNetworkConnected(SplashActivity.this)) {
                    Utils.showToast(topReminder, "未连接网络");
                }
                nickname = eNickname.getText().toString().trim();
                register(etEmail.getText().toString().trim(), etUsername.getText().toString().trim(), etPassword.getText().toString().trim(), nickname);
            }
        });
    }

    private void initResetPassword(final View forgetPsw) {
        final EditText etEmail = (EditText) forgetPsw.findViewById(R.id.etEmail);
        Button btCancel = (Button) forgetPsw.findViewById(R.id.btCancel);
        Button btDone = (Button) forgetPsw.findViewById(R.id.btDone);
        btCancel.setOnClickListener(v -> backLogin(forgetPsw));
        btDone.setOnClickListener(v -> resetPassword(etEmail.getText().toString().trim()));

    }

    volatile int count = 0;

    private void login(String username, String password) {
        if (TextUtils.isEmpty(username) || username.length() < 6) {
            Utils.makeToast(SplashActivity.this, getResources().getString(R.string.username_too_short));
            return;
        }
        if (TextUtils.isEmpty(password) || password.length() < 6) {
            Utils.makeToast(SplashActivity.this, getResources().getString(R.string.psw_too_short));
            return;
        }

        topReminder.dismiss();
        if (loadingDialog == null) {
            loadingDialog = ProgressDialogUtil.getCustomProgressDialog(this);
        }
        loadingDialog.show();
        final BUser user = new BUser();
        user.setUsername(username);
        user.setPassword(password);
        user.loginObservable(BUser.class).subscribe(new Subscriber<BUser>() {
            @Override
            public void onCompleted() {
            }

            @Override
            public void onError(Throwable throwable) {
                loadingDialog.dismiss();
                Utils.makeToast(SplashActivity.this, throwable.getMessage());
            }

            @Override
            public void onNext(final BUser bmobUser) {
                //get profile and cache it
                if (TextUtils.isEmpty(bmobUser.getProfileId())) {
                    final UserProfile profile = new UserProfile();
                    profile.setUserId(bmobUser.getObjectId());
                    profile.setNickName(username.substring(0, (username.length() - 2)));
                    profile.save(new SaveListener<String>() {
                        @Override
                        public void done(String profileId, BmobException e) {
                            if (e != null) {
                                Utils.showToast(topReminder, e.getMessage());
                            } else {
                                App.currentUserProfile = profile;
                                bmobUser.setProfileId(profileId);
                                bmobUser.update(bmobUser.getObjectId(), new UpdateListener() {
                                    @Override
                                    public void done(BmobException e) {
                                        if (e != null) {
                                            Utils.makeToast(SplashActivity.this, e.getMessage());
                                        }
                                    }
                                });
                                generateNewUserAndSave(profileId);
                                ACache.get(context).put(CURRENT_USER_CACHE + bmobUser.getObjectId(), bmobUser);
                                ACache.get(context).put(PROFILE_CACHE + bmobUser.getObjectId(), profile);
                                loadingDialog.dismiss();
                                goToMain();
                            }
                        }
                    });
                } else {
                    count = 0;
                    //get profile and update user
                    BmobQuery<UserProfile> query = new BmobQuery<>();
                    query.getObject(bmobUser.getProfileId(), new QueryListener<UserProfile>() {
                        @Override
                        public void done(UserProfile userProfile, BmobException e) {
                            App.currentUserProfile = userProfile;
                            ACache.get(context).put(PROFILE_CACHE + bmobUser.getObjectId(), userProfile);
                            loadingDialog.dismiss();
                            count++;
                            checkJump();
                        }
                    });

                    //query user, then cache the obj id
                    BmobQuery<User> queryUser = new BmobQuery<>();
                    queryUser.addWhereEqualTo("id", bmobUser.getProfileId()).findObjects(new FindListener<User>() {
                        @Override
                        public void done(List<User> list, BmobException e) {
                            if (e == null && list != null && list.size() > 0) {
                                Utils.updateUser(list.get(0));
                                String objId = list.get(0).getObjectId();
                                ACache.get(context).put(SHORT_USER_ID_CACHE + BUser.getCurrentUser().getObjectId(), objId);
                                count++;
                                checkJump();
                            } else {
                                generateNewUserAndSave(bmobUser.getProfileId());
                                goToMain();
                            }
                        }
                    });
                }
            }
        });
    }

    private void checkJump() {
        if (count == 2) {
            goToMain();
        }
    }

    private void generateNewUserAndSave(String profileId) {
        //generate a user and save, then cache the obj id
        String nickName = "";
        if (App.currentUserProfile != null && !TextUtils.isEmpty(App.currentUserProfile.getNickName())) {
            nickName = App.currentUserProfile.getNickName();
        }
        User user = new User(nickName, App.currentUserProfile.getAvatar() + "");
        user.id = profileId;
        user.sex = App.currentUserProfile.getSex();
        user.save(new SaveListener<String>() {
            @Override
            public void done(String objId, BmobException e) {
                if (e != null) {
                    Utils.showToast(e.getMessage());
                } else {
                    user.setObjectId(objId);
                    BaseRealmDao.insertOrUpdate(user.toRealmObject());
                    ACache.get(context).put(SHORT_USER_ID_CACHE + BUser.getCurrentUser().getObjectId(), objId);
                }
            }
        });
    }

    /**
     * 解决Subscription内存泄露问题
     *
     * @param s
     */
    protected void addSubscription(Subscription s) {
        if (this.mCompositeSubscription == null) {
            this.mCompositeSubscription = new CompositeSubscription();
        }
        this.mCompositeSubscription.add(s);
    }
}
