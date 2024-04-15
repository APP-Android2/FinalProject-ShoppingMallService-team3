package kr.co.lion.farming_customer.fragment.tradeCrop

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kr.co.lion.farming_customer.MainFragmentName
import kr.co.lion.farming_customer.R
import kr.co.lion.farming_customer.activity.MainActivity
import kr.co.lion.farming_customer.activity.TradeSearchActivity
import kr.co.lion.farming_customer.databinding.FragmentTradeBinding
import kr.co.lion.farming_customer.databinding.ItemProductBinding
import kr.co.lion.farming_customer.databinding.RowLikeCropBinding
import kr.co.lion.farming_customer.model.Product
import kr.co.lion.farming_customer.model.ProductCard
import kr.co.lion.farming_customer.viewmodel.tradeCrop.TradeViewModel

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

        setToolbar()
        setupViewPager()
        setupViewPager2()
        setupRecyclerView()

        return fragmentTradeBinding.root
    }

    // 툴바 설정
    private fun setToolbar(){
        fragmentTradeBinding.apply {
            toolbarTrade.apply {
                title = "farming"

                // 메뉴 등록
                inflateMenu(R.menu.menu_trade)

                // 검색 버튼 눌렀을 때
                setOnMenuItemClickListener {
                    when(it.itemId){
                        R.id.menuItemTradeSearch -> {
                            val intent = Intent(mainActivity, TradeSearchActivity::class.java)
                            startActivity(intent)
                        }
                    }
                    true
                }
            }
        }
    }

    // 특가 농산물 관련 설정
    private fun setupViewPager() {
        // TODO("특가 농산물 DB와 연결필요, 여기선 임시로 products 추가")
        val products = listOf(
            Product(R.drawable.farming_mark, "사과", "강원도 횡성군", "10,000원", 999, ratings = 4.5),
            Product(R.drawable.farming_mark, "사과", "강원도 횡성군", "10,000원", 999, ratings = 4.5),
            Product(R.drawable.farming_mark, "사과", "강원도 횡성군", "10,000원", 999, ratings = 4.5),
            Product(R.drawable.farming_mark, "사과", "강원도 횡성군", "10,000원", 999, ratings = 4.5),
        )
        fragmentTradeBinding.viewPagerTradeSpecialPrice.adapter = ProductPagerAdapter(products)
    }

    // 인기 농산물 관련 설정
    private fun setupViewPager2() {
        // TODO("인기 농작물 DB와 연결 필요, 여기선 임시로 products 추가")
        val products = listOf(
            Product(R.drawable.farming_mark, "사과", "강원도 횡성군", "10,000원", 999, ratings = 1.0),
            Product(R.drawable.farming_mark, "배", "강원도 횡성군", "10,000원", 999, ratings = 2.0),
            Product(R.drawable.farming_mark, "감자", "강원도 횡성군", "10,000원", 999, ratings = 3.0),
            Product(R.drawable.farming_mark, "사과", "강원도 횡성군", "10,000원", 999, ratings = 4.0),
        )
        fragmentTradeBinding.viewPagerTradeTrend.adapter = ProductPagerAdapter(products)
    }

    // 최신 등록 농산물 관련 설정
    private fun setupRecyclerView(){
        // TODO("최신 농작물 DB와 연결 필요, 여기선 임시로 products 추가")
        // 한 행에 표시될 항목의 수 2로 설정
        val layoutManager = GridLayoutManager(context, 2)
        fragmentTradeBinding.recyclerViewTradeNew.layoutManager = layoutManager

        val products = listOf(
            ProductCard(R.drawable.farming_mark, "사과", "10,000원", 999, 1.0, ),
            ProductCard(R.drawable.farming_mark, "배", "10,000원", 999, 2.0),
            ProductCard(R.drawable.farming_mark, "감자", "10,000원", 999, 3.0),
            ProductCard(R.drawable.farming_mark, "바나나", "10,000원", 999, 4.0),
            ProductCard(R.drawable.farming_mark, "사과", "10,000원", 999, 5.0),
        )

        //어댑터 설정
        fragmentTradeBinding.recyclerViewTradeNew.adapter = TradeNewAdapter(products)
    }

    // 특가, 인기 농산물 Adapter
    inner class ProductPagerAdapter(private val products: List<Product>) : RecyclerView.Adapter<ProductPagerAdapter.ProductViewHolder>() {
        inner class ProductViewHolder(val binding: ItemProductBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(product: Product) {
                binding.imageViewCropImage.setImageResource(product.imageResourceId)
                binding.textViewCropName.text = product.name
                binding.textViewCropLocation.text = product.location
                binding.textViewTradePrice.text = product.price
                binding.textViewTradeLike.text = if (product.likes >= 1000) "999+"  // 좋아요 개수 1000이상이면 초기값 999+
                else "${product.likes}"
                binding.RatingBarTrade.rating = product.ratings.toFloat()

                binding.imageButtonProductLike.setOnClickListener {
                    product.isLiked = !product.isLiked // 좋아요 선택
                    if (product.isLiked) {
                        product.likes++ // 좋아요 상태면 좋아요 수 증가, 텍스트 흰색
                        binding.textViewTradeLike.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                    } else {
                        product.likes-- // 좋아요 취소면 좋아요 수 감소, 텍스트 갈색
                        binding.textViewTradeLike.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown_01))
                    }
                    // 좋아요 수가 1000 이상이면 999+로 표기
                    binding.textViewTradeLike.text = if (product.likes >= 1000) "999+"
                    else "${product.likes}"




                    binding.imageButtonProductLike.setImageResource(
                        if (product.isLiked) R.drawable.heart_01 else R.drawable.heart_02
                    )
                }

                // 아이템 클릭 이벤트 처리
                itemView.setOnClickListener{
                    mainActivity.replaceFragment(MainFragmentName.TRADE_DETAIL_FRAGMENT,
                        addToBackStack = true, isAnimate = true, data = null)
                }
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

    // 최신 등록 농산물 Adapter
    inner class TradeNewAdapter(private val products: List<ProductCard>): RecyclerView.Adapter<TradeNewAdapter.ViewHolder>() {
        inner class ViewHolder(val binding: RowLikeCropBinding) : RecyclerView.ViewHolder(binding.root) {
            fun bind(product: ProductCard){
                binding.apply {
                    imageViewLikeCrop.setImageResource(product.imageResourceId)
                    textViewLikeCropName.text = product.name
                    textViewLikeCropPrice.text = product.price
                    ratingBarLikeCrop.rating = product.ratings.toFloat()
                    binding.textViewLikeCropCnt.text = if (product.likes >= 1000) "999+"  // 좋아요 개수 1000이상이면 초기값 999+
                    else "${product.likes}"

                    imageViewHeartCrop.setOnClickListener {
                        product.isLiked = !product.isLiked // 좋아요 선택
                        if (product.isLiked) {
                            product.likes++ // 좋아요 상태면 좋아요 수 증가, 텍스트 흰색
                            binding.textViewLikeCropCnt.setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                        } else {
                            product.likes-- // 좋아요 취소면 좋아요 수 감소, 텍스트 갈색
                            binding.textViewLikeCropCnt.setTextColor(ContextCompat.getColor(requireContext(), R.color.brown_01))
                        }
                        // 좋아요 수가 1000 이상이면 999+로 표기
                        binding.textViewLikeCropCnt.text = if (product.likes >= 1000) "999+"
                        else "${product.likes}"
                        binding.imageViewHeartCrop.setImageResource(
                            if (product.isLiked) R.drawable.heart_01 else R.drawable.heart_02
                        )
                    }

                    // 아이템 클릭 이벤트 처리
                    itemView.setOnClickListener{
                        mainActivity.replaceFragment(MainFragmentName.TRADE_DETAIL_FRAGMENT,
                            addToBackStack = true, isAnimate = true, data = null)
                    }
                }
            }
        }
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TradeNewAdapter.ViewHolder {
            val inflater = LayoutInflater.from(parent.context)
            val binding = RowLikeCropBinding.inflate(inflater, parent, false)
            return ViewHolder(binding)
        }

        override fun onBindViewHolder(holder: TradeNewAdapter.ViewHolder, position: Int) {
            val product = products[position]
            holder.bind(product)
        }

        override fun getItemCount(): Int = products.size

    }
}