package top.wifistar.adapter;

import android.content.Context;
import androidx.recyclerview.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import java.util.List;
import top.wifistar.R;
import top.wifistar.bean.bmob.Follow;
import top.wifistar.bean.bmob.User;
import top.wifistar.chain.user.NetUserRequest;
import top.wifistar.utils.Utils;

public class FollowAdapter extends BaseRecycleViewAdapter {


    Context mContext;

    public FollowAdapter(Context context, List<Follow> datas) {
        this.datas = datas;
        this.mContext = context;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new FollowViewHolder(LayoutInflater.from(mContext).inflate(
                R.layout.item_connection, parent, false
        ));
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder baseHolder, int position) {
        Follow follow = (Follow) datas.get(position);
        final FollowViewHolder followViewHolder = (FollowViewHolder) baseHolder;
        followViewHolder.tvName.setText("");
        followViewHolder.tvFollow.setText("");
        String cUserId = Utils.getCurrentShortUser().getObjectId();
        if (!TextUtils.isEmpty(cUserId)) {
            String[] users = follow.followers.split("_");
            String uid = cUserId.equals(users[0]) ? users[1] : users[0];
            Utils.queryShortUser(uid, new NetUserRequest.NetRequestCallBack() {
                @Override
                public void onSuccess(User user) {
                    followViewHolder.tvName.setText(user.name);
                    followViewHolder.tvFollow.setText(user.name);
                    //绿色互相关注；橙色我关注对方；蓝色对方关注我
                    if (follow.followState == 3) {
                        followViewHolder.tvFollow.setBackgroundResource(R.drawable.text_bg_green);
                        followViewHolder.tvFollow.setText("互相关注");
                    } else if (follow.isFollowing()) {
                        followViewHolder.tvFollow.setBackgroundResource(R.drawable.text_bg_orange);
                        followViewHolder.tvFollow.setText("你已关注");
                    } else {
                        followViewHolder.tvFollow.setBackgroundResource(R.drawable.text_bg_blue);
                        followViewHolder.tvFollow.setText("正关注你");
                    }
                    Utils.setUserAvatar(user, followViewHolder.mHeadImg);
                    followViewHolder.itemView.setOnClickListener(v -> Utils.jumpToProfile(followViewHolder.mHeadImg.getContext(), user, followViewHolder.mHeadImg));

                }

                @Override
                public void onFailure(String msg) {

                }
            });
        }
    }


    static public class FollowViewHolder extends RecyclerView.ViewHolder {

        LinearLayout mLinearLayout;
        ImageView mHeadImg;
        TextView tvName;
        TextView tvFollow;

        public FollowViewHolder(View itemView) {
            super(itemView);
            mLinearLayout = (LinearLayout) itemView.findViewById(R.id.linearlayout_item);
            mHeadImg = (ImageView) itemView.findViewById(R.id.imageView_avatar);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvFollow = (TextView) itemView.findViewById(R.id.tvFollow);
        }
    }
}
