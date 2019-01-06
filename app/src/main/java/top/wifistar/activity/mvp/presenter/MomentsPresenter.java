package top.wifistar.activity.mvp.presenter;

import android.text.TextUtils;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import io.realm.RealmResults;
import top.wifistar.activity.mvp.contract.MomentsContract;
import top.wifistar.activity.mvp.listener.AddCommentListener;
import top.wifistar.activity.mvp.listener.IDataRequestListener;
import top.wifistar.activity.mvp.modle.MomentsModel;
import top.wifistar.bean.bmob.Comment;
import top.wifistar.bean.bmob.Moment;
import top.wifistar.bean.bmob.CommentConfig;
import top.wifistar.bean.bmob.User;
import top.wifistar.realm.BaseRealmDao;
import top.wifistar.realm.MomentRealm;
import top.wifistar.utils.Utils;

import static top.wifistar.fragment.MomentsFragment.TYPE_PULLDOWNREFRESH;


/**
 * @author yiw
 * @ClassName: MomentsPresenter
 * @Description: 通知model请求服务器和通知view更新
 * @date 2015-12-28 下午4:06:03
 */
public class MomentsPresenter implements MomentsContract.Presenter {
    private MomentsModel momentsModel;
    private MomentsContract.View view;

    public static int SKIP = 0;
    public static final int PAGE_LIMIT = 10;
    public static boolean NO_MORE_DATA = false;

    public MomentsPresenter(MomentsContract.View view) {
        momentsModel = new MomentsModel();
        this.view = view;
    }

    public void loadData(int loadType, String userId, String topicName) {
        if (loadType == TYPE_PULLDOWNREFRESH) {
            NO_MORE_DATA = false;
            SKIP = 0;
        }
        if (TextUtils.isEmpty(userId)) {
            queryBySubscribe(loadType, topicName);
        } else {
            queryByUser(loadType, userId);
        }
    }

    private void queryByUser(int loadType, String userId) {
        if (Utils.isNetworkConnected()) {
            //TODO 有待优化，根据订阅的时间节点请求Bmob
            BmobQuery<Moment> query = new BmobQuery<>();
            query.addWhereEqualTo("user", userId);
            query.setLimit(PAGE_LIMIT);
            query.setSkip(SKIP);
            if(Utils.getCurrentShortUser()==null || !userId.equals(Utils.getCurrentShortUser().getObjectId())){
                query.addWhereEqualTo("isPrivate", false);
            }
            query.order("-createdAt")
                    .findObjects(new FindListener<Moment>() {
                        @Override
                        public void done(List<Moment> data, BmobException e) {
                            if (e == null) {
                                if (data != null) {
                                    SKIP += data.size();
                                    if (view != null) {
                                        view.update2loadData(loadType, data);
                                    }
                                }
                            } else {
                                Utils.showToast(e.getMessage());
                            }
                        }
                    });
        } else {
            RealmResults<MomentRealm> dbData = (RealmResults<MomentRealm>) BaseRealmDao.findAll(MomentRealm.class, "createAt");
            if (!dbData.isEmpty()) {
                if (dbData.isLoaded()) {
                    // 完成查询
                    NO_MORE_DATA = true;
                    List<Moment> data = new ArrayList<>();
                    for (MomentRealm momentRealm : dbData) {
                        data.add(momentRealm.toBmobObject());
                    }
                    view.update2loadData(loadType, data);
                }
            }

        }
    }

    private void queryBySubscribe(int loadType, String topic) {
        if (Utils.isNetworkConnected()) {
            //TODO 有待优化，根据订阅的时间节点请求Bmob
            BmobQuery<Moment> query = new BmobQuery<>();
            query.setLimit(PAGE_LIMIT);
            query.setSkip(SKIP);
            query.addWhereEqualTo("topic", topic)
                    .addWhereEqualTo("isPrivate", false)
                    .order("-createdAt")
                    .findObjects(new FindListener<Moment>() {
                        @Override
                        public void done(List<Moment> data, BmobException e) {
                            if (e == null) {
                                if (data != null) {
                                    SKIP += data.size();
                                    if (view != null) {
                                        view.update2loadData(loadType, data);
                                    }
                                }
                            } else {
                                Utils.showToast(e.getMessage());
                            }
                        }
                    });
        } else {
            RealmResults<MomentRealm> dbData = (RealmResults<MomentRealm>) BaseRealmDao.findAll(MomentRealm.class, "createAt");
            if (!dbData.isEmpty()) {
                if (dbData.isLoaded()) {
                    // 完成查询
                    NO_MORE_DATA = true;
                    List<Moment> data = new ArrayList<>();
                    for (MomentRealm momentRealm : dbData) {
                        data.add(momentRealm.toBmobObject());
                    }
                    view.update2loadData(loadType, data);
                }
            }

        }
    }


    /**
     * @param momentId
     * @return void    返回类型
     * @throws
     * @Title: deleteMoment
     * @Description: 删除动态
     */
    public void deleteMoment(final String momentId, int position) {
        momentsModel.deleteMoment(momentId, new IDataRequestListener() {

            @Override
            public void onSuccess() {
                if (view != null) {
                    view.update2DeleteMoment(momentId, position);
                }
            }
        });
    }

    /**
     * @param dataPosition
     * @return void    返回类型
     * @throws
     * @Title: addFavort
     * @Description: 点赞
     */
    public void addFavort(String momentId, final int dataPosition) {
        momentsModel.addFavort(momentId, new IDataRequestListener() {
            @Override
            public void onSuccess() {
                if (view != null) {
                    view.update2AddFavorite(dataPosition);
                }
            }
        });
    }

    /**
     * @param @param dataPosition
     * @param @param momentId
     * @return void    返回类型
     * @throws
     * @Title: deleteFavor
     * @Description: 取消点赞
     */
    public void deleteFavort(final String momentId, final int dataPosition) {
        momentsModel.deleteFavort(momentId, new IDataRequestListener() {
            @Override
            public void onSuccess() {
                if (view != null) {
                    view.update2DeleteFavort(dataPosition);
                }
            }
        });
    }

    /**
     * @param content
     * @return void    返回类型
     * @throws
     * @Title: addComment
     * @Description: 增加评论
     */
    public void addComment(final String content, final String momentId, final User toReplyUser) {

        momentsModel.addComment(content, momentId, toReplyUser, new AddCommentListener() {

            @Override
            public void onSuccess(Comment comment) {
                if (view != null) {
                    view.update2AddComment(comment);
                }
            }
        });
    }

    /**
     * @param @param circlePosition
     * @param @param commentId
     * @return void    返回类型
     * @throws
     * @Title: deleteComment
     * @Description: 删除评论
     */
    public void deleteComment(final int circlePosition, final String commentId) {
        momentsModel.deleteComment(commentId, new IDataRequestListener() {

            @Override
            public void onSuccess() {
                if (view != null) {
                    view.update2DeleteComment(circlePosition, commentId);
                }
            }

        });
    }

    /**
     * @param commentConfig
     */
    public void showEditTextBody(CommentConfig commentConfig) {
        if (view != null) {
            view.updateEditTextBodyVisible(View.VISIBLE, commentConfig);
        }
    }

    /**
     * 清除对外部对象的引用，反正内存泄露。
     */
    public void recycle() {
        this.view = null;
    }

    public void setCurrentMomentId(String momentId, User replyUser) {
        view.setCurrentMomentId(momentId, replyUser);
    }
}
