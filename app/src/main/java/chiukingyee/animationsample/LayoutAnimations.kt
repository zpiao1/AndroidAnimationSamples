package chiukingyee.animationsample

import android.animation.*
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CheckBox

class LayoutAnimations : AppCompatActivity() {

  private var numButtons = 1
  internal lateinit var container: ViewGroup
  internal lateinit var defaultAppearingAnim: Animator
  internal lateinit var defaultDisappearingAnim: Animator
  internal lateinit var defaultChangingAppearingAnim: Animator
  internal lateinit var defaultChangingDisappearingAnim: Animator
  internal lateinit var customAppearingAnim: Animator
  internal lateinit var customDisappearingAnim: Animator
  internal lateinit var customChangingAppearingAnim: Animator
  internal lateinit var customChangingDisappearingAnim: Animator
  internal lateinit var currentAppearingAnim: Animator
  internal lateinit var currentDisappearingAnim: Animator
  internal lateinit var currentChangingAppearingAnim: Animator
  internal lateinit var currentChangingDisappearingAnim: Animator

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.layout_animations)

    container = FixedGridLayout(this).apply {
      clipChildren = false
      cellHeight = 90
      cellWidth = 100
    }

    val transitioner = LayoutTransition()
    container.layoutTransition = transitioner
    defaultAppearingAnim = transitioner.getAnimator(LayoutTransition.APPEARING)
    defaultDisappearingAnim = transitioner.getAnimator(LayoutTransition.DISAPPEARING)
    defaultChangingAppearingAnim = transitioner.getAnimator(LayoutTransition.CHANGE_APPEARING)
    defaultChangingDisappearingAnim = transitioner.getAnimator(LayoutTransition.CHANGE_DISAPPEARING)

    createCustomAnimations(transitioner)

    currentAppearingAnim = defaultAppearingAnim
    currentDisappearingAnim = defaultDisappearingAnim
    currentChangingAppearingAnim = defaultChangingAppearingAnim
    currentChangingDisappearingAnim = defaultChangingDisappearingAnim

    (findViewById(R.id.parent) as ViewGroup).apply {
      addView(container)
      clipChildren = false
    }

    (findViewById(R.id.addNewButton) as Button).apply {
      setOnClickListener {
        val newButton = Button(this@LayoutAnimations).apply {
          text = numButtons++.toString()
          setOnClickListener { container.removeView(it) }
        }
        container.addView(newButton, Math.min(1, container.childCount))
      }
    }

    (findViewById(R.id.customAnimCB) as CheckBox).apply {
      setOnCheckedChangeListener { _, _ ->
        setupTransition(transitioner)
      }
    }

    (findViewById(R.id.appearingCB) as CheckBox).apply {
      setOnCheckedChangeListener { _, _ ->
        setupTransition(transitioner)
      }
    }

    (findViewById(R.id.disappearingCB) as CheckBox).apply {
      setOnCheckedChangeListener { _, _ ->
        setupTransition(transitioner)
      }
    }

    (findViewById(R.id.changingAppearingCB) as CheckBox).apply {
      setOnCheckedChangeListener { _, _ ->
        setupTransition(transitioner)
      }
    }

    (findViewById(R.id.changingDisappearingCB) as CheckBox).apply {
      setOnCheckedChangeListener { _, _ ->
        setupTransition(transitioner)
      }
    }
  }

  private fun setupTransition(transition: LayoutTransition) {
    val customAnimCB = findViewById(R.id.customAnimCB) as CheckBox
    val appearingCB = findViewById(R.id.appearingCB) as CheckBox
    val disappearingCB = findViewById(R.id.disappearingCB) as CheckBox
    val changingAppearingCB = findViewById(R.id.changingAppearingCB) as CheckBox
    val changingDisappearingCB = findViewById(R.id.changingDisappearingCB) as CheckBox
    transition.apply {
      setAnimator(LayoutTransition.APPEARING,
         if (appearingCB.isChecked)
           if (customAnimCB.isChecked) customAppearingAnim
           else defaultAppearingAnim
         else null)
      setAnimator(LayoutTransition.DISAPPEARING,
         if (disappearingCB.isChecked)
           if (customAnimCB.isChecked) customDisappearingAnim
           else defaultDisappearingAnim
         else null)
      setAnimator(LayoutTransition.CHANGE_APPEARING,
         if (changingAppearingCB.isChecked)
           if (customAnimCB.isChecked) customChangingAppearingAnim
           else defaultChangingAppearingAnim
         else null)
      setAnimator(LayoutTransition.CHANGE_DISAPPEARING,
         if (changingDisappearingCB.isChecked)
           if (customAnimCB.isChecked) customChangingDisappearingAnim
           else defaultChangingDisappearingAnim
         else null)
    }
  }

  private fun createCustomAnimations(transition: LayoutTransition) {
    val pvhLeft = PropertyValuesHolder.ofInt("left", 0, 1)
    val pvhTop = PropertyValuesHolder.ofInt("top", 0, 1)
    val pvhRight = PropertyValuesHolder.ofInt("right", 0, 1)
    val pvhBottom = PropertyValuesHolder.ofInt("bottom", 0, 1)
    val pvhScaleX = PropertyValuesHolder.ofFloat("scaleX", 1f, 0f, 1f)
    val pvhScaleY = PropertyValuesHolder.ofFloat("scaleY", 1f, 0f, 1f)
    customChangingAppearingAnim = ObjectAnimator.ofPropertyValuesHolder(
       this, pvhLeft, pvhRight, pvhTop, pvhBottom, pvhScaleX, pvhScaleY).apply {
      duration = transition.getDuration(LayoutTransition.CHANGE_APPEARING)
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
    customChangingDisappearingAnim = ObjectAnimator.ofPropertyValuesHolder(
       this, pvhLeft, pvhTop, pvhRight, pvhBottom, pvhRotation).apply {
      duration = transition.getDuration(LayoutTransition.CHANGE_DISAPPEARING)
      addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(anim: Animator) {
          ((anim as ObjectAnimator).target as View).rotation = 0f
        }
      })
    }

    customAppearingAnim = ObjectAnimator.ofFloat(null, "rotationY", 90f, 0f).apply {
      duration = transition.getDuration(LayoutTransition.APPEARING)
      addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(anim: Animator) {
          ((anim as ObjectAnimator).target as View).rotationY = 0f
        }
      })
    }

    customDisappearingAnim = ObjectAnimator.ofFloat(null, "rotationX", 0f, 90f).apply {
      duration = transition.getDuration(LayoutTransition.DISAPPEARING)
      addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(anim: Animator) {
          ((anim as ObjectAnimator).target as View).rotationX = 0f
        }
      })
    }
  }
}
