package top.wifistar.adapter.viewholder;

import android.view.View;
import android.view.ViewStub;


/**
 * Created by suneee on 2016/8/16.
 */
public class TextViewHolder extends MomentViewHolder {


    public TextViewHolder(View itemView){
        super(itemView, TYPE_TEXT);
    }

    @Override
    public void initSubView(int viewType, ViewStub viewStub) {
        if(viewStub == null){
            throw new IllegalArgumentException("viewStub is null...");
        }
        viewStub.setVisibility(View.GONE);

    }
}
