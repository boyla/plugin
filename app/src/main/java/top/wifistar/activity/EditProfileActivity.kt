package top.wifistar.activity

import android.app.DatePickerDialog
import android.app.Dialog
import android.support.v7.widget.Toolbar
import android.text.TextUtils
import android.view.*
import android.widget.*
import com.flyco.dialog.entity.DialogMenuItem
import com.flyco.dialog.widget.NormalListDialog
import kotlinx.android.synthetic.main.activity_edit_info.*
import top.wifistar.R
import top.wifistar.adapter.SingleChoiceAdapter
import top.wifistar.app.ToolbarActivity
import top.wifistar.bean.BUser
import top.wifistar.bean.bmob.BmobUtils
import top.wifistar.bean.bmob.User
import top.wifistar.utils.Utils
import java.util.ArrayList
import com.kongzue.dialog.listener.InputDialogOkButtonClickListener
import com.kongzue.dialog.v2.InputDialog


class EditProfileActivity : ToolbarActivity() {

    lateinit var mUser: User

    override fun initTopBar() {
        mToolbar.setNavigationIcon(R.drawable.back)
    }

    override fun initUI() {
        super.setContentView(R.layout.activity_edit_info)
        setCenterTitle("编辑个人资料")

        mUser = Utils.getCurrentShortUser()
        if (mUser == null) {
            return
        }
        initText()
        setClickListener()

    }

    private fun setClickListener() {
        mToolbar.setOnMenuItemClickListener(object : Toolbar.OnMenuItemClickListener {
            override fun onMenuItemClick(item: MenuItem?): Boolean {
                when (item?.getItemId()) {
                    R.id.done -> commitData()
                }
                return true
            }
        })

        rlNick.setOnClickListener {
            showEditDialog("修改昵称", mUser.name, object : EditDialogCallBack {
                override fun onFinish(result: String) {
                    mUser.name = result
                    tvNickName.text = result
                }
            })
        }
        rlSex.setOnClickListener {
            showSingleChoiceDialog("修改性别", arrayOf("女", "男"), mUser.sex, object : SingleChoiceDialogCallBack {
                override fun onFinish(result: Int) {
                    mUser.sex = result
                    tvSex.text = if (mUser.sex == 0) "女" else "男"
                }
            })
        }
        rlBirth.setOnClickListener {
            openDatePicker()
        }
        rlEmail.setOnClickListener {
            Utils.makeSysToast("注册邮箱暂时不支持修改")
        }
        rlSignature1.setOnClickListener {
            showEditDialog("修改签名1", mUser.startWord1, object : EditDialogCallBack {
                override fun onFinish(result: String) {
                    mUser.startWord1 = result
                    tvSignature1.text = result
                }
            })
        }
        rlSignature2.setOnClickListener {
            showEditDialog("修改签名2", mUser.startWord2, object : EditDialogCallBack {
                override fun onFinish(result: String) {
                    mUser.startWord2 = result
                    tvSignature2.text = result
                }
            })
        }
        rlSignature3.setOnClickListener {
            showEditDialog("修改签名3", mUser.startWord3, object : EditDialogCallBack {
                override fun onFinish(result: String) {
                    mUser.startWord3 = result
                    tvSignature3.text = result
                }
            })
        }
        rlSelfIntro.setOnClickListener {
            showEditDialog("修改个人介绍", mUser.selfIntroduce, object : EditDialogCallBack {
                override fun onFinish(result: String) {
                    mUser.selfIntroduce = result
                    tvSelfIntro.text = result
                }
            })
        }
    }

    private fun commitData() {
        BmobUtils.updateUser(mUser)
        finish()
    }

    private fun showEditDialog(title: String, content: String?, editDialogCallBack: EditProfileActivity.EditDialogCallBack) {
        InputDialog.show(this, title, null, object : InputDialogOkButtonClickListener {
            override fun onClick(dialog: Dialog, inputText: String) {
                editDialogCallBack.onFinish(inputText)
            }
        }).setDefaultInputHint(if (TextUtils.isEmpty(content)) "" else content)
    }

    private fun showSingleChoiceDialog(title: String, strings: Array<String>, sex: Int, singleChoiceDialogCallBack: EditProfileActivity.SingleChoiceDialogCallBack) {
        var mMenuItems = ArrayList<DialogMenuItem>()
        for (item in strings) {
            mMenuItems.add(DialogMenuItem(item, 0))
        }
        val singleChoiceDialog = NormalListDialog(mContext, SingleChoiceAdapter(mContext, mMenuItems)).titleBgColor(0x88FF571D2.toInt()).titleTextColor(0xFFFFFFFFF.toInt()).layoutAnimation(null)
        singleChoiceDialog.title(title).show()
        singleChoiceDialog.setOnOperItemClickL({ parent, view, position, id ->
            singleChoiceDialogCallBack.onFinish(position)
            singleChoiceDialog.dismiss()
        })
    }

    interface EditDialogCallBack {
        fun onFinish(result: String)
    }

    interface SingleChoiceDialogCallBack {
        fun onFinish(result: Int)
    }

    private fun initText() {
        tvNickName.text = mUser.name
        tvSex.text = if (mUser.sex == 0) "女" else "男"
        tvBirth.text = mUser.birth
        tvEmail.text = BUser.getCurrentUser().email
        tvSignature1.text = mUser.startWord1
        tvSignature2.text = mUser.startWord2
        tvSignature3.text = mUser.startWord3
        tvSelfIntro.text = mUser.selfIntroduce
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        getMenuInflater().inflate(R.menu.done, menu)
        return true
    }

    private fun openDatePicker() {
        var currentYear = 1990
        var currentMonth = 0
        var currentDay = 1
        if (!TextUtils.isEmpty(mUser.birth)) {
            currentYear = mUser.birth.substring(0, 4).toInt()
            currentMonth = mUser.birth.substring(5, 7).toInt() - 1
            currentDay = mUser.birth.substring(8, 10).toInt()
        }
        val dialog = DatePickerDialog(this, R.style.MyDatePickerDialogTheme, object : DatePickerDialog.OnDateSetListener {
            override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
                var realMonth = month + 1
                mUser.birth = "" + year + "-" + (if (realMonth < 10) {
                    "0$realMonth"
                } else {
                    realMonth
                }) + "-" + (if (dayOfMonth < 10) {
                    "0$dayOfMonth"
                } else {
                    dayOfMonth
                })
                tvBirth.text = mUser.birth
            }
        }, currentYear, currentMonth, currentDay)
        val datePicker = dialog.datePicker
        datePicker.maxDate = System.currentTimeMillis()
        dialog.show()
    }
}
