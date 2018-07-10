package top.wifistar.activity.mvp.contract;


import java.util.List;

import top.wifistar.activity.mvp.BasePresenter;
import top.wifistar.activity.mvp.BaseView;
import top.wifistar.bean.bmob.Comment;
import top.wifistar.bean.bmob.Moment;
import top.wifistar.bean.bmob.CommentConfig;
import top.wifistar.bean.bmob.User;

/**
 * Created by suneee on 2016/7/15.
 */
public interface MomentsContract {

    interface View extends BaseView {
        void update2DeleteMoment(String momentId, int position);

        void update2AddFavorite(int circlePosition);

        void update2DeleteFavort(int dataPosition);

        void update2AddComment(Comment comment);

        void update2DeleteComment(int circlePosition, String commentId);

        void updateEditTextBodyVisible(int visibility, CommentConfig commentConfig);

        void update2loadData(int loadType, List<Moment> datas);

        void setCurrentMomentId(String momentId, User replyUserId);
    }

    interface Presenter extends BasePresenter {
        void loadData(int loadType, String userId);

        void deleteMoment(final String circleId, int position);

        void addFavort(final String momentId, final int circlePosition);

        void deleteFavort(final String momentId, final int dataPosition);

        void deleteComment(final int circlePosition, final String commentId);

    }
}
