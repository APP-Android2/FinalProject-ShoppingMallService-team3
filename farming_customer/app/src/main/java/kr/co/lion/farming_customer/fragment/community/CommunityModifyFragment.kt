package kr.co.lion.farming_customer.fragment.community

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.CommunityFragmentName
import kr.co.lion.farming_customer.DialogYesNo
import kr.co.lion.farming_customer.DialogYesNoInterface
import kr.co.lion.farming_customer.PostStatus
import kr.co.lion.farming_customer.PostType
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.community.CommunityActivity
import kr.co.lion.farming_customer.dao.CommunityPostDao
import kr.co.lion.farming_customer.databinding.FragmentCommunityModifyBinding
import kr.co.lion.farming_customer.databinding.RowCommunityModifyImageBinding
import kr.co.lion.farming_customer.model.CommunityModel
import kr.co.lion.farming_customer.viewmodel.community.CommunityAddModifyViewModel
import kr.co.lion.farming_customer.viewmodel.community.CommunityViewModel
import java.text.SimpleDateFormat
import java.util.Date

class CommunityModifyFragment : Fragment(), DialogYesNoInterface {
    lateinit var fragmentCommunityModifyBinding: FragmentCommunityModifyBinding
    lateinit var communityActivity: CommunityActivity
    lateinit var communityAddModifyViewModel: CommunityAddModifyViewModel

    var model: CommunityModel? = null

    // 현재 글 번호를 담을 변수
    var postIdx = 0
    // 게시글 작성 날짜
    var postRegDt = ""

    // 이미지 저장용 리스트
    var imageCommunityStringList = mutableListOf<String>()

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentCommunityModifyBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_modify, container, false)
        communityAddModifyViewModel = CommunityAddModifyViewModel()
        fragmentCommunityModifyBinding.communityAddModifyViewModel = communityAddModifyViewModel
        fragmentCommunityModifyBinding.lifecycleOwner = this
        communityActivity = activity as CommunityActivity

        // 글 번호를 받는다.
        postIdx = arguments?.getInt("postIdx")!!

        settingToolbar()
        settingTextViewCommunityModifyInput()
        settingButtonCommunityModify()

        return fragmentCommunityModifyBinding.root
    }

    // 툴바 설정
    fun settingToolbar() {
        fragmentCommunityModifyBinding.apply {
            toolbarCommunityModify.apply {
                setNavigationIcon(R.drawable.back)
                setNavigationOnClickListener {
                    communityActivity.removeFragment(CommunityFragmentName.COMMUNITY_MODIFY_FRAGMENT)
                }
            }
        }
    }

    // 입력 요소에 값을 넣어준다.
    fun settingTextViewCommunityModifyInput() {

        // 입력 요소에 띄어쓰기를 넣어준다.
        communityAddModifyViewModel.textViewCommunityModifySubject.value = " "
        communityAddModifyViewModel.textViewCommunityModifyContent.value = " "


        CoroutineScope(Dispatchers.Main).launch {
            // 현재 글 번호에 해당하는 글 데이터를 가져온다.
            val communityPostModel = CommunityPostDao.selectCommunityPostData(postIdx)

            if (communityPostModel?.postImages != null) {
                imageCommunityStringList = communityPostModel?.postImages!!
            } else {
                fragmentCommunityModifyBinding.viewPager2CommunityModifyImage.visibility = View.GONE
                fragmentCommunityModifyBinding.circleIndicatorCommunityModify.visibility = View.GONE
            }

            // 가져온 데이터를 보여준다.
            communityAddModifyViewModel.textViewCommunityModifySubject.value =
                communityPostModel?.postTitle
            communityAddModifyViewModel.textViewCommunityModifyContent.value =
                communityPostModel?.postContent

            if (communityPostModel?.postType == "정보") {
                communityAddModifyViewModel.communityPostModifyType.value = "정보 게시판"
            } else if (communityPostModel?.postType == "소통") {
                communityAddModifyViewModel.communityPostModifyType.value = "소통 게시판"
            } else {
                communityAddModifyViewModel.communityPostModifyType.value = "구인구직"
            }

            postRegDt = communityPostModel?.postRegDt!!

            settingTextInputLayoutCommunityModifyType()

            settingViewPager2CommunityModifyImage()
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
            textViewCommunityModifyType.setText( communityAddModifyViewModel?.communityPostModifyType?.value, false)
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
    inner class CommunityModifyImageViewPager2Adapter :
        RecyclerView.Adapter<CommunityModifyFragment.CommunityModifyImageViewPager2Adapter.CommunityModifyImageViewHolder>() {
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

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int ): CommunityModifyImageViewHolder {
            val rowCommunityModifyImageBinding = DataBindingUtil.inflate<RowCommunityModifyImageBinding>(layoutInflater, R.layout.row_community_modify_image, parent, false)
            val communityViewModel = CommunityViewModel()
            rowCommunityModifyImageBinding.communityViewModel = communityViewModel
            rowCommunityModifyImageBinding.lifecycleOwner = this@CommunityModifyFragment

            val communityModifyImageViewHolder = CommunityModifyImageViewHolder(rowCommunityModifyImageBinding)

            return communityModifyImageViewHolder
        }

        override fun getItemCount(): Int {
            return imageCommunityStringList.size
        }

        override fun onBindViewHolder(holder: CommunityModifyImageViewHolder, position: Int) {
            CoroutineScope(Dispatchers.Main).launch {
                CommunityPostDao.gettingCommunityPostImage(communityActivity, imageCommunityStringList[position], holder.rowCommunityModifyImageBinding.imageViewRowCommunityModify)
            }
        }
    }

    // 등록 버튼 활성화/비활성화
    fun settingButtonCommunityModify() {
        fragmentCommunityModifyBinding.apply {

            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                }

                override fun afterTextChanged(s: Editable?) {
                    val isTitleFilled = textViewCommunityModifySubject.text?.isNotBlank() ?: false
                    val isContentFilled = textViewCommunityModifyContent.text?.isNotBlank() ?: false

                    val isButtonEnabled = isTitleFilled && isContentFilled

                    buttonCommunityModify.isEnabled = isButtonEnabled

                    if (isButtonEnabled) {
                        // 활성 상태일 때
                        buttonCommunityModify.backgroundTintList = ColorStateList.valueOf(
                            ContextCompat.getColor(requireContext(), R.color.green_main)
                        )
                        buttonCommunityModify.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.white)
                        )
                    } else {
                        // 비활성 상태일 때
                        buttonCommunityModify.backgroundTintList =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(requireContext(), R.color.white)
                            )
                        // stroke 설정
                        buttonCommunityModify.strokeWidth = 1
                        buttonCommunityModify.strokeColor =
                            ColorStateList.valueOf(
                                ContextCompat.getColor(requireContext(), R.color.green_main)
                            )
                        // 텍스트 색상 설정
                        buttonCommunityModify.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.green_main)
                        )
                    }
                }
            }

            textViewCommunityModifySubject.addTextChangedListener(textWatcher)
            textViewCommunityModifyContent.addTextChangedListener(textWatcher)

            buttonCommunityModify.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    model = generatingPostObject()
                    // 글 데이터 업로드
                    updateCommunityPostData(model!!.postIdx)
                    val dialog = DialogYesNo(this@CommunityModifyFragment, null, "게시글이 수정되었습니다", communityActivity, null)
                    dialog.show(communityActivity.supportFragmentManager, "DialogYesNo")
                }

            }
        }
    }

    override fun onYesButtonClick(id: Int) {

    }

    override fun onYesButtonClick(activity: AppCompatActivity) {
       communityActivity.finish()

    }

    // 게시글 수정 객체 생성
    suspend fun generatingPostObject(): CommunityModel {

        var communityModel = CommunityModel()

        val job1 = CoroutineScope(Dispatchers.Main).launch {
            // 게시글 시퀀스 값을 가져온다.
            val communityPostSequence = CommunityPostDao.getCommunityPostSequence()
            // 게시글 시퀀스 값을 업데이트한다.
            CommunityPostDao.updateCommunityPostSequence(communityPostSequence)

            // 업로드할 정보를 담아준다.
            communityModel.postIdx = communityPostSequence
            communityModel.postTitle = communityAddModifyViewModel.textViewCommunityModifySubject.value!!
            if (communityAddModifyViewModel.communityPostAddType.value == "정보 게시판") {
                communityModel.postType = PostType.TYPE_INFORMATION.str
            } else if (communityAddModifyViewModel.communityPostAddType.value == "소통 게시판") {
                communityModel.postType = PostType.TYPE_SOCIAL.str
            } else {
                communityModel.postType = PostType.TYPE_JOB.str
            }
            communityModel.postContent = communityAddModifyViewModel.textViewCommunityModifyContent.value!!
            communityModel.postLikeCnt = 0
            communityModel.postCommentCnt = 0
            communityModel.postImages = mutableListOf<String>()
            communityModel.postUserIdx = 1

            val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd")
            communityModel.postRegDt = postRegDt
            communityModel.postModDt = simpleDateFormat.format(Date())
            communityModel.postStatus = PostStatus.POST_STATUS_NORMAL.number

        }
        job1.join()

        return communityModel
    }

    // 게시글 수정 작성
    fun updateCommunityPostData(postIdx: Int) {
        CoroutineScope(Dispatchers.Main).launch {
            CommunityPostDao.updateCommunityPostData(model!!)
            CommunityPostDao.updateCommunityPostStatus(postIdx, PostStatus.POST_STATUS_MODIFY)
        }
    }

}



