package kr.co.lion.farming_customer

import android.content.Context
import android.util.AttributeSet
import com.google.android.material.button.MaterialButton
import com.google.android.material.button.MaterialButtonToggleGroup
import kotlin.math.max


class MaterialButtonToggleGroupWithRadius
@JvmOverloads
constructor(context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0)
    : MaterialButtonToggleGroup(context, attrs, defStyleAttr){

    private val buttonSpacing = resources.getDimensionPixelSize(R.dimen.button_spacing)
    private val buttonRadius = resources.getDimension(R.dimen.button_toggle_group_radius)

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        var totalWidth = 0 // 모든 버튼의 너비 합
        var totalHeight = 0 // 버튼 그룹의 높이

        for (i in 0 until childCount) {
            val button = getChildAt(i) as MaterialButton
            if (button.visibility == GONE) {
                continue
            }
            totalWidth += button.measuredWidth // 각 버튼의 너비 합산

            val params = button.layoutParams as LayoutParams
            if(i == 0){
                params.leftMargin = 0
            }else{
                params.leftMargin = buttonSpacing
                totalWidth += buttonSpacing // 왼쪽 여백 추가
            }

            totalHeight = max(totalHeight, button.measuredHeight) // 버튼 그룹의 높이는 가장 높은 버튼의 높이로 설정
            // 버튼 그룹의 너비와 높이를 설정
            setMeasuredDimension(totalWidth, totalHeight)

            val builder = button.shapeAppearanceModel.toBuilder()
            button.shapeAppearanceModel = builder.setTopLeftCornerSize(buttonRadius)
                .setBottomLeftCornerSize(buttonRadius)
                .setTopRightCornerSize(buttonRadius)
                .setBottomRightCornerSize(buttonRadius).build()

        }
    }
}

