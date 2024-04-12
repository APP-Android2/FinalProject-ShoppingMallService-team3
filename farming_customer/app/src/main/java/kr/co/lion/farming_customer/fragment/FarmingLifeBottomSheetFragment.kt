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
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.co.lion.farming_customer.CommunityFragmentName
import kr.co.lion.farming_customer.MainFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.databinding.FragmentFarmingLifeBottomSheetBinding
import kr.co.lion.farming_customer.fragment.community.CommunityFragment
import kr.co.lion.farming_customer.fragment.community.FarmActivityFragment
import kr.co.lion.farming_customer.fragment.community.RentalFragment
import kr.co.lion.farming_customer.viewmodel.FarmingLifeBottomSheetViewModel

class FarmingLifeBottomSheetFragment(var name: MainFragmentName) : BottomSheetDialogFragment() {
    lateinit var fragmentFarmingLifeBottomSheetBinding: FragmentFarmingLifeBottomSheetBinding
    lateinit var mainActivity: MainActivity
    lateinit var farmingLifeBottomSheetViewModel: FarmingLifeBottomSheetViewModel

    var fragmentName = name

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentFarmingLifeBottomSheetBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_farming_life_bottom_sheet, container, false)
        farmingLifeBottomSheetViewModel = FarmingLifeBottomSheetViewModel()
        fragmentFarmingLifeBottomSheetBinding.farmingLifeBottomSheetViewModel = farmingLifeBottomSheetViewModel
        fragmentFarmingLifeBottomSheetBinding.lifecycleOwner = this

        mainActivity = activity as MainActivity

        //settingInit()
        //settingInitState(FarmingLifeFragmentName.COMMUNITY_FRAGMENT)
        //settingFarmingLifeBottom()

        return fragmentFarmingLifeBottomSheetBinding.root
    }

    fun settingInitState(name: MainFragmentName) {
        fragmentFarmingLifeBottomSheetBinding.apply {
            when(name) {
                MainFragmentName.COMMUNITY_FRAGMENT -> {
                    buttonFarmingLifeBottomCommunity.apply {
                        setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.green_main))
                        setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                    }
                }
                MainFragmentName.FARM_ACTIVITY_FRAGMENT -> {
                    buttonFarmingLifeBottomFarmActivity.apply {
                        setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.green_main))
                        setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                    }
                }
                MainFragmentName.RENTAL_FRAGMENT -> {
                    buttonFarmingLifeBottomRental.apply {
                        setBackgroundColor(ContextCompat.getColor(requireContext(),R.color.green_main))
                        setTextColor(ContextCompat.getColor(requireContext(),R.color.white))
                    }
                }
                else -> 0
            }
        }
    }


    // 파밍생활 커뮤니티 / 주말농장 및 체험활동 / 농기구 임대
    fun settingFarmingLifeBottom() {
        fragmentFarmingLifeBottomSheetBinding.apply {

            // 커뮤니티 프래그먼트
            buttonFarmingLifeBottomCommunity.setOnClickListener {
                dismiss()
                mainActivity.replaceFragment(MainFragmentName.COMMUNITY_FRAGMENT, false, false, null)
                //changeButtonState(R.id.buttonFarmingLifeBottomFarmActivity)
            }
            // 주말농장 및 체험활동 프래그먼트
            buttonFarmingLifeBottomFarmActivity.setOnClickListener {
                dismiss()
                mainActivity.replaceFragment(MainFragmentName.FARM_ACTIVITY_FRAGMENT, false, false, null)
                //changeButtonState(R.id.buttonFarmingLifeBottomFarmActivity)
            }
            // 농기구 임대 프래그먼트
            buttonFarmingLifeBottomRental.setOnClickListener {
                dismiss()
                mainActivity.replaceFragment(MainFragmentName.RENTAL_FRAGMENT, false, false, null)
                //changeButtonState(R.id.buttonFarmingLifeBottomRental)
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

            settingFarmingLifeBottom()
            settingInitState(fragmentName)
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