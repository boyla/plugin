package top.wifistar.adapter.viewholder.im;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.text.SimpleDateFormat;

import cn.bmob.newim.bean.BmobIMAudioMessage;
import cn.bmob.newim.bean.BmobIMMessage;
import cn.bmob.newim.bean.BmobIMUserInfo;
import cn.bmob.newim.core.BmobDownloadManager;
import cn.bmob.newim.listener.FileDownloadListener;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import top.wifistar.R;
import top.wifistar.bean.BUser;
import top.wifistar.utils.GlideCircleTransform;

/**
 * 接收到的文本类型
 */
public class ReceiveVoiceHolder extends BaseViewHolder {

    protected ImageView iv_avatar;

    protected TextView tv_time;

    protected TextView tv_voice_length;
    protected ImageView iv_voice;
    protected ProgressBar progress_load;

    private String currentUid = "";

    public ReceiveVoiceHolder(Context context, ViewGroup root, OnRecyclerViewListener onRecyclerViewListener) {
        super(context, root, R.layout.item_chat_received_voice, onRecyclerViewListener);
        initView(itemView);
        try {
            currentUid = BmobUser.getCurrentUser(BUser.class).getObjectId();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initView(View itemView) {
        iv_avatar = (ImageView) itemView.findViewById(R.id.iv_avatar);
        tv_time = (TextView) itemView.findViewById(R.id.tv_time);
        tv_voice_length = (TextView) itemView.findViewById(R.id.tv_voice_length);
        iv_voice = (ImageView) itemView.findViewById(R.id.iv_voice);
        progress_load = (ProgressBar) itemView.findViewById(R.id.progress_load);
    }

    @Override
    public void bindData(Object o) {
        BmobIMMessage msg = (BmobIMMessage) o;
        //用户信息的获取必须在buildFromDB之前，否则会报错'Entity is detached from DAO context'
        final BmobIMUserInfo info = msg.getBmobIMUserInfo();
        Glide.with(context)
                .load(info != null ? info.getAvatar() : R.drawable.default_avartar)
                .dontAnimate()
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .bitmapTransform(new GlideCircleTransform(context))
                .error(R.drawable.default_avartar)
                .into(iv_avatar);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy年MM月dd日 HH:mm");
        String time = dateFormat.format(msg.getCreateTime());
        tv_time.setText(time);
        iv_avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toast("点击" + info.getName() + "的头像");
            }
        });
        //显示特有属性
        final BmobIMAudioMessage message = BmobIMAudioMessage.buildFromDB(false, msg);
        boolean isExists = BmobDownloadManager.isAudioExist(currentUid, message);
        if (!isExists) {//若指定格式的录音文件不存在，则需要下载，因为其文件比较小，故放在此下载
            BmobDownloadManager downloadTask = new BmobDownloadManager(getContext(), msg, new FileDownloadListener() {

                @Override
                public void onStart() {
                    progress_load.setVisibility(View.VISIBLE);
                    tv_voice_length.setVisibility(View.GONE);
                    iv_voice.setVisibility(View.INVISIBLE);//只有下载完成才显示播放的按钮
                }

                @Override
                public void done(BmobException e) {
                    if (e == null) {
                        progress_load.setVisibility(View.GONE);
                        tv_voice_length.setVisibility(View.VISIBLE);
                        tv_voice_length.setText(message.getDuration() + "\''");
                        iv_voice.setVisibility(View.VISIBLE);
                    } else {
                        progress_load.setVisibility(View.GONE);
                        tv_voice_length.setVisibility(View.GONE);
                        iv_voice.setVisibility(View.INVISIBLE);
                    }
                }
            });
            downloadTask.execute(message.getContent());
        } else {
            tv_voice_length.setVisibility(View.VISIBLE);
            tv_voice_length.setText(message.getDuration() + "\''");
        }
        iv_voice.setOnClickListener(new NewRecordPlayClickListener(getContext(), message, iv_voice));

        iv_voice.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                if (onRecyclerViewListener != null) {
                    onRecyclerViewListener.onItemLongClick(getAdapterPosition());
                }
                return true;
            }
        });

    }

    public void showTime(boolean isShow) {
        tv_time.setVisibility(isShow ? View.VISIBLE : View.GONE);
    }
}