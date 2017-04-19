package chiukingyee.animationsample

import android.animation.*
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.RadialGradient
import android.graphics.Shader
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout

class AnimationLoading : AppCompatActivity() {

  companion object {
    private const val DURATION = 1500
  }

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.animation_loading)
    val container = findViewById(R.id.container) as LinearLayout
    val animView = MyAnimationView(this)
    container.addView(animView)

    val starter = findViewById(R.id.startButton) as Button
    starter.setOnClickListener { animView.startAnimation() }
  }

  open inner class MyAnimationView(context: Context) : View(context), ValueAnimator.AnimatorUpdateListener {

    private object Constants {
      const val BALL_SIZE = 100f
    }

    internal val balls = arrayListOf<ShapeHolder>()
    private var animation: Animator? = null

    init {
      addBall(50f, 50f)
      addBall(200f, 50f)
      addBall(350f, 50f)
      addBall(500f, 50f, Color.GREEN)
      addBall(650f, 50f)
      addBall(800f, 50f)
      addBall(950f, 50f)
      addBall(800f, 50f, Color.YELLOW)
    }

    private fun createAnimation() {
      val appContext = this@AnimationLoading

      if (animation == null) {
        val anim = (AnimatorInflater.loadAnimator(appContext, R.animator.object_animator) as ObjectAnimator).apply {
          addUpdateListener(this@MyAnimationView)
          target = balls[0]
        }

        val fader = (AnimatorInflater.loadAnimator(appContext, R.animator.animator) as ValueAnimator).apply {
          addUpdateListener { balls[1].alpha = it.animatedValue as Float }
        }

        val seq = (AnimatorInflater.loadAnimator(appContext, R.animator.animator_set) as AnimatorSet).apply {
          setTarget(balls[2])
        }

        val colorizer = (AnimatorInflater.loadAnimator(appContext, R.animator.color_animator) as ObjectAnimator).apply {
          target = balls[3]
        }

        val animPvh = (AnimatorInflater.loadAnimator(appContext, R.animator.object_animator_pvh) as ObjectAnimator).apply {
          target = balls[4]
        }

        val animPvhKf = (AnimatorInflater.loadAnimator(appContext, R.animator.object_animator_pvh_kf) as ObjectAnimator).apply {
          target = balls[5]
        }

        val faderKf = (AnimatorInflater.loadAnimator(appContext, R.animator.value_animator_pvh_kf) as ValueAnimator).apply {
          addUpdateListener { balls[6].alpha = it.animatedValue as Float }
        }

        val animPvhKfInterpolated = (AnimatorInflater.loadAnimator(appContext, R.animator.object_animator_pvh_kf_interpolated) as ObjectAnimator).apply {
          target = balls[7]
        }

        animation = AnimatorSet().apply {
          playTogether(anim, fader, seq, colorizer, animPvh, animPvhKf, faderKf, animPvhKfInterpolated)
        }
      }
    }

    fun startAnimation() {
      createAnimation()
      animation!!.start()
    }

    private fun createBall(x: Float, y: Float) = ShapeHolder(
       ShapeDrawable(
          OvalShape().apply {
            resize(Constants.BALL_SIZE, Constants.BALL_SIZE)
          })).apply {
      this.x = x
      this.y = y
    }

    private fun addBall(x: Float, y: Float, color: Int) {
      balls.add(createBall(x, y).apply {
        this.color = color
      })
    }

    private fun addBall(x: Float, y: Float) {
      balls.add(createBall(x, y).apply {
        val red = (100 + Math.random() * 155).toInt()
        val green = (100 + Math.random() * 155).toInt()
        val blue = (100 + Math.random() * 155).toInt()
        val color = 0xff000000.toInt() or (red shl 16) or (green shl 8) or blue
        val paint = shape.paint
        val darkColor = 0xff000000.toInt() or (red / 4 shl 16) or (green / 4 shl 8) or blue / 4
        paint.shader = RadialGradient(37.5f, 12.5f, 50f, color, darkColor, Shader.TileMode.CLAMP)
      })
    }

    override fun onDraw(canvas: Canvas) {
      balls.forEach {
        canvas.translate(it.x, it.y)
        it.shape.draw(canvas)
        canvas.translate(-it.x, -it.y)
      }
    }

    override fun onAnimationUpdate(animation: ValueAnimator) {
      invalidate()
      balls[0].y = animation.animatedValue as Float
    }
  }
}
