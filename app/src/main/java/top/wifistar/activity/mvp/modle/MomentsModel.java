package top.wifistar.activity.mvp.modle;

import android.os.AsyncTask;
import android.util.Log;
import android.view.View;

import cn.bmob.v3.datatype.BmobRelation;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;
import top.wifistar.activity.mvp.listener.IDataRequestListener;
import top.wifistar.app.App;
import top.wifistar.bean.BUser;
import top.wifistar.bean.demo.Moment;
import top.wifistar.bean.demo.User;
import top.wifistar.utils.ACache;


/**
 * 
* @ClassName: MomentsModel
* @Description: 因为逻辑简单，这里我就不写model的接口了
* @author yiw
* @date 2015-12-28 下午3:54:55 
 */
public class MomentsModel {
	
	
	public MomentsModel(){
		//
	}

	public void loadData(final IDataRequestListener listener){
		requestServer(listener);
	}
	
	public void deleteCircle( final IDataRequestListener listener) {
		requestServer(listener);
	}

	public void addFavort( String momentId,final IDataRequestListener listener) {
		Moment moment = new Moment();
		moment.setObjectId(momentId);

		User user = new User(App.currentUserProfile.getNickName(),App.currentUserProfile.getAvatar());
		user.id = App.currentUserProfile.getObjectId();
		String objId = ACache.get(App.getInstance()).getAsString("SHORT_USER_ID_" + BUser.getCurrentUser().getObjectId());
		user.setObjectId(objId);

		BmobRelation relation = new BmobRelation();
		relation.add(user);
		moment.setFavors(relation);
		moment.update(new UpdateListener() {
			@Override
			public void done(BmobException e) {
				if(e==null){
					Log.i("bmob","点赞成功");
					listener.onSuccess();
				}else{
					Log.i("bmob","点赞失败："+e.getMessage());
				}
			}

		});
	}

	public void deleteFavort(final IDataRequestListener listener) {
		requestServer(listener);
	}

	public void addComment( final IDataRequestListener listener) {
		requestServer(listener);
	}

	public void deleteComment( final IDataRequestListener listener) {
		requestServer(listener);
	}
	
	/**
	 * 
	* @Title: requestServer 
	* @Description: 与后台交互, 因为demo是本地数据，不做处理
	* @param  listener    设定文件 
	* @return void    返回类型 
	* @throws
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
//				listener.onSuccess();
//			}
//		}.execute();

//		mMoment.getObjectId()
	}
	
}
