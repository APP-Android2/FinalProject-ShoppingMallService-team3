package kr.co.lion.farming_customer.fragment.community

import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kr.co.lion.farming_customer.CommunityFragmentName
import kr.co.lion.farming_customer.DialogYesNo
import kr.co.lion.farming_customer.DialogYesNoInterface
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.community.CommunityActivity
import kr.co.lion.farming_customer.databinding.FragmentCommunityBottomSheetBinding
import kr.co.lion.farming_customer.viewmodel.community.CommunityViewModel


class CommunityBottomSheetFragment(var communityReadFragment: CommunityReadFragment) : BottomSheetDialogFragment(), DialogYesNoInterface {
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
                val dialog = DialogYesNo(
                    this@CommunityBottomSheetFragment,
                    "게시글을 삭제하시겠습니까?",
                    "한 번 삭제한 게시물은 복원할 수 없습니다.",
                    communityActivity,
                    0
                )
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
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

    }

    override fun onYesButtonClick(id: Int) {
        // 게시글 아이디로 삭제
    }

    override fun onYesButtonClick(activity: AppCompatActivity) {

    }
}