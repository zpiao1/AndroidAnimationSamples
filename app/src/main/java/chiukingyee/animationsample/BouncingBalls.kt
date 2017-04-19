package chiukingyee.animationsample

import android.animation.*
import android.content.Context
import android.graphics.Canvas
import android.graphics.RadialGradient
import android.graphics.Shader
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.widget.LinearLayout

class BouncingBalls : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.bouncing_balls)
    (findViewById(R.id.container) as LinearLayout).addView(MyAnimationView(this))
  }

  inner class MyAnimationView(context: Context) : View(context) {
    private object Constants {
      const val RED = 0xffFF8080.toInt()
      const val BLUE = 0xff8080FF.toInt()
      const val CYAN = 0xff80ffff.toInt()
      const val GREEN = 0xff80ff80.toInt()
    }

    val balls = arrayListOf<ShapeHolder>()
    internal var animation: AnimatorSet? = null

    init {
      ObjectAnimator.ofInt(this, "backgroundColor", Constants.RED, Constants.BLUE).apply {
        duration = 3000
        setEvaluator(ArgbEvaluator())
        repeatCount = ValueAnimator.INFINITE
        repeatMode = ValueAnimator.REVERSE
        start()
      }
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
      if (event.action != MotionEvent.ACTION_DOWN && event.action != MotionEvent.ACTION_MOVE) {
        return false
      }
      val newBall = addBall(event.x, event.y)

      val startY = newBall.y
      val endY = height - 50f
      val h = height.toFloat()
      val eventY = event.y
      val duration = (500 * (h - eventY) / h).toLong()

      val bounceAnim = ObjectAnimator.ofFloat(newBall, "y", startY, endY).apply {
        this.duration = duration
        interpolator = AccelerateInterpolator()
      }

      val squashAnim1 = ObjectAnimator.ofFloat(newBall, "x", newBall.x, newBall.x - 25f).apply {
        this.duration = duration / 4
        repeatCount = 1
        repeatMode = ValueAnimator.REVERSE
        interpolator = DecelerateInterpolator()
      }

      val squashAnim2 = ObjectAnimator.ofFloat(newBall, "width", newBall.width, newBall.width + 50).apply {
        this.duration = duration / 4
        repeatCount = 1
        repeatMode = ValueAnimator.REVERSE
        interpolator = DecelerateInterpolator()
      }

      val stretchAnim1 = ObjectAnimator.ofFloat(newBall, "y", endY, endY + 25f).apply {
        this.duration = duration / 4
        repeatCount = 1
        repeatMode = ValueAnimator.REVERSE
        interpolator = DecelerateInterpolator()
      }

      val stretchAnim2 = ObjectAnimator.ofFloat(newBall, "height", newBall.height, newBall.height - 25).apply {
        this.duration = duration / 4
        repeatCount = 1
        interpolator = DecelerateInterpolator()
        repeatMode = ValueAnimator.REVERSE
      }

      val bounceBackAnim = ObjectAnimator.ofFloat(newBall, "y", endY, startY).apply {
        this.duration = duration
        interpolator = DecelerateInterpolator()
      }

      val bouncer = AnimatorSet().apply {
        play(bounceAnim).before(squashAnim1)
        play(squashAnim1).with(squashAnim2)
        play(squashAnim1).with(stretchAnim1)
        play(squashAnim1).with(stretchAnim2)
        play(bounceBackAnim).after(stretchAnim2)
      }

      val fadeAnim = ObjectAnimator.ofFloat(newBall, "alpha", 1f, 0f).apply {
        this.duration = 250
        addListener(object : AnimatorListenerAdapter() {
          override fun onAnimationEnd(animation: Animator) {
            balls.remove((animation as ObjectAnimator).target)
          }
        })
      }

      AnimatorSet().apply {
        play(bouncer).before(fadeAnim)
        start()
      }

      return true
    }

    private fun addBall(x: Float, y: Float): ShapeHolder {
      val red = (Math.random() * 255).toInt()
      val green = (Math.random() * 255).toInt()
      val blue = (Math.random() * 255).toInt()
      val color = 0xff000000.toInt() or (red shl 16) or (green shl 8) or blue
      val darkColor = 0xff000000.toInt() or (red / 4 shl 16) or (green / 4 shl 8) or blue / 4
      val drawable = ShapeDrawable(OvalShape().apply { resize(50f, 50f) }).apply {
        paint.shader = RadialGradient(37.5f, 12.5f, 50f, color, darkColor, Shader.TileMode.CLAMP)
      }
      val shapeHolder = ShapeHolder(drawable).apply {
        this.x = x - 25f
        this.y = y - 25f
        paint = drawable.paint
      }
      balls.add(shapeHolder)
      return shapeHolder
    }

    override fun onDraw(canvas: Canvas) {
      balls.forEach {
        canvas.save()
        canvas.translate(it.x, it.y)
        it.shape.draw(canvas)
        canvas.restore()
      }
    }
  }
}
