package kr.co.lion.farming_customer.activity.farmingLifeTools.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.farming_customer.databinding.RowFarmingLifeToolsDetailToolNameBinding

class ToolsNameRVAdapter : RecyclerView.Adapter<ToolsNameRVAdapter.NameViewHolder>() {
    inner class NameViewHolder(val rowBinding: RowFarmingLifeToolsDetailToolNameBinding) : RecyclerView.ViewHolder(rowBinding.root) {
        init {
            rowBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NameViewHolder {
        val rowBinding = RowFarmingLifeToolsDetailToolNameBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val nameViewHolder = NameViewHolder(rowBinding)

        return nameViewHolder
    }

    override fun getItemCount(): Int {
        return 20
    }

    override fun onBindViewHolder(holder: NameViewHolder, position: Int) {
        holder.rowBinding.rowFarmingLifeToolsName.text = "농기구 이름 $position"
    }
}