package kr.co.lion.farming_customer.fragment.dialog

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.DialogFragment
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.databinding.FragmentDialogLoginBinding

class DialogLoginFragment : DialogFragment() {

    lateinit var fragmentDialogLoginBinding: FragmentDialogLoginBinding
    lateinit var mainActivity: MainActivity

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentDialogLoginBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_dialog_login, container, false)
        mainActivity = activity as MainActivity

        buttonDialogLogin()

        return fragmentDialogLoginBinding.root
    }

    // 로그인 하러 가기 버튼 눌렀을 때
    fun buttonDialogLogin() {
        fragmentDialogLoginBinding.apply {
            // 로그인 버튼
            buttonDialogLogin.setOnClickListener {
                // 다이얼로그를 내린다.
                dismiss()
                // 로그인 화면으로 간다.
                // TODO: 로그인 액티비티 전환
            }

            // 취소 버튼
            buttonDialogCancel.setOnClickListener {
                dismiss()
            }
        }
    }

}