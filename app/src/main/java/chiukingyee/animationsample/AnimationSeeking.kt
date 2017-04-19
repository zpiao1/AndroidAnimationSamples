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
import android.view.animation.BounceInterpolator
import android.widget.Button
import android.widget.LinearLayout
import android.widget.SeekBar

class AnimationSeeking : AppCompatActivity() {

  companion object {
    private const val DURATION = 1500
  }

  private lateinit var mSeekBar: SeekBar

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.animation_seeking)
    val container = findViewById(R.id.container) as LinearLayout
    val animView = MyAnimationView(this)
    container.addView(animView)

    val starter = findViewById(R.id.startButton) as Button
    starter.setOnClickListener { animView.startAnimation() }

    mSeekBar = (findViewById(R.id.seekBar) as SeekBar).apply {
      max = DURATION
      setOnSeekBarChangeListener(object : SeekBar.OnSeekBarChangeListener {
        override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
          if (animView.height != 0) {
            animView.seek(progress.toLong())
          }
        }

        override fun onStartTrackingTouch(seekBar: SeekBar?) {
        }

        override fun onStopTrackingTouch(seekBar: SeekBar?) {
        }
      })
    }
  }

  inner class MyAnimationView(context: Context) : View(context), ValueAnimator.AnimatorUpdateListener, Animator.AnimatorListener {

    private object Constants {
      const val RED = 0xffFF8080.toInt()
      const val BLUE = 0xff8080FF.toInt()
      const val CYAN = 0xff80ffff.toInt()
      const val GREEN = 0xff80ff80.toInt()
      const val BALL_SIZE = 100f
    }

    val balls = arrayListOf<ShapeHolder>()
    internal var animation: AnimatorSet? = null
    internal var bounceAnim: ValueAnimator? = null
    internal val ball = addBall(200f, 0f)

    private fun createAnimation() {
      if (bounceAnim == null) {
        bounceAnim = ObjectAnimator.ofFloat(ball, "y", ball.y, height - Constants.BALL_SIZE).apply {
          duration = 1500
          interpolator = BounceInterpolator()
          addUpdateListener(this@MyAnimationView)
        }
      }
    }

    fun startAnimation() {
      createAnimation()
      bounceAnim!!.start()
    }

    fun seek(seekTime: Long) {
      createAnimation()
      bounceAnim!!.currentPlayTime = seekTime
    }

    private fun addBall(x: Float, y: Float): ShapeHolder {
      val red = (100 + Math.random() * 155).toInt()
      val green = (100 + Math.random() * 155).toInt()
      val blue = (100 + Math.random() * 155).toInt()
      val color = 0xff000000.toInt() or (red shl 16) or (green shl 8) or blue
      val darkColor = 0xff000000.toInt() or (red / 4 shl 16) or (green / 4 shl 8) or blue / 4
      val drawable = ShapeDrawable(OvalShape().apply { resize(Constants.BALL_SIZE, Constants.BALL_SIZE) })
         .apply { paint.shader = RadialGradient(37.5f, 12.5f, 50f, color, darkColor, Shader.TileMode.CLAMP) }
      val shapeHolder = ShapeHolder(drawable).apply {
        this.x = x
        this.y = y
        paint = drawable.paint
      }
      balls.add(shapeHolder)
      return shapeHolder
    }

    override fun onDraw(canvas: Canvas) {
      canvas.translate(ball.x, ball.y)
      ball.shape.draw(canvas)
    }

    override fun onAnimationEnd(animation: Animator?) {
      balls.remove((animation as ObjectAnimator).target)
    }

    override fun onAnimationStart(animation: Animator?) {}

    override fun onAnimationUpdate(animation: ValueAnimator?) {
      invalidate()
      val playTime = bounceAnim!!.currentPlayTime
      mSeekBar.progress = playTime.toInt()
    }

    override fun onAnimationRepeat(animation: Animator?) {}

    override fun onAnimationCancel(animation: Animator?) {}
  }
}
