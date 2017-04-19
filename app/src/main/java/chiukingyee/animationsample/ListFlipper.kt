package chiukingyee.animationsample

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.ObjectAnimator
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.animation.AccelerateInterpolator
import android.view.animation.DecelerateInterpolator
import android.view.animation.Interpolator
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.SeekBar

class ListFlipper : AppCompatActivity() {

  companion object {
    private const val DURATION = 1500
    private val LIST_STRINGS_EN = arrayOf(
       "One",
       "Two",
       "Three",
       "Four",
       "Five",
       "Six"
    )
    private val LIST_STRINGS_FR = arrayOf(
       "Un",
       "Deux",
       "Trois",
       "Quatre",
       "Le Five",
       "Six"
    )
  }

  private lateinit var mSeekBar: SeekBar
  internal lateinit var mEnglishList: ListView
  internal lateinit var mFrenchList: ListView

  private val accelerator: Interpolator = AccelerateInterpolator()
  private val decelerator: Interpolator = DecelerateInterpolator()

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.rotating_list)
    mEnglishList = findViewById(R.id.list_en) as ListView
    mFrenchList = findViewById(R.id.list_fr) as ListView

    val adapterEn = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, LIST_STRINGS_EN)
    val adapterFr = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, LIST_STRINGS_FR)

    mEnglishList.adapter = adapterEn
    mFrenchList.adapter = adapterFr
    mFrenchList.rotationY = -90f

    val starter = findViewById(R.id.button) as Button
    starter.setOnClickListener { flipit() }
  }

  private fun flipit() {
    val visibleList = if (mEnglishList.visibility == View.GONE) mFrenchList else mEnglishList
    val invisibleList = if (mEnglishList.visibility == View.GONE) mEnglishList else mFrenchList
    val visToInvis = ObjectAnimator.ofFloat(visibleList, "rotationY", 0f, 90f).apply {
      duration = 500
      interpolator = accelerator
    }
    val invisToVis = ObjectAnimator.ofFloat(invisibleList, "rotationY", -90f, 0f).apply {
      duration = 500
      interpolator = decelerator
    }

    visToInvis.apply {
      addListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
          visibleList.visibility = View.GONE
          invisToVis.start()
          invisibleList.visibility = View.VISIBLE
        }
      })
    }
    visToInvis.start()
  }
}
