package kr.co.lion.farming_customer.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.databinding.FragmentTradeBinding
import kr.co.lion.farming_customer.databinding.ItemProductBinding
import kr.co.lion.farming_customer.model.Product
import kr.co.lion.farming_customer.viewmodel.TradeViewModel

class TradeFragment : Fragment() {
    lateinit var fragmentTradeBinding: FragmentTradeBinding
    lateinit var mainActivity: MainActivity

    lateinit var tradeViewModel: TradeViewModel
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        fragmentTradeBinding = DataBindingUtil.inflate(layoutInflater, R.layout.fragment_trade, container, false)
        tradeViewModel = TradeViewModel()
        fragmentTradeBinding.tradeViewModel = tradeViewModel
        fragmentTradeBinding.lifecycleOwner = this
        mainActivity = activity as MainActivity

        setupViewPager()

        return fragmentTradeBinding.root
    }

    private fun setupViewPager() {
        // TODO("products와 DB연결필요, 여기선 임시로 products 추가")
        val products = listOf(
            Product(R.drawable.farming_mark, "사과", "강원도 횡성군", "10,000원", 999, 4.5),
            Product(R.drawable.farming_mark, "사과", "강원도 횡성군", "10,000원", 999, 4.5),
            Product(R.drawable.farming_mark, "사과", "강원도 횡성군", "10,000원", 999, 4.5),
            Product(R.drawable.farming_mark, "사과", "강원도 횡성군", "10,000원", 999, 4.5),
        )
        fragmentTradeBinding.viewPagerTradeSpecialPrice.adapter = ProductPagerAdapter(products)
    }

    inner class ProductPagerAdapter(private val products: List<Product>) : RecyclerView.Adapter<ProductPagerAdapter.ProductViewHolder>() {
        inner class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(product: Product) {
                binding.imageViewCropImage.setImageResource(product.imageResourceId)
                binding.textViewCropName.text = product.name
                binding.textViewCropLocation.text = product.location
                binding.textViewTradePrice.text = product.price
                binding.textViewTradeLike.text = "${product.likes}+"
                binding.RatingBarTrade.rating = product.ratings.toFloat()
                // 별점 로직 및 좋아요 버튼 이벤트 처리를 여기에 추가
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = ItemProductBinding.inflate(inflater, parent, false)
            return ProductViewHolder(binding)
        }

        override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
            holder.bind(products[position])
        }

        override fun getItemCount(): Int = products.size
    }
}