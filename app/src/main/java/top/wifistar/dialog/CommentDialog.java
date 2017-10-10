package top.wifistar.dialog;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.text.ClipboardManager;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

import top.wifistar.R;
import top.wifistar.activity.mvp.presenter.MomentsPresenter;
import top.wifistar.bean.demo.Comment;
import top.wifistar.utils.DatasUtil;


/**
 * 
* @ClassName: CommentDialog 
* @Description: 评论长按对话框，保护复制和删除 
* @author yiw
* @date 2015-12-28 下午3:36:39 
*
 */
public class CommentDialog extends Dialog implements
		View.OnClickListener {

	private Context mContext;
	private MomentsPresenter mPresenter;
	private Comment mComment;
	private int mCirclePosition;

	public CommentDialog(Context context, MomentsPresenter presenter,
                         Comment comment, int circlePosition) {
		super(context, R.style.comment_dialog);
		mContext = context;
		this.mPresenter = presenter;
		this.mComment = comment;
		this.mCirclePosition = circlePosition;
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_comment);
		initWindowParams();
		initView();
	}

	private void initWindowParams() {
		Window dialogWindow = getWindow();
		// 获取屏幕宽、高用
		WindowManager wm = (WindowManager) mContext
				.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		WindowManager.LayoutParams lp = dialogWindow.getAttributes();
		lp.width = (int) (display.getWidth() * 0.65); // 宽度设置为屏幕的0.65

		dialogWindow.setGravity(Gravity.CENTER);
		dialogWindow.setAttributes(lp);
	}

	private void initView() {
		TextView copyTv = (TextView) findViewById(R.id.copyTv);
		copyTv.setOnClickListener(this);
		TextView deleteTv = (TextView) findViewById(R.id.deleteTv);
		if (mComment != null
				&& DatasUtil.curUser.getObjectId().equals(
						mComment.getUser().getObjectId())) {
			deleteTv.setVisibility(View.VISIBLE);
		} else {
			deleteTv.setVisibility(View.GONE);
		}
		deleteTv.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.copyTv:
			if (mComment != null) {
				ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
				clipboard.setText(mComment.getContent());
			}
			dismiss();
			break;
		case R.id.deleteTv:
			if (mPresenter != null && mComment != null) {
				mPresenter.deleteComment(mCirclePosition, mComment.getMomentId());
			}
			dismiss();
			break;
		default:
			break;
		}
	}

}
