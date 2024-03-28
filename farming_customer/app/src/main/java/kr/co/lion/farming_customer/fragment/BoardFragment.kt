package kr.co.lion.farming_customer.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.databinding.FragmentBoardBinding
import kr.co.lion.farming_customer.viewmodel.BoardViewModel


class BoardFragment : Fragment() {
    lateinit var fragmentBoardBinding: FragmentBoardBinding
    lateinit var mainActivity: MainActivity

    lateinit var boardViewModel: BoardViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentBoardBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_board, container, false)
        boardViewModel = BoardViewModel()
        fragmentBoardBinding.boardViewModel = boardViewModel
        fragmentBoardBinding.lifecycleOwner = this
        mainActivity = activity as MainActivity

        return fragmentBoardBinding.root
    }
}