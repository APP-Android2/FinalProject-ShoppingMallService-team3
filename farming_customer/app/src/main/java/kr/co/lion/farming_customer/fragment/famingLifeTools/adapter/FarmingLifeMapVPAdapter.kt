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
import kr.co.lion.farming_customer.model.farminLifeTools.RentalModel

class FarmingLifeMapVPAdapter(val context: Context, rentalList: MutableList<RentalModel>) :
    RecyclerView.Adapter<FarmingLifeMapVPAdapter.MapViewHolder>() {
    private var isHeartBtn: Boolean = true
    var rentalList = rentalList

    inner class MapViewHolder(val rowBinding: RowLikeRentalVpItemBinding) :
        RecyclerView.ViewHolder(rowBinding.root) {
        init {
            rowBinding.root.layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MapViewHolder {
        val rowBinding =
            RowLikeRentalVpItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        val mapViewHolder = MapViewHolder(rowBinding)

        return mapViewHolder
    }

    override fun getItemCount(): Int {
        return rentalList.size
    }

    override fun onBindViewHolder(holder: MapViewHolder, position: Int) {
        holder.rowBinding.vpItemTextViewLikeRentalName.text = rentalList[position].rental_name
        holder.rowBinding.vpItemTextViewLikeRentalPhoneNumber.text = rentalList[position].rental_phone
        holder.rowBinding.vpItemTextViewLikeRentalAddress.text = rentalList[position].rental_address

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
            intent.putExtra("rentalIdx", rentalList[position].rental_idx)
            context.startActivity(intent)
        }
    }
}