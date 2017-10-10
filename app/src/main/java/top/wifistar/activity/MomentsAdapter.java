package top.wifistar.activity;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;
import java.util.stream.Stream;

import top.wifistar.bean.MomentRealm;

/**
 * Created by hasee on 2017/4/14.
 */

public class MomentsAdapter extends RecyclerView.Adapter {
    Context mContext;
    LayoutInflater layoutInflater;
    List<MomentRealm> data;


    public MomentsAdapter(Context context, int layoutID, List<MomentRealm> mListBean, String profileID){
        if (null == context) {
            return;
        }
        this.mContext = context;
//        this.profileID = profileID;
//        this.layoutID = layoutID;
//        this.data = mListBean;
//        this.limitCommentCnt = 3;
//        layoutInflater = LayoutInflater.from(context);
//        isShowAllComments = false;

    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }
}
