package kr.co.lion.farming_customer

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpaceItemDecoration(var spanCount:Int, var outSideSpace: Int,var inSideRightSpace:Int, var inSideLeftSpace:Int, var topSpace:Int):RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView,
                                state: RecyclerView.State) {

        super.getItemOffsets(outRect, view, parent, state)
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount + 1

        if(column != 1){
            outRect.left = inSideLeftSpace
        }
        if(column != 2){
            outRect.right = inSideRightSpace
        }
        if(column == 1){
            outRect.left = outSideSpace
        }
        if(column == 2){
            outRect.right = outSideSpace
        }

        outRect.top = topSpace

    }
}