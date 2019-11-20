package top.wifistar.activity.mvp.modle;

import android.text.TextUtils;
import android.util.Log;

import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;
import top.wifistar.activity.mvp.listener.AddCommentListener;
import top.wifistar.activity.mvp.listener.IDataRequestListener;
import top.wifistar.app.App;
import top.wifistar.bean.BUser;
import top.wifistar.bean.bmob.Comment;
import top.wifistar.bean.bmob.Moment;
import top.wifistar.bean.bmob.User;
import top.wifistar.utils.ACache;
import top.wifistar.utils.Utils;


/**
 * @author yiw
 * @ClassName: MomentsModel
 * @Description: 因为逻辑简单，这里我就不写model的接口了
 * @date 2015-12-28 下午3:54:55
 */
public class MomentsModel {


    public MomentsModel() {
        //
    }

    public void loadData(final IDataRequestListener listener) {
        requestServer(listener);
    }

    public void deleteMoment(String momentId, final IDataRequestListener listener) {
        Moment moment = new Moment();
        moment.setObjectId(momentId);
        moment.delete(new UpdateListener() {

            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Utils.showToast("删除动态成功");
                    listener.onSuccess();
                } else {
                    Utils.showToast("删除动态失败");
                }
            }
        });
    }

    public void addFavort(final String momentId, final IDataRequestListener listener) {
        Moment moment = new Moment();
        moment.setObjectId(momentId);

        User user = new User(App.currentUserProfile.getNickName(), App.currentUserProfile.getAvatar() + "");
        user.id = App.currentUserProfile.getObjectId();
        String objId = ACache.get(App.getInstance()).getAsString("SHORT_USER_ID_" + BUser.getCurrentUser().getObjectId());
        user.setObjectId(objId);

        BmobRelation relation = new BmobRelation();
        relation.add(user);
        moment.setFavors(relation);
        moment.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("bmob", "点赞成功");
                    listener.onSuccess();
                } else {
                    Log.i("bmob", "点赞失败：" + e.getMessage());
                }
            }

        });
    }

    public void deleteFavort(final String momentId, final IDataRequestListener listener) {
        Moment moment = new Moment();
        moment.setObjectId(momentId);

        User user = new User(App.currentUserProfile.getNickName(), App.currentUserProfile.getAvatar());
        user.id = App.currentUserProfile.getObjectId();
        String objId = ACache.get(App.getInstance()).getAsString("SHORT_USER_ID_" + BUser.getCurrentUser().getObjectId());
        user.setObjectId(objId);

        BmobRelation relation = new BmobRelation();
        relation.remove(user);
        moment.setFavors(relation);
        moment.update(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    Log.i("bmob", "取消赞成功");
                    listener.onSuccess();
                } else {
                    Log.i("bmob", "取消赞失败：" + e.getMessage());
                }
            }
        });
    }

    public void addComment(String content, final String momentId, final User toReplyUser, final AddCommentListener listener) {
        Comment comment = new Comment();
        comment.setUser(Utils.getCurrentShortUser());
        comment.setMomentId(momentId);
        comment.setContent(content);
        Moment moment = new Moment();
        moment.setObjectId(momentId);
        comment.moment = moment;
        if (toReplyUser != null) {
            comment.setToReplyUser(toReplyUser);
        }
        comment.save(new SaveListener<String>() {
            @Override
            public void done(String objId, BmobException e) {
                if (e == null) {
                    comment.setObjectId(objId);
                    listener.onSuccess(comment);
                } else {
                    Utils.makeSysToast(e.getMessage());
                }
            }
        });
    }

    public void deleteComment(final String commentId, final IDataRequestListener listener) {
        Comment comment = new Comment();
        comment.setObjectId(commentId);
        comment.delete(new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (TextUtils.isEmpty(commentId) || e == null || e.getErrorCode() == 101) {
                    listener.onSuccess();
                }
            }
        });
    }

    /**
     * @param listener 设定文件
     * @return void    返回类型
     * @throws
     * @Title: requestServer
     * @Description: 与后台交互, 因为demo是本地数据，不做处理
     */
    private void requestServer(final IDataRequestListener listener) {
//		new AsyncTask<Object, Integer, Object>(){
//			@Override
//			protected Object doInBackground(Object... params) {
//				//和后台交互
//				return null;
//			}
//
//			protected void onMomentExecute(Object result) {
//				onTouchListener.onSuccess();
//			}
//		}.execute();

//		mMoment.getObjectId()
    }

}
