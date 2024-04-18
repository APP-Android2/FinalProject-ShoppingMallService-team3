package kr.co.lion.farming_customer.fragment.myPageMyPost

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.myPageMyPost.MyPageMyPostActivity
import kr.co.lion.farming_customer.databinding.FragmentMyPageMyPostBoardBinding
import kr.co.lion.farming_customer.databinding.RowMyPageMyPostBinding
import kr.co.lion.farming_customer.viewmodel.myPageMyPost.MyPostBoardViewModel

class MyPageMyPostBoardFragment : Fragment() {

    lateinit var binding : FragmentMyPageMyPostBoardBinding
    lateinit var  myPageMyPostActivity: MyPageMyPostActivity

    lateinit var myPostBoardViewModel : MyPostBoardViewModel
//    lateinit var boardAdapter: BoardAdapter


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_my_page_my_post_board, container, false)
        myPostBoardViewModel = MyPostBoardViewModel()

        binding.lifecycleOwner = this
        myPageMyPostActivity = activity as MyPageMyPostActivity

        settingRecyclerMain()

        return binding.root
    }

    // RecyclerView 설정
    fun settingRecyclerMain() {
        binding.apply {
            myPostRecyclerView.apply {
                // adapter 설정
                adapter = RecyclerViewAdapter()
                // 레이아웃 매니저 설정 (RecyclerView 의 항목을 보여줄 방식을 설정)
                layoutManager = LinearLayoutManager(myPageMyPostActivity)
            }
        }
    }

    inner class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.ViewHolderClass>() {
        inner class ViewHolderClass(rowBinding: RowMyPageMyPostBinding) :
            RecyclerView.ViewHolder(rowBinding.root) {
            val rowBinding: RowMyPageMyPostBinding
            init {
                this.rowBinding = rowBinding
                rowBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }

        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolderClass {
            val rowBinding = DataBindingUtil.inflate<RowMyPageMyPostBinding>(layoutInflater, R.layout.row_my_page_my_post, parent, false)
            val rowViewModel = MyPostBoardViewModel()
            rowBinding.myPageMyPostViewModel = rowViewModel
            rowBinding.lifecycleOwner = this@MyPageMyPostBoardFragment

            val RowViewHolder = ViewHolderClass(rowBinding)
            return RowViewHolder

        }

        override fun getItemCount(): Int {
            return 10
        }

        override fun onBindViewHolder(holder: ViewHolderClass, position: Int) {

        }
    }
}