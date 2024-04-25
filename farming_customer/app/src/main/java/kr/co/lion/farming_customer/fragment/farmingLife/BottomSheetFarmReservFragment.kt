package kr.co.lion.farming_customer.fragment.farmingLife

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kr.co.lion.farming_customer.DialogYesNo
import kr.co.lion.farming_customer.DialogYesNoInterface
import kr.co.lion.farming_customer.OrderProductType
import kr.co.lion.farming_customer.PaymentFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.cart.CartActivity
import kr.co.lion.farming_customer.activity.farmingLife.FarmingLifeActivity
import kr.co.lion.farming_customer.activity.payment.PaymentActivity
import kr.co.lion.farming_customer.dao.farmingLife.FarmDao
import kr.co.lion.farming_customer.databinding.FragmentBottomSheetFarmReservBinding
import kr.co.lion.farming_customer.model.farmingLife.FarmModel
import kr.co.lion.farming_customer.viewmodel.farmingLife.BottomSheetFarmReservViewModel

class BottomSheetFarmReservFragment(idx: Int) : BottomSheetDialogFragment(), DialogYesNoInterface {
    lateinit var fragmentBottomSheetDialogFragment: FragmentBottomSheetFarmReservBinding
    lateinit var farmingLifeActivity : FarmingLifeActivity
    lateinit var bottomSheetFarmReservViewModel: BottomSheetFarmReservViewModel

    val farmIdx = idx
    var farmModel : FarmModel? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentBottomSheetDialogFragment = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_bottom_sheet_farm_reserv, container, false)
        bottomSheetFarmReservViewModel = BottomSheetFarmReservViewModel()
        fragmentBottomSheetDialogFragment.bottomSheetFarmReservViewModel = bottomSheetFarmReservViewModel
        fragmentBottomSheetDialogFragment.lifecycleOwner = this
        farmingLifeActivity = activity as FarmingLifeActivity

        settingInitData()
        settingEvent()

        return fragmentBottomSheetDialogFragment.root
    }

    private fun settingInitData() {
        CoroutineScope(Dispatchers.Main).launch {
            farmModel = FarmDao.selectFarmData(farmIdx)
            settingData()
        }
    }

    private fun settingEvent() {
        fragmentBottomSheetDialogFragment.apply {
            // 장바구니 버튼
            buttonReservCart.setOnClickListener {
                val dialog = DialogYesNo(
                    this@BottomSheetFarmReservFragment,
                    "장바구니에 예약한 상품이 담겼습니다.",
                    "장바구니로 이동하시겠습니까?",
                    farmingLifeActivity
                )
                dialog.show(farmingLifeActivity.supportFragmentManager, "DialogYesNo")
            }
            // 예약하기 버튼
            buttonReservReserv.setOnClickListener {
                val intent = Intent(requireContext(), PaymentActivity::class.java)
                intent.putExtra("PaymentFragmentName", PaymentFragmentName.PAYMENT_FARM_ACTIVITY_FRAGMENT)

                val mapList = ArrayList<HashMap<String, Any>>()
                var map1 = HashMap<String, Any>()
                map1["productType"] = OrderProductType.ORDER_PRODUCT_TYPE_FARM.number
                map1["product_idx"] = farmIdx
                map1["totalPrice"] = farmModel!!.farm_option_detail["price_area"] as String
                map1["option"] = mutableMapOf<String, Any>(
                    "optionName" to "이용기간\n${farmModel!!.farm_use_date_start}~${farmModel!!.farm_use_date_end}",
                    "optionCnt" to 1,
                    "optionPrice" to farmModel!!.farm_option_detail["price_area"] as String
                )
                mapList.add(map1)
                val bundle = Bundle()
                bundle.putSerializable("mapList", mapList)
                intent.putExtra("paymentData", bundle)
                startActivity(intent)


                dismiss()
            }
        }
    }

    private fun settingData() {
        bottomSheetFarmReservViewModel!!.apply {
            textViewReserv_farmName.value = farmModel!!.farm_title
            textViewReserv_remainArea.value = farmModel!!.farm_option_detail["remain_area"]
            textViewReserv_price.value = farmModel!!.farm_option_detail["price_area"]
        }
    }

    // 다이얼로그가 만들어질 때 자동으로 호출되는 메서드
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        // 다이얼로그를 받는다.
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setOnShowListener{
            val bottomSheetDialog = it as BottomSheetDialog
            // 높이를 설정한다.
            setBottomSheetHeight(bottomSheetDialog)
        }
        return dialog
    }

    // BottomSheet의 높이를 설정해준다.
    fun setBottomSheetHeight(bottomSheetDialog: BottomSheetDialog){
        // BottomSheet의 기본 뷰 객체를 가져온다
        val bottomSheet = bottomSheetDialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)!!
        // BottomSheet 높이를 설정할 수 있는 객체를 생성한다.
        val behavior = BottomSheetBehavior.from(bottomSheet)
        // 높이를 설정한다.
        val layoutParams = bottomSheet.layoutParams
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT
        bottomSheet.layoutParams = layoutParams
        behavior.state = BottomSheetBehavior.STATE_EXPANDED

    }

    override fun onYesButtonClick(id: Int) {

    }

    override fun onYesButtonClick(activity: AppCompatActivity){
        val intent = Intent(activity.baseContext, CartActivity::class.java)
        startActivity(intent)
    }

}