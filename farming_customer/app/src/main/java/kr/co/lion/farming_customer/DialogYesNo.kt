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
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import kr.co.lion.farming_customer.activity.PointActivity
import kr.co.lion.farming_customer.databinding.DialogYesnoBinding
import kr.co.lion.farming_customer.viewmodel.CustomDialogViewModel

class DialogYesNo : DialogFragment() {

    lateinit var dialogYesnoBinding: DialogYesnoBinding
    lateinit var customDialogViewModel: CustomDialogViewModel
    lateinit var pointActivity: PointActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialogYesnoBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_yesno, container, false)
        customDialogViewModel = CustomDialogViewModel()
        dialogYesnoBinding.customDialogViewModel = customDialogViewModel
        dialogYesnoBinding.lifecycleOwner = this

        pointActivity = activity as PointActivity

        // 레이아웃 배경을 투명하게
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))

        settingDialog()

        return dialogYesnoBinding.root
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
        dialogYesnoBinding.apply {
            // 다이얼로그 제목
            customDialogViewModel?.textViewDialogYesNoSubject?.value = "탈퇴하기"

            // 다이얼로그 내용
            customDialogViewModel?.textViewDialogYesNoText?.value = "파밍을 탈퇴하시겠습니까?"

            // 확인 버튼
            buttonCustomDialogYesNoYes.setOnClickListener {
                dismiss()
                // Todo : 회원 탈퇴 / 리뷰 삭제 등등
            }

            // 취소 버튼
            buttonCustomDialogYesNoNo.setOnClickListener {
                dismiss()
            }
        }
    }


    // 디바이스 크기 구하기
    fun gettingDeviceSize() : Int {
        val windowManager = pointActivity.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        return size.x
    }
}
