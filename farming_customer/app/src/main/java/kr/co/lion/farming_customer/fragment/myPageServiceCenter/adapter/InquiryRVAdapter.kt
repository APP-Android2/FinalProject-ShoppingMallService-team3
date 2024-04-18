package kr.co.lion.farming_customer.fragment.myPageServiceCenter.adapter

import android.content.Context
import android.content.Intent
import android.text.Layout
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.myPageServiceCenter.MyPageServiceCenterReplyActivity
import kr.co.lion.farming_customer.databinding.RowServiceCenterInquiryBinding
import kr.co.lion.farming_customer.databinding.RowServiceCenterInquiryPhotoBinding
import kr.co.lion.farming_customer.model.myPageServiceCenterModel.InquiryModel
import kr.co.lion.farming_customer.viewmodel.myPageServiceCenter.ServiceCenterInquiryViewModel

class InquiryRVAdapter(private val context: Context, var inquiryList: MutableList<InquiryModel>) : RecyclerView.Adapter<InquiryRVAdapter.InquiryViewHolder>(){

    inner class InquiryViewHolder(val binding: RowServiceCenterInquiryBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    fun setInquiryListData(inquiryList: MutableList<InquiryModel>) {
        this.inquiryList = inquiryList
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InquiryViewHolder {
        val rowBinding = DataBindingUtil.inflate<RowServiceCenterInquiryBinding>(LayoutInflater.from(parent.context), R.layout.row_service_center_inquiry, parent, false)
        val rowInquiryViewModel = ServiceCenterInquiryViewModel()
        rowBinding.serviceCenterInquiryViewModel = rowInquiryViewModel
        rowBinding.lifecycleOwner = parent.context as LifecycleOwner
        val inquiryViewHolder = InquiryViewHolder(rowBinding)

        return inquiryViewHolder
    }

    override fun getItemCount(): Int {
        return inquiryList.size
    }

    override fun onBindViewHolder(holder: InquiryViewHolder, position: Int) {
        holder.binding.serviceCenterInquiryViewModel?.inquiryType?.value = when (inquiryList[position].inquiry_type) {
            0 -> "농산물"
            1 -> "주말농장"
            2 -> "체험활동"
            3 -> "구인구직"
            4 -> "농기구"
            5 -> "커뮤니티"
            6 -> "기타"
            else -> "기타"
        }

        holder.binding.serviceCenterInquiryViewModel?.inquiryContent?.value = inquiryList[position].inquiry_content

        if (inquiryList[position].inquiry_is_answered) {
            holder.binding.serviceCenterInquiryViewModel?.inquiryLabel?.value = "답변 완료"
            val textViewInquiryLabel = holder.binding.rowServiceCenterInquiryLabel
            textViewInquiryLabel.setBackgroundResource(R.drawable.back_round_orange)
            textViewInquiryLabel.setTextColor(ContextCompat.getColor(context, R.color.orange_01))
        } else {
            holder.binding.serviceCenterInquiryViewModel?.inquiryLabel?.value = "답변 대기중"
            val textViewInquiryLabel = holder.binding.rowServiceCenterInquiryLabel
            textViewInquiryLabel.setBackgroundResource(R.drawable.back_round)
            textViewInquiryLabel.setTextColor(ContextCompat.getColor(context, R.color.green_main))
        }

        holder.binding.root.setOnClickListener {
            val intent = Intent(context, MyPageServiceCenterReplyActivity::class.java)
            intent.putExtra("inquiryIdx", inquiryList[position].inquiry_idx)
            context.startActivity(intent)
        }
    }
}