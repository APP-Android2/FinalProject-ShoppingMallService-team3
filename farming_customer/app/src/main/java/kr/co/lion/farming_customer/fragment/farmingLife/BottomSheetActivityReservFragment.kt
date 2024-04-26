package kr.co.lion.farming_customer.fragment.farmingLife

import android.app.Dialog
import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CalendarView
import android.widget.CalendarView.OnDateChangeListener
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.divider.MaterialDividerItemDecoration
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
import kr.co.lion.farming_customer.dao.farmingLife.ActivityDao
import kr.co.lion.farming_customer.databinding.DropdownItemBottomSheetProgramBinding
import kr.co.lion.farming_customer.databinding.FragmentBottomSheetActivityReservBinding
import kr.co.lion.farming_customer.databinding.RowBottomSheetActivityReservOptionBinding
import kr.co.lion.farming_customer.databinding.RowBottomSheetActivityReservSelectedOptionBinding
import kr.co.lion.farming_customer.model.SelectedOptionModel
import kr.co.lion.farming_customer.model.farmingLife.ActivityModel
import kr.co.lion.farming_customer.viewmodel.farmingLife.BottomSheetActivityReservViewModel
import kr.co.lion.farming_customer.viewmodel.farmingLife.DropdownItemBottmSheetProgramViewModel
import kr.co.lion.farming_customer.viewmodel.farmingLife.RowBottomSheetActivityReservOptionViewModel
import kr.co.lion.farming_customer.viewmodel.farmingLife.RowBottomSheetActivityReservSelectedOptionViewModel
import org.checkerframework.checker.units.qual.s
import java.text.DecimalFormat
import java.text.SimpleDateFormat

class BottomSheetActivityReservFragment(idx: Int) : BottomSheetDialogFragment(), DialogYesNoInterface {
    lateinit var fragmentBottomSheetActivityReservBinding: FragmentBottomSheetActivityReservBinding
    lateinit var farmingLifeActivity : FarmingLifeActivity
    lateinit var bottomSheetActivityReservViewModel: BottomSheetActivityReservViewModel

    val selectedOptionList = mutableListOf<SelectedOptionModel>()
    var selectedOption = SelectedOptionModel()

    val activityIdx = idx
    var activityModel : ActivityModel? = null

    var activity_optionNameList = mutableListOf<String>()
    var activity_optionPriceList = mutableListOf<String>()
    var activity_optionTimeList = mutableListOf<MutableList<String>>()
    var activity_recruit = mutableListOf<String>()

    var option_time_pos = -1
    var date = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        fragmentBottomSheetActivityReservBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_bottom_sheet_activity_reserv, container, false)
        bottomSheetActivityReservViewModel = BottomSheetActivityReservViewModel()
        fragmentBottomSheetActivityReservBinding.bottomSheetActivityReservViewModel = bottomSheetActivityReservViewModel
        fragmentBottomSheetActivityReservBinding.lifecycleOwner = this
        farmingLifeActivity = activity as FarmingLifeActivity

        settingInitData()
        settingEvent()



        return fragmentBottomSheetActivityReservBinding.root
    }

    private fun settingInitData() {
        CoroutineScope(Dispatchers.Main).launch {
            activityModel = ActivityDao.selectActivityData(activityIdx)
            activityModel!!.activity_option_detail.forEach {
                activity_optionNameList.add(it["option_name"] as String)
                activity_optionPriceList.add(it["option_price"] as String)
                activity_recruit.add(it["option_recruit"] as String)
                activity_optionTimeList.add(it["option_time"] as MutableList<String>)
            }
            settingData()
            settingRecyclerView()
        }
    }

    private fun settingRecyclerView() {
        fragmentBottomSheetActivityReservBinding.apply {
            recyclerViewOption.apply {
                adapter = OptionAdapter()
                layoutManager = LinearLayoutManager(farmingLifeActivity, LinearLayoutManager.HORIZONTAL, false)
                addItemDecoration(itemDecoration(Right = 20))
            }
            recyclerViewDropDownMenu.apply {
                adapter = DropdownAdapter()
                layoutManager = LinearLayoutManager(farmingLifeActivity)
                addItemDecoration(MaterialDividerItemDecoration(farmingLifeActivity, RecyclerView.VERTICAL))
                addItemDecoration(itemDecoration(Top = 30, Bottom = 30))
            }
            recyclerViewSelected.apply {
                adapter = SelectedOptionAdapter()
                layoutManager = LinearLayoutManager(farmingLifeActivity)
                addItemDecoration(MaterialDividerItemDecoration(farmingLifeActivity, RecyclerView.VERTICAL))
            }
        }
    }
    // 옵션 리사이클러 아이템 데코레이션
    inner class itemDecoration(val Left : Int = 0, val Right : Int = 0, val Top : Int = 0, val Bottom : Int = 0) : RecyclerView.ItemDecoration() {
        override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
            // 여백 설정
            outRect.apply {
                left = Left
                right = Right
                top = Top
                bottom = Bottom
            }
        }
    }

    private fun settingData() {
        fragmentBottomSheetActivityReservBinding.apply {
            bottomSheetActivityReservViewModel!!.textView_option.value = "프로그램 선택"
        }
    }

    private fun settingEvent() {
        fragmentBottomSheetActivityReservBinding.apply {
            // 날짜 선택
            calendarTextLayout.setOnClickListener {
                calendar.visibility = View.VISIBLE
            }
            buttonDone.setOnClickListener {
                if(date == ""){
                    val dateFormat = SimpleDateFormat("yyyy.MM.dd")
                    bottomSheetActivityReservViewModel!!.textView_selectedDate.value = dateFormat.format(calendarView.date)
                }else{
                    bottomSheetActivityReservViewModel!!.textView_selectedDate.value = date
                }
                calendar.visibility = View.INVISIBLE
                textinputOption.visibility = View.VISIBLE

            }
            calendarView.setOnDateChangeListener(object : OnDateChangeListener{
                override fun onSelectedDayChange(p0: CalendarView, year: Int, month: Int, day: Int) {
                    date = "$year.${month+1}.$day"
                    bottomSheetActivityReservViewModel!!.textView_selectedDate.value = date
                }
            })

            // 프로그램 선택
            textinputOption.setOnClickListener {
                recyclerViewDropDownMenu.visibility = View.VISIBLE
            }

            // 장바구니 버튼
            buttonActivityReservCart.setOnClickListener {
                buttonActivityReservCart.setOnClickListener {
                    val dialog = DialogYesNo(
                        this@BottomSheetActivityReservFragment,
                        "장바구니에 예약한 상품이 담겼습니다.",
                        "장바구니로 이동하시겠습니까?",
                        farmingLifeActivity
                    )
                    dialog.show(farmingLifeActivity.supportFragmentManager, "DialogYesNo")
                }
            }
            // 예약하기 버튼
            buttonActivityReservReserv.setOnClickListener {
                val intent = Intent(requireContext(), PaymentActivity::class.java)
                intent.putExtra("PaymentFragmentName", PaymentFragmentName.PAYMENT_FARM_ACTIVITY_FRAGMENT)

                val mapList = ArrayList<HashMap<String, Any>>()
                var map1 = HashMap<String, Any>()
                map1["productType"] = OrderProductType.ORDER_PRODUCT_TYPE_ACTIVITY.number
                map1["product_idx"] = activityIdx
                map1["totalPrice"] = bottomSheetActivityReservViewModel!!.textView_totalPrice.value.toString()
                map1["option"] = mutableListOf<MutableMap<String, Any>>()
                for(i in 0..<selectedOptionList.size){
                    val map_option = mutableMapOf<String, Any>(
                        "optionName" to selectedOptionList[i].programName,
                        "optionPrice" to selectedOptionList[i].price,
                        "optionCnt" to selectedOptionList[i].cnt,
                        "optionTime" to selectedOptionList[i].time,
                        "optionTotalPrice" to DecimalFormat("#,###").format(selectedOptionList[i].price.replace(",", "").replace("원", "").toInt() * selectedOptionList[i].cnt) + "원"
                    )
                    (map1["option"] as MutableList<MutableMap<String, Any>>).add(map_option)
                }
                mapList.add(map1)

                val bundle = Bundle()
                bundle.putSerializable("mapList", mapList)
                intent.putExtra("paymentData", bundle)
                startActivity(intent)

                dismiss()
            }
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

    // 옵션 리사이클러 어댑터
    inner class OptionAdapter : RecyclerView.Adapter<OptionAdapter.OptionViewHolder>(){
        inner class OptionViewHolder(rowBottomSheetActivityReservOptionBinding: RowBottomSheetActivityReservOptionBinding) : RecyclerView.ViewHolder(rowBottomSheetActivityReservOptionBinding.root){
            val rowBottomSheetActivityReservOptionBinding : RowBottomSheetActivityReservOptionBinding
            init {
                this.rowBottomSheetActivityReservOptionBinding = rowBottomSheetActivityReservOptionBinding

                rowBottomSheetActivityReservOptionBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OptionViewHolder {
            val rowBottomSheetActivityReservOptionBinding = DataBindingUtil.inflate<RowBottomSheetActivityReservOptionBinding>(layoutInflater, R.layout.row_bottom_sheet_activity_reserv_option, parent, false)
            val rowBottomSheetActivityReservOptionViewModel = RowBottomSheetActivityReservOptionViewModel()
            rowBottomSheetActivityReservOptionBinding.rowBottomSheetActivityReservOptionViewModel = rowBottomSheetActivityReservOptionViewModel
            rowBottomSheetActivityReservOptionBinding.lifecycleOwner = this@BottomSheetActivityReservFragment

            val OptionViewHolder = OptionViewHolder(rowBottomSheetActivityReservOptionBinding)
            return OptionViewHolder
        }

        override fun getItemCount(): Int {
            return activity_optionTimeList[option_time_pos].size
        }

        override fun onBindViewHolder(holder: OptionViewHolder, position: Int) {
            holder.rowBottomSheetActivityReservOptionBinding.apply {
                rowBottomSheetActivityReservOptionViewModel!!.apply {

                    textView_time.value = activity_optionTimeList[option_time_pos][position]
                    textView_cnt.value = activity_recruit[option_time_pos]
                }
            }
            // 아아템 클릭 이벤트
            holder.rowBottomSheetActivityReservOptionBinding.apply {
                root.setOnClickListener {
                    selectedOption.time = "${date} ${rowBottomSheetActivityReservOptionViewModel!!.textView_time.value!!}"
                    selectedOption.cnt = 1

                    if(bottomSheetActivityReservViewModel.textView_totalPrice.value != null){
                        var price_int = bottomSheetActivityReservViewModel.textView_totalPrice.value!!.replace(",", "").replace("원", "").toInt() + selectedOption.price.replace(",", "").replace("원", "").toInt()
                        val t_dec_up = DecimalFormat("#,###")
                        var str_change_money_up = t_dec_up.format(price_int)
                        var price = str_change_money_up + "원"
                        bottomSheetActivityReservViewModel.textView_totalPrice.value = price
                    }else{
                        var price_int = selectedOption.price.replace(",", "").replace("원", "").toInt()
                        val t_dec_up = DecimalFormat("#,###")
                        var str_change_money_up = t_dec_up.format(price_int)
                        var price = str_change_money_up + "원"
                        bottomSheetActivityReservViewModel.textView_totalPrice.value = price
                    }

                    selectedOptionList.add(selectedOption)
                    selectedOption = SelectedOptionModel()

                    fragmentBottomSheetActivityReservBinding.apply {
                        recyclerViewOption.visibility = View.GONE
                        textView14.visibility = View.VISIBLE
                        textViewTotalPrice.visibility = View.VISIBLE
                        buttonActivityReservCart.visibility = View.VISIBLE
                        buttonActivityReservReserv.visibility = View.VISIBLE
                        recyclerViewSelected.visibility = View.VISIBLE
                    }


                    // 업데이트
                    fragmentBottomSheetActivityReservBinding.recyclerViewSelected.adapter?.notifyDataSetChanged()
                }
            }
        }
    }
    // 드롭다운 리사이클러뷰 어댑터
    inner class DropdownAdapter : RecyclerView.Adapter<DropdownAdapter.DropdownViewHolder>(){
        inner class DropdownViewHolder(dropdownItemBottomSheetProgramBinding: DropdownItemBottomSheetProgramBinding) : RecyclerView.ViewHolder(dropdownItemBottomSheetProgramBinding.root){
            val dropdownItemBottomSheetProgramBinding : DropdownItemBottomSheetProgramBinding
            init {
                this.dropdownItemBottomSheetProgramBinding = dropdownItemBottomSheetProgramBinding

                dropdownItemBottomSheetProgramBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DropdownViewHolder {
            val dropdownItemBottomSheetProgramBinding = DataBindingUtil.inflate<DropdownItemBottomSheetProgramBinding>(layoutInflater, R.layout.dropdown_item_bottom_sheet_program, parent, false)
            val dropdownItemBottomSheetProgramViewModel = DropdownItemBottmSheetProgramViewModel()
            dropdownItemBottomSheetProgramBinding.dropDownItemBottomSheetProgramViewModel = dropdownItemBottomSheetProgramViewModel
            dropdownItemBottomSheetProgramBinding.lifecycleOwner = this@BottomSheetActivityReservFragment

            val dropdownViewHolder = DropdownViewHolder(dropdownItemBottomSheetProgramBinding)
            return dropdownViewHolder
        }

        override fun getItemCount(): Int {
            return activity_optionNameList.size
        }

        override fun onBindViewHolder(holder: DropdownViewHolder, position: Int) {
            holder.dropdownItemBottomSheetProgramBinding.apply {
                dropDownItemBottomSheetProgramViewModel!!.apply {
                    dropDownItem_programName.value =activity_optionNameList[position]
                    dropDownItem_price.value = activity_optionPriceList[position]
                    option_time_pos = position
                    fragmentBottomSheetActivityReservBinding.recyclerViewOption.adapter?.notifyDataSetChanged()
                }
            }
            // 아이템 클릭 이벤트
            holder.dropdownItemBottomSheetProgramBinding.apply{
                root.setOnClickListener {
                    bottomSheetActivityReservViewModel!!.apply {
                        textView_option.value = "${dropDownItemBottomSheetProgramViewModel!!.dropDownItem_programName.value} ${dropDownItemBottomSheetProgramViewModel!!.dropDownItem_price.value}"
                        selectedOption.programName = dropDownItemBottomSheetProgramViewModel!!.dropDownItem_programName.value!!
                        selectedOption.price = dropDownItemBottomSheetProgramViewModel!!.dropDownItem_price.value!!

                        fragmentBottomSheetActivityReservBinding.recyclerViewDropDownMenu.visibility = View.GONE
                        fragmentBottomSheetActivityReservBinding.recyclerViewOption.visibility = View.VISIBLE
                    }
                }
            }
        }
    }

    // 선택된 옵션 리사이클러뷰 어댑터
    inner class SelectedOptionAdapter : RecyclerView.Adapter<SelectedOptionAdapter.SelectedOptionViewHolder>(){
        inner class SelectedOptionViewHolder(rowBottomSheetActivityReservSelectedOptionBinding: RowBottomSheetActivityReservSelectedOptionBinding) : RecyclerView.ViewHolder(rowBottomSheetActivityReservSelectedOptionBinding.root){
            val rowBottomSheetActivityReservSelectedOptionBinding : RowBottomSheetActivityReservSelectedOptionBinding

            init {
                this.rowBottomSheetActivityReservSelectedOptionBinding = rowBottomSheetActivityReservSelectedOptionBinding

                rowBottomSheetActivityReservSelectedOptionBinding.root.layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                )
            }
        }

        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): SelectedOptionViewHolder {
            val rowBottomSheetActivityReservSelectedOptionBinding = DataBindingUtil.inflate<RowBottomSheetActivityReservSelectedOptionBinding>(layoutInflater, R.layout.row_bottom_sheet_activity_reserv_selected_option, parent, false)
            val rowBottomSheetActivityReservOptionViewModel = RowBottomSheetActivityReservSelectedOptionViewModel()
            rowBottomSheetActivityReservSelectedOptionBinding.rowBottomSheetActivityReservSelectedOptionViewModel = rowBottomSheetActivityReservOptionViewModel
            rowBottomSheetActivityReservSelectedOptionBinding.lifecycleOwner = this@BottomSheetActivityReservFragment

            val selectedOptionViewHolder = SelectedOptionViewHolder(rowBottomSheetActivityReservSelectedOptionBinding)
            return selectedOptionViewHolder
        }

        override fun getItemCount(): Int {
            return selectedOptionList.size
        }

        override fun onBindViewHolder(holder: SelectedOptionViewHolder, position: Int) {
            holder.rowBottomSheetActivityReservSelectedOptionBinding.rowBottomSheetActivityReservSelectedOptionViewModel!!.apply {
                textView_programName.value = selectedOptionList[position].programName
                textView_date.value = selectedOptionList[position].date
                textView_time.value = selectedOptionList[position].time
                textView_price.value =  selectedOptionList[position].price
                textView_cnt.value = selectedOptionList[position].cnt.toString()
            }

            // -, + 버튼 리스너
            holder.rowBottomSheetActivityReservSelectedOptionBinding.apply {
                buttonMinus.setOnClickListener {
                    rowBottomSheetActivityReservSelectedOptionViewModel!!.apply {
                        textView_cnt.value = (rowBottomSheetActivityReservSelectedOptionViewModel!!.textView_cnt.value!!.toInt() - 1).toString()
                        selectedOptionList[position].cnt --
                        if(textView_cnt.value == "0"){
                            // 갯수가 0이 되면 아이템 삭제
                            selectedOptionList.removeAt(position)
                            notifyDataSetChanged()
                        }
                        var price_int = bottomSheetActivityReservViewModel.textView_totalPrice.value!!.replace(",", "").replace("원", "").toInt() - textView_price.value!!.replace(",", "").replace("원", "").toInt()
                        val t_dec_up = DecimalFormat("#,###")
                        var str_change_money_up = t_dec_up.format(price_int)
                        var price = str_change_money_up + "원"
                        bottomSheetActivityReservViewModel.textView_totalPrice.value = price
                    }
                }
                buttonPlus.setOnClickListener {
                    rowBottomSheetActivityReservSelectedOptionViewModel!!.apply {
                        textView_cnt.value = (rowBottomSheetActivityReservSelectedOptionViewModel!!.textView_cnt.value!!.toInt() + 1).toString()
                        selectedOptionList[position].cnt ++
                        var price_int = bottomSheetActivityReservViewModel.textView_totalPrice.value!!.replace(",", "").replace("원", "").toInt() + textView_price.value!!.replace(",", "").replace("원", "").toInt()
                        val t_dec_up = DecimalFormat("#,###")
                        var str_change_money_up = t_dec_up.format(price_int)
                        var price = str_change_money_up + "원"
                        bottomSheetActivityReservViewModel.textView_totalPrice.value = price
                    }


                }
            }
        }
    }

    override fun onYesButtonClick(id: Int) {

    }

    override fun onYesButtonClick(activity: AppCompatActivity) {
        val intent = Intent(activity.baseContext, CartActivity::class.java)
        startActivity(intent)
    }

}