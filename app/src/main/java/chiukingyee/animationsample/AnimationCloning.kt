package chiukingyee.animationsample

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
import android.view.animation.DecelerateInterpolator
import android.widget.Button
import android.widget.LinearLayout

class AnimationCloning : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.animation_cloning)
    val container = findViewById(R.id.container) as LinearLayout
    val animView = MyAnimationView(this)
    container.addView(animView)

    val starter = findViewById(R.id.startButton) as Button
    starter.setOnClickListener { animView.startAnimation() }
  }

  class MyAnimationView(context: Context) : View(context), ValueAnimator.AnimatorUpdateListener {

    companion object {
      val balls = arrayListOf<ShapeHolder>()
    }

    internal var animation: AnimatorSet? = null
    private var mDensity = context.resources.displayMetrics.density

    init {
      addBall(50f, 25f)
      addBall(150f, 25f)
      addBall(250f, 25f)
      addBall(350f, 25f)
    }

    private fun createAnimation() {
      if (animation == null) {
        val anim1 = ObjectAnimator.ofFloat(balls[0], "y", 0f, height - balls[0].height).apply {
          duration = 500
          addUpdateListener(this@MyAnimationView)
        }
        val anim2 = anim1.clone().apply {
          target = balls[1]
        }

        val ball2 = balls[2]
        val animDown = ObjectAnimator.ofFloat(ball2, "y", 0f, height - ball2.height).apply {
          duration = 500
          interpolator = AccelerateInterpolator()
          addUpdateListener(this@MyAnimationView)
        }
        val animUp = ObjectAnimator.ofFloat(ball2, "y", height - ball2.height, 0f).apply {
          duration = 500
          interpolator = DecelerateInterpolator()
          addUpdateListener(this@MyAnimationView)
        }
        val s1 = AnimatorSet().apply {
          playSequentially(animDown, animUp)
        }
        val s2 = s1.clone().apply {
          setTarget(balls[3])
        }

        animation = AnimatorSet().apply {
          playTogether(anim1, anim2, s1)
          playSequentially(s1, s2)
        }
      }
    }

    private fun addBall(x: Float, y: Float): ShapeHolder {
      val circle = OvalShape().apply {
        resize(50f * mDensity, 50f * mDensity)
      }
      val drawable = ShapeDrawable(circle)
      val shapeHolder = ShapeHolder(drawable).apply {
        this.x = x - 25f
        this.y = y - 25f
      }
      val red = (100 + Math.random() * 155).toInt()
      val green = (100 + Math.random() * 155).toInt()
      val blue = (100 + Math.random() * 155).toInt()
      val color = 0xff000000.toInt() or (red shl 16) or (green shl 8) or blue
      val paint = drawable.paint  // Paint(Paint.ANTI_ALIAS_FLAG)
      val darkColor = 0xff000000.toInt() or (red / 4 shl 16) or (green / 4 shl 8) or blue / 4
      val gradient = RadialGradient(37.5f, 12.5f, 50f, color, darkColor, Shader.TileMode.CLAMP)
      paint.shader = gradient
      shapeHolder.paint = paint
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

    fun startAnimation() {
      createAnimation()
      animation!!.start()
    }

    override fun onAnimationUpdate(animation: ValueAnimator?) {
      invalidate()
    }
  }
}
