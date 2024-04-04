package kr.co.lion.farming_customer

import android.content.Context
import android.graphics.Color
import android.graphics.Point
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import kr.co.lion.farming_customer.databinding.DialogYesBinding
import kr.co.lion.farming_customer.viewmodel.CustomDialogViewModel

class DialogYes(subject: String?, content: String, content2: String?, activity: AppCompatActivity ) : DialogFragment() {

    lateinit var dialogYesBinding: DialogYesBinding
    lateinit var customDialogViewModel: CustomDialogViewModel

    var subject = subject
    var content = content
    var content2 = content2
    var activity = activity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialogYesBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_yes, container, false)
        customDialogViewModel = CustomDialogViewModel()
        dialogYesBinding.customDialogViewModel = customDialogViewModel
        dialogYesBinding.lifecycleOwner = this

        // 레이아웃 배경을 투명하게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        settingDialog()

        return dialogYesBinding.root
    }

    override fun onResume() {
        super.onResume()
        val params: ViewGroup.LayoutParams? = dialog?.window?.attributes
        val deviceWidth = gettingDeviceSize()
        params?.width = (deviceWidth * 0.9).toInt()
        dialog?.window?.attributes = params as WindowManager.LayoutParams
    }


    // 버튼 처리
    fun settingDialog() {
        dialogYesBinding.apply {
            // 다이얼로그 제목
            customDialogViewModel?.textViewDialogYesSubject?.value = subject

            // 다이얼로그 내용
            customDialogViewModel?.textViewDialogYesText?.value = content

            // 다이얼로그 내용2
            customDialogViewModel?.textViewDialogYesText2?.value = content2

            // 확인 버튼
            buttonCustomDialogYes.setOnClickListener {
                dismiss()
                // Todo : 회원 탈퇴 / 리뷰 삭제 등등
            }

        }
    }


    // 디바이스 크기 구하기
    fun gettingDeviceSize() : Int {
        val windowManager = activity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        return size.x
    }
}