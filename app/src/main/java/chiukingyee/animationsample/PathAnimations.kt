package chiukingyee.animationsample

import android.animation.ObjectAnimator
import android.animation.TypeConverter
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.AttributeSet
import android.util.Property
import android.view.View
import android.view.animation.Animation
import android.view.animation.LinearInterpolator
import android.widget.FrameLayout
import android.widget.RadioGroup

class PathAnimations : AppCompatActivity(), RadioGroup.OnCheckedChangeListener, View.OnLayoutChangeListener {

  companion object {
    internal val sTraversalPath = Path()
    internal const val TRAVERSE_PATH_SIZE = 7.0f
    internal val POINT_PROPERTY = object : Property<PathAnimations, Point>(Point::class.java, "point") {
      override fun get(`object`: PathAnimations): Point {
        val v = `object`.findViewById(R.id.moved_item)
        return Point(Math.round(v.x), Math.round(v.y))
      }
    }

    init {
      with(sTraversalPath) {
        val inverse_sqrt8 = Math.sqrt(0.125).toFloat()
        val bounds = RectF(1f, 1f, 3f, 3f)
        addArc(bounds, 45f, 180f)
        addArc(bounds, 225f, 180f)

        bounds.set(1.5f + inverse_sqrt8, 1.5f + inverse_sqrt8, 2.5f + inverse_sqrt8, 2.5f + inverse_sqrt8)
        addArc(bounds, 45f, 180f)
        addArc(bounds, 225f, 180f)

        bounds.set(4f, 1f, 6f, 3f)
        addArc(bounds, 135f, -180f)
        addArc(bounds, -45f, -180f)

        bounds.set(4.5f - inverse_sqrt8, 1.5f + inverse_sqrt8, 5.5f - inverse_sqrt8, 2.5f + inverse_sqrt8)
        addArc(bounds, 135f, -180f)
        addArc(bounds, -45f, -180f)

        addCircle(3.5f, 3.5f, 0.5f, Path.Direction.CCW)

        addArc(RectF(1f, 2f, 6f, 6f), 0f, 180f)
      }
    }
  }

  private lateinit var mCanvasView: CanvasView
  private var mAnimator: ObjectAnimator? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.path_animations)
    mCanvasView = findViewById(R.id.canvas) as CanvasView
    mCanvasView.addOnLayoutChangeListener(this)
    (findViewById(R.id.path_animation_type) as RadioGroup).setOnCheckedChangeListener(this)
  }

  fun setCoordinates(x: Int, y: Int) {
    changeCoordinates(x.toFloat(), y.toFloat())
  }

  fun changeCoordinates(x: Float, y: Float) {
    findViewById(R.id.moved_item).apply {
      this.x = x
      this.y = y
    }
  }

  fun setPoint(point: PointF) {
    changeCoordinates(point.x, point.y)
  }

  override fun onCheckedChanged(group: RadioGroup?, checkedId: Int) {
    startAnimator(checkedId)
  }

  override fun onLayoutChange(v: View?, left: Int, top: Int, right: Int, bottom: Int, oldLeft: Int, oldTop: Int, oldRight: Int, oldBottom: Int) {
    val checkedId = (findViewById(R.id.path_animation_type) as RadioGroup).checkedRadioButtonId
    if (checkedId != RadioGroup.NO_ID) {
      startAnimator(checkedId)
    }
  }

  private fun startAnimator(checkedId: Int) {
    if (mAnimator != null) {
      mAnimator!!.cancel()
      mAnimator = null
    }

    val view = findViewById(R.id.moved_item)
    val path = mCanvasView.path
    if (path.isEmpty) {
      return
    }

    when (checkedId) {
      R.id.named_components -> mAnimator = ObjectAnimator.ofFloat(view, "x", "y", path)
      R.id.property_components -> mAnimator = ObjectAnimator.ofFloat(view, View.X, View.Y, path)
      R.id.multi_int -> mAnimator = ObjectAnimator.ofMultiInt(this, "setCoordinates", path)
      R.id.multi_float -> mAnimator = ObjectAnimator.ofMultiFloat(this, "changeCoordinates", path)
      R.id.named_setter -> mAnimator = ObjectAnimator.ofObject(this, "point", null as TypeConverter<PointF, *>?, path)
      R.id.property_setter -> mAnimator = ObjectAnimator.ofObject(this, POINT_PROPERTY, PointFToPointConverter(), path)
    }

    mAnimator!!.apply {
      duration = 10000
      repeatMode = Animation.RESTART
      repeatCount = ValueAnimator.INFINITE
      interpolator = LinearInterpolator()
      start()
    }
  }

  class CanvasView : FrameLayout {

    internal val mPath = Path()
    internal val mPathPaint = Paint()

    val path
      get() = mPath

    constructor(context: Context) : super(context) {
      init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
      init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(context, attrs, defStyle) {
      init()
    }

    private fun init() {
      setWillNotDraw(false)
      mPathPaint.apply {
        color = 0xFFFF0000.toInt()
        strokeWidth = 2.0f
        style = Paint.Style.STROKE
      }
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
      super.onLayout(changed, left, top, right, bottom)
      if (changed) {
        val scale = Matrix()
        val scaleWidth = (right - left) / TRAVERSE_PATH_SIZE
        val scaleHeight = (bottom - top) / TRAVERSE_PATH_SIZE
        scale.setScale(scaleWidth, scaleHeight)
        sTraversalPath.transform(scale, mPath)
      }
    }

    override fun draw(canvas: Canvas) {
      canvas.drawPath(mPath, mPathPaint)
      super.draw(canvas)
    }
  }

  private class PointFToPointConverter : TypeConverter<PointF, Point>(PointF::class.java, Point::class.java) {

    internal val mPoint = Point()

    override fun convert(value: PointF): Point {
      mPoint.set(Math.round(value.x), Math.round(value.y))
      return mPoint
    }
  }
}
