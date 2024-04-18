package kr.co.lion.farming_customer.fragment.community

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.ImageDecoder
import android.net.Uri
import android.graphics.drawable.BitmapDrawable
import android.os.Build
import android.os.Bundle
import android.provider.MediaStore
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.DialogYesNo
import kr.co.lion.farming_customer.DialogYesNoInterface
import kr.co.lion.farming_customer.PostStatus
import kr.co.lion.farming_customer.PostType
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.Tools
import kr.co.lion.farming_customer.activity.community.CommunityActivity
import kr.co.lion.farming_customer.activity.community.CommunityAddActivity
import kr.co.lion.farming_customer.dao.CommunityPostDao
import kr.co.lion.farming_customer.databinding.FragmentCommunityAddBinding
import kr.co.lion.farming_customer.databinding.RowCommunityAddImageBinding
import kr.co.lion.farming_customer.model.CommunityModel
import kr.co.lion.farming_customer.viewmodel.community.CommunityAddModifyViewModel
import java.text.SimpleDateFormat
import java.util.Date

class CommunityAddFragment : Fragment(), DialogYesNoInterface {
    lateinit var fragmentCommunityAddBinding: FragmentCommunityAddBinding
    lateinit var communityAddActivity: CommunityAddActivity
    lateinit var communityAddModifyViewModel: CommunityAddModifyViewModel

    var model:CommunityModel? = null

    // 이미지 리스트
    val imageCommunityAddUriList = mutableListOf<Uri>()

    val imageCommunityAddBitmapList = mutableListOf<Bitmap>()

    // 이미지 저장용 리스트
    val imageCommunityStringList = mutableListOf<String>()

    // 이미지 등록 가능 갯수
    val imageUploadPossible = 10

    // 앨범 실행을 위한 런처
    lateinit var albumLauncher: ActivityResultLauncher<Intent>

    // 이미지를 첨부한 적이 있는지..
    var isAddPicture = false

    // 확인할 권한 등록
    val permissionList = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_MEDIA_LOCATION
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentCommunityAddBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_add, container, false)
        communityAddModifyViewModel = CommunityAddModifyViewModel()
        fragmentCommunityAddBinding.communityAddModifyViewModel = communityAddModifyViewModel
        fragmentCommunityAddBinding.lifecycleOwner = this
        communityAddActivity = activity as CommunityAddActivity

        settingToolbar()
        settingInputForm()
        settingAlbumLauncher(imageUploadPossible)
        settingTextInputLayoutCommunityAddType()
        settingButtonCommunityAdd()
        settingViewPager2CommunityAddImage()
        settingImageViewCommunityAdd()

        // 권한 확인
        requestPermissions(permissionList, 0)

        return fragmentCommunityAddBinding.root
    }

    // 툴바 설정
    fun settingToolbar() {
        fragmentCommunityAddBinding.apply {
            toolbarCommunityAdd.apply {
                setNavigationIcon(R.drawable.back)
                setNavigationOnClickListener {
                    communityAddActivity.finish()
                }
            }
        }
    }

    // 드롭다운 설정
    fun settingTextInputLayoutCommunityAddType() {
        fragmentCommunityAddBinding.apply {
            // 드롭다운 설정
            val typeList = listOf("정보 게시판", "소통 게시판", "구인구직")
            val typeArrayAdapter = ArrayAdapter(requireContext(), R.layout.item_spinner_community_add, typeList)
            textViewCommunityAddType.setAdapter(typeArrayAdapter)
        }
    }

    // 뷰페이저 업데이트
    fun updateViewPager2CommunityAddImage() {
        fragmentCommunityAddBinding.viewPager2CommunityAddImage.adapter?.notifyDataSetChanged()
    }

    fun getBitmapFromDrawable(): Bitmap {
        val drawable = resources.getDrawable(R.drawable.community_add_default)
        val canvas = Canvas()
        val bitmap = Bitmap.createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, Bitmap.Config.ARGB_8888)
        canvas.setBitmap(bitmap)
        drawable.setBounds(0, 0, drawable.intrinsicWidth, drawable.intrinsicHeight)
        drawable.draw(canvas)

        return bitmap
    }

    // 커뮤니티 글 작성 이미지 뷰페이저 설정
    fun settingViewPager2CommunityAddImage() {
        fragmentCommunityAddBinding.apply {
            viewPager2CommunityAddImage.apply {
                imageCommunityAddBitmapList.add(getBitmapFromDrawable())
                adapter = CommunityAddImageViewPager2Adapter()
            }

            circleIndicatorCommunityAdd.setViewPager(viewPager2CommunityAddImage)
            viewPager2CommunityAddImage.adapter?.registerAdapterDataObserver(circleIndicatorCommunityAdd.adapterDataObserver)
        }
    }

    // 커뮤니티 글 작성 이미지 뷰페이저 어댑터
    inner class CommunityAddImageViewPager2Adapter : RecyclerView.Adapter<CommunityAddImageViewPager2Adapter.CommunityAddImageViewHolder>() {
        inner class CommunityAddImageViewHolder(rowCommunityAddImageBinding: RowCommunityAddImageBinding) : RecyclerView.ViewHolder(rowCommunityAddImageBinding.root) {
            val rowCommunityAddImageBinding: RowCommunityAddImageBinding

            init {
                this.rowCommunityAddImageBinding = rowCommunityAddImageBinding

                this.rowCommunityAddImageBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup,viewType: Int): CommunityAddImageViewHolder {
            val rowCommunityAddImageBinding = DataBindingUtil.inflate<RowCommunityAddImageBinding>(layoutInflater, R.layout.row_community_add_image, parent,false)
            rowCommunityAddImageBinding.lifecycleOwner = this@CommunityAddFragment

            val communityAddImageViewHolder = CommunityAddImageViewHolder(rowCommunityAddImageBinding)

            return communityAddImageViewHolder
        }

        override fun getItemCount(): Int {
            return imageCommunityAddBitmapList.size
        }

        override fun onBindViewHolder(holder: CommunityAddImageViewHolder, position: Int) {
            holder.rowCommunityAddImageBinding.imageViewRowCommunityAdd.setImageBitmap(imageCommunityAddBitmapList[position])
            holder.rowCommunityAddImageBinding.imageButtonCommunityAddDelete.setOnClickListener {
                if(position != imageCommunityAddBitmapList.size-1){
                    imageCommunityAddBitmapList.removeAt(position)
                    updateViewPager2CommunityAddImage()
                }else{
                    imageCommunityAddBitmapList[position] = getBitmapFromDrawable()
                    fragmentCommunityAddBinding.viewPager2CommunityAddImage.adapter?.notifyDataSetChanged()
                }
            }
            // 마지막 아이템의 경우
            holder.rowCommunityAddImageBinding.root.setOnClickListener {
                if(position == imageCommunityAddBitmapList.size-1){
                    startAlbumLauncher()
                }
            }
        }


    }

    // 사진 첨부하기 버튼
    fun settingImageViewCommunityAdd() {
        fragmentCommunityAddBinding.apply {
            imageViewCommunityAdd.setOnClickListener {
                startAlbumLauncher()
                imageViewCommunityAddDefault.isVisible = false
            }
        }
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
                        if (imageCommunityAddBitmapList.size > imageUploadPossible + 1) {
                            Snackbar.make(fragmentCommunityAddBinding.root, "사진 첨부는 최대 10장까지 가능합니다.", Snackbar.LENGTH_SHORT).show()
                            break
                        } else {
                            val uri = uriclip.getItemAt(i).uri
                            // 한 장씩 리스트에 추가
                            imageCommunityAddUriList.add(uri)

                            if (uri != null) {
                                // 안드로이드 Q(10) 이상이라면
                                val bitmap = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                                    // 이미지를 생성할 수 있는 객체를 생성한다.
                                    val source = ImageDecoder.createSource(requireContext().contentResolver, uri)
                                    // Bitmap을 생성한다.
                                    ImageDecoder.decodeBitmap(source)
                                } else {
                                    // 컨텐츠 프로바이더를 통해 이미지 데이터에 접근한다.
                                    val cursor = requireContext().contentResolver.query(uri, null, null, null, null)
                                    if (cursor != null) {
                                        cursor.moveToNext()

                                        // 이미지의 경로를 가져온다.
                                        val idx = cursor.getColumnIndex(MediaStore.Images.Media.DATA)
                                        val source = cursor.getString(idx)

                                        // 이미지를 생성한다
                                        BitmapFactory.decodeFile(source)
                                    } else {
                                        null
                                    }
                                }

                                // 회전 각도값을 가져온다.
                                val degree = Tools.getDegree(requireContext(), uri)
                                // 회전 이미지를 가져온다
                                val bitmap2 = Tools.rotateBitmap(bitmap!!, degree.toFloat())
                                // 크기를 줄인 이미지를 가져온다.
                                val bitmap3 = Tools.resizeBitmap(bitmap2, 1024)

                                // 이미지 리스트에 추가한다.
                                imageCommunityAddBitmapList.add(bitmap3)
                                if(imageCommunityAddBitmapList.size == imageUploadPossible + 1){
                                    imageCommunityAddBitmapList.removeAt(imageCommunityAddBitmapList.size-2)
                                    break
                                }else{
                                    // add_default 이미지를 리스트 마지막으로 이동
                                    var tmp = imageCommunityAddBitmapList[imageCommunityAddBitmapList.size-2]
                                    imageCommunityAddBitmapList[imageCommunityAddBitmapList.size-2] = bitmap3
                                    imageCommunityAddBitmapList[imageCommunityAddBitmapList.size-1] = tmp
                                }
                                isAddPicture = true
                            }
                        }
                    }
                }
                // 리사이클러뷰 업데이트
                updateViewPager2CommunityAddImage()
                fragmentCommunityAddBinding.circleIndicatorCommunityAdd.setViewPager(fragmentCommunityAddBinding.viewPager2CommunityAddImage)
            }
        }
    }

    // 등록 버튼 활성화/비활성화
    fun settingButtonCommunityAdd() {
        fragmentCommunityAddBinding.apply {

            val textWatcher = object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    val isTitleFilled = textViewCommunityAddSubject.text?.isNotBlank() ?: false
                    val isContentFilled = textViewCommunityAddContent.text?.isNotBlank() ?: false

                    val isButtonEnabled = isTitleFilled && isContentFilled

                    buttonCommunityAdd.isEnabled = isButtonEnabled

                    if (isButtonEnabled) {
                        // 활성 상태일 때
                        buttonCommunityAdd.backgroundTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.green_main))
                        buttonCommunityAdd.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    } else {
                        // 비활성 상태일 때
                        buttonCommunityAdd.backgroundTintList =
                            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.white))
                        // stroke 설정
                        buttonCommunityAdd.strokeWidth = 1
                        buttonCommunityAdd.strokeColor =
                            ColorStateList.valueOf(ContextCompat.getColor(requireContext(), R.color.green_main))
                        // 텍스트 색상 설정
                        buttonCommunityAdd.setTextColor(
                            ContextCompat.getColor(requireContext(), R.color.green_main)
                        )
                    }
                }
            }

            textViewCommunityAddSubject.addTextChangedListener(textWatcher)
            textViewCommunityAddContent.addTextChangedListener(textWatcher)

            buttonCommunityAdd.setOnClickListener {

                CoroutineScope(Dispatchers.Main).launch {
                    model = generatingPostObject()
                    // 글 데이터 업로드
                    uploadCommunityPostData(model!!.postIdx)

                    val dialog = DialogYesNo(this@CommunityAddFragment, null, "게시글이 등록되었습니다", communityAddActivity, null)
                    dialog.show(communityAddActivity.supportFragmentManager, "DialogYesNo")

                }

            }
        }
    }

    override fun onYesButtonClick(id: Int) {

    }

    override fun onYesButtonClick(activity: AppCompatActivity) {
        val communityIntent = Intent(communityAddActivity, CommunityActivity::class.java)
        communityIntent.putExtra("postIdx", model!!.postIdx)
        startActivity(communityIntent)
        communityAddActivity.finish()
    }

    // 입력 요소 설정
    fun settingInputForm() {
        communityAddModifyViewModel?.textViewCommunityAddSubject?.value = ""
        communityAddModifyViewModel?.textViewCommunityAddContent?.value = ""
        settingTextInputLayoutCommunityAddType()
        // communityAddModifyViewModel.settingCommunityType(PostType.TYPE_INFORMATION)

        isAddPicture = false

        Tools.showSoftInput(communityAddActivity, fragmentCommunityAddBinding.textViewCommunityAddSubject)
    }

    // 게시글 객체 생성
   suspend fun generatingPostObject() : CommunityModel {

        var communityModel = CommunityModel()

        val job1 = CoroutineScope(Dispatchers.Main).launch {
            // 게시글 시퀀스 값을 가져온다.
            val communityPostSequence = CommunityPostDao.getCommunityPostSequence()
            // 게시글 시퀀스 값을 업데이트한다.
            CommunityPostDao.updateCommunityPostSequence(communityPostSequence + 1)

            // 업로드할 정보를 담아준다.
            communityModel.postIdx = communityPostSequence + 1
            communityModel.postTitle = communityAddModifyViewModel.textViewCommunityAddSubject.value!!
            if (communityAddModifyViewModel.communityPostAddType.value == "정보 게시판") {
                communityModel.postType = PostType.TYPE_INFORMATION.str
            } else if (communityAddModifyViewModel.communityPostAddType.value == "소통 게시판") {
                communityModel.postType = PostType.TYPE_SOCIAL.str
            } else {
                communityModel.postType = PostType.TYPE_JOB.str
            }
            communityModel.postContent = communityAddModifyViewModel.textViewCommunityAddContent.value!!
            communityModel.postLikeCnt = 0
            communityModel.postCommentCnt = 0
            communityModel.postImages = mutableListOf<String>()
            communityModel.postUserIdx = 1

            val simpleDateFormat = SimpleDateFormat("yyyy.MM.dd")
            communityModel.postRegDt = simpleDateFormat.format(Date())
            communityModel.postModDt = simpleDateFormat.format(Date())
            communityModel.postStatus = PostStatus.POST_STATUS_NORMAL.number

        }
        job1.join()

        return communityModel
    }


    // 게시글 작성
    fun uploadCommunityPostData(postIdx:Int) {
        CoroutineScope(Dispatchers.Main).launch {

            // 첨부된 이미지가 있다면
            if (isAddPicture == true) {
                // 서버로 업로드 한다.
                val uploadList = CommunityPostDao.uploadImage(communityAddActivity, postIdx, imageCommunityAddUriList)
                model!!.postImages = uploadList
            }
            CommunityPostDao.insertCommunityPostData(model!!)
        }
    }

}