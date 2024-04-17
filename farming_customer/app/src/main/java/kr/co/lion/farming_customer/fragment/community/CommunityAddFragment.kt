package kr.co.lion.farming_customer.fragment.community

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.ImageDecoder
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
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.Tools
import kr.co.lion.farming_customer.activity.CommunityAddActivity
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.databinding.FragmentCommunityAddBinding
import kr.co.lion.farming_customer.databinding.RowCommunityAddImageBinding
import kr.co.lion.farming_customer.databinding.RowCommunityReadImageBinding
import kr.co.lion.farming_customer.viewmodel.CommunityViewModel
import kr.co.lion.farming_customer.viewmodel.FarminglLifeViewModel

class CommunityAddFragment : Fragment() {
    lateinit var fragmentCommunityAddBinding: FragmentCommunityAddBinding
    lateinit var communityAddActivity: CommunityAddActivity
    lateinit var communityViewModel: CommunityViewModel


    // 이미지 리스트
    val imageCommunityAddList = mutableListOf<Bitmap>()

    // 이미지 등록 가능 갯수
    val imageUploadPossible = 10

    // 앨범 실행을 위한 런처
    lateinit var albumLauncher: ActivityResultLauncher<Intent>

    // 확인할 권한 등록
    val permissionList = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.ACCESS_MEDIA_LOCATION
    )

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentCommunityAddBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_community_add, container, false)
        communityViewModel = CommunityViewModel()
        fragmentCommunityAddBinding.communityViewModel = communityViewModel
        fragmentCommunityAddBinding.lifecycleOwner = this
        communityAddActivity = activity as CommunityAddActivity

        settingToolbar()
        settingTextInputLayoutCommunityAddType()
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

    // 커뮤니티 글 작성 이미지 뷰페이저 설정
    fun settingViewPager2CommunityAddImage() {
        fragmentCommunityAddBinding.apply {
            viewPager2CommunityAddImage.apply {
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
            val communityViewModel = CommunityViewModel()
            rowCommunityAddImageBinding.communityViewModel = communityViewModel
            rowCommunityAddImageBinding.lifecycleOwner = this@CommunityAddFragment

            val communityAddImageViewHolder = CommunityAddImageViewHolder(rowCommunityAddImageBinding)

            return communityAddImageViewHolder
        }

        override fun getItemCount(): Int {
            return imageCommunityAddList.size
        }

        override fun onBindViewHolder(holder: CommunityAddImageViewHolder, position: Int) {
            holder.rowCommunityAddImageBinding.imageViewRowCommunityAdd.setImageBitmap(imageCommunityAddList[position])

            holder.rowCommunityAddImageBinding.imageButtonCommunityAddDelete.setOnClickListener {
                imageCommunityAddList.removeAt(position)
                updateViewPager2CommunityAddImage()
            }
        }
    }

    fun settingImageViewCommunityAdd() {
        // 사진 첨부하기 버튼
        fragmentCommunityAddBinding.apply {
            imageViewCommunityAddDefault.setOnClickListener {
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
                        if (imageCommunityAddList.size > imageUploadPossible) {
                            Snackbar.make(fragmentCommunityAddBinding.root, "사진 첨부는 최대 10장까지 가능합니다.", Snackbar.LENGTH_SHORT).show()
                            break
                        } else {
                            val uri = uriclip.getItemAt(i).uri
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
                                imageCommunityAddList.add(bitmap3)
                            }
                        }
                    }

                    // 10개면 + 삭제
                    if (imageCommunityAddList.size >= imageUploadPossible) {
                        fragmentCommunityAddBinding.imageViewCommunityAddDefault.visibility = View.GONE
                    } else {
                        fragmentCommunityAddBinding.imageViewCommunityAddDefault.visibility = View.VISIBLE
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
        }
    }


}