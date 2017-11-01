package top.wifistar.realm;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import top.wifistar.bean.bmob.Comment;

/**
 * 
* @ClassName: Comment
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author yiw
* @date 2015-12-28 下午3:44:38 
*
 */
public class CommentRealm extends RealmObject implements ToBmobObject{

	public String momentId;
	public UserRealm user;
	public UserRealm toReplyUser;
	public String content;
	@PrimaryKey
	public String objectId;

	@Override
	public Comment toBmobObject() {
		Comment comment = new Comment();
		comment.setObjectId(objectId);
		comment.content = content;
		comment.momentId = momentId;
		comment.user = user.toBmobObject();
		comment.toReplyUser = toReplyUser.toBmobObject();
		return comment;
	}
}
