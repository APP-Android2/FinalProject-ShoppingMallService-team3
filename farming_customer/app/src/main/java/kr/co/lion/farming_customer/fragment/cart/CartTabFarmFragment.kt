package kr.co.lion.farming_customer.fragment.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.farming_customer.DialogYesNo
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.cart.CartActivity
import kr.co.lion.farming_customer.databinding.FragmentCartTabFarmBinding
import kr.co.lion.farming_customer.databinding.RowCartFarmBinding
import kr.co.lion.farming_customer.viewmodel.cart.MyPageCartViewModel

class CartTabFarmFragment : Fragment() {
    lateinit var fragmentCartTabFarmBinding: FragmentCartTabFarmBinding
    lateinit var cartActivity: CartActivity
    lateinit var myPageCartViewModel: MyPageCartViewModel

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentCartTabFarmBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart_tab_farm, container, false)
        myPageCartViewModel = MyPageCartViewModel()
        fragmentCartTabFarmBinding.myPageCartViewModel = myPageCartViewModel
        fragmentCartTabFarmBinding.lifecycleOwner = this

        cartActivity = activity as CartActivity

        settingRecyclerViewCartTabCrop()

        return fragmentCartTabFarmBinding.root
    }

    // 장바구니 주말농장 탭 리사이클러뷰 설정
    fun settingRecyclerViewCartTabCrop() {
        fragmentCartTabFarmBinding.apply {
            recyclerViewCartTabFarm.apply {
                adapter = CartTabFarmRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(cartActivity)
            }
        }
    }

    // 장바구니 주말농장 탭 리사이클러뷰 어댑터 설정
    inner class CartTabFarmRecyclerViewAdapter : RecyclerView.Adapter<CartTabFarmRecyclerViewAdapter.CartTabFarmViewHolder>() {
        inner class CartTabFarmViewHolder(rowCartFarmBinding: RowCartFarmBinding) : RecyclerView.ViewHolder(rowCartFarmBinding.root) {
            val rowCartFarmBinding: RowCartFarmBinding

            init {
                this.rowCartFarmBinding = rowCartFarmBinding

                this.rowCartFarmBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartTabFarmViewHolder {
            val rowCartFarmBinding = DataBindingUtil.inflate<RowCartFarmBinding>(layoutInflater, R.layout.row_cart_farm, parent, false)
            val myPageCartViewModel = MyPageCartViewModel()
            rowCartFarmBinding.myPageCartViewModel = myPageCartViewModel
            rowCartFarmBinding.lifecycleOwner = this@CartTabFarmFragment

            val cartTabFarmViewHolder = CartTabFarmViewHolder(rowCartFarmBinding)

            return cartTabFarmViewHolder
        }

        override fun getItemCount(): Int {
            return 100
        }

        override fun onBindViewHolder(holder: CartTabFarmViewHolder, position: Int) {
            holder.rowCartFarmBinding.myPageCartViewModel?.textViewRowCartTabFarmName?.value = "파밍이네 감자"
            holder.rowCartFarmBinding.myPageCartViewModel?.textViewRowCartTabFarmOption?.value = "못난이 감자 5kg"
            holder.rowCartFarmBinding.myPageCartViewModel?.textViewRowCartTabFarmPrice?.value = "10,000원"

            holder.rowCartFarmBinding.imageViewRowCartTabFarmDelete.setOnClickListener {
                val dialog = DialogYesNo(null, "장바구니에서 빼시겠습니까?", cartActivity)
                dialog.show(this@CartTabFarmFragment?.parentFragmentManager!!, "DialogYesNo")
            }

            holder.rowCartFarmBinding.buttonRowCartTabFarmReservation.setOnClickListener {
                val dialog = DialogYesNo(null, "예약하시겠습니까?", cartActivity)
                dialog.show(this@CartTabFarmFragment?.parentFragmentManager!!, "DialogYesNo")
            }
        }
    }

}