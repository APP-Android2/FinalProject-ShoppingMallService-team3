package kr.co.lion.farming_customer.fragment.myPageServiceCenter.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.farming_customer.databinding.RowServiceCenterInquiryBinding

class InquiryRVAdapter : RecyclerView.Adapter<InquiryRVAdapter.InquiryViewHolder>(){

    inner class InquiryViewHolder(val binding: RowServiceCenterInquiryBinding): RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): InquiryViewHolder {
        val binding = RowServiceCenterInquiryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val inquiryViewHolder = InquiryViewHolder(binding)

        return inquiryViewHolder
    }

    override fun getItemCount(): Int {
        return 20
    }

    override fun onBindViewHolder(holder: InquiryViewHolder, position: Int) {
        holder.binding.rowServiceCenterInquiryTitle.text = "문의 제목 $position"
        holder.binding.rowServiceCenterInquiryContent.text = "글 내용 $position"
    }
}