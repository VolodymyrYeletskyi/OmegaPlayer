package io.yeletskyiv.omegaplayer.utils.decorations

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class HorizontalListItemDecoration(
    private val space: Int,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val position = parent.getChildAdapterPosition(view)
        val childrenCount = parent.adapter?.itemCount ?: 0
        with(outRect) {
            top = 0
            left = if (position == 0) 0 else space / 2
            right = if (position == childrenCount - 1) 0 else space / 2
            bottom = 0
        }
    }
}