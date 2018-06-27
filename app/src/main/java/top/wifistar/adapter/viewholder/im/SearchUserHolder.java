package top.wifistar.adapter.viewholder.im;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import top.wifistar.R;
import top.wifistar.bean.bmob.User;
import top.wifistar.utils.GlideCircleTransform;
import top.wifistar.utils.Utils;

public class SearchUserHolder extends BaseViewHolder {

  public ImageView avatar;
  public TextView name;
  public Button btn_add;

  public SearchUserHolder(Context context, ViewGroup root,OnRecyclerViewListener onRecyclerViewListener) {
    super(context, root, R.layout.item_search_user,onRecyclerViewListener);
    initView(itemView);
  }

  private void initView(View itemView) {
    avatar = (ImageView) itemView.findViewById(R.id.avatar);
    name = (TextView) itemView.findViewById(R.id.name);
    btn_add = (Button) itemView.findViewById(R.id.btn_add);
  }

  @Override
  public void bindData(Object o) {
    final User user =(User)o;
    String avatarUrl = user != null ? user.getHeadUrl() : null;
    Glide.with(context)
            .load(!Utils.isEmpty(avatarUrl) ? avatarUrl : R.drawable.default_avartar)
            .dontAnimate()
            .diskCacheStrategy(DiskCacheStrategy.ALL)
            .bitmapTransform(new GlideCircleTransform(context))
            .into(avatar);
    name.setText(user.getName());
    btn_add.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {//查看个人详情
          Bundle bundle = new Bundle();
          bundle.putSerializable("u", user);
          Utils.jumpToProfile(context,user,null);
        }
    });
  }
}