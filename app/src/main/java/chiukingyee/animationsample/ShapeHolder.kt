package chiukingyee.animationsample

import android.graphics.Paint
import android.graphics.RadialGradient
import android.graphics.drawable.ShapeDrawable

class ShapeHolder(var shape: ShapeDrawable) {
  var x = 0f

  var y = 0f

  var color: Int = 0
    set(value) {
      shape.paint.color = value
      field = value
    }

  lateinit var radialGradient: RadialGradient

  var alpha: Float = 1f
    set(value) {
      field = value
      shape.alpha = ((alpha * 255f) + .5f).toInt()
    }

  lateinit var paint: Paint

  var width
    get() = shape.shape.width
    set(value) {
      val s = shape.shape
      s.resize(value, s.height)
    }

  var height
    get() = shape.shape.height
    set(value) {
      val s = shape.shape
      s.resize(s.width, value)
    }
}