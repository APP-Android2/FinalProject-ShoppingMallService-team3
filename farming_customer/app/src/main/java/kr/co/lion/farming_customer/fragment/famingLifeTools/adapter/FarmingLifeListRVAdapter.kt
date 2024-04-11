package kr.co.lion.farming_customer.fragment.famingLifeTools.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.contentValuesOf
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.farming_customer.activity.farmingLifeTools.FarmingLifeToolsDetailActivity
import kr.co.lion.farming_customer.databinding.RowLikeRentalBinding

class FarmingLifeListRVAdapter(val context: Context) : RecyclerView.Adapter<FarmingLifeListRVAdapter.ListViewHolder>() {
    inner class ListViewHolder(val rowBinding: RowLikeRentalBinding) : RecyclerView.ViewHolder(rowBinding.root) {
        init {
            rowBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ListViewHolder {
        val binding = RowLikeRentalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val listViewHolder = ListViewHolder(binding)

        return listViewHolder
    }

    override fun getItemCount(): Int {
        return 20
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        holder.rowBinding.textViewLikeRentalName.text = "농기구 $position"
        holder.rowBinding.textViewLikeRentalPhoneNumber.text = "010-1111-1111"
        holder.rowBinding.textViewLikeRentalAddress.text = "농기구 임대점${position} 주소"

        holder.rowBinding.root.setOnClickListener {
            val intent = Intent(context, FarmingLifeToolsDetailActivity::class.java)
            context.startActivity(intent)
        }
    }
}