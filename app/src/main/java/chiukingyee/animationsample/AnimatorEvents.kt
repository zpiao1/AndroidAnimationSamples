package chiukingyee.animationsample

import android.animation.Animator
import android.animation.AnimatorSet
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
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView

class AnimatorEvents : AppCompatActivity() {

  lateinit private var startText: TextView
  lateinit private var repeatText: TextView
  lateinit private var cancelText: TextView
  lateinit private var endText: TextView
  lateinit private var startTextAnimator: TextView
  lateinit private var repeatTextAnimator: TextView
  lateinit private var cancelTextAnimator: TextView
  lateinit private var endTextAnimator: TextView


  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.animator_events)
    val container = findViewById(R.id.container) as LinearLayout
    val animView = MyAnimationView(this)
    container.addView(animView)
    startText = (findViewById(R.id.startText) as TextView).apply { alpha = .5f }
    repeatText = (findViewById(R.id.repeatText) as TextView).apply { alpha = .5f }
    cancelText = (findViewById(R.id.cancelText) as TextView).apply { alpha = .5f }
    endText = (findViewById(R.id.endText) as TextView).apply { alpha = .5f }
    startTextAnimator = (findViewById(R.id.startTextAnimator) as TextView).apply { alpha = .5f }
    repeatTextAnimator = (findViewById(R.id.repeatTextAnimator) as TextView).apply { alpha = .5f }
    cancelTextAnimator = (findViewById(R.id.cancelTextAnimator) as TextView).apply { alpha = .5f }
    endTextAnimator = (findViewById(R.id.endTextAnimator) as TextView).apply { alpha = .5f }
    val endCB = findViewById(R.id.endCB) as CheckBox

    (findViewById(R.id.startButton) as Button).apply {
      setOnClickListener { animView.startAnimation(endCB.isChecked) }
    }

    (findViewById(R.id.cancelButton) as Button).apply {
      setOnClickListener { animView.cancelAnimation() }
    }
    (findViewById(R.id.endButton) as Button).apply {
      setOnClickListener { animView.endAnimation() }
    }

  }

  inner class MyAnimationView(context: Context) : View(context), Animator.AnimatorListener, ValueAnimator.AnimatorUpdateListener {

    val balls = arrayListOf<ShapeHolder>()
    internal var animation: Animator? = null
    internal val ball = createBall(25f, 25f)
    internal var endImmediately = false

    private fun createAnimation() {
      if (animation == null) {
        val yAnim = ObjectAnimator.ofFloat(ball, "y", ball.y, height - 50f).apply {
          duration = 1500
          repeatCount = 0
          repeatMode = ValueAnimator.REVERSE
          interpolator = AccelerateInterpolator(2f)
          addUpdateListener(this@MyAnimationView)
          addListener(this@MyAnimationView)
        }

        val xAnim = ObjectAnimator.ofFloat(ball, "x", ball.x, ball.x + 300).apply {
          duration = 1000
          startDelay = 0
          repeatCount = 0
          repeatMode = ValueAnimator.REVERSE
          interpolator = AccelerateInterpolator(2f)
        }

        val alphaAnim = ObjectAnimator.ofFloat(ball, "alpha", 1f, .5f).apply { duration = 1000 }
        AnimatorSet().apply { play(alphaAnim) }

        animation = AnimatorSet().apply {
          playTogether(yAnim, xAnim)
          addListener(this@MyAnimationView)
        }
      }
    }

    fun startAnimation(endImmediately: Boolean) {
      this.endImmediately = endImmediately
      startText.alpha = .5f
      repeatText.alpha = .5f
      cancelText.alpha = .5f
      endText.alpha = .5f
      startTextAnimator.alpha = .5f
      repeatTextAnimator.alpha = .5f
      cancelTextAnimator.alpha = .5f
      endTextAnimator.alpha = .5f
      createAnimation()
      animation!!.start()
    }

    fun cancelAnimation() {
      createAnimation()
      animation!!.cancel()
    }

    fun endAnimation() {
      createAnimation()
      animation!!.end()
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
        ball.shape.draw(this)
        restore()
      }
    }

    override fun onAnimationEnd(animation: Animator) {
      when (animation) {
        is AnimatorSet -> endText.alpha = 1f
        else -> endTextAnimator.alpha = 1f
      }
    }

    override fun onAnimationStart(animation: Animator) {
      when (animation) {
        is AnimatorSet -> startText.alpha = 1f
        else -> startTextAnimator.alpha = 1f
      }
      if (endImmediately) {
        animation.end()
      }
    }

    override fun onAnimationRepeat(animation: Animator?) {
      when (animation) {
        is AnimatorSet -> repeatText.alpha = 1f
        else -> repeatTextAnimator.alpha = 1f
      }
    }

    override fun onAnimationCancel(animation: Animator) {
      when (animation) {
        is AnimatorSet -> cancelText.alpha = 1f
        else -> cancelTextAnimator.alpha = 1f
      }
    }

    override fun onAnimationUpdate(animation: ValueAnimator?) {
      invalidate()
    }
  }
}
