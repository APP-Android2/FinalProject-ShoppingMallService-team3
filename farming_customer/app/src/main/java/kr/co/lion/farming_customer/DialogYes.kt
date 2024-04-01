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
import kr.co.lion.farming_customer.databinding.DialogYesBinding
import kr.co.lion.farming_customer.viewmodel.CustomDialogViewModel

class DialogYes : DialogFragment() {

    lateinit var dialogYesBinding: DialogYesBinding
    lateinit var customDialogViewModel: CustomDialogViewModel
    lateinit var pointActivity: PointActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        dialogYesBinding = DataBindingUtil.inflate(inflater, R.layout.dialog_yes, container, false)
        customDialogViewModel = CustomDialogViewModel()
        dialogYesBinding.customDialogViewModel = customDialogViewModel
        dialogYesBinding.lifecycleOwner = this

        pointActivity = activity as PointActivity

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
            customDialogViewModel?.textViewDialogYesSubject?.value = "포인트 이용안내"

            // 다이얼로그 내용
            customDialogViewModel?.textViewDialogYesText?.value = "포인트 사용 기간은 적입일 이후 90일 까지이며,\n" +
                    "90일 이후 잔여 포인트는 자동 소멸 됩니다."

            // 다이얼로그 내용2
            customDialogViewModel?.textViewDialogYesText2?.value = "*포인트를 사용한 상품을 포인트 기간 내에 취소하시면\n" +
                    "환급되오나, 기간이 지난 이후 취소시 환급되지 않습니다."

            // 확인 버튼
            buttonCustomDialogYes.setOnClickListener {
                dismiss()
                // Todo : 회원 탈퇴 / 리뷰 삭제 등등
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