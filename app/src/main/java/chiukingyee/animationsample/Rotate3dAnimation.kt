package chiukingyee.animationsample

import android.graphics.Camera
import android.view.animation.Animation
import android.view.animation.Transformation

class Rotate3dAnimation(
   private val fromDegrees: Float,
   private val toDegrees: Float,
   private val centerX: Float,
   private val centerY: Float,
   private val depthZ: Float,
   private val reverse: Boolean) : Animation() {

  private lateinit var mCamera: Camera

  override fun initialize(width: Int, height: Int, parentWidth: Int, parentHeight: Int) {
    super.initialize(width, height, parentWidth, parentHeight)
    mCamera = Camera()
  }

  override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
    val degrees = fromDegrees + ((toDegrees - fromDegrees) * interpolatedTime)

    val matrix = t.matrix

    mCamera.apply {
      save()
      if (reverse) {
        translate(0.0f, 0.0f, depthZ * interpolatedTime)
      } else {
        translate(0.0f, 0.0f, depthZ * (1.0f - interpolatedTime))
      }
      rotateY(degrees)
      getMatrix(matrix)
      restore()
    }

    matrix.apply {
      preTranslate(-centerX, -centerY)
      postTranslate(centerX, centerY)
    }
  }
}