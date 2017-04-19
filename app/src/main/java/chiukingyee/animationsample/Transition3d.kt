package chiukingyee.animationsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateInterpolator
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ImageView
import android.widget.ListView

class Transition3d : AppCompatActivity(), AdapterView.OnItemClickListener, View.OnClickListener {

  companion object {
    private val PHOTO_NAMES = arrayOf(
       "Lyon",
       "Livermore",
       "Tahoe Pier",
       "Lake Tahoe",
       "Grand Canyon",
       "Bodie"
    )

    private val PHOTO_RESOURCES = arrayOf(
       R.drawable.photo1,
       R.drawable.photo2,
       R.drawable.photo3,
       R.drawable.photo4,
       R.drawable.photo5,
       R.drawable.photo6
    )
  }

  private lateinit var mPhotoList: ListView
  private lateinit var mContainer: ViewGroup
  private lateinit var mImageView: ImageView

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.animations_main_screen)

    mPhotoList = findViewById(android.R.id.list) as ListView
    mImageView = findViewById(R.id.picture) as ImageView
    mContainer = findViewById(R.id.container) as ViewGroup

    val adapter = ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, PHOTO_NAMES)

    mPhotoList.apply {
      this.adapter = adapter
      onItemClickListener = this@Transition3d
    }

    mImageView.apply {
      isClickable = true
      isFocusable = true
      setOnClickListener(this@Transition3d)
    }

    mContainer.persistentDrawingCache = ViewGroup.PERSISTENT_ANIMATION_CACHE
  }

  private fun applyRotation(position: Int, start: Float, end: Float) {
    val centerX = mContainer.width / 2.0f
    val centerY = mContainer.height / 2.0f

    val rotation = Rotate3dAnimation(start, end, centerX, centerY, 310.0f, true).apply {
      duration = 500
      fillAfter = true
      interpolator = AccelerateInterpolator()
      setAnimationListener(DisplayNextView(position))
    }

    mContainer.startAnimation(rotation)
  }

  override fun onItemClick(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
    mImageView.setImageResource(PHOTO_RESOURCES[position])
    applyRotation(position, 0f, 90f)
  }

  override fun onClick(v: View?) {
    applyRotation(-1, 180f, 90f)
  }

  private inner class DisplayNextView(private val position: Int) : Animation.AnimationListener {
    override fun onAnimationRepeat(animation: Animation?) {
    }

    override fun onAnimationEnd(animation: Animation?) {
      mContainer.post(SwapViews(position))
    }

    override fun onAnimationStart(animation: Animation?) {
    }
  }

  private inner class SwapViews(private val position: Int) : Runnable {

    override fun run() {
      val centerX = mContainer.width / 2.0f
      val centerY = mContainer.height / 2.0f

      val rotation = if (position > -1) Rotate3dAnimation(90f, 180f, centerX, centerY, 310.0f, false)
      else Rotate3dAnimation(90f, 0f, centerX, centerY, 310.0f, false)

      if (position > -1) {
        mPhotoList.visibility = View.GONE
        mImageView.visibility = View.VISIBLE
        mImageView.requestFocus()
      } else {
        mImageView.visibility = View.GONE
        mPhotoList.visibility = View.VISIBLE
        mPhotoList.requestFocus()
      }

      rotation.apply {
        duration = 500
        fillAfter = true
        interpolator = DecelerateInterpolator()
      }

      mContainer.startAnimation(rotation)
    }
  }
}
