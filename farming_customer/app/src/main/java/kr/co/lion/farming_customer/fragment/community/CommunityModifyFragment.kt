package kr.co.lion.farming_customer.fragment.community

import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.farming_customer.CommunityFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.CommunityActivity
import kr.co.lion.farming_customer.databinding.FragmentCommunityModifyBinding
import kr.co.lion.farming_customer.databinding.RowCommunityModifyImageBinding
import kr.co.lion.farming_customer.viewmodel.community.CommunityAddModifyViewModel
import kr.co.lion.farming_customer.viewmodel.community.CommunityViewModel

class CommunityModifyFragment : Fragment() {
    lateinit var fragmentCommunityModifyBinding: FragmentCommunityModifyBinding
    lateinit var communityActivity: CommunityActivity
    lateinit var communityAddModifyViewModel: CommunityAddModifyViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentCommunityModifyBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_modify, container, false)
        communityAddModifyViewModel = CommunityAddModifyViewModel()
        fragmentCommunityModifyBinding.communityAddModifyViewModel = communityAddModifyViewModel
        fragmentCommunityModifyBinding.lifecycleOwner = this
        communityActivity = activity as CommunityActivity

        settingToolbar()
        settingInitializeInputForm()
        settingTextInputLayoutCommunityModifyType()
        settingViewPager2CommunityModifyImage()
        settingButtonCommunityModify()

        return fragmentCommunityModifyBinding.root
    }

    // 툴바 설정
    fun settingToolbar() {
        fragmentCommunityModifyBinding.apply {
            toolbarCommunityModify.apply {
                setNavigationIcon(R.drawable.back)
                setNavigationOnClickListener {
                    communityActivity.replaceFragment(CommunityFragmentName.COMMUNITY_READ_FRAGMENT, false, false, null)
                }
            }
        }
    }

    // 입력요소 초기화
    fun settingInitializeInputForm() {
        fragmentCommunityModifyBinding.apply {
            communityAddModifyViewModel?.textViewCommunityModifySubject?.value = "글 제목입니다"
            communityAddModifyViewModel?.textViewCommunityModifyContent?.value = "글 내용입니다 글 내용입니다 글 내용입니다 글 내용입니다 \n" +
                    "글 내용입니다 글 내용입니다 글 내용입니다 글 내용입니다 "
        }
    }

    // 드롭다운 설정
    fun settingTextInputLayoutCommunityModifyType() {
        fragmentCommunityModifyBinding.apply {
            // 드롭다운 설정
            val typeList = listOf("정보 게시판", "소통 게시판", "구인구직")
            val typeArrayAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner_community_modify, typeList)
            textViewCommunityModifyType.setAdapter(typeArrayAdapter)

            // 초기 선택 항목 설정
            textViewCommunityModifyType.setText("정보 게시판", false)
        }
    }

    // 커뮤니티 글 수정 이미지 뷰페이저 설정
    fun settingViewPager2CommunityModifyImage() {
        fragmentCommunityModifyBinding.apply {
            viewPager2CommunityModifyImage.apply {
                adapter = CommunityModifyImageViewPager2Adapter()
            }

            val circleIndicatorCommunityModify = circleIndicatorCommunityModify
            circleIndicatorCommunityModify.setViewPager(viewPager2CommunityModifyImage)
        }
    }

    // 커뮤니티 글 수정 이미지 뷰페이저 어댑터
    inner class CommunityModifyImageViewPager2Adapter : RecyclerView.Adapter<CommunityModifyFragment.CommunityModifyImageViewPager2Adapter.CommunityModifyImageViewHolder>() {
        inner class CommunityModifyImageViewHolder(rowCommunityModifyImageBinding: RowCommunityModifyImageBinding) :
            RecyclerView.ViewHolder(rowCommunityModifyImageBinding.root) {
            val rowCommunityModifyImageBinding: RowCommunityModifyImageBinding

            init {
                this.rowCommunityModifyImageBinding = rowCommunityModifyImageBinding

                this.rowCommunityModifyImageBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): CommunityModifyImageViewHolder {
            val rowCommunityModifyImageBinding =
                DataBindingUtil.inflate<RowCommunityModifyImageBinding>(
                    layoutInflater,
                    R.layout.row_community_modify_image,
                    parent,
                    false
                )
            val communityViewModel = CommunityViewModel()
            rowCommunityModifyImageBinding.communityViewModel = communityViewModel
            rowCommunityModifyImageBinding.lifecycleOwner = this@CommunityModifyFragment

            val communityModifyImageViewHolder =
                CommunityModifyImageViewHolder(rowCommunityModifyImageBinding)

            return communityModifyImageViewHolder
        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: CommunityModifyImageViewHolder, position: Int) {
            holder.rowCommunityModifyImageBinding.imageViewRowCommunityModify.setImageResource(R.color.grey_03)
        }
    }

    // 등록 버튼 활성화/비활성화
    fun settingButtonCommunityModify() {
        fragmentCommunityModifyBinding.apply {

            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val isTitleFilled = textViewCommunityModifySubject.text?.isNotBlank() ?: false
                    val isContentFilled = textViewCommunityModifyContent.text?.isNotBlank() ?: false

                    val isButtonEnabled = isTitleFilled && isContentFilled

                    buttonCommunityModify.isEnabled = isButtonEnabled

                    if (isButtonEnabled) {
                        // 활성 상태일 때
                        buttonCommunityModify.backgroundTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.green_main))
                        buttonCommunityModify.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    } else {
                        // 비활성 상태일 때
                        buttonCommunityModify.backgroundTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
                        // stroke 설정
                        buttonCommunityModify.strokeWidth = 1
                        buttonCommunityModify.strokeColor =
                            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.green_main))
                        // 텍스트 색상 설정
                        buttonCommunityModify.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.green_main)
                        )
                    }
                }
            }

            textViewCommunityModifySubject.addTextChangedListener(textWatcher)
            textViewCommunityModifyContent.addTextChangedListener(textWatcher)
        }
    }



}