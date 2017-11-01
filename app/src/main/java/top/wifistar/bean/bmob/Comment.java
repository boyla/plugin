package top.wifistar.bean.bmob;

import cn.bmob.v3.BmobObject;
import top.wifistar.realm.CommentRealm;
import top.wifistar.realm.ToRealmObject;

/**
 * 
* @ClassName: Comment
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author yiw
* @date 2015-12-28 下午3:44:38 
*
 */
public class Comment extends BmobObject implements ToRealmObject{

	public String momentId;
	public top.wifistar.bean.bmob.User user;
	public top.wifistar.bean.bmob.User toReplyUser;
	public String content;

	public String getMomentId() {
		return momentId;
	}
	public void setMomentId(String momentId) {
		this.momentId = momentId;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public top.wifistar.bean.bmob.User getUser() {
		return user;
	}
	public void setUser(top.wifistar.bean.bmob.User user) {
		this.user = user;
	}
	public top.wifistar.bean.bmob.User getToReplyUser() {
		return toReplyUser;
	}
	public void setToReplyUser(top.wifistar.bean.bmob.User toReplyUser) {
		this.toReplyUser = toReplyUser;
	}

	@Override
	public CommentRealm toRealmObject() {
		CommentRealm commentRealm = new CommentRealm();
		commentRealm.objectId = getObjectId();
		commentRealm.content = content;
		commentRealm.momentId = momentId;
		commentRealm.user = user.toRealmObject();
		commentRealm.toReplyUser = toReplyUser.toRealmObject();
		return commentRealm;
	}
}
