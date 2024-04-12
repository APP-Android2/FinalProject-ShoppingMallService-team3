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
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import kr.co.lion.farming_customer.databinding.DialogYesnoBinding
import kr.co.lion.farming_customer.viewmodel.CustomDialogViewModel

class DialogYesNo(
    dialogYesNoInterface: DialogYesNoInterface,
    subject: String?,
    content: String,
    activity: AppCompatActivity,
    position: Int
) : DialogFragment() {

    lateinit var dialogYesnoBinding: DialogYesnoBinding
    lateinit var customDialogViewModel: CustomDialogViewModel

    var subject = subject
    var content = content
    var activit = activity
    var dialogYesNoInterface = dialogYesNoInterface
    var position = position


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialogYesnoBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_yesno, container, false)
        customDialogViewModel = CustomDialogViewModel()
        dialogYesnoBinding.customDialogViewModel = customDialogViewModel
        dialogYesnoBinding.lifecycleOwner = this

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
            if (subject == null) {
                textViewDialogYesNoSubject.isVisible = false
            } else {
                customDialogViewModel?.textViewDialogYesNoSubject?.value = subject
            }

            // 다이얼로그 내용
            customDialogViewModel?.textViewDialogYesNoText?.value = content

            // 확인 버튼
            buttonCustomDialogYesNoYes.setOnClickListener {
                dialogYesNoInterface?.onYesButtonClick(position)
                dismiss()
            }

            // 취소 버튼
            buttonCustomDialogYesNoNo.setOnClickListener {
                dismiss()
            }
        }
    }


    // 디바이스 크기 구하기
    fun gettingDeviceSize() : Int {
        val windowManager = activit.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)

        return size.x
    }
}

interface DialogYesNoInterface {
    fun onYesButtonClick(id: Int)
}
