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
import kr.co.lion.farming_customer.databinding.FragmentCartTabCropBinding
import kr.co.lion.farming_customer.databinding.RowCartCropBinding
import kr.co.lion.farming_customer.viewmodel.cart.MyPageCartViewModel

class CartTabCropFragment : Fragment() {
    lateinit var fragmentCartTabCropBinding: FragmentCartTabCropBinding
    lateinit var cartActivity: CartActivity
    lateinit var myPageCartViewModel: MyPageCartViewModel

    var buyCountCrop:Int = 1
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentCartTabCropBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart_tab_crop, container, false)
        myPageCartViewModel = MyPageCartViewModel()
        fragmentCartTabCropBinding.myPageCartViewModel = myPageCartViewModel
        fragmentCartTabCropBinding.lifecycleOwner = this

        cartActivity = activity as CartActivity

        settingRecyclerViewCartTabCrop()

        return fragmentCartTabCropBinding.root
    }

    // 장바구니 농산물 탭 리사이클러뷰 설정
    fun settingRecyclerViewCartTabCrop() {
        fragmentCartTabCropBinding.apply {
            recyclerViewCartTabCrop.apply {
                adapter = CartTabCropRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(cartActivity)
            }
        }
    }

    // 장바구니 농산물 탭 리사이클러뷰 어댑터 설정
    inner class CartTabCropRecyclerViewAdapter : RecyclerView.Adapter<CartTabCropRecyclerViewAdapter.CartTabCropViewHolder>() {
        inner class CartTabCropViewHolder(rowCartCropBinding: RowCartCropBinding) : RecyclerView.ViewHolder(rowCartCropBinding.root) {
            val rowCartCropBinding: RowCartCropBinding

            init {
                this.rowCartCropBinding = rowCartCropBinding

                this.rowCartCropBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartTabCropViewHolder {
            val rowCartCropBinding = DataBindingUtil.inflate<RowCartCropBinding>(layoutInflater, R.layout.row_cart_crop, parent, false)
            val myPageCartViewModel = MyPageCartViewModel()
            rowCartCropBinding.myPageCartViewModel = myPageCartViewModel
            rowCartCropBinding.lifecycleOwner = this@CartTabCropFragment

            val cartTabCropViewHolder = CartTabCropViewHolder(rowCartCropBinding)

            return cartTabCropViewHolder
        }

        override fun getItemCount(): Int {
            return 100
        }

        override fun onBindViewHolder(holder: CartTabCropViewHolder, position: Int) {
            holder.rowCartCropBinding.myPageCartViewModel?.textViewRowCartTabCropName?.value = "파밍이네 감자"
            holder.rowCartCropBinding.myPageCartViewModel?.textViewRowCartTabCropOption?.value = "못난이 감자 5kg"
            holder.rowCartCropBinding.myPageCartViewModel?.textViewRowCartTabCropPrice?.value = "10,000원"

            holder.rowCartCropBinding.imageViewRowCartTabCropDelete.setOnClickListener {
                val dialog = DialogYesNo(null, "장바구니에서 빼시겠습니까?", cartActivity)
                dialog.show(this@CartTabCropFragment?.parentFragmentManager!!, "DialogYesNo")
            }

            holder.rowCartCropBinding.myPageCartViewModel?.textViewRowCartTabCropBuyCount?.value = buyCountCrop.toString()

            holder.rowCartCropBinding.buttonRowCartTabCropPlus.setOnClickListener {
                buyCountCrop++
                holder.rowCartCropBinding.myPageCartViewModel?.textViewRowCartTabCropBuyCount?.value = buyCountCrop.toString()
            }
            holder.rowCartCropBinding.buttonRowCartTabCropMinus.setOnClickListener {
                if (buyCountCrop > 1) {
                    buyCountCrop--
                    holder.rowCartCropBinding.myPageCartViewModel?.textViewRowCartTabCropBuyCount?.value = buyCountCrop.toString()
                }
            }
        }
    }
}