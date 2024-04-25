package kr.co.lion.farming_customer.fragment.review

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.DialogYesNo
import kr.co.lion.farming_customer.DialogYesNoInterface
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.ReviewState
import kr.co.lion.farming_customer.activity.review.ReviewActivity
import kr.co.lion.farming_customer.dao.myPageReview.MyPageReviewDao
import kr.co.lion.farming_customer.databinding.FragmentReviewTabFarmBinding
import kr.co.lion.farming_customer.databinding.RowReviewHistoryFarmBinding
import kr.co.lion.farming_customer.databinding.RowReviewHistoryImageFarmBinding
import kr.co.lion.farming_customer.model.myPageReview.ReviewModel
import kr.co.lion.farming_customer.viewmodel.review.MyPageReviewViewModel

class ReviewTabFarmFragment : Fragment(), DialogYesNoInterface {
    lateinit var fragmentReviewTabFarmBinding: FragmentReviewTabFarmBinding
    lateinit var reviewActivity: ReviewActivity
    lateinit var myPageReviewViewModel: MyPageReviewViewModel

    var farmReviewList = mutableListOf<ReviewModel>()
    private var reviewIdx = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentReviewTabFarmBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_review_tab_farm, container, false)
        myPageReviewViewModel = MyPageReviewViewModel()
        fragmentReviewTabFarmBinding.myPageReviewViewModel = myPageReviewViewModel
        fragmentReviewTabFarmBinding.lifecycleOwner = this

        reviewActivity = activity as ReviewActivity

        settingRecyclerViewReviewTabFarm()
        gettingFarmReviewData()

        return fragmentReviewTabFarmBinding.root
    }

    // 리뷰 주말농장 탭 리사이클러뷰 설정
    fun settingRecyclerViewReviewTabFarm() {
        fragmentReviewTabFarmBinding.apply {
            recyclerViewReviewTabFarm.apply {
                adapter = ReviewTabFarmRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(reviewActivity)
                val deco = MaterialDividerItemDecoration(reviewActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun gettingFarmReviewData() {
        CoroutineScope(Dispatchers.Main).launch {
            farmReviewList = MyPageReviewDao.gettingFarmReviewList()

            fragmentReviewTabFarmBinding.recyclerViewReviewTabFarm.adapter?.notifyDataSetChanged()

            val reviewCount = farmReviewList.size
            fragmentReviewTabFarmBinding.myPageReviewViewModel?.textViewReviewTabFarmCount?.value = "내가 쓴 리뷰 총 ${reviewCount}개"
        }
    }

    // 리뷰 주말농장 탭 리사이클러뷰 어댑터 설정
    inner class ReviewTabFarmRecyclerViewAdapter : RecyclerView.Adapter<ReviewTabFarmRecyclerViewAdapter.ReviewTabFarmViewHolder>() {
        inner class ReviewTabFarmViewHolder(rowReviewHistoryFarmBinding: RowReviewHistoryFarmBinding) : RecyclerView.ViewHolder(rowReviewHistoryFarmBinding.root) {
            val rowReviewHistoryFarmBinding: RowReviewHistoryFarmBinding

            init {
                this.rowReviewHistoryFarmBinding = rowReviewHistoryFarmBinding

                this.rowReviewHistoryFarmBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewTabFarmViewHolder {
            val rowReviewHistoryFarmBinding = DataBindingUtil.inflate<RowReviewHistoryFarmBinding>(layoutInflater, R.layout.row_review_history_farm, parent, false)
            val myPageReviewViewModel = MyPageReviewViewModel()
            rowReviewHistoryFarmBinding.myPageReviewViewModel = myPageReviewViewModel
            rowReviewHistoryFarmBinding.lifecycleOwner = this@ReviewTabFarmFragment

            val reviewTabFarmViewHolder = ReviewTabFarmViewHolder(rowReviewHistoryFarmBinding)

            return reviewTabFarmViewHolder
        }

        override fun getItemCount(): Int {
            return farmReviewList.size
        }

        override fun onBindViewHolder(holder: ReviewTabFarmViewHolder, position: Int) {
            val farmImageList = farmReviewList[position].review_images
            reviewIdx = farmReviewList[position].review_idx

            holder.rowReviewHistoryFarmBinding.myPageReviewViewModel?.textViewRowReviewTabFarmDate?.value = farmReviewList[position].review_reg_dt
            holder.rowReviewHistoryFarmBinding.myPageReviewViewModel?.textViewRowReviewTabFarmName?.value = farmReviewList[position].review_title
            holder.rowReviewHistoryFarmBinding.myPageReviewViewModel?.textViewRowReviewTabFarmText?.value = farmReviewList[position].review_content
            holder.rowReviewHistoryFarmBinding.myPageReviewViewModel?.textViewRowReviewTabFarmLabel?.value = farmReviewList[position].review_option
            holder.rowReviewHistoryFarmBinding.ratingBarRowReviewHistoryFarm.rating =
                farmReviewList[position].review_rate.toFloat()

            holder.rowReviewHistoryFarmBinding.buttonReviewTabFarmDelete.setOnClickListener {
                val dialog = DialogYesNo(
                    this@ReviewTabFarmFragment, null, "리뷰를 삭제하시면 재작성이 불가합니다.\n" +
                            "삭제하시겠습니까?", reviewActivity, position
                )
                dialog.show(this@ReviewTabFarmFragment?.parentFragmentManager!!, "DialogYesNo")
            }

            // 리뷰 주말농장 탭 이미지 리사이클러뷰 설정
            holder.rowReviewHistoryFarmBinding.recyclerViewReviewImageFarm.apply {
                adapter = ReviewImageFarmRecyclerViewAdapter(farmImageList)
                layoutManager = LinearLayoutManager(reviewActivity, LinearLayoutManager.HORIZONTAL, false)
            }
        }

        // 리뷰 주말농장 탭 이미지 리사이클러뷰 설정
        inner class ReviewImageFarmRecyclerViewAdapter(farmImageList: MutableList<String>) : RecyclerView.Adapter<ReviewImageFarmRecyclerViewAdapter.ReviewImageFarmViewHolder>() {
            var farmImages = farmImageList
            inner class ReviewImageFarmViewHolder(rowReviewHistoryImageFarmBinding: RowReviewHistoryImageFarmBinding) : RecyclerView.ViewHolder(rowReviewHistoryImageFarmBinding.root) {
                val rowReviewHistoryImageFarmBinding: RowReviewHistoryImageFarmBinding

                init {
                    this.rowReviewHistoryImageFarmBinding = rowReviewHistoryImageFarmBinding

                    this.rowReviewHistoryImageFarmBinding.root.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ReviewImageFarmViewHolder {
                val rowReviewHistoryImageFarmBinding = DataBindingUtil.inflate<RowReviewHistoryImageFarmBinding>(layoutInflater, R.layout.row_review_history_image_farm, parent, false)
                val myPageReviewViewModel = MyPageReviewViewModel()
                rowReviewHistoryImageFarmBinding.myPageReviewViewModel = myPageReviewViewModel
                rowReviewHistoryImageFarmBinding.lifecycleOwner = this@ReviewTabFarmFragment

                val reviewImageFarmViewHolder = ReviewImageFarmViewHolder(rowReviewHistoryImageFarmBinding)

                return reviewImageFarmViewHolder
            }

            override fun getItemCount(): Int {
                return farmImages.size
            }

            override fun onBindViewHolder(holder: ReviewImageFarmViewHolder, position: Int) {
                CoroutineScope(Dispatchers.Main).launch {
                    MyPageReviewDao.gettingCropReviewPostImage(requireContext(), farmImages[position], holder.rowReviewHistoryImageFarmBinding.imageViewRowReviewTabFarm)
                }
            }
        }
    }

    override fun onYesButtonClick(id: Int) {
        fragmentReviewTabFarmBinding.recyclerViewReviewTabFarm.adapter!!.notifyItemRemoved(id)

        CoroutineScope(Dispatchers.Main).launch {
            // 글 상태를 삭제 상태로 변경한다.
            Log.e("reviewIdxddddd", reviewIdx.toString())
            MyPageReviewDao.updateReviewState(reviewIdx, ReviewState.REVIEW_STATE_REMOVE)
        }
    }

    override fun onYesButtonClick(activity: AppCompatActivity) {

    }
}