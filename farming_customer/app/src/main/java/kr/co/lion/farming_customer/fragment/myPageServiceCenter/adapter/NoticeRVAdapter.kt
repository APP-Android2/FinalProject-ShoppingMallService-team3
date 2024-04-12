package kr.co.lion.farming_customer.fragment.myPageServiceCenter.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.databinding.RowServiceCenterNoticeBinding

class NoticeRVAdapter: RecyclerView.Adapter<NoticeRVAdapter.NoticeViewHolder>() {
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


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoticeViewHolder {
        val binding = RowServiceCenterNoticeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val noticeViewHolder = NoticeViewHolder(binding)

        return noticeViewHolder
    }

    override fun getItemCount(): Int {
        return 20
    }

    override fun onBindViewHolder(holder: NoticeViewHolder, position: Int) {
        holder.binding.rowServiceCenterNoticeTitle.text = "공지사항 $position"
        holder.binding.rowServiceCenterNoticeContent.text = "공지사항 ${position}번 공지 내용 입니다"
        holder.binding.rowServiceCenterNoticeDate.text = "2024.03.01"

        holder.binding.rowServiceCenterNoticeContentLayout.visibility =
            if (expandedPosition == position) View.VISIBLE else View.GONE

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