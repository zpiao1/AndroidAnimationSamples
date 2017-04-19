package chiukingyee.animationsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Button
import android.widget.GridLayout

class LayoutAnimationsByDefault : AppCompatActivity() {

  private var numButtons = 1
  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.layout_animations_by_default)

    val gridContainer = findViewById(R.id.gridContainer) as GridLayout

    (findViewById(R.id.addNewButton) as Button).apply {
      setOnClickListener {
        val newButton = Button(this@LayoutAnimationsByDefault).apply {
          text = "${numButtons++}"
          setOnClickListener { gridContainer.removeView(it) }
        }
        gridContainer.addView(newButton, Math.min(1, gridContainer.childCount))
      }
    }
  }
}
