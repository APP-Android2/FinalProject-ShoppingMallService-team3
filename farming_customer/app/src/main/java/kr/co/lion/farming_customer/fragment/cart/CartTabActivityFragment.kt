package kr.co.lion.farming_customer.fragment.cart

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.farming_customer.DialogYesNo
import kr.co.lion.farming_customer.DialogYesNoInterface
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.cart.CartActivity
import kr.co.lion.farming_customer.databinding.FragmentCartTabActivityBinding
import kr.co.lion.farming_customer.databinding.RowCartActivityBinding
import kr.co.lion.farming_customer.viewmodel.cart.MyPageCartViewModel

class CartTabActivityFragment : Fragment(), DialogYesNoInterface {
    lateinit var fragmentCartTabActivityBinding: FragmentCartTabActivityBinding
    lateinit var cartActivity: CartActivity
    lateinit var myPageCartViewModel: MyPageCartViewModel

    var buyCountActivity:Int = 1

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        fragmentCartTabActivityBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart_tab_activity, container, false)
        myPageCartViewModel = MyPageCartViewModel()
        fragmentCartTabActivityBinding.myPageCartViewModel = myPageCartViewModel
        fragmentCartTabActivityBinding.lifecycleOwner = this

        cartActivity = activity as CartActivity

        settingRecyclerViewCartTabActivity()

        return fragmentCartTabActivityBinding.root
    }

    // 장바구니 농산물 탭 리사이클러뷰 설정
    fun settingRecyclerViewCartTabActivity() {
        fragmentCartTabActivityBinding.apply {
            recyclerViewCartTabActivity.apply {
                adapter = CartTabActivityRecyclerViewAdapter()
                layoutManager = LinearLayoutManager(cartActivity)
            }
        }
    }

    // 장바구니 농산물 탭 리사이클러뷰 어댑터 설정
    inner class CartTabActivityRecyclerViewAdapter : RecyclerView.Adapter<CartTabActivityRecyclerViewAdapter.CartTabActivityViewHolder>() {
        inner class CartTabActivityViewHolder(rowCartActivityBinding: RowCartActivityBinding) : RecyclerView.ViewHolder(rowCartActivityBinding.root) {
            val rowCartActivityBinding: RowCartActivityBinding

            init {
                this.rowCartActivityBinding = rowCartActivityBinding

                this.rowCartActivityBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartTabActivityViewHolder {
            val rowCartActivityBinding = DataBindingUtil.inflate<RowCartActivityBinding>(layoutInflater, R.layout.row_cart_activity, parent, false)
            val myPageCartViewModel = MyPageCartViewModel()
            rowCartActivityBinding.myPageCartViewModel = myPageCartViewModel
            rowCartActivityBinding.lifecycleOwner = this@CartTabActivityFragment

            val cartTabCropViewHolder = CartTabActivityViewHolder(rowCartActivityBinding)

            return cartTabCropViewHolder
        }

        override fun getItemCount(): Int {
            return 100
        }

        override fun onBindViewHolder(holder: CartTabActivityViewHolder, position: Int) {
            holder.rowCartActivityBinding.myPageCartViewModel?.textViewRowCartTabActivityName?.value = "파밍이네 감자"
            holder.rowCartActivityBinding.myPageCartViewModel?.textViewRowCartTabActivityOption?.value = "못난이 감자 5kg"
            holder.rowCartActivityBinding.myPageCartViewModel?.textViewRowCartTabActivityPrice?.value = "10,000원"

            holder.rowCartActivityBinding.imageViewRowCartTabActivityDelete.setOnClickListener {
                val dialog = DialogYesNo(
                    this@CartTabActivityFragment,
                    null,
                    "장바구니에서 빼시겠습니까?",
                    cartActivity,
                    position
                )
                dialog.show(this@CartTabActivityFragment?.parentFragmentManager!!, "DialogYesNo")
            }

            holder.rowCartActivityBinding.myPageCartViewModel?.textViewRowCartTabActivityBuyCount?.value = buyCountActivity.toString()

            holder.rowCartActivityBinding.buttonRowCartTabActivityPlus.setOnClickListener {
                buyCountActivity++
                holder.rowCartActivityBinding.myPageCartViewModel?.textViewRowCartTabActivityBuyCount?.value = buyCountActivity.toString()
            }
            holder.rowCartActivityBinding.buttonRowCartTabActivityMinus.setOnClickListener {
                if (buyCountActivity > 1) {
                    buyCountActivity--
                    holder.rowCartActivityBinding.myPageCartViewModel?.textViewRowCartTabActivityBuyCount?.value = buyCountActivity.toString()
                }
            }

            holder.rowCartActivityBinding.buttonRowCartTabActivityReservation.setOnClickListener {
                val dialog = DialogYesNo(
                    this@CartTabActivityFragment,
                    null,
                    "예약하시겠습니까?",
                    cartActivity,
                    position
                )
                dialog.show(this@CartTabActivityFragment?.parentFragmentManager!!, "DialogYesNo")
            }
        }
    }

    override fun onYesButtonClick(id: Int) {
        // 결제 화면으로 넘어가기
    }

    override fun onYesButtonClick(activity: AppCompatActivity) {

    }
}