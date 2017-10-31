package top.wifistar.bean.bmob;

import android.text.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import cn.bmob.v3.BmobObject;
import cn.bmob.v3.datatype.BmobRelation;
import top.wifistar.app.App;


public class Moment extends BmobObject {

	public final static String TYPE_TEXT = "4";
	public final static String TYPE_URL = "1";
	public final static String TYPE_IMG = "2";
	public final static String TYPE_VIDEO = "3";

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String content;
	private String type;//4:文字 1:链接  2:图片 3:视频
	private String linkImg;
	private String linkTitle;
	private String photos;
	private BmobRelation favors;
	private List<Comment> comments;
	private User user;
	private String videoUrl;
	private String videoImgUrl;
	public List<User> likes;
	public ArrayList<Photo> photosData;

	private boolean isExpand;

	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}

	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public BmobRelation getFavors() {
		return favors;
	}
	public void setFavors(BmobRelation favors) {
		this.favors = favors;
	}
	public List<Comment> getComments() {
		return comments;
	}
	public void setComments(List<Comment> comments) {
		this.comments = comments;
	}
	public String getLinkImg() {
		return linkImg;
	}
	public void setLinkImg(String linkImg) {
		this.linkImg = linkImg;
	}
	public String getLinkTitle() {
		return linkTitle;
	}
	public void setLinkTitle(String linkTitle) {
		this.linkTitle = linkTitle;
	}
	public String getPhotos() {
		return photos;
	}
	public void setPhotos(String photos) {
		this.photos = photos;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}

	public String getVideoUrl() {
		return videoUrl;
	}

	public void setVideoUrl(String videoUrl) {
		this.videoUrl = videoUrl;
	}

	public String getVideoImgUrl() {
		return videoImgUrl;
	}

	public void setVideoImgUrl(String videoImgUrl) {
		this.videoImgUrl = videoImgUrl;
	}

	public void setExpand(boolean isExpand){
		this.isExpand = isExpand;
	}

	public boolean isExpand(){
		return this.isExpand;
	}

	public boolean hasLikes(){
		if(likes !=null && likes.size()>0){
			return true;
		}
		return false;
	}
	
	public boolean hasComment(){
		if(comments!=null && comments.size()>0){
			return true;
		}
		return false;
	}
	
	public boolean isCurUserLike(){
		String profileId = App.currentUserProfile.getObjectId();
		if(!TextUtils.isEmpty(profileId) && hasLikes()){
			for(User item : likes){
				if(profileId.equals(item.id)){
					return true;
				}
			}
		}
		return false;
	}

	public List<Photo> getPhotosBean(){
		if(photosData!=null){
			return photosData;
		}
		if(!TextUtils.isEmpty(this.getPhotos())){
			List<String> photoStrs = Arrays.asList(this.getPhotos().split(","));
			photosData = new ArrayList<>();
			for (String item : photoStrs) {
				Photo photo = new Photo();
				photo.url = item;
				photosData.add(photo);
			}
		}
		return photosData;
	}
}
