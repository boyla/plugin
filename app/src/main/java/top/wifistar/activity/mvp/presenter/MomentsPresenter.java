package top.wifistar.activity.mvp.presenter;

import android.view.View;

import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.FindListener;
import top.wifistar.activity.mvp.contract.MomentsContract;
import top.wifistar.activity.mvp.listener.IDataRequestListener;
import top.wifistar.activity.mvp.modle.MomentsModel;
import top.wifistar.bean.demo.User;
import top.wifistar.bean.demo.Moment;
import top.wifistar.bean.demo.CommentConfig;
import top.wifistar.bean.demo.Comment;
import top.wifistar.utils.DatasUtil;
import top.wifistar.utils.Utils;


/**
 * 
* @ClassName: MomentsPresenter
* @Description: 通知model请求服务器和通知view更新
* @author yiw
* @date 2015-12-28 下午4:06:03 
*
 */
public class MomentsPresenter implements MomentsContract.Presenter{
	private MomentsModel momentsModel;
	private MomentsContract.View view;

	public static int SKIP = 0;
	public static int PAGE_LIMIT = 10;


	public MomentsPresenter(MomentsContract.View view){
		momentsModel = new MomentsModel();
		this.view = view;
	}

	public void loadData(int loadType){
		BmobQuery<Moment> query = new BmobQuery<>();
		query.setLimit(PAGE_LIMIT);
		query.setSkip(SKIP);
		query.order("-createdAt")
				.findObjects(new FindListener<Moment>() {
					@Override
					public void done(List<Moment> data, BmobException e) {
						if (e == null) {
							if(data.size()>0){
								//SKIP += data.size();
								if(view!=null){
									view.update2loadData(loadType, data);
								}
							}
						} else {
							Utils.showToast(e.getMessage());
						}
					}
				});
	}


	/**
	 * 
	* @Title: deleteCircle 
	* @Description: 删除动态 
	* @param  circleId     
	* @return void    返回类型 
	* @throws
	 */
	public void deleteCircle(final String circleId){
		momentsModel.deleteCircle(new IDataRequestListener() {

			@Override
			public void onSuccess() {
                if(view!=null){
                    view.update2DeleteCircle(circleId);
                }
			}
		});
	}
	/**
	 * 
	* @Title: addFavort 
	* @Description: 点赞
	* @param  dataPosition
	* @return void    返回类型 
	* @throws
	 */
	public void addFavort(String momentId,final int dataPosition){
		momentsModel.addFavort(momentId,new IDataRequestListener() {
			@Override
			public void onSuccess() {
                if(view !=null ){
                    view.update2AddFavorite(dataPosition);
                }
			}
		});
	}
	/**
	 * 
	* @Title: deleteFavor
	* @Description: 取消点赞 
	* @param @param circlePosition
	* @param @param favortId     
	* @return void    返回类型 
	* @throws
	 */
	public void deleteFavort(final int circlePosition, final String favortId){
		momentsModel.deleteFavort(new IDataRequestListener() {

			@Override
			public void onSuccess() {
                if(view !=null ){
                    view.update2DeleteFavort(circlePosition, favortId);
                }
			}
		});
	}
	
	/**
	 * 
	* @Title: addComment 
	* @Description: 增加评论
	* @param  content
	* @param  config  CommentConfig
	* @return void    返回类型 
	* @throws
	 */
	public void addComment(final String content, final CommentConfig config){
		if(config == null){
			return;
		}
		momentsModel.addComment(new IDataRequestListener() {

			@Override
			public void onSuccess() {
				Comment newItem = null;
				if (config.commentType == CommentConfig.Type.PUBLIC) {
					newItem = DatasUtil.createPublicComment(content);
				} else if (config.commentType == CommentConfig.Type.REPLY) {
					newItem = DatasUtil.createReplyComment(config.replyUser, content);
				}
                if(view!=null){
                    view.update2AddComment(config.circlePosition, newItem);
                }
			}

		});
	}
	
	/**
	 * 
	* @Title: deleteComment 
	* @Description: 删除评论 
	* @param @param circlePosition
	* @param @param commentId     
	* @return void    返回类型 
	* @throws
	 */
	public void deleteComment(final int circlePosition, final String commentId){
		momentsModel.deleteComment(new IDataRequestListener(){

			@Override
			public void onSuccess() {
                if(view!=null){
                    view.update2DeleteComment(circlePosition, commentId);
                }
			}
			
		});
	}

	/**
	 *
	 * @param commentConfig
	 */
	public void showEditTextBody(CommentConfig commentConfig){
        if(view != null){
            view.updateEditTextBodyVisible(View.VISIBLE, commentConfig);
        }
	}


    /**
     * 清除对外部对象的引用，反正内存泄露。
     */
    public void recycle(){
        this.view = null;
    }
}
