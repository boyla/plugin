package top.wifistar.bean.bmob;

import android.text.TextUtils;

import java.lang.reflect.Field;

import cn.bmob.v3.BmobObject;
import io.realm.RealmObject;
import top.wifistar.realm.IMUserRealm;
import top.wifistar.realm.ToRealmObject;
import top.wifistar.realm.UserRealm;



public class User extends BmobObject implements ToRealmObject {
	//nick name, from profile
	public String name;
	public String headUrl;
	public String id;
	public Integer sex = 1;
	public String favorMoments = "";
	public Integer age = 1;
	public String loaction;
	public String country;
	public String region;
	public String city;
	public String headBgUrl;
	public String recentImgs;
	public String startWord1;
	public String startWord2;
	public String startWord3;
	public String selfIntroduce;
	public String birth;

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
//		if(name == null){
//			return "";
//		}else{
			return name;
//		}
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
		realm.sex = sex;
		realm.age = age;
		realm.favorMoments = favorMoments;
		realm.loaction = loaction;
		realm.country = country;
		realm.region = region;
		realm.city = city;
		realm.headBgUrl = headBgUrl;
		realm.recentImgs = recentImgs;
		realm.startWord1 = startWord1;
		realm.startWord2 = startWord2;
		realm.startWord3 = startWord3;
		realm.selfIntroduce = selfIntroduce;
		realm.birth = birth;

		return realm;
	}

	public IMUserRealm toIMRealm() {
		IMUserRealm realm = new IMUserRealm();
		realm.name = name;
		realm.headUrl = headUrl;
		realm.id = id;
		realm.objectId = getObjectId();
		realm.sex = sex;
		realm.age = age;
		realm.favorMoments = favorMoments;
		realm.loaction = loaction;
		realm.country = country;
		realm.region = region;
		realm.city = city;
		realm.headBgUrl = headBgUrl;
		realm.recentImgs = recentImgs;
		realm.startWord1 = startWord1;
		realm.startWord2 = startWord2;
		realm.startWord3 = startWord3;
		realm.selfIntroduce = selfIntroduce;
		realm.birth = birth;

		return realm;
	}

	@Override
	public boolean equals(Object obj) {
		if(obj instanceof User){
			if(this==obj){
				return true;
			}else if(!isSame(this.getObjectId(),((User) obj).getObjectId())){
				return false;
			}else if(!this.name.equals(((User) obj).name)){
				return false;
			}else{
				Field[] objectFields = obj.getClass().getDeclaredFields();
				Field[] thisFields = obj.getClass().getDeclaredFields();
				try {
					for(int i = 0;i< objectFields.length;i++){
						if(objectFields[i].get(obj) instanceof String){
							if(!isSame((String)objectFields[i].get(obj), (String)thisFields[i].get(this))){
								return false;
							}
						}else if(!objectFields[i].get(obj).equals(thisFields[i].get(this))){
							return false;
                        }
				}
				} catch (IllegalAccessException e) {
					e.printStackTrace();
				}
				return true;
			}
		}else{
			return false;
		}
	}

	private boolean isSame(String str1,String str2){
		if(str1 == null && str2 == null){
			return true;
		}else if(str1 != null && str2 != null){
			if(str1.equals(str2)){
				return true;
			}else{
				return false;
			}
		}else{
			return false;
		}
	}
}
