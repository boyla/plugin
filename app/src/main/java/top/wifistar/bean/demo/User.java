package top.wifistar.bean.demo;

import cn.bmob.v3.BmobObject;

/**
 * 
* @ClassName: User 
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author yiw
* @date 2015-12-28 下午3:45:04 
*
 */
public class User extends BmobObject {
	//nick name, from profile
	private String name;
	private String headUrl;
	public String id;
	public User(){
	}
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
}
