package kr.co.lion.farming_customer.fragment.community

import android.app.Dialog
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
import kr.co.lion.farming_customer.CommunityFragmentName
import kr.co.lion.farming_customer.DialogYesNo
import kr.co.lion.farming_customer.FarmingLifeFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.CommunityActivity
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.databinding.FragmentCommunityBottomSheetBinding
import kr.co.lion.farming_customer.fragment.FarmingLifeBottomSheetFragment
import kr.co.lion.farming_customer.fragment.FarmingLifeFragment
import kr.co.lion.farming_customer.viewmodel.CommunityViewModel
import kr.co.lion.farming_customer.viewmodel.FarmingLifeBottomSheetViewModel


class CommunityBottomSheetFragment(var communityReadFragment: CommunityReadFragment) : BottomSheetDialogFragment() {
    lateinit var fragmentCommunityBottomSheetBinding: FragmentCommunityBottomSheetBinding
    lateinit var communityActivity: CommunityActivity
    lateinit var communityViewModel: CommunityViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentCommunityBottomSheetBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_bottom_sheet, container, false)
        communityViewModel = CommunityViewModel()
        fragmentCommunityBottomSheetBinding.communityViewModel = communityViewModel
        fragmentCommunityBottomSheetBinding.lifecycleOwner = this

        communityActivity = activity as CommunityActivity

        settingCommunityBottom()

        return fragmentCommunityBottomSheetBinding.root
    }


    // 수정하기 / 삭제하기
    fun settingCommunityBottom() {
        fragmentCommunityBottomSheetBinding.apply {
            // 수정하기
            buttonCommunityReadModify.setOnClickListener {
                dismiss()
                communityActivity.replaceFragment(CommunityFragmentName.COMMUNITY_MODIFY_FRAGMENT, false, false, null)
            }
            // 삭제하기
            buttonCommunityReadDelete.setOnClickListener {
                val dialog = DialogYesNo("게시글을 삭제하시겠습니까?", "한 번 삭제한 게시물은 복원할 수 없습니다.", communityActivity)
                dialog.show(communityActivity.supportFragmentManager, "DialogYesNo")
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
        return (getWindowHeight() * 0.13).toInt()
    }

    // 사용자 단말기 액정의 길이를 구해 반환하는 메서드
    fun getWindowHeight() : Int {
        // 화면 크기 정보를 담을 배열 객체
        val displayMetrics = DisplayMetrics()
        // 액정의 가로 세로 길이 정보를 담아준다.
        communityActivity.windowManager.defaultDisplay.getMetrics(displayMetrics)
        // 세로길이를 반환해준다.
        return displayMetrics.heightPixels
    }

}