package top.wifistar.activity

import android.app.DatePickerDialog
import android.app.Dialog
import androidx.appcompat.widget.Toolbar
import android.text.TextUtils
import android.view.*
import android.widget.*
import com.airbnb.lottie.LottieAnimationView
import kotlinx.android.synthetic.main.activity_edit_info.*
import top.wifistar.R
import top.wifistar.app.ToolbarActivity
import top.wifistar.bean.BUser
import top.wifistar.bean.bmob.BmobUtils
import top.wifistar.bean.bmob.User
import top.wifistar.utils.Utils
import com.kongzue.dialog.listener.InputDialogOkButtonClickListener
import com.kongzue.dialog.v2.InputDialog
import android.animation.ValueAnimator
import com.kongzue.dialog.v2.DialogSettings
import top.wifistar.constant.Constants


class EditProfileActivity : ToolbarActivity() {

    lateinit var mUser: User
    var startWord1 = ""
    var startWord2 = ""
    var startWord3 = ""


    override fun initTopBar() {
        mToolbar.setNavigationIcon(R.drawable.back)
    }

    override fun initUI() {
        super.setContentView(R.layout.activity_edit_info)
        setCenterTitle("编辑个人资料")

        mUser = Utils.getCurrentShortUser() ?: return
        initText()
        lavSwitch.setAnimation("lottie/sex.json")
        if (mUser.sex != 0) {
            lavSwitch.progress = 1f
        } else {
            lavSwitch.progress = 0f
        }
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
                override fun onFinish(dialog: Dialog, result: String) {
                    mUser.name = result
                    tvNickName.text = result
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
            showEditDialog("修改签名1", startWord1, object : EditDialogCallBack {
                override fun onFinish(dialog: Dialog, result: String) {
                    startWord1 = result
                    tvSignature1.text = result
                    mUser.startWords = TextUtils.join(Constants.JOIN_STR, arrayOf(startWord1, startWord2, startWord3))
                }
            })
        }
        rlSignature2.setOnClickListener {
            showEditDialog("修改签名2", startWord2, object : EditDialogCallBack {
                override fun onFinish(dialog: Dialog, result: String) {
                    startWord2 = result
                    tvSignature2.text = result
                    mUser.startWords = TextUtils.join(Constants.JOIN_STR, arrayOf(startWord1, startWord2, startWord3))
                }
            })
        }
        rlSignature3.setOnClickListener {
            showEditDialog("修改签名3", startWord3, object : EditDialogCallBack {
                override fun onFinish(dialog: Dialog, result: String) {
                    startWord3 = result
                    tvSignature3.text = result
                    mUser.startWords = TextUtils.join(Constants.JOIN_STR, arrayOf(startWord1, startWord2, startWord3))
                }
            })
        }
        rlSelfIntro.setOnClickListener {
            showEditDialog("修改个人介绍", mUser.selfIntroduce, object : EditDialogCallBack {
                override fun onFinish(dialog: Dialog, result: String) {
                    mUser.selfIntroduce = result
                    tvSelfIntro.text = result
                }
            })
        }
        lavSwitch.setOnClickListener {
            if (mUser.sex != 0) {
                startAnima(lavSwitch, 1f, 0f)
                lavSwitch.progress = 0f
                mUser.sex = 0
            } else {
                startAnima(lavSwitch, 0f, 1f)
                lavSwitch.progress = 1f
                mUser.sex = 1
            }
        }
    }

    private fun commitData() {
        BmobUtils.updateUser(mUser)
        finish()
    }

    private fun showEditDialog(title: String, content: String?, editDialogCallBack: EditProfileActivity.EditDialogCallBack) {
        InputDialog.show(this, title, null, object : InputDialogOkButtonClickListener {
            override fun onClick(dialog: Dialog, inputText: String) {
                editDialogCallBack.onFinish(dialog, inputText)
                DialogSettings.unloadAllDialog()
            }
        }).setDefaultInputText(if (TextUtils.isEmpty(content)) "" else content)
    }

    interface EditDialogCallBack {
        fun onFinish(dialog: Dialog, result: String)
    }

    private fun initText() {
        tvNickName.text = mUser.name
        tvBirth.text = mUser.birth
        tvEmail.text = BUser.getCurrentUser().email
        var words = if (!TextUtils.isEmpty(mUser.startWords)) mUser.startWords.split(Constants.JOIN_STR) else listOf("")
        var size = words.size
        startWord1 = if (size > 0) words[0] else ""
        tvSignature1.text = startWord1
        startWord2 = if (size > 1) words[1] else ""
        tvSignature2.text = startWord2
        startWord3 = if (size > 2) words[2] else ""
        tvSignature3.text = startWord3
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

    fun startAnima(animationView: LottieAnimationView, start: Float, end: Float) {
        val animator = ValueAnimator.ofFloat(start, end)
        animator.addUpdateListener { animation -> animationView.progress = animation?.animatedValue as Float }
        animator.start()
    }
}
