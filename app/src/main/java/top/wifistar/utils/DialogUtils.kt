package top.wifistar.utils

import android.app.Dialog
import android.content.Context
import android.text.TextUtils
import com.kongzue.dialog.listener.InputDialogOkButtonClickListener
import com.kongzue.dialog.v2.InputDialog
import top.wifistar.activity.EditProfileActivity

/**
 * Created by boyla on 2019/1/2.
 */
class DialogUtils {
    companion object {
        fun showEditDialog(context: Context, title: String, content: String?, editDialogCallBack: EditProfileActivity.EditDialogCallBack) {
            InputDialog.show(context, title, null, object : InputDialogOkButtonClickListener {
                override fun onClick(dialog: Dialog, inputText: String) {
                    editDialogCallBack.onFinish(inputText)
                }
            }).setDefaultInputHint(if (TextUtils.isEmpty(content)) "" else content)
        }

        fun showWhoCanSeeDialog(context: Context, title: String, content: String?, editDialogCallBack: EditProfileActivity.EditDialogCallBack) {
            InputDialog.show(context, title, null, object : InputDialogOkButtonClickListener {
                override fun onClick(dialog: Dialog, inputText: String) {
                    editDialogCallBack.onFinish(inputText)
                }
            }).setDefaultInputHint(if (TextUtils.isEmpty(content)) "" else content)
        }
    }
}