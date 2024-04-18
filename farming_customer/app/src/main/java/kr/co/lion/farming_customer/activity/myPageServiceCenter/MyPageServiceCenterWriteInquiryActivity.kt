package kr.co.lion.farming_customer.activity.myPageServiceCenter

import android.annotation.SuppressLint
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
import android.net.Uri
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.InquiryState
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.Tools
import kr.co.lion.farming_customer.dao.myPageServiceCenter.MyPageServiceCenterInquiryDao
import kr.co.lion.farming_customer.databinding.ActivityMyPageServiceCenterWriteInquiryBinding
import kr.co.lion.farming_customer.databinding.RowServiceCenterInquiryPhotoBinding
import kr.co.lion.farming_customer.model.myPageServiceCenterModel.InquiryModel
import kr.co.lion.farming_customer.viewmodel.myPageServiceCenter.ServiceCenterInquiryViewModel
import java.text.SimpleDateFormat
import java.util.Date

class MyPageServiceCenterWriteInquiryActivity : AppCompatActivity() {
    lateinit var binding: ActivityMyPageServiceCenterWriteInquiryBinding
    lateinit var albumLauncher: ActivityResultLauncher<Intent>
    lateinit var viewModel: ServiceCenterInquiryViewModel

    // 이미지 리스트
    val imageBitmapList = mutableListOf<Bitmap>()
    val imageUriList = mutableListOf<Uri>()

    // 이미지 등록 가능 갯수
    private val imageUploadPossible = 5

    // 이미지 첨부 유무
    private var isAddPicture = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(
            this,
            R.layout.activity_my_page_service_center_write_inquiry
        )
        viewModel = ServiceCenterInquiryViewModel()
        binding.serviceCenterInquiryViewModel = viewModel
        binding.lifecycleOwner = this

        settingToolbar()
        settingDropdown()
        settingAlbumLauncher(imageUploadPossible)
        settingRecyclerView()
        settingEvent()
        settingButton()

        settingInputUI()

    }

    fun settingToolbar() {
        binding.serviceCenterWriteInquiryToolbar.apply {
            setNavigationOnClickListener {
                finish()
            }
        }
    }

    fun settingEvent() {
        // 사진 첨부하기 버튼
        binding.serviceCenterWriteInquiryCameraBtnLayout.setOnClickListener {
            startAlbumLauncher()
        }

        // 문의 버튼
        binding.serviceCenterWriteInquiryBtn.setOnClickListener {
            val check = checkInputForm()
            if (check) {
                viewModel.settingInquiryType(viewModel.inquiryType.value!!)
                saveInquiryData()
                Log.e("inquiryData", "문의 사항 저장 완료")
                finish()
            }
        }
    }

    private fun settingDropdown() {
        binding.apply {
            // 드롭다운 설정
            val typeList = listOf("농산물", "주말농장", "체험활동", "구인구직", "농기구", "게시판", "기타")
            val typeArrayAdapter = ArrayAdapter(
                applicationContext,
                R.layout.item_spinner_service_center_inquiry,
                typeList
            )
            serviceCenterWriteInquiryType.setAdapter(typeArrayAdapter)
        }
    }

    private fun settingRecyclerView() {
        binding.apply {
            serviceCenterWriteInquiryPhotoRv.apply {
                adapter = WriteReviewRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(context, RecyclerView.HORIZONTAL, false)
            }
        }
    }

    // 앨범 런처 설정
    fun settingAlbumLauncher(imageUploadPossible: Int) {
        // 앨범 실행을 위한 런처
        val contract2 = ActivityResultContracts.StartActivityForResult()
        albumLauncher = registerForActivityResult(contract2) {
            // 사진 선택을 완료한 후 돌아왔다면
            if (it.resultCode == RESULT_OK) {
                // 선택한 이미지의 경로 데이터를 관리하는 Uri 객체 리스트를 추출한다.
                val uriclip = it.data?.clipData
                uriclip?.let { clipData ->
                    for (i in 0 until uriclip!!.itemCount) {
                        if (imageBitmapList.size >= imageUploadPossible) {
                            Snackbar.make(
                                binding.root,
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
                                    val source = ImageDecoder.createSource(contentResolver, uri)
                                    // Bitmap을 생성한다.
                                    ImageDecoder.decodeBitmap(source)
                                } else {
                                    // 컨텐츠 프로바이더를 통해 이미지 데이터에 접근한다.
                                    val cursor = contentResolver.query(uri, null, null, null, null)
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
                                val degree = Tools.getDegree(applicationContext, uri)
                                // 회전 이미지를 가져온다
                                val bitmap2 = Tools.rotateBitmap(bitmap!!, degree.toFloat())
                                // 크기를 줄인 이미지를 가져온다.
                                val bitmap3 = Tools.resizeBitmap(bitmap2, 1024)

                                // 이미지 리스트에 추가한다.
                                imageBitmapList.add(bitmap3)
                                isAddPicture = true
                            }
                        }
                    }
                }
                // 이미지 리스트에 이미지가 있으면 스크롤뷰 나타남
                if (imageBitmapList.isNotEmpty()) {
                    binding.serviceCenterWriteInquiryPhotoRv.visibility = View.VISIBLE
                }
                // 리사이클러뷰 업데이트
                updateRecyclerView()
            }
        }
    }

    fun settingButton() {
        binding.serviceCenterWriteInquiryComment.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                val isButtonEnabled = s?.length ?: 0 >= 1
                binding.serviceCenterWriteInquiryBtn.isEnabled = isButtonEnabled
                if (isButtonEnabled) {
                    // 활성 상태일 때
                    binding.serviceCenterWriteInquiryBtn.backgroundTintList =
                        ColorStateList.valueOf(
                            ContextCompat.getColor(applicationContext, R.color.green_main)
                        )
                    binding.serviceCenterWriteInquiryBtn.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.white
                        )
                    )
                } else {
                    // 비활성 상태일 때
                    binding.serviceCenterWriteInquiryBtn.backgroundTintList =
                        ColorStateList.valueOf(
                            ContextCompat.getColor(applicationContext, R.color.white)
                        )
                    // stroke 설정
                    binding.serviceCenterWriteInquiryBtn.strokeWidth = 1
                    binding.serviceCenterWriteInquiryBtn.strokeColor = ColorStateList.valueOf(
                        ContextCompat.getColor(applicationContext, R.color.green_main)
                    )
                    // 텍스트 색상 설정
                    binding.serviceCenterWriteInquiryBtn.setTextColor(
                        ContextCompat.getColor(
                            applicationContext,
                            R.color.green_main
                        )
                    )
                }
            }
        })
    }

    // 앨범 런처를 실행하는 메서드
    fun startAlbumLauncher() {
        // 앨범에서 사진을 선택할 수 있도록 셋팅된 인텐트를 생성한다.
        val albumIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        // 실행할 액티비티의 타입을 설정(이미지를 선택할 수 있는 것이 뜨게 한다)
        albumIntent.setType("image/*")

        // 이미지 여러개 선택 가능
        albumIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        // 액티비티를 실행한다.
        albumLauncher.launch(albumIntent)
    }

    @SuppressLint("NotifyDataSetChanged")
    fun updateRecyclerView() {
        binding.serviceCenterWriteInquiryPhotoRv.adapter?.notifyDataSetChanged()
    }

    inner class WriteReviewRecyclerViewAdapter :
        RecyclerView.Adapter<WriteReviewRecyclerViewAdapter.WriteReviewViewHolder>() {
        inner class WriteReviewViewHolder(rowOrderHistoryWritePhotoBinding: RowServiceCenterInquiryPhotoBinding) :
            RecyclerView.ViewHolder(rowOrderHistoryWritePhotoBinding.root) {
            val rowBinding: RowServiceCenterInquiryPhotoBinding

            init {
                this.rowBinding = rowOrderHistoryWritePhotoBinding

                rowOrderHistoryWritePhotoBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): WriteReviewViewHolder {
            val rowBinding = DataBindingUtil.inflate<RowServiceCenterInquiryPhotoBinding>(
                layoutInflater,
                R.layout.row_service_center_inquiry_photo,
                parent,
                false
            )

            val writeReviewViewHolder = WriteReviewViewHolder(rowBinding)
            return writeReviewViewHolder
        }

        override fun getItemCount(): Int {
            return imageBitmapList.size
        }

        override fun onBindViewHolder(holder: WriteReviewViewHolder, position: Int) {
            holder.rowBinding.apply {
                serviceCenterWriteInquiryPhotoIv.setImageBitmap(imageBitmapList[position])
                serviceCenterWriteInquiryPhotoCancel.setOnClickListener {
                    // 이미지 삭제
                    imageBitmapList.removeAt(position)
                    // 이미지 리스트에 이미지가 없으면 스크롤뷰 사라짐
                    if (imageBitmapList.isEmpty()) {
                        binding.serviceCenterWriteInquiryPhotoRv.visibility = View.GONE
                    }
                    updateRecyclerView()
                }
            }
        }
    }

    // 입력 요소 유효성 검사
    fun checkInputForm(): Boolean {
        // 입력한 내용을 가져온다.
        val inquiryType = viewModel.inquiryType.value!!
        val content = viewModel.inquiryContent.value!!

        if (inquiryType.isEmpty()) {
            Tools.showErrorDialog(
                this@MyPageServiceCenterWriteInquiryActivity, binding.serviceCenterWriteInquiryType,
                "유형 선택 오류", "유형을 선택해주세요"
            )
            return false
        }

        if (content.isEmpty()) {
            Tools.showErrorDialog(
                this@MyPageServiceCenterWriteInquiryActivity,
                binding.serviceCenterWriteInquiryComment,
                "내용 입력 오류",
                "내용을 입력해주세요"
            )
            return false
        }
        return true
    }

    // 입력 요소 관련 설정
    fun settingInputUI() {
        // 입력 요소들을 초기화 한다.
        viewModel.inquiryContent.value = ""

        isAddPicture = false

        // 의견 입력란에 포커스를 준다.
        Tools.showSoftInput(this, binding.serviceCenterWriteInquiryComment)
    }

    // 데이터를 저장하고 이동한다.
    @SuppressLint("SimpleDateFormat")
    fun saveInquiryData() {
        CoroutineScope(Dispatchers.Main).launch {
            val model = InquiryModel()
            // 사용자 번호 시퀀스 값을 가져온다.
            val inquirySequence = MyPageServiceCenterInquiryDao.getContentSequence()
            // 시퀀스값을 1 증가시켜 덮어씌워준다.
            MyPageServiceCenterInquiryDao.updateContentSequence(inquirySequence + 1)

            val inquiryIdx = inquirySequence + 1
            val inquiryType = viewModel.gettingContentType().number
            val inquiryContent = viewModel.inquiryContent.value!!
            val inquiryStatus = InquiryState.INQUIRY_STATE_NORMAL.number
            val inquiryImages = mutableListOf<String?>()

            // 첨부된 이미지가 있다면
            if (isAddPicture) {
                // 서버로 업로드한다.
                val uploadList = MyPageServiceCenterInquiryDao.uploadImage(
                    this@MyPageServiceCenterWriteInquiryActivity,
                    model.inquiry_idx,
                    imageUriList
                )
                inquiryImages.addAll(uploadList)
            } else {
                // 첨부된 이미지가 없으면 빈 리스트를 사용
                inquiryImages.addAll(emptyList())
            }

            val simpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
            val inquiryRegDt = simpleDateFormat.format(Date())

            // 저장할 데이터를 객체에 담는다.
            val inquiryModel = InquiryModel(
                inquiryIdx,
                inquiryIdx,
                inquiryType,
                inquiryContent,
                inquiryRegDt.toString(),
                false,
                "",
                inquiryImages,
                inquiryStatus
            )

            // 사용자 정보를 저장한다.
            MyPageServiceCenterInquiryDao.insertContentData(inquiryModel)
        }
    }
}