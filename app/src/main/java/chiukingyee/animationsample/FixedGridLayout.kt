package chiukingyee.animationsample

import android.content.Context
import android.view.View
import android.view.ViewGroup

class FixedGridLayout(context: Context) : ViewGroup(context) {
  internal var cellWidth = 0
    set(px) {
      field = px
      requestLayout()
    }

  internal var cellHeight = 0
    set(px) {
      field = px
      requestLayout()
    }

  override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
    val cellWidthSpec = MeasureSpec.makeMeasureSpec(cellWidth, MeasureSpec.AT_MOST)
    val cellHeightSpec = MeasureSpec.makeMeasureSpec(cellHeight, MeasureSpec.AT_MOST)

    for (index in 0..childCount - 1) {
      getChildAt(index).measure(cellWidthSpec, cellHeightSpec)
    }

    val minCount = if (childCount > 3) childCount else 3
    setMeasuredDimension(View.resolveSize(cellWidth * minCount, widthMeasureSpec),
       View.resolveSize(cellHeight * minCount, heightMeasureSpec))
  }

  override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
    val columns = if (r < l) 1 else (r - l) / cellWidth
    var x = 0
    var y = 0
    var i = 0
    for (index in 0..childCount - 1) {
      val child = getChildAt(index)

      val w = child.measuredWidth
      val h = child.measuredHeight

      val left = x + (cellWidth - w) / 2
      val top = y + (cellHeight - h) / 2

      child.layout(left, top, left + w, top + h)
      if (i >= columns - 1) {
        i = 0
        x = 0
        y += cellHeight
      } else {
        i++
        x += cellWidth
      }
    }
  }
}