package top.wifistar.bean.bmob;

import cn.bmob.v3.BmobObject;
import io.realm.RealmObject;
import top.wifistar.realm.ToRealmObject;
import top.wifistar.realm.UserRealm;



public class User extends BmobObject implements ToRealmObject {
	//nick name, from profile
	public String name;
	public String headUrl;
	public String id;

	public User(){}

	public User(String name, String headUrl){
		this.name = name;
		this.headUrl = headUrl;
	}

	public User(String name, String headUrl,String id){
		this.name = name;
		this.headUrl = headUrl;
		this.id = id;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getHeadUrl() {
		return headUrl;
	}
	public void setHeadUrl(String headUrl) {
		this.headUrl = headUrl;
	}

	@Override
	public String toString() {
		return "objectId = " + getObjectId()
				+ "; name = " + name
				+ "; headUrl = " + headUrl;
	}

	@Override
	public UserRealm toRealmObject() {
		UserRealm realm = new UserRealm();
		realm.name = name;
		realm.headUrl = headUrl;
		realm.id = id;
		realm.objectId = getObjectId();
		return realm;
	}
}
