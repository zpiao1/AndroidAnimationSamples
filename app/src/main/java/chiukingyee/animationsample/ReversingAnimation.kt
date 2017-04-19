package chiukingyee.animationsample

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.RadialGradient
import android.graphics.Shader
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.widget.Button
import android.widget.LinearLayout

class ReversingAnimation : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.animation_reversing)
    val container = findViewById(R.id.container) as LinearLayout
    val animView = MyAnimationView(this)
    container.addView(animView)

    val starter = findViewById(R.id.startButton) as Button
    starter.setOnClickListener { animView.startAnimation() }

    val reverser = findViewById(R.id.reverseButton) as Button
    reverser.setOnClickListener { animView.reverseAnimation() }
  }

  inner class MyAnimationView(context: Context) : View(context), ValueAnimator.AnimatorUpdateListener {

    val balls = arrayListOf<ShapeHolder>()
    internal var bounceAnim: ValueAnimator? = null
    internal val ball = createBall(25f, 25f)

    private fun createAnimation() {
      if (bounceAnim == null) {
        bounceAnim = ObjectAnimator.ofFloat(ball, "y", ball.y, height - 50f).apply {
          duration = 1500
          interpolator = AccelerateInterpolator(2f)
          addUpdateListener(this@MyAnimationView)
        }
      }
    }

    fun startAnimation() {
      createAnimation()
      bounceAnim!!.start()
    }

    fun reverseAnimation() {
      createAnimation()
      bounceAnim!!.reverse()
    }

    fun seek(seekTime: Long) {
      createAnimation()
      bounceAnim!!.currentPlayTime = seekTime
    }

    private fun createBall(x: Float, y: Float): ShapeHolder {
      val red = (Math.random() * 255).toInt()
      val green = (Math.random() * 255).toInt()
      val blue = (Math.random() * 255).toInt()
      val color = 0xff000000.toInt() or (red shl 16) or (green shl 8) or blue
      val darkColor = 0xff000000.toInt() or (red / 4 shl 16) or (green / 4 shl 8) or blue / 4
      val drawable = ShapeDrawable(OvalShape().apply { resize(50f, 50f) })
         .apply {
           paint.shader = RadialGradient(37.5f, 12.5f, 50f, color, darkColor, Shader.TileMode.CLAMP)
         }
      return ShapeHolder(drawable).apply {
        this.x = x - 25f
        this.y = y - 25f
        paint = drawable.paint
      }
    }

    override fun onDraw(canvas: Canvas) {
      canvas.apply {
        save()
        translate(ball.x, ball.y)
        ball.shape.draw(canvas)
        canvas.restore()
      }
    }

    override fun onAnimationUpdate(animation: ValueAnimator?) = invalidate()
  }
}
