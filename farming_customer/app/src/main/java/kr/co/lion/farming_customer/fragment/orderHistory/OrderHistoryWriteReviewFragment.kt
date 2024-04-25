package kr.co.lion.farming_customer.fragment.orderHistory

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.InquiryState
import kr.co.lion.farming_customer.OrderHistoryFragmentName
import kr.co.lion.farming_customer.OrderProductType
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.Tools
import kr.co.lion.farming_customer.activity.orderHistory.OrderHistoryActivity
import kr.co.lion.farming_customer.dao.crop.CropDao
import kr.co.lion.farming_customer.dao.farmingLife.ActivityDao
import kr.co.lion.farming_customer.dao.farmingLife.FarmDao
import kr.co.lion.farming_customer.dao.orderHistory.OrderDao
import kr.co.lion.farming_customer.dao.review.ReviewDao
import kr.co.lion.farming_customer.databinding.FragmentOrderHistoryWriteReviewBinding
import kr.co.lion.farming_customer.databinding.RowOrderHistoryWritePhotoBinding
import kr.co.lion.farming_customer.model.CropModel
import kr.co.lion.farming_customer.model.farmingLife.ActivityModel
import kr.co.lion.farming_customer.model.farmingLife.FarmModel
import kr.co.lion.farming_customer.model.orderHistory.OrderModel
import kr.co.lion.farming_customer.model.review.ReviewModel
import kr.co.lion.farming_customer.viewmodel.orderHistory.OrderHistoryReviewViewModel
import java.text.SimpleDateFormat
import java.util.Date

class OrderHistoryWriteReviewFragment : Fragment() {
    lateinit var fragmentOrderHistoryWriteReviewBinding: FragmentOrderHistoryWriteReviewBinding
    lateinit var orderHistoryActivity: OrderHistoryActivity

    lateinit var orderHistoryReviewViewModel: OrderHistoryReviewViewModel

    lateinit var albumLauncher: ActivityResultLauncher<Intent>

    // 이미지 리스트
    val imageList = mutableListOf<Bitmap>()
    val imageUriList = mutableListOf<Uri>()

    // 이미지 등록 가능 갯수
    val imageUploadPossible = 5
    // 이미지 첨부 유무
    private var isAddPicture = false

    var orderModel : OrderModel? = null
    var productType : Int? = null
    var farmModel : FarmModel? = null
    var activityModel : ActivityModel? = null
    var cropModel : CropModel? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentOrderHistoryWriteReviewBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_order_history_write_review, container, false)
        orderHistoryReviewViewModel = OrderHistoryReviewViewModel()
        fragmentOrderHistoryWriteReviewBinding.orderHistoryReviewViewModel = orderHistoryReviewViewModel
        fragmentOrderHistoryWriteReviewBinding.lifecycleOwner = this
        orderHistoryActivity = activity as OrderHistoryActivity

        settingInitData()
        settingToolbar()
        settingEvent()
        settingAlbumLauncher(imageUploadPossible)
        settingRecyclerView()

        return fragmentOrderHistoryWriteReviewBinding.root
    }

    private fun settingInitData() {
        CoroutineScope(Dispatchers.Main).launch {
            orderModel = OrderDao.selectOrderData(arguments?.getInt("orderIdx")!!)
            productType = arguments?.getInt("orderProductType")
            if(productType == OrderProductType.ORDER_PRODUCT_TYPE_FARM.number){
                // 주말농장
                farmModel = FarmDao.selectFarmData(arguments?.getInt("orderProductIdx")!!)
            }
            else if (productType == OrderProductType.ORDER_PRODUCT_TYPE_ACTIVITY.number){
                // 체험활동
                activityModel = ActivityDao.selectActivityData(arguments?.getInt("orderProductIdx")!!)
            }
            else {
                // 농산물
                cropModel = CropDao.selectCropData(arguments?.getInt("orderProductIdx")!!)
            }

            settingData()
            settingImage()

        }
    }

    private fun settingImage() {
        fragmentOrderHistoryWriteReviewBinding.apply {
            CoroutineScope(Dispatchers.Main).launch {
                if(productType == OrderProductType.ORDER_PRODUCT_TYPE_FARM.number){
                    FarmDao.gettingFarmImage(orderHistoryActivity, farmModel!!.farm_images[0], imageViewWriteReviewProductImage)
                }else if (productType == OrderProductType.ORDER_PRODUCT_TYPE_ACTIVITY.number){
                    ActivityDao.gettingActivityImage(orderHistoryActivity, activityModel!!.activity_images[0], imageViewWriteReviewProductImage)
                }else {
                    CropDao.gettingCropImage(orderHistoryActivity, cropModel!!.crop_images[0], imageViewWriteReviewProductImage)
                }
            }
        }
    }

    private fun settingData() {
        fragmentOrderHistoryWriteReviewBinding.apply {
            orderHistoryReviewViewModel!!.apply {
                if(orderModel!!.order_option_detail.size > 1){
                    textViewWriteReview_productName.value = "${orderModel!!.order_option_detail[0]["option_name"]}...외 ${orderModel!!.order_option_detail.size-1}개"
                }else{
                    textViewWriteReview_productName.value = "${orderModel!!.order_option_detail[0]["option_name"]}"
                }
                textViewWriteReview_price.value = orderModel!!.order_total_price
            }
        }
    }

    private fun settingRecyclerView() {
        fragmentOrderHistoryWriteReviewBinding.apply {
            recyclerViewWriteReviewPhoto.apply {
                adapter = WriteReviewRecyclerViewAdater()
                layoutManager = LinearLayoutManager(orderHistoryActivity, RecyclerView.HORIZONTAL, false)
            }
        }
    }

    private fun settingEvent() {
        fragmentOrderHistoryWriteReviewBinding.apply {
            // 앨범에서 사진 추가 버튼
            buttonWriteReviewUploadImage.setOnClickListener {
                startAlbumLauncher()
            }

            // 완료
            buttonWriteReviewDone.setOnClickListener {
                // 리뷰 작성 업로드 작업 추가
                saveReviewData()
                orderHistoryActivity.removeFragment(OrderHistoryFragmentName.ORDER_HISTORY_WRITE_REVIEW_FRAGMENT)
            }
        }
    }

    private fun settingToolbar() {
        fragmentOrderHistoryWriteReviewBinding.apply {
            toolbarWriteReview.apply {
                setNavigationOnClickListener {
                    orderHistoryActivity.removeFragment(OrderHistoryFragmentName.ORDER_HISTORY_WRITE_REVIEW_FRAGMENT)
                }
            }
        }
    }

    // 앨범 런처 설정
    fun settingAlbumLauncher(imageUploadPossible: Int) {
        // 앨범 실행을 위한 런처
        val contract2 = ActivityResultContracts.StartActivityForResult()
        albumLauncher = registerForActivityResult(contract2) {
            // 사진 선택을 완료한 후 돌아왔다면
            if (it.resultCode == AppCompatActivity.RESULT_OK) {
                // 선택한 이미지의 경로 데이터를 관리하는 Uri 객체 리스트를 추출한다.
                val uriclip = it.data?.clipData
                uriclip?.let { clipData ->
                    for (i in 0 until uriclip!!.itemCount) {
                        if (imageList.size >= imageUploadPossible) {
                            Snackbar.make(
                                fragmentOrderHistoryWriteReviewBinding.root,
                                "사진 첨부는 최대 5장까지 가능합니다.",
                                Snackbar.LENGTH_SHORT
                            ).show()
                            break
                        } else {
                            val uri = uriclip.getItemAt(i).uri
                            // 한 장씩 리스트에 추가
                            imageUriList.add(uri)

                            if (uri != null) {
                                // 안드로이드 Q(10) 이상이라면
                                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    // 이미지를 생성할 수 있는 객체를 생성한다.
                                    val source = ImageDecoder.createSource(orderHistoryActivity.contentResolver, uri)
                                    // Bitmap을 생성한다.
                                    ImageDecoder.decodeBitmap(source)
                                } else {
                                    // 컨텐츠 프로바이더를 통해 이미지 데이터에 접근한다.
                                    val cursor = orderHistoryActivity.contentResolver.query(uri, null, null, null, null)
                                    if (cursor != null) {
                                        cursor.moveToNext()

                                        // 이미지의 경로를 가져온다.
                                        val idx =
                                            cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                                        val source = cursor.getString(idx)

                                        // 이미지를 생성한다
                                        BitmapFactory.decodeFile(source)
                                    } else {
                                        null
                                    }
                                }

                                // 회전 각도값을 가져온다.
                                val degree = Tools.getDegree(orderHistoryActivity, uri)
                                // 회전 이미지를 가져온다
                                val bitmap2 = Tools.rotateBitmap(bitmap!!, degree.toFloat())
                                // 크기를 줄인 이미지를 가져온다.
                                val bitmap3 = Tools.resizeBitmap(bitmap2, 1024)

                                // 이미지 리스트에 추가한다.
                                imageList.add(bitmap3)
                                isAddPicture = true
                            }
                        }
                    }
                }
                // 이미지 리스트에 이미지가 있으면 스크롤뷰 나타남
                if (imageList.isNotEmpty()) {
                    fragmentOrderHistoryWriteReviewBinding.recyclerViewWriteReviewPhoto.visibility = View.VISIBLE
                }
                // 리사이클러뷰 업데이트
                updateRecyclerView()
            }
        }
    }

    // 앨범 런처를 실행하는 메서드
    fun startAlbumLauncher(){
        // 앨범에서 사진을 선택할 수 있도록 셋팅된 인텐트를 생성한다.
        val albumIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        // 실행할 액티비티의 타입을 설정(이미지를 선택할 수 있는 것이 뜨게 한다)
        albumIntent.setType("image/*")

        // 이미지 여러개 선택 가능
        albumIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        // 액티비티를 실행한다.
        albumLauncher.launch(albumIntent)
    }

    fun updateRecyclerView(){
        fragmentOrderHistoryWriteReviewBinding.recyclerViewWriteReviewPhoto.adapter?.notifyDataSetChanged()
    }

    inner class WriteReviewRecyclerViewAdater : RecyclerView.Adapter<WriteReviewRecyclerViewAdater.WriteReviewViewHolder>(){
        inner class WriteReviewViewHolder(rowOrderHistoryWritePhotoBinding: RowOrderHistoryWritePhotoBinding) : RecyclerView.ViewHolder(rowOrderHistoryWritePhotoBinding.root){
            val rowOrderHistoryWritePhotoBinding : RowOrderHistoryWritePhotoBinding
            init {
                this.rowOrderHistoryWritePhotoBinding = rowOrderHistoryWritePhotoBinding

                rowOrderHistoryWritePhotoBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WriteReviewViewHolder {
            val rowOrderHistoryWritePhotoBinding = DataBindingUtil.inflate<RowOrderHistoryWritePhotoBinding>(layoutInflater, R.layout.row_order_history_write_photo, parent, false)

            val writeReviewViewHolder = WriteReviewViewHolder(rowOrderHistoryWritePhotoBinding)
            return writeReviewViewHolder
        }

        override fun getItemCount(): Int {
            return imageList.size
        }

        override fun onBindViewHolder(holder: WriteReviewViewHolder, position: Int) {
            holder.rowOrderHistoryWritePhotoBinding.apply {
                imageView.setImageBitmap(imageList[position])
                imageButton.setOnClickListener {
                    // 이미지 삭제
                    imageList.removeAt(position)
                    // 이미지 리스트에 이미지가 없으면 스크롤뷰 사라짐
                    if(imageList.isEmpty()){
                        fragmentOrderHistoryWriteReviewBinding.recyclerViewWriteReviewPhoto.visibility = View.GONE
                    }
                    updateRecyclerView()
                }
            }
        }

    }


    // 데이터를 저장하고 이동한다.
    @SuppressLint("SimpleDateFormat")
    fun saveReviewData() {
        CoroutineScope(Dispatchers.Main).launch {
            val model = ReviewModel()
            // 사용자 번호 시퀀스 값을 가져온다.
            val reviewSequence = ReviewDao.getReviewSequence()
            // 시퀀스값을 1 증가시켜 덮어씌워준다.
            ReviewDao.updateReviewSequence(reviewSequence + 1)

            val reviewIdx = reviewSequence + 1
            var reviewUserIdx = orderModel!!.order_user_idx
            val reviewOrderIdx = orderModel!!.order_product_idx
            val reviewSellerIdx = orderModel!!.order_seller_idx
            val reviewRate =
                fragmentOrderHistoryWriteReviewBinding.ratingBarWriteReview.rating.toDouble()
            val reviewContent =
                orderHistoryReviewViewModel.textViewWriteReview_reviewContent.value!!
            val reviewTitle = orderHistoryReviewViewModel.textViewWriteReview_productName.value!!
            val reviewOption = orderHistoryReviewViewModel.textViewWriteReview_productName.value!!
            val reviewImages = mutableListOf<String?>()
            val reviewIsRead = false

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            val reviewRegDt = simpleDateFormat.format(Date())

            val reviewModDt = ""
            val reviewStatus = 0
            val reviewType = when (orderModel?.order_product_type) {
                1 -> 0
                2 -> 1
                3 -> 2
                else -> 0
            }

            // 첨부된 이미지가 있다면
            if (isAddPicture) {
                // 서버로 업로드한다.
                val uploadList = ReviewDao.uploadImage(
                    orderHistoryActivity,
                    model.review_idx,
                    imageUriList
                )
                reviewImages.addAll(uploadList)
            } else {
                // 첨부된 이미지가 없으면 빈 리스트를 사용
                reviewImages.addAll(emptyList())
            }

            // 저장할 데이터를 객체에 담는다.
            var reviewModel = ReviewModel(
                reviewIdx,
                reviewUserIdx,
                reviewOrderIdx,
                reviewSellerIdx,
                reviewRate,
                reviewContent,
                reviewTitle,
                reviewOption,
                reviewImages,
                reviewIsRead,
                reviewRegDt,
                reviewModDt,
                reviewStatus,
                reviewType
            )

            // 사용자 정보를 저장한다.
            ReviewDao.insertReviewData(reviewModel)
        }
    }

}