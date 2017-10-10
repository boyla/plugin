package top.wifistar.bean.demo;

import cn.bmob.v3.BmobObject;

/**
 * 
* @ClassName: Comment
* @Description: TODO(这里用一句话描述这个类的作用) 
* @author yiw
* @date 2015-12-28 下午3:44:38 
*
 */
public class Comment extends BmobObject {

	private String momentId;
	private top.wifistar.bean.demo.User user;
	private top.wifistar.bean.demo.User toReplyUser;
	private String content;
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
	public top.wifistar.bean.demo.User getUser() {
		return user;
	}
	public void setUser(top.wifistar.bean.demo.User user) {
		this.user = user;
	}
	public top.wifistar.bean.demo.User getToReplyUser() {
		return toReplyUser;
	}
	public void setToReplyUser(top.wifistar.bean.demo.User toReplyUser) {
		this.toReplyUser = toReplyUser;
	}
	
}
