package kr.co.lion.farming_customer.fragment.farmingLife

import android.app.Dialog
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.NonDisposableHandle.parent
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.activity.farmingLife.FarmingLifeActivity
import kr.co.lion.farming_customer.databinding.FragmentBottomSheetFarmReservBinding
import kr.co.lion.farming_customer.viewmodel.farmingLife.BottomSheetFarmReservViewModel

class BottomSheetFarmReservFragment : BottomSheetDialogFragment() {
    lateinit var fragmentBottomSheetDialogFragment: FragmentBottomSheetFarmReservBinding
    lateinit var farmingLifeActivity : FarmingLifeActivity
    lateinit var bottomSheetFarmReservViewModel: BottomSheetFarmReservViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentBottomSheetDialogFragment = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_bottom_sheet_farm_reserv, container, false)
        bottomSheetFarmReservViewModel = BottomSheetFarmReservViewModel()
        fragmentBottomSheetDialogFragment.bottomSheetFarmReservViewModel = bottomSheetFarmReservViewModel
        fragmentBottomSheetDialogFragment.lifecycleOwner = this
        farmingLifeActivity = activity as FarmingLifeActivity

        settingData()
        settingEvent()

        return fragmentBottomSheetDialogFragment.root
    }

    private fun settingEvent() {
        fragmentBottomSheetDialogFragment.apply {
            // 카트 담기 버튼
            buttonReservCart.setOnClickListener {
                dismiss()
            }
            // 예약하기 버튼
            buttonReservReserv.setOnClickListener {
                dismiss()
            }
        }
    }

    private fun settingData() {
        bottomSheetFarmReservViewModel!!.apply {
            textViewReserv_farmName.value = "파밍이네 주말농장"
            textViewReserv_remainArea.value = "10구획"
            textViewReserv_price.value = "10,000원"
        }
    }

    // 다이얼로그가 만들어질 때 자동으로 호출되는 메서드
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // 다이얼로그를 받는다.
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener{
            val bottomSheetDialog = it as BottomSheetDialog
            // 높이를 설정한다.
            setBottomSheetHeight(bottomSheetDialog)
        }
        return dialog
    }

    // BottomSheet의 높이를 설정해준다.
    fun setBottomSheetHeight(bottomSheetDialog: BottomSheetDialog){
        // BottomSheet의 기본 뷰 객체를 가져온다
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)!!
        // BottomSheet 높이를 설정할 수 있는 객체를 생성한다.
        val behavior = BottomSheetBehavior.from(bottomSheet)
        // 높이를 설정한다.
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

    }

}