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
import kr.co.lion.farming_customer.databinding.FragmentReviewTabCropBinding
import kr.co.lion.farming_customer.databinding.RowReviewHistoryCropBinding
import kr.co.lion.farming_customer.databinding.RowReviewHistoryImageCropBinding
import kr.co.lion.farming_customer.model.myPageReview.ReviewModel
import kr.co.lion.farming_customer.viewmodel.review.MyPageReviewViewModel


class ReviewTabCropFragment : Fragment(), DialogYesNoInterface {
    lateinit var fragmentReviewTabCropBinding: FragmentReviewTabCropBinding
    lateinit var reviewActivity: ReviewActivity
    lateinit var myPageReviewViewModel: MyPageReviewViewModel

    var cropReviewList = mutableListOf<ReviewModel>()
    private var reviewIdx = 0


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentReviewTabCropBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_review_tab_crop, container, false)
        myPageReviewViewModel = MyPageReviewViewModel()
        fragmentReviewTabCropBinding.myPageReviewViewModel = myPageReviewViewModel
        fragmentReviewTabCropBinding.lifecycleOwner = this

        reviewActivity = activity as ReviewActivity

        settingRecyclerViewReviewTabCrop()
        gettingCropReviewData()

        return fragmentReviewTabCropBinding.root
    }

    // 리뷰 농산물 탭 리사이클러뷰 설정
    fun settingRecyclerViewReviewTabCrop() {
        fragmentReviewTabCropBinding.apply {
            recyclerViewReviewTabCrop.apply {
                adapter = ReviewTabCropRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(reviewActivity)
                val deco = MaterialDividerItemDecoration(reviewActivity, MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    fun gettingCropReviewData() {
        CoroutineScope(Dispatchers.Main).launch {
            cropReviewList = MyPageReviewDao.gettingCropReviewList()

            fragmentReviewTabCropBinding.recyclerViewReviewTabCrop.adapter?.notifyDataSetChanged()

            val reviewCount = cropReviewList.size
            fragmentReviewTabCropBinding.myPageReviewViewModel?.textViewReviewTabCropCount?.value = "내가 쓴 리뷰 총 ${reviewCount}개"

        }
    }

    // 리뷰 농산물 탭 리사이클러뷰 어댑터 설정
    inner class ReviewTabCropRecyclerViewAdapter : RecyclerView.Adapter<ReviewTabCropRecyclerViewAdapter.ReviewTabCropViewHolder>() {
        inner class ReviewTabCropViewHolder(rowReviewHistoryCropBinding: RowReviewHistoryCropBinding) : RecyclerView.ViewHolder(rowReviewHistoryCropBinding.root) {
            val rowReviewHistoryCropBinding: RowReviewHistoryCropBinding

            init {
                this.rowReviewHistoryCropBinding = rowReviewHistoryCropBinding

                this.rowReviewHistoryCropBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReviewTabCropViewHolder {
            val rowReviewHistoryCropBinding = DataBindingUtil.inflate<RowReviewHistoryCropBinding>(layoutInflater, R.layout.row_review_history_crop, parent, false)
            val myPageReviewViewModel = MyPageReviewViewModel()
            rowReviewHistoryCropBinding.myPageReviewViewModel = myPageReviewViewModel
            rowReviewHistoryCropBinding.lifecycleOwner = this@ReviewTabCropFragment

            val reviewTabCropViewHolder = ReviewTabCropViewHolder(rowReviewHistoryCropBinding)

            return reviewTabCropViewHolder
        }

        override fun getItemCount(): Int {
            return cropReviewList.size
        }

        override fun onBindViewHolder(holder: ReviewTabCropViewHolder, position: Int) {
            val cropImageList = cropReviewList[position].review_images

            reviewIdx = cropReviewList[position].review_idx

            holder.rowReviewHistoryCropBinding.myPageReviewViewModel?.textViewRowReviewTabCropDate?.value = cropReviewList[position].review_reg_dt
            holder.rowReviewHistoryCropBinding.myPageReviewViewModel?.textViewRowReviewTabCropName?.value = cropReviewList[position].review_title
            holder.rowReviewHistoryCropBinding.myPageReviewViewModel?.textViewRowReviewTabCropText?.value = cropReviewList[position].review_content
            holder.rowReviewHistoryCropBinding.myPageReviewViewModel?.textViewRowReviewTabCropLabel?.value = cropReviewList[position].review_option
            holder.rowReviewHistoryCropBinding.ratingBarRowReviewHistoryCrop.rating =
                cropReviewList[position].review_rate.toFloat()

            holder.rowReviewHistoryCropBinding.buttonReviewTabCropDelete.setOnClickListener {
                val dialog = DialogYesNo(this@ReviewTabCropFragment, null, "리뷰를 삭제하시면 재작성이 불가합니다.\n" +
                        "삭제하시겠습니까?", reviewActivity, position)
                dialog.show(this@ReviewTabCropFragment?.parentFragmentManager!!, "DialogYesNo")
            }

            // 리뷰 농산물 탭 이미지 리사이클러뷰 설정
            holder.rowReviewHistoryCropBinding.recyclerViewReviewImageCrop.apply {
                adapter = ReviewImageCropRecyclerViewAdapter(cropImageList)
                layoutManager = LinearLayoutManager(reviewActivity, LinearLayoutManager.HORIZONTAL, false)
            }
        }


        // 리뷰 농산물 탭 이미지 리사이클러뷰 설정
        inner class ReviewImageCropRecyclerViewAdapter(cropImageList: MutableList<String>) : RecyclerView.Adapter<ReviewImageCropRecyclerViewAdapter.ReviewImageCropViewHolder>() {
            var cropImages = cropImageList
            inner class ReviewImageCropViewHolder(rowReviewHistoryImageCropBinding: RowReviewHistoryImageCropBinding) : RecyclerView.ViewHolder(rowReviewHistoryImageCropBinding.root) {
                val rowReviewHistoryImageCropBinding: RowReviewHistoryImageCropBinding

                init {
                    this.rowReviewHistoryImageCropBinding = rowReviewHistoryImageCropBinding

                    this.rowReviewHistoryImageCropBinding.root.layoutParams = ViewGroup.LayoutParams(
                        ViewGroup.LayoutParams.WRAP_CONTENT,
                        ViewGroup.LayoutParams.WRAP_CONTENT
                    )
                }
            }

            override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): ReviewImageCropViewHolder {
                val rowReviewHistoryImageCropBinding = DataBindingUtil.inflate<RowReviewHistoryImageCropBinding>(layoutInflater, R.layout.row_review_history_image_crop, parent, false)
                val myPageReviewViewModel = MyPageReviewViewModel()
                rowReviewHistoryImageCropBinding.myPageReviewViewModel = myPageReviewViewModel
                rowReviewHistoryImageCropBinding.lifecycleOwner = this@ReviewTabCropFragment

                val reviewImageCropViewHolder = ReviewImageCropViewHolder(rowReviewHistoryImageCropBinding)

                return reviewImageCropViewHolder
            }

            override fun getItemCount(): Int {
                return cropImages.size
            }

            override fun onBindViewHolder(holder: ReviewImageCropViewHolder, position: Int) {
                CoroutineScope(Dispatchers.Main).launch {
                    MyPageReviewDao.gettingCropReviewPostImage(requireContext(), cropImages[position], holder.rowReviewHistoryImageCropBinding.imageViewRowReviewTabCrop)
                }
            }
        }

    }

    override fun onYesButtonClick(id: Int) {
        fragmentReviewTabCropBinding.recyclerViewReviewTabCrop.adapter!!.notifyItemRemoved(id)

        CoroutineScope(Dispatchers.Main).launch {
            // 글 상태를 삭제 상태로 변경한다.
            MyPageReviewDao.updateReviewState(reviewIdx, ReviewState.REVIEW_STATE_REMOVE)
        }
    }

    override fun onYesButtonClick(activity: AppCompatActivity) {

    }
}