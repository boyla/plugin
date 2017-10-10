package top.wifistar.activity.mvp.contract;


import java.util.List;

import top.wifistar.activity.mvp.BasePresenter;
import top.wifistar.activity.mvp.BaseView;
import top.wifistar.bean.demo.Comment;
import top.wifistar.bean.demo.Moment;
import top.wifistar.bean.demo.CommentConfig;
import top.wifistar.bean.demo.User;

/**
 * Created by suneee on 2016/7/15.
 */
public interface MomentsContract {

    interface View extends BaseView {
        void update2DeleteCircle(String circleId);
        void update2AddFavorite(int circlePosition);
        void update2DeleteFavort(int circlePosition, String favortId);
        void update2AddComment(int circlePosition, Comment addItem);
        void update2DeleteComment(int circlePosition, String commentId);
        void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig);
        void update2loadData(int loadType, List<Moment> datas);
    }

    interface Presenter extends BasePresenter {
        void loadData(int loadType);
        void deleteCircle(final String circleId);
        void addFavort(String momentId,final int circlePosition);
        void deleteFavort(final int circlePosition, final String favortId);
        void deleteComment(final int circlePosition, final String commentId);

    }
}
