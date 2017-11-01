package top.wifistar.realm;

import cn.bmob.v3.BmobObject;
import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;
import top.wifistar.bean.bmob.User;

/**
 * 
* @ClassName: User 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author yiw
* @date 2015-12-28 下午3:45:04 
*
 */

public class UserRealm  extends RealmObject implements ToBmobObject{
	//nick name, from profile
	public String name;
	public String headUrl;
	@PrimaryKey
	public String id;
	public String objectId;

	@Override
	public String toString() {
		return "objectId = " + objectId
				+ "; name = " + name
				+ "; headUrl = " + headUrl;
	}

	@Override
	public User toBmobObject() {
		User user = new User();
		user.name = name;
		user.headUrl = headUrl;
		user.id = id;
		user.setObjectId(objectId);
		return user;
	}
}
