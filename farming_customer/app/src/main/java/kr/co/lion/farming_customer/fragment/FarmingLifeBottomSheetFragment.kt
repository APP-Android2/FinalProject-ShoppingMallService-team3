package kr.co.lion.farming_customer.fragment

import android.app.Dialog
import android.content.DialogInterface
import android.graphics.Color
import android.os.Bundle
import android.util.DisplayMetrics
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.co.lion.farming_customer.FarmingLifeFragmentName
import kr.co.lion.farming_customer.MainFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.databinding.FragmentFarmingLifeBinding
import kr.co.lion.farming_customer.databinding.FragmentFarmingLifeBottomSheetBinding
import kr.co.lion.farming_customer.viewmodel.FarmingLifeBottomSheetViewModel

class FarmingLifeBottomSheetFragment(var farmingLifeFragment: FarmingLifeFragment) : BottomSheetDialogFragment() {
    lateinit var fragmentFarmingLifeBottomSheetBinding: FragmentFarmingLifeBottomSheetBinding
    lateinit var mainActivity: MainActivity
    lateinit var farmingLifeBottomSheetViewModel: FarmingLifeBottomSheetViewModel

    var selectedButton:Int = -1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentFarmingLifeBottomSheetBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_farming_life_bottom_sheet, container, false)
        farmingLifeBottomSheetViewModel = FarmingLifeBottomSheetViewModel()
        fragmentFarmingLifeBottomSheetBinding.farmingLifeBottomSheetViewModel = farmingLifeBottomSheetViewModel
        fragmentFarmingLifeBottomSheetBinding.lifecycleOwner = this

        mainActivity = activity as MainActivity

        settingInit()
        settingFarmingLifeBottom()

        if (selectedButton != -1) {
            changeButtonState(selectedButton)
        }

        return fragmentFarmingLifeBottomSheetBinding.root
    }


    fun settingInit() {
        fragmentFarmingLifeBottomSheetBinding.apply {
            buttonFarmingLifeBottomCommunity.isSelected = true
        }
    }

    fun changeButtonState(buttonId: Int) {
        selectedButton = buttonId

        fragmentFarmingLifeBottomSheetBinding.apply {
            buttonFarmingLifeBottomCommunity.isSelected = false
            buttonFarmingLifeBottomFarmActivity.isSelected = false
            buttonFarmingLifeBottomRental.isSelected = false
        }

        when(buttonId) {
            R.id.buttonFarmingLifeBottomCommunity -> {
                fragmentFarmingLifeBottomSheetBinding.buttonFarmingLifeBottomCommunity.isSelected = true
            }
            R.id.buttonFarmingLifeBottomFarmActivity-> {
                fragmentFarmingLifeBottomSheetBinding.buttonFarmingLifeBottomFarmActivity.isSelected = true
            }
            R.id.buttonFarmingLifeBottomRental -> {
                fragmentFarmingLifeBottomSheetBinding.buttonFarmingLifeBottomRental.isSelected = true
            }
        }
    }

    // 파밍생활 커뮤니티 / 주말농장 및 체험활동 / 농기구 임대
    fun settingFarmingLifeBottom() {
        fragmentFarmingLifeBottomSheetBinding.apply {

            // 커뮤니티 프래그먼트
            buttonFarmingLifeBottomCommunity.setOnClickListener {
                changeButtonState(R.id.buttonFarmingLifeBottomCommunity)
                dismiss()
                farmingLifeFragment.replaceFragment(FarmingLifeFragmentName.COMMUNITY_FRAGMENT, false, false, null)
            }
            // 주말농장 및 체험활동 프래그먼트
            buttonFarmingLifeBottomFarmActivity.setOnClickListener {
                changeButtonState(R.id.buttonFarmingLifeBottomFarmActivity)
                dismiss()

            }
            // 농기구 임대 프래그먼트
            buttonFarmingLifeBottomRental.setOnClickListener {
                changeButtonState(R.id.buttonFarmingLifeBottomRental)
                dismiss()

            }
        }
    }

    // 다이얼로그가 만들어질 때 자동으로 호출되는 메서드
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // 다이얼로그를 받는다.
        val dialog = super.onCreateDialog(savedInstanceState)
        // 다이얼로그가 보일때 동작하는 리스너
        dialog.setOnShowListener {

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
        layoutParams.height = getBottomSheetDialogHeight()
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

    }

    // BottomSheet의 높이를 구한다(화면 액정의 85% 크기)
    fun getBottomSheetDialogHeight() : Int {
        return (getWindowHeight() * 0.19).toInt()
    }

    // 사용자 단말기 액정의 길이를 구해 반환하는 메서드
    fun getWindowHeight() : Int {
        // 화면 크기 정보를 담을 배열 객체
        val displayMetrics = DisplayMetrics()
        // 액정의 가로 세로 길이 정보를 담아준다.
        mainActivity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        // 세로길이를 반환해준다.
        return displayMetrics.heightPixels
    }
}