package kr.co.lion.farming_customer.fragment.famingLifeTools.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.farmingLifeTools.FarmingLifeToolsDetailActivity
import kr.co.lion.farming_customer.databinding.RowLikeRentalVpItemBinding

class FarmingLifeListRVAdapter(val context: Context) : RecyclerView.Adapter<FarmingLifeListRVAdapter.ListViewHolder>() {
    private var isHeartBtn : Boolean = true

    inner class ListViewHolder(val rowBinding: RowLikeRentalVpItemBinding) : RecyclerView.ViewHolder(rowBinding.root) {
        init {
            rowBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = RowLikeRentalVpItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val listViewHolder = ListViewHolder(binding)

        return listViewHolder
    }

    override fun getItemCount(): Int {
        return 20
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.rowBinding.vpItemTextViewLikeRentalName.text = "농기구 $position"
        holder.rowBinding.vpItemTextViewLikeRentalPhoneNumber.text = "010-1111-1111"
        holder.rowBinding.vpItemTextViewLikeRentalAddress.text = "농기구 임대점${position} 주소"

        holder.rowBinding.vpItemImageViewHeart.setOnClickListener {
            if (isHeartBtn) {
                holder.rowBinding.vpItemImageViewHeart.setImageResource(R.drawable.heart_01)
                holder.rowBinding.vpItemTextViewHeartCnt.setTextColor(ContextCompat.getColor(context, R.color.white))
            }
            else {
                holder.rowBinding.vpItemImageViewHeart.setImageResource(R.drawable.heart_02)
                holder.rowBinding.vpItemTextViewHeartCnt.setTextColor(ContextCompat.getColor(context, R.color.brown_01))
            }
            // 상태 변경
            isHeartBtn = !isHeartBtn
        }

        holder.rowBinding.root.setOnClickListener {
            val intent = Intent(context, FarmingLifeToolsDetailActivity::class.java)
            context.startActivity(intent)
        }
    }
}