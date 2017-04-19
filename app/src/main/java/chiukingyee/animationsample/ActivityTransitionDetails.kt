package chiukingyee.animationsample

import android.app.ActivityOptions
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.ImageView

class ActivityTransitionDetails : AppCompatActivity() {

  companion object {
    private const val TAG = "ActivityTransitionDetails"

    private const val KEY_ID = "ViewTransitionValues:id"

    private fun randomColor(): Int {
      val red = (Math.random() * 128).toInt()
      val green = (Math.random() * 128).toInt()
      val blue = (Math.random() * 128).toInt()
      return 0xFF000000.toInt() or (red shl 16) or (green shl 8) or blue
    }
  }

  private var mImageResourceId = R.drawable.ducky

  private var mName = "ducky"

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    window.setBackgroundDrawable(ColorDrawable(randomColor()))
    setContentView(R.layout.image_details)
    val titleImage = findViewById(R.id.titleImage) as ImageView
    titleImage.setImageDrawable(getHeroDrawable())
  }

  private fun getHeroDrawable(): Drawable {
    val name = intent.getStringExtra(KEY_ID)
    if (name != null) {
      mName = name
      mImageResourceId = ActivityTransition.getDrawableIdForKey(name)
    }

    return getDrawable(mImageResourceId)
  }

  fun clicked(v: View) {
    val intent = Intent(this, ActivityTransition::class.java)
    intent.putExtra(KEY_ID, mName)
    val activityOptions = ActivityOptions.makeSceneTransitionAnimation(this, v, "hero")
    startActivity(intent, activityOptions.toBundle())
  }
}
