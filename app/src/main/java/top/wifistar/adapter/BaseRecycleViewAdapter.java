package top.wifistar.adapter;

import androidx.recyclerview.widget.RecyclerView;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by yiwei on 16/4/9.
 */
public abstract class BaseRecycleViewAdapter<T,VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {
    protected RecycleViewItemListener itemListener;
    protected List<T> datas = new ArrayList<>();

    public List<T> getDatas() {
        if (datas==null)
            datas = new ArrayList<T>();
        return datas;
    }

    public void setDatas(List<T> datas) {
        this.datas.clear();
        this.datas.addAll(datas);
    }

    public void setItemListener(RecycleViewItemListener listener){
        this.itemListener = listener;
    }

    public interface RecycleViewItemListener {
        void onItemClick(int position);
        boolean onItemLongClick(int position);
    }
}
