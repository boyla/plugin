package top.wifistar.realm;

import java.util.ArrayList;
import java.util.Arrays;

import io.realm.RealmList;
import io.realm.RealmObject;
import top.wifistar.bean.bmob.Moment;
import top.wifistar.bean.bmob.Photo;


public class MomentRealm extends RealmObject implements ToBmobObject{

	public String content;
	public String type;//4:文字 1:链接  2:图片 3:视频
	public String linkImg;
	public String linkTitle;
	public String photos;
	public RealmList<CommentRealm> comments;
	public UserRealm user;
	public String videoUrl;
	public String videoImgUrl;
	public RealmList<UserRealm> likes;
	public RealmList<Photo> photosData;
	public String objectId;

	public boolean isExpand;

	@Override
	public Moment toBmobObject() {
		Moment moment = new Moment();
		moment.setObjectId(objectId);
		moment.content = content;
		moment.type = type;
		moment.linkImg = linkImg;
		moment.linkTitle = linkTitle;
		moment.photos = photos;
		//comment things
		moment.comments = new ArrayList<>();
		for(CommentRealm commentRealm : comments){
			moment.comments.add(commentRealm.toBmobObject());
		}
		moment.user = user.toBmobObject();
		moment.videoUrl = videoUrl;
		moment.videoImgUrl = videoImgUrl;
		moment.likes = new ArrayList<>();
		for(UserRealm userRealm : likes){
			moment.likes.add(userRealm.toBmobObject());
		}
		moment.photosData = new ArrayList<>();
		for(Photo photo : photosData){
			moment.photosData.add(photo);
		}



		return moment;
	}
}
