package top.wifistar.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import io.realm.RealmModel;
import io.realm.RealmRecyclerViewAdapter;
import io.realm.RealmResults;
import top.wifistar.utils.Utils;

/**
 * recyclerView adapter base.
 * Created by rockliu on 2016/12/21.
 */
public abstract class BaseRealmAdapter<E extends RealmModel> extends RealmRecyclerViewAdapter<E, BaseRealmAdapter.BaseViewHolder> implements View.OnLongClickListener, View.OnClickListener {

    public interface OnItemClickListener<E> {
        void onItemClick(View view, E item, int position);
    }



    public interface OnItemLongClickListener<E> {
        void onItemLongClick(View view, E item, int position);
    }

    protected abstract void onBindViewHolder(BaseRealmAdapter.BaseViewHolder baseHolder, E item);

    protected OnItemClickListener<E> mListener;
    protected OnItemLongClickListener<E> mLongClickListener;

    public BaseRealmAdapter(Context context, RealmResults<E> data) {
        super(context, data, true);
    }

    public void setOnItemClickListener(OnItemClickListener<E> mListener) {
        this.mListener = mListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener<E> mLongClickListener) {
        this.mLongClickListener = mLongClickListener;
    }

    @Override
    public void onBindViewHolder(BaseRealmAdapter.BaseViewHolder holder, int position) {
        if (mListener != null) {
            holder.itemView.setTag(position);
            holder.itemView.setOnClickListener(this);
            holder.itemView.setOnLongClickListener(this);
        }
        E item = getData().get(position);
        onBindViewHolder(holder, item);
    }

    @Override
    public boolean onLongClick(View v) {
        if (v.getTag() == null) return false;
        int position = Utils.toInt(v.getTag().toString(), -1);
        if (position == -1 || position >= getItemCount()) return false;
        E item = getData().get(position);
        if (mLongClickListener != null) {
            mLongClickListener.onItemLongClick(v, item, position);
            return true;
        }
        return false;
    }

    @Override
    public void onClick(View v) {
        if (v.getTag() == null) return;
        int position = Utils.toInt(v.getTag().toString(), -1);
        if (position == -1 || position >= getItemCount()) return;
        E item = getData().get(position);
        if (mListener != null) {
            mListener.onItemClick(v, item, position);
        }

    }

    public static class BaseViewHolder extends RecyclerView.ViewHolder {
        public BaseViewHolder(View itemView) {
            super(itemView);
        }
    }

}
