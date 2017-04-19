package chiukingyee.animationsample

import android.app.ActivityOptions
import android.app.SharedElementCallback
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView

class ActivityTransition : AppCompatActivity() {

  companion object {
    private const val TAG = "ActivityTransition"

    private const val KEY_ID = "ViewTransitionValues:id"

    val DRAWABLES = intArrayOf(
       R.drawable.ball,
       R.drawable.block,
       R.drawable.ducky,
       R.drawable.jellies,
       R.drawable.mug,
       R.drawable.pencil,
       R.drawable.scissors,
       R.drawable.woot
    )

    val IDS = intArrayOf(
       R.id.ball,
       R.id.block,
       R.id.ducky,
       R.id.jellies,
       R.id.mug,
       R.id.pencil,
       R.id.scissors,
       R.id.woot
    )

    val NAMES = arrayOf(
       "ball",
       "block",
       "ducky",
       "jellies",
       "mug",
       "pencil",
       "scissors",
       "woot"
    )

    fun getIdForKey(id: String) = IDS[getIndexForKey(id)]

    fun getDrawableIdForKey(id: String) = DRAWABLES[getIndexForKey(id)]

    fun getIndexForKey(id: String): Int {
      (0..NAMES.size).forEach {
        val name = NAMES[it]
        if (name == id) {
          return it
        }
      }
      return 2
    }

    private fun randomColor(): Int {
      val red = (Math.random() * 128).toInt()
      val green = (Math.random() * 128).toInt()
      val blue = (Math.random() * 128).toInt()
      return 0xFF000000.toInt() or (red shl 16) or (green shl 8) or blue
    }
  }

  private var mHero: ImageView? = null

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    window.setBackgroundDrawable(ColorDrawable(randomColor()))
    setContentView(R.layout.image_block)
    setupHero()
  }

  private fun setupHero() {
    val name = intent.getStringExtra(KEY_ID)
    mHero = null
    if (name != null) {
      mHero = findViewById(getIdForKey(name)) as ImageView
      setEnterSharedElementCallback(object : SharedElementCallback() {
        override fun onMapSharedElements(names: MutableList<String>, sharedElements: MutableMap<String, View>) {
          sharedElements.put("hero", mHero!!)
        }
      })
    }
  }

  fun clicked(v: View) {
    mHero = v as ImageView
    val intent = Intent(this, ActivityTransitionDetails::class.java)
    intent.putExtra(KEY_ID, v.transitionName)
    val activityOptions = ActivityOptions.makeSceneTransitionAnimation(this, mHero, "hero")
    startActivity(intent, activityOptions.toBundle())
  }
}
