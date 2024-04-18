package kr.co.lion.farming_customer.fragment.myPageServiceCenter.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.databinding.RowServiceCenterNoticeBinding
import kr.co.lion.farming_customer.model.myPageServiceCenterModel.NoticeModel
import kr.co.lion.farming_customer.viewmodel.myPageServiceCenter.ServiceCenterNoticeViewModel

class NoticeRVAdapter(val context: Context, private var noticeList : MutableList<NoticeModel>): RecyclerView.Adapter<NoticeRVAdapter.NoticeViewHolder>() {
    private var expandedPosition = -1

    inner class NoticeViewHolder(val binding: RowServiceCenterNoticeBinding): RecyclerView.ViewHolder(binding.root), View.OnClickListener{
        init {
            binding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )

            // 항목 클릭 시 onClick 함수 호출
            binding.root.setOnClickListener(this)
        }

        override fun onClick(v: View?) {
            val position = adapterPosition
            // 현재 클릭된 항목의 확정 여부를 업데이트 후 리사이클러뷰 갱신
            expandedPosition = if (expandedPosition == position) -1 else position
            notifyDataSetChanged()
        }
    }

    // 데이터를 설정하고 어댑터를 갱신
    fun setData(noticeList: MutableList<NoticeModel>) {
        this.noticeList = noticeList
        notifyDataSetChanged()
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val rowBinding = DataBindingUtil.inflate<RowServiceCenterNoticeBinding>(LayoutInflater.from(parent.context), R.layout.row_service_center_notice, parent, false )
        val rowNoticeViewModel = ServiceCenterNoticeViewModel()
        rowBinding.serviceCenterNoticeViewModel = rowNoticeViewModel
        rowBinding.lifecycleOwner = parent.context as LifecycleOwner
        val noticeViewHolder = NoticeViewHolder(rowBinding)

        return noticeViewHolder
    }

    override fun getItemCount(): Int {
        return noticeList.size
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.binding.serviceCenterNoticeViewModel?.noticeTitle?.value = noticeList[position].notice_title
        holder.binding.serviceCenterNoticeViewModel?.noticeContent?.value = noticeList[position].notice_content
        holder.binding.serviceCenterNoticeViewModel?.noticeRegDt?.value = noticeList[position].notice_reg_dt

        holder.binding.rowServiceCenterNoticeContentLayout.visibility =
            if (expandedPosition == position) View.VISIBLE else View.GONE


        if (noticeList[position].notice_fix) {
            val textViewNoticeTitle = holder.binding.rowServiceCenterNoticeTitle
            textViewNoticeTitle.setTextColor(ContextCompat.getColor(context, R.color.orange_01))
        } else {
            val textViewNoticeTitle = holder.binding.rowServiceCenterNoticeTitle
            textViewNoticeTitle.setTextColor(ContextCompat.getColor(context, R.color.brown_01))
        }

        // 버튼 이미지 설정
        if (expandedPosition == position) {
            // 열린 상태일 때
            holder.binding.rowServiceCenterNoticeOpenBtn.setImageResource(R.drawable.ic_notice_close_btn)
        } else {
            // 닫힌 상태일 때
            holder.binding.rowServiceCenterNoticeOpenBtn.setImageResource(R.drawable.ic_notice_open_btn)
        }
    }
}