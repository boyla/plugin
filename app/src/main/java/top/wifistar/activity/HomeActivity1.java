//package top.wifistar.activity;
//
//import android.content.Intent;
//import android.net.Uri;
//import android.text.TextUtils;
//import android.util.Log;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.google.flatbuffers.FlatBufferBuilder;
//
//import org.apache.http.HttpResponse;
//import org.apache.http.NameValuePair;
//import org.apache.http.client.HttpClient;
//import org.apache.http.client.entity.UrlEncodedFormEntity;
//import org.apache.http.client.methods.HttpPost;
//import org.apache.http.impl.client.DefaultHttpClient;
//import org.apache.http.message.BasicNameValuePair;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.security.MessageDigest;
//import java.util.ArrayList;
//import java.util.List;
//
//import io.rong.imkit.RongIM;
//import io.rong.imkit.fragment.ConversationListFragment;
//import io.rong.imlib.RongIMClient;
//import io.rong.imlib.model.Conversation;
//import io.rong.imlib.model.UserInfo;
//import top.wifistar.R;
//import top.wifistar.app.ToolbarActivity;
//import top.wifistar.bean.User;
//import top.wifistar.customview.LoadingView;
//import top.wifistar.im.IMUser;
//import top.wifistar.service.WiFiNetworkService;
//import top.wifistar.utils.ACache;
//
//public class HomeActivity1 extends ToolbarActivity implements RongIM.UserInfoProvider{
//
//    TextView tvContent;
//    LoadingView loadingView;
//
//    @Override
//    protected void initUI() {
//        super.setContentView(R.layout.activity_main);
//        tvContent = (TextView) findViewById(R.id.tvContent);
//        loadingView = (LoadingView)findViewById(R.id.loadingView);
//
//        FlatBufferBuilder builder = new FlatBufferBuilder(0);
//        int accountOffset = builder.createString("admin");
//        int passwordOffset = builder.createString("admin");
//        int idOffset = builder.createString("10001");
//        int nameOffset = builder.createString("谷歌");
//        int genderOffset = builder.createString("男");
//        int birthdayOffset = builder.createString("1988/9/26");
//        int languageOffset = builder.createString("中文");
//        int moblieOffset = builder.createString("1595482296");
//        int emailOffset = builder.createString("dolegu@gmail.com");
//        int descriptionOffset = builder.createString("http://www.52im.net/thread-388-1-1.html\n" +
//                "http://www.runoob.com/ruby/ruby-syntax.html\n" +
//                "http://down.51cto.com/zt/5011\n" +
//                "http://www.th7.cn/Program/java/201509/575527.shtml\n" +
//                "flatc -j -b repos_schema.fbs reps_json.json");
//        int countryOffset = builder.createString("中国");
//        int stateOffset = builder.createString("四川");
//        int cityOffset = builder.createString("成都");
//        int avatar_urlOffset = builder.createString("www.google.com.hk");
//        int avatar_idOffset = builder.createString("654666");
//
//
//        int user = User.createUser(builder, accountOffset,
//                passwordOffset,
//                idOffset,
//                nameOffset,
//                genderOffset,
//                birthdayOffset,
//                languageOffset,
//                moblieOffset,
//                emailOffset,
//                descriptionOffset,
//                countryOffset,
//                stateOffset,
//                cityOffset,
//                avatar_urlOffset,
//                avatar_idOffset,
//                true,
//                0,
//                0);
////        int friendDave = Person.createPerson(builder, builder.createString("Dave"),
////                FriendshipStatus.Friend, 0, 0);
////        int friendTom = Person.createPerson(builder, builder.createString("Tom"),
////                FriendshipStatus.Friend, 0, 0);
////        int name = builder.createString("John");
////        int[] friendsArr = new int[]{friendDave, friendTom};
////        int friends = Person.createFriendsVector(builder, friendsArr);
////        User.startUser(builder);
////        User.addAccount(builder,accountOffset);
////        User.addPassword(builder,passwordOffset);
////        User.addId(builder,idOffset);
////        User.addName(builder, nameOffset);
////        User.addGender(builder, genderOffset);
////        User.addBirthday(builder, birthdayOffset);
////        User.addLanguage(builder, languageOffset);
////        User.addMoblie(builder, moblieOffset);
////        User.addEmail(builder, emailOffset);
////        User.addDescription(builder, descriptionOffset);
////        User.addCountry(builder, countryOffset);
////        User.addState(builder, stateOffset);
////        User.addCity(builder, cityOffset);
////        User.addAvatarUrl(builder, avatar_urlOffset);
////        User.addGravatarId(builder, avatar_idOffset);
////        User.addInvisible(builder,false);
//
////        int john = User.endUser(builder);
//        builder.finish(user);
//
////        return builder.dataBuffer();
//
//        User mUser = User.getRootAsUser(builder.dataBuffer());
//        if (mUser == null)
//            Toast.makeText(this, "Null object", Toast.LENGTH_LONG).show();
//        else {
//            String str = "account name : " + mUser.account() + "\n"
//                    + mUser.country() + "." + mUser.state() + "." + mUser.city() + "\n"
//                    + mUser.description() + "\n";
// //           Toast.makeText(this, str, Toast.LENGTH_LONG).show();
//        }
//
//        Intent sevice = new Intent(this, WiFiNetworkService.class);
//        this.startService(sevice);
//
//        RongIM.setUserInfoProvider(this,true);
//        final String token = ACache.get(mContext).getAsString("token");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                try {
//                    if(TextUtils.isEmpty(token)){
//                        IMUser imUser = new IMUser(HomeActivity1.this);
//                        String genToken = imUser.getToken("test1001","二狗","https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png").getToken();
//                        connect(genToken);
//                        ACache.get(mContext).put("token", genToken);
//                    }else{
//                        connect(token);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//            }
//        }).start();
//
//
////        loadingView.setOnClickListener(new View.OnClickListener() {
////            @Override
////            public void onClick(View view) {
////
////                    loadingView.stop();
////
////            }
////        });
//    }
//
//    @Override
//    protected void initTopBar() {
//
//    }
//
//    private void connect(final String token) {
//
//            RongIM.connect(token, new RongIMClient.ConnectCallback() {
//
//                /**
//                 * Token 错误。可以从下面两点检查 1.  Token 是否过期，如果过期您需要向 App Server 重新请求一个新的 Token
//                 *                  2.  token 对应的 appKey 和工程里设置的 appKey 是否一致
//                 */
//                @Override
//                public void onTokenIncorrect() {
//                    Toast.makeText(HomeActivity1.this, "Token 验证成功", Toast.LENGTH_LONG).show();
//                }
//
//                /**
//                 * 连接融云成功
//                 * @param userid 当前 token 对应的用户 id
//                 */
//                @Override
//                public void onSuccess(String userid) {
//                    Log.d("=======Token=======", token);
//                    tvContent.setText("您的好友 "+userid+" 已上线");
//                    enterFragment();
//                }
//
//                /**
//                 * 连接融云失败
//                 * @param errorCode 错误码，可到官网 查看错误码对应的注释
//                 */
//                @Override
//                public void onError(RongIMClient.ErrorCode errorCode) {
//                    tvContent.setText("登陆失败");
//                }
//            });
//    }
//
//    public static String GetRongCloudToken(String username) {
//        StringBuffer res = new StringBuffer();
//        String url = "https://api.cn.ronghub.com/user/getToken.json";
//        String App_Key = "n19jmcy5n1hi9"; //开发者平台分配的 App Key。
//        String App_Secret = "B9bKOLywJvt5u2";
//        String Timestamp = String.valueOf(System.currentTimeMillis() / 1000);//时间戳，从 1970 年 1 月 1 日 0 点 0 分 0 秒开始到现在的秒数。
//        String Nonce = String.valueOf(Math.floor(Math.random() * 1000000));//随机数，无长度限制。
//        String Signature = sha1(App_Secret + Nonce + Timestamp);//数据签名。
//        Log.i("=======Signature=======",Signature);
//        HttpClient httpClient = new DefaultHttpClient();
//        HttpPost httpPost = new HttpPost(url);
//        httpPost.setHeader("App-Key", App_Key);
//        httpPost.setHeader("Timestamp", Timestamp);
//        httpPost.setHeader("Nonce", Nonce);
//        httpPost.setHeader("Signature", Signature);
//        httpPost.setHeader("Content-Type", "application/x-www-form-urlencoded");
//        List<NameValuePair> nameValuePair = new ArrayList<NameValuePair>(1);
//        nameValuePair.add(new BasicNameValuePair("userId",username));
//        HttpResponse httpResponse = null;
//        try {
//            httpPost.setEntity(new UrlEncodedFormEntity(nameValuePair,"utf-8"));
//            httpResponse = httpClient.execute(httpPost);
//            BufferedReader br = new BufferedReader(new InputStreamReader(httpResponse.getEntity().getContent()));
//            String line = null;
//            while ((line = br.readLine()) != null) {
//                res.append(line);
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//
//        Log.i("=======Signature=======",res.toString());
//        return res.toString();
//    }
//    //SHA1加密//http://www.rongcloud.cn/docs/server.html#通用_API_接口签名规则
//    private static String sha1(String data){
//        StringBuffer buf = new StringBuffer();
//        try{
//            MessageDigest md = MessageDigest.getInstance("SHA1");
//            md.update(data.getBytes());
//            byte[] bits = md.digest();
//            for(int i = 0 ; i < bits.length;i++){
//                int a = bits[i];
//                if(a<0) a+=256;
//                if(a<16) buf.append("0");
//                buf.append(Integer.toHexString(a));
//            }
//        }catch(Exception e){
//
//        }
//        return buf.toString();
//    }
//
//    @Override
//    public UserInfo getUserInfo(String id) {
//        if(!TextUtils.isEmpty(id)){
//            if("test1000".equals(id)){
//                return new UserInfo(id,"大狗", Uri.parse("https://ss0.bdstatic.com/5aV1bjqh_Q23odCf/static/superman/img/logo/bd_logo1_31bdc765.png"));
//            }else{
//                return new UserInfo(id,"二狗", Uri.parse("http://i0.hdslb.com/bfs/face/4dfa08179b577ea1a8e893955b4a920490c83516.jpg"));
//            }
//        }
//        return null;
//    }
//
//    /**
//     * 加载 会话列表 ConversationListFragment
//     */
//    private void enterFragment() {
//        ConversationListFragment fragment = (ConversationListFragment) getSupportFragmentManager().findFragmentById(R.id.conversationlist);
//
//        Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
//                .appendPath("conversationlist")
//                .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话非聚合显示
//                .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//设置群组会话非聚合显示
//                .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//设置讨论组会话非聚合显示
//                .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//设置系统会话非聚合显示
//                .build();
//
//        fragment.setUri(uri);
//    }
//
//    /**
//     * 初始化会话列表
//     * @return  会话列表
//     */
////    private Fragment  initConversationList(){
////        if (mConversationFragment == null) {
////            ConversationListFragment listFragment = ConversationListFragment.getInstance();
////            Uri uri = Uri.parse("rong://" + getApplicationInfo().packageName).buildUpon()
////                    .appendPath("conversationlist")
////                    .appendQueryParameter(Conversation.ConversationType.PRIVATE.getName(), "false") //设置私聊会话是否聚合显示
////                    .appendQueryParameter(Conversation.ConversationType.GROUP.getName(), "false")//群组
////                    .appendQueryParameter(Conversation.ConversationType.DISCUSSION.getName(), "false")//讨论组
////                    .appendQueryParameter(Conversation.ConversationType.PUBLIC_SERVICE.getName(), "false")//公共服务号
////                    .appendQueryParameter(Conversation.ConversationType.APP_PUBLIC_SERVICE.getName(), "false")//订阅号
////                    .appendQueryParameter(Conversation.ConversationType.SYSTEM.getName(), "false")//系统
////                    .build();
////            listFragment.setUri(uri);
////            return  listFragment;
////        } else {
////            return  mConversationFragment;
////        }
////    }
//
//
//    protected void setToolbarTitle() {
//        mToolbar.setNavigationIcon(null);
////        mToolbar.setTitle("First page");
//    }
//
//
//
//
//}
