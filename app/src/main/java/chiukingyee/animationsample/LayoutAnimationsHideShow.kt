package chiukingyee.animationsample

import android.animation.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox
import android.widget.LinearLayout

class LayoutAnimationsHideShow : AppCompatActivity() {

  private var numButtons = 1
  internal lateinit var container: ViewGroup
  private lateinit var mTransitioner: LayoutTransition

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.layout_animations_hideshow)

    val hideGoneCB = findViewById(R.id.hideGoneCB) as CheckBox

    container = LinearLayout(this).apply {
      layoutParams = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
    }

    for (i in 0..3) {
      Button(this).apply {
        text = "$i"
        container.addView(this)
        setOnClickListener { it.visibility = if (hideGoneCB.isChecked) View.GONE else View.INVISIBLE }
      }
    }

    resetTransition()

    (findViewById(R.id.parent) as ViewGroup).apply {
      addView(container)
    }

    (findViewById(R.id.addNewButton) as Button).apply {
      setOnClickListener {
        for (i in 0..container.childCount - 1) {
          container.getChildAt(i).visibility = View.VISIBLE
        }
      }
    }

    (findViewById(R.id.customAnimCB) as CheckBox).apply {
      setOnCheckedChangeListener { _, isChecked ->
        val duration = if (isChecked) 500 else 300
        if (isChecked) {
          mTransitioner.setStagger(LayoutTransition.CHANGE_APPEARING, 30)
          mTransitioner.setStagger(LayoutTransition.CHANGE_DISAPPEARING, 30)
          setupCustomAnimations()
        } else {
          resetTransition()
        }
        mTransitioner.setDuration(duration.toLong())
      }
    }
  }

  private fun resetTransition() {
    mTransitioner = LayoutTransition()
    container.layoutTransition = mTransitioner
  }

  private fun setupCustomAnimations() {
    val pvhLeft = PropertyValuesHolder.ofInt("left", 0, 1)
    val pvhTop = PropertyValuesHolder.ofInt("top", 0, 1)
    val pvhRight = PropertyValuesHolder.ofInt("right", 0, 1)
    val pvhBottom = PropertyValuesHolder.ofInt("bottom", 0, 1)
    val pvhScaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f, 1f)
    val pvhScaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f, 1f)
    ObjectAnimator.ofPropertyValuesHolder(
       this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhScaleX, pvhScaleY).apply {
      duration = mTransitioner.getDuration(LayoutTransition.CHANGE_APPEARING)
      mTransitioner.setAnimator(LayoutTransition.CHANGE_APPEARING, this)
      addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(anim: Animator) {
          ((anim as ObjectAnimator).target as View).apply {
            scaleX = 1f
            scaleY = 1f
          }
        }
      })
    }

    val kf0 = Keyframe.ofFloat(0f, 0f)
    val kf1 = Keyframe.ofFloat(.9999f, 360f)
    val kf2 = Keyframe.ofFloat(1f, 0f)
    val pvhRotation = PropertyValuesHolder.ofKeyframe("rotation", kf0, kf1, kf2)
    ObjectAnimator.ofPropertyValuesHolder(this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhRotation).apply {
      duration = mTransitioner.getDuration(LayoutTransition.CHANGE_DISAPPEARING)
      mTransitioner.setAnimator(LayoutTransition.CHANGE_DISAPPEARING, this)
      addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(anim: Animator) {
          ((anim as ObjectAnimator).target as View).rotation = 0f
        }
      })
    }

    ObjectAnimator.ofFloat(null, "rotationY", 90f, 0f).apply {
      duration = mTransitioner.getDuration(LayoutTransition.APPEARING)
      mTransitioner.setAnimator(LayoutTransition.APPEARING, this)
      addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(anim: Animator) {
          ((anim as ObjectAnimator).target as View).rotationY = 0f
        }
      })
    }

    ObjectAnimator.ofFloat(null, "rotationX", 0f, 90f).apply {
      duration = mTransitioner.getDuration(LayoutTransition.DISAPPEARING)
      mTransitioner.setAnimator(LayoutTransition.DISAPPEARING, this)
      addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(anim: Animator) {
          ((anim as ObjectAnimator).target as View).rotationX = 0f
        }
      })
    }
  }
}
