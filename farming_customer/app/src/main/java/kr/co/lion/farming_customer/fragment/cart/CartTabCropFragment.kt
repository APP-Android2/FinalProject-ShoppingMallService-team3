package kr.co.lion.farming_customer.fragment.cart


import android.content.Context
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.divider.MaterialDividerItemDecoration
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.DialogYesNo
import kr.co.lion.farming_customer.DialogYesNoInterface
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.cart.CartActivity
import kr.co.lion.farming_customer.dao.cart.CartDao
import kr.co.lion.farming_customer.databinding.FragmentCartTabCropBinding
import kr.co.lion.farming_customer.databinding.RowCartCropBinding
import kr.co.lion.farming_customer.model.cart.CartModel
import kr.co.lion.farming_customer.viewmodel.cart.MyPageCartViewModel

class CartTabCropFragment : Fragment(), DialogYesNoInterface {
    private lateinit var fragmentCartTabCropBinding: FragmentCartTabCropBinding
    lateinit var cartActivity: CartActivity
    lateinit var myPageCartViewModel: MyPageCartViewModel


    private var items = mutableListOf<CartModel>()

//     항목 갯수만큼 리스트 만들어야 함!!!!
    var checkBoxList = MutableList(0){
        false
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle?): View {
        // Inflate the layout for this fragment
        fragmentCartTabCropBinding = DataBindingUtil.inflate(inflater, R.layout.fragment_cart_tab_crop, container, false)
        myPageCartViewModel = MyPageCartViewModel()
        fragmentCartTabCropBinding.myPageCartViewModel = myPageCartViewModel
        fragmentCartTabCropBinding.lifecycleOwner = this

        cartActivity = activity as CartActivity

        settingData()
        settingRecyclerViewCartTabCrop()

        return fragmentCartTabCropBinding.root
    }

    // 장바구니 농산물 탭 리사이클러뷰 설정
    private fun settingRecyclerViewCartTabCrop() {
        val adapter = CartTabCropRecyclerViewAdapter()

        fragmentCartTabCropBinding.recyclerViewCartTabCrop.adapter = adapter
        fragmentCartTabCropBinding.recyclerViewCartTabCrop.layoutManager = LinearLayoutManager(cartActivity)
        fragmentCartTabCropBinding.recyclerViewCartTabCrop.
        addItemDecoration(MaterialDividerItemDecoration(cartActivity, MaterialDividerItemDecoration.VERTICAL))

    }


    private fun settingData(){
        CoroutineScope(Dispatchers.Main).launch {
            val sharedPreferences = cartActivity.getSharedPreferences("AutoLogin",
                Context.MODE_PRIVATE)
            val userIdx = sharedPreferences.getInt("loginUserIdx", -1)
            items = CartDao.getAllCropCart(userIdx)
            checkBoxList = MutableList(items.size){false}
            fragmentCartTabCropBinding.recyclerViewCartTabCrop.adapter?.notifyDataSetChanged()
//            Log.d("test1234", "itemList.size : ${items.size}")
//            Log.d("test1234", "items.count: ${items[0].cart_count}")
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
            val rowCartCropBinding = DataBindingUtil.inflate<RowCartCropBinding>(
                layoutInflater, R.layout.row_cart_crop, parent, false)
            val myPageCartViewModel = MyPageCartViewModel()
            rowCartCropBinding.myPageCartViewModel = myPageCartViewModel
            rowCartCropBinding.lifecycleOwner = this@CartTabCropFragment.viewLifecycleOwner

            val cartTabCropViewHolder = CartTabCropViewHolder(rowCartCropBinding)

            return cartTabCropViewHolder
        }

        override fun getItemCount(): Int {
            return items.size
        }

        override fun onBindViewHolder(holder: CartTabCropViewHolder, position: Int) {
            holder.rowCartCropBinding.myPageCartViewModel?.textViewRowCartTabCropName?.value = items[position].cart_crop_name
            holder.rowCartCropBinding.myPageCartViewModel?.textViewRowCartTabCropOption?.value = items[position].cart_crop_option
            holder.rowCartCropBinding.myPageCartViewModel?.textViewRowCartTabCropPrice?.value = items[position].cart_price
            holder.rowCartCropBinding.myPageCartViewModel?.checkBoxRowCartTabCrop?.value = checkBoxList[position]
            CoroutineScope(Dispatchers.Main).launch {
                CartDao.gettingContentImage(cartActivity, items[position].cart_image_file_name,
                    holder.rowCartCropBinding.imageViewRowCartTabCrop)
            }

            holder.rowCartCropBinding.checkBoxRowCartTabCrop.setOnCheckedChangeListener { buttonView, isChecked ->
                checkBoxList[position] = isChecked
            }

            holder.rowCartCropBinding.imageViewRowCartTabCropDelete.setOnClickListener {
                val dialog = DialogYesNo(
                    this@CartTabCropFragment,
                    null,
                    "장바구니에서 빼시겠습니까?",
                    cartActivity,
                    position
                )
                dialog.show(this@CartTabCropFragment?.parentFragmentManager!!, "DialogYesNo")
            }

            holder.rowCartCropBinding.myPageCartViewModel?.textViewRowCartTabCropBuyCount?.value = items[position].cart_count.toString()

            holder.rowCartCropBinding.buttonRowCartTabCropPlus.setOnClickListener {
                CoroutineScope(Dispatchers.Main).launch {
                    // firebase position의 개수 변경 반영
                    items[position].cart_count++
                    CartDao.updateCartCropData(items[position])
                    settingData()
                }


            }
            holder.rowCartCropBinding.buttonRowCartTabCropMinus.setOnClickListener {
                if (items[position].cart_count > 1) {
                    CoroutineScope(Dispatchers.Main).launch {
                        // firebase position의 개수 변경
                        items[position].cart_count--
                        CartDao.updateCartCropData(items[position])
                        settingData()
                    }

                }
            }
        }
    }

    override fun onYesButtonClick(id: Int) {
        fragmentCartTabCropBinding.recyclerViewCartTabCrop.adapter!!.notifyItemRemoved(id)
    }

    override fun onYesButtonClick(activity: AppCompatActivity) {

    }

}