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
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.BounceInterpolator
import android.widget.LinearLayout

class MultiPropertyAnimation : AppCompatActivity() {

  companion object {
    private const val DURATION = 1500.toLong()
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.animation_multi_property)
    val container = findViewById(R.id.container) as LinearLayout
    val animView = MyAnimationView(this)
    container.addView(animView)

    val starter = findViewById(R.id.startButton)
    starter.setOnClickListener { animView.startAnimation() }
  }

  inner class MyAnimationView(context: Context) : View(context), ValueAnimator.AnimatorUpdateListener {

    private object Constants {
      const val BALL_SIZE = 100f
    }

    val balls = arrayListOf<ShapeHolder>()
    var bounceAnim: Animator? = null
    lateinit var ball: ShapeHolder
    lateinit var animation: AnimatorSet

    init {
      addBall(50f, 0f)
      addBall(150f, 0f)
      addBall(250f, 0f)
      addBall(350f, 0f)
    }

    private fun createAnimation() {
      if (bounceAnim == null) {
        var ball = balls[0]
        val yBouncer = ObjectAnimator.ofFloat(ball, "y", ball.y, height - Constants.BALL_SIZE).apply {
          duration = DURATION
          interpolator = BounceInterpolator()
          addUpdateListener(this@MyAnimationView)
        }

        ball = balls[1]
        var pvhY = PropertyValuesHolder.ofFloat("y", ball.y, height - Constants.BALL_SIZE)
        val pvhAlpha = PropertyValuesHolder.ofFloat("alpha", 1.0f, 0f)
        val yAlphaBouncer = ObjectAnimator.ofPropertyValuesHolder(ball, pvhY, pvhAlpha).apply {
          duration = DURATION / 2
          interpolator = AccelerateInterpolator()
          repeatCount = 1
          repeatMode = ValueAnimator.REVERSE
        }

        ball = balls[2]
        val pvhW = PropertyValuesHolder.ofFloat("width", ball.width, ball.width * 2)
        val pvhH = PropertyValuesHolder.ofFloat("height", ball.height, ball.height * 2)
        val pvTX = PropertyValuesHolder.ofFloat("x", ball.x, ball.x - Constants.BALL_SIZE / 2f)
        val pvTY = PropertyValuesHolder.ofFloat("y", ball.y, ball.y - Constants.BALL_SIZE / 2f)
        val whxyBouncer = ObjectAnimator.ofPropertyValuesHolder(ball, pvhW, pvhH, pvTX, pvTY).apply {
          duration = DURATION / 2
          repeatCount = 1
          repeatMode = ValueAnimator.REVERSE
        }

        ball = balls[3]
        pvhY = PropertyValuesHolder.ofFloat("y", ball.y, height - Constants.BALL_SIZE)
        val ballX = ball.x
        val kf0 = Keyframe.ofFloat(0f, ballX)
        val kf1 = Keyframe.ofFloat(.5f, ballX + 100f)
        val kf2 = Keyframe.ofFloat(1f, ballX + 50f)
        val pvhX = PropertyValuesHolder.ofKeyframe("x", kf0, kf1, kf2)
        val yxBouncer = ObjectAnimator.ofPropertyValuesHolder(ball, pvhY, pvhX).apply {
          duration = DURATION / 2
          repeatCount = 1
          repeatMode = ValueAnimator.REVERSE
        }

        bounceAnim = AnimatorSet().apply {
          playTogether(yBouncer, yAlphaBouncer, whxyBouncer, yxBouncer)
        }
      }
    }

    fun startAnimation() {
      createAnimation()
      bounceAnim!!.start()
    }

    private fun addBall(x: Float, y: Float): ShapeHolder {
      val red = (100 + Math.random() * 155).toInt()
      val green = (100 + Math.random() * 155).toInt()
      val blue = (100 + Math.random() * 155).toInt()
      val color = 0xff000000.toInt() or (red shl 16) or (green shl 8) or blue
      val darkColor = 0xff000000.toInt() or (red / 4 shl 16) or (green / 4 shl 8) or blue / 4
      val drawable = ShapeDrawable(OvalShape().apply {
        resize(Constants.BALL_SIZE, Constants.BALL_SIZE)
      }).apply {
        paint.shader = RadialGradient(37.5f, 12.5f, 50f, color, darkColor, Shader.TileMode.CLAMP)
      }
      val shapeHolder = ShapeHolder(drawable).apply {
        this.x = x
        this.y = y
        paint = drawable.paint
      }
      balls.add(shapeHolder)
      return shapeHolder
    }

    override fun onDraw(canvas: Canvas) {
      balls.forEach {
        canvas.translate(it.x, it.y)
        it.shape.draw(canvas)
        canvas.translate(-it.x, -it.y)
      }
    }

    override fun onAnimationUpdate(animation: ValueAnimator?) = invalidate()
  }
}
