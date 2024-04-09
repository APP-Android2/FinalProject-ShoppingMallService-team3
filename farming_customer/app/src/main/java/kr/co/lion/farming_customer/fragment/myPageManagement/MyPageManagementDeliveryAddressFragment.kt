package kr.co.lion.farming_customer.fragment.myPageManagement

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kr.co.lion.farming_customer.MyPageManagementName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.myPageManagement.MyPageManagementActivity
import kr.co.lion.farming_customer.databinding.FragmentMyPageManagementDeliveryAddressBinding
import kr.co.lion.farming_customer.databinding.RowMyPageManagementDeliveryAddressBinding

class MyPageManagementDeliveryAddressFragment : Fragment() {

    lateinit var fragmentMyPageManagementDeliveryAddressBinding: FragmentMyPageManagementDeliveryAddressBinding
    lateinit var myPageManagementActivity: MyPageManagementActivity
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentMyPageManagementDeliveryAddressBinding = DataBindingUtil.inflate(inflater,R.layout.fragment_my_page_management_delivery_address, container, false)
        myPageManagementActivity = activity as MyPageManagementActivity


        settingRecyclerViewDeliveryAddress()
        settingToolbar()
        settingButtonDeliveryAddressAdd()

        return fragmentMyPageManagementDeliveryAddressBinding.root
    }

    // 툴바 설정
    fun settingToolbar(){
        fragmentMyPageManagementDeliveryAddressBinding.apply {
            toolbarMyPageManagementDeliveryAddress.apply {
                // 네비게이션 Back
                setNavigationOnClickListener {
                    myPageManagementActivity.removeFragment(MyPageManagementName.MY_PAGE_MANAGEMENT_DELIVERY_ADDRESS)
                }
            }
        }
    }

    // 배송지 추가 버튼 메서드
    fun settingButtonDeliveryAddressAdd(){
        fragmentMyPageManagementDeliveryAddressBinding.apply {
            buttonMyPageManagementDeliveryAddressAdd.setOnClickListener {
                myPageManagementActivity.replaceFragment(MyPageManagementName.MY_PAGE_MANAGEMENT_ADD_DELIVERY_ADDRESS,true,true,null)
            }
        }
    }

    fun settingRecyclerViewDeliveryAddress(){
        fragmentMyPageManagementDeliveryAddressBinding.apply {
            recyclerViewMyPageManagementDeliveryAddress.apply {
                adapter = MyPageManagementDeliveryAddressRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(requireContext())
                val deco = MaterialDividerItemDecoration(requireContext(), MaterialDividerItemDecoration.VERTICAL)
                addItemDecoration(deco)
            }
        }
    }

    inner class MyPageManagementDeliveryAddressRecyclerViewAdapter:RecyclerView.Adapter<MyPageManagementDeliveryAddressRecyclerViewAdapter.MyPageManagementDeliveryyAddressViewHolder>(){

        inner class MyPageManagementDeliveryyAddressViewHolder(rowMyPageManagementDeliveryAddressBinding: RowMyPageManagementDeliveryAddressBinding):RecyclerView.ViewHolder(rowMyPageManagementDeliveryAddressBinding.root){
            val rowMyPageManagementDeliveryAddressBinding:RowMyPageManagementDeliveryAddressBinding
            init{
                this.rowMyPageManagementDeliveryAddressBinding = rowMyPageManagementDeliveryAddressBinding
                this.rowMyPageManagementDeliveryAddressBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyPageManagementDeliveryyAddressViewHolder {
            val rowMyPageManagementDeliveryAddressBinding = RowMyPageManagementDeliveryAddressBinding.inflate(layoutInflater)
            val myPageManagementDeliveryAddressViewHolder = MyPageManagementDeliveryyAddressViewHolder(rowMyPageManagementDeliveryAddressBinding)
            return myPageManagementDeliveryAddressViewHolder
        }

        override fun getItemCount(): Int {
            return 20
        }

        override fun onBindViewHolder(holder: MyPageManagementDeliveryyAddressViewHolder, position: Int) {
            holder.rowMyPageManagementDeliveryAddressBinding.textViewMyPageManagementDeliveryAddressName.text = "홍길동"
            holder.rowMyPageManagementDeliveryAddressBinding.textViewMyPageManagementDeliveryAddressPhoneNumber.text = "010-1111-2222"
            holder.rowMyPageManagementDeliveryAddressBinding.textViewMyPageManagementDeliveryAddressAddress.text = "[213213] 서울광역시 서울광역시\n서욹버ㅣㅏㅈ버이ㅏㅂ저"
            if(position==0){
                holder.rowMyPageManagementDeliveryAddressBinding.buttonMyPageManagementDeliveryAddressDefaultDeliveryAddress.isVisible = true
            }else{
                holder.rowMyPageManagementDeliveryAddressBinding.buttonMyPageManagementDeliveryAddressDefaultDeliveryAddress.isVisible = false
            }
            // 수정버튼을 눌렀을 때
            holder.rowMyPageManagementDeliveryAddressBinding.buttonMyPageManagementDeliveryAddressModify.setOnClickListener {
                myPageManagementActivity.replaceFragment(MyPageManagementName.MY_PAGE_MANAGEMENT_MODIFY_DELIVERY_ADDRESS,true,true,null)
            }
        }
    }
}