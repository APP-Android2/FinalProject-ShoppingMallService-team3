package kr.co.lion.farming_customer.fragment.loginRegister

import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.style.ForegroundColorSpan
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import kr.co.lion.farming_customer.LoginFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.loginRegister.LoginActivity
import kr.co.lion.farming_customer.databinding.FragmentFindPwDone2Binding

class FindPwDone2Fragment : Fragment() {

    lateinit var fragmentFindPwDone2Binding: FragmentFindPwDone2Binding
    lateinit var loginActivity: LoginActivity


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        fragmentFindPwDone2Binding = DataBindingUtil.inflate(inflater, R.layout.fragment_find_pw_done2, container, false)
        fragmentFindPwDone2Binding.lifecycleOwner = this

        loginActivity = activity as LoginActivity

        settingButton()
        settingTextView()

        return fragmentFindPwDone2Binding.root
    }

    fun settingButton() {
        fragmentFindPwDone2Binding.apply {
            buttonFindPwDone2.setOnClickListener {
                loginActivity.replaceFragment(LoginFragmentName.LOGIN_FRAGMENT,
                    addToBackStack = false, isAnimate = true, data = null)
            }
        }
    }

    // 텍스트 설정
    fun settingTextView(){
        fragmentFindPwDone2Binding.apply {
            val fullText = textView6.text.toString()
            val spannableString = SpannableString(fullText)

            // 색상 리소스 값 가져오기
            val greenColor = ContextCompat.getColor(loginActivity, R.color.green_main)

            // "비밀번호" 부분의 시작과 끝 인덱스 값을 찾음
            val start = fullText.indexOf("완료")
            val end = start + "완료".length

            // SpannableString에 색상을 적용
            spannableString.setSpan(ForegroundColorSpan(greenColor), start, end, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            textView6.text = spannableString
        }
    }
}