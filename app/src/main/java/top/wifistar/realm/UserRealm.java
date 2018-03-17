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
	public String id;
	@PrimaryKey
	public String objectId;
	public String favorMoments = "";
	public String loaction;
	public String country;
	public String region;
	public String city;
	public String headBgUrl;
	public Integer sex;
	public Integer age;

	@Override
	public String toString() {
		return "objectId = " + objectId
				+ "; name = " + name
				+ "; headUrl = " + headUrl;
	}

	@Override
	public String getRealmId() {
		return objectId;
	}

	@Override
	public User toBmobObject() {
		User user = new User();
		user.name = name;
		user.headUrl = headUrl;
		user.id = id;
		user.favorMoments = favorMoments;
		user.sex = sex;
		user.age = age;
		user.loaction = loaction;
		user.headBgUrl = headBgUrl;
		user.country = country;
		user.region = region;
		user.city = city;
		user.setObjectId(objectId);
		return user;
	}
}
