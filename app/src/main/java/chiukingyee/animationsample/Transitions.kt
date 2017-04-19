package chiukingyee.animationsample

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.transition.Scene
import android.transition.TransitionInflater
import android.transition.TransitionManager
import android.view.View
import android.view.ViewGroup

class Transitions : AppCompatActivity() {

  private lateinit var mScene1: Scene
  private lateinit var mScene2: Scene
  private lateinit var mScene3: Scene
  private lateinit var mSceneRoot: ViewGroup
  private lateinit var mTransitionManger: TransitionManager

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.transition)

    mSceneRoot = findViewById(R.id.sceneRoot) as ViewGroup

    val inflater = TransitionInflater.from(this)

    mScene1 = Scene.getSceneForLayout(mSceneRoot, R.layout.transition_scene1, this)
    mScene2 = Scene.getSceneForLayout(mSceneRoot, R.layout.transition_scene2, this)
    mScene3 = Scene.getSceneForLayout(mSceneRoot, R.layout.transition_scene3, this)
    mTransitionManger = inflater.inflateTransitionManager(R.transition.transitions_mgr, mSceneRoot)
  }

  fun selectScene(view: View) {
    when (view.id) {
      R.id.scene1 -> mTransitionManger.transitionTo(mScene1)
      R.id.scene2 -> mTransitionManger.transitionTo(mScene2)
      R.id.scene3 -> mTransitionManger.transitionTo(mScene3)
      R.id.scene4 -> {
        TransitionManager.beginDelayedTransition(mSceneRoot)
        setNewSize(R.id.view1, 150, 25)
        setNewSize(R.id.view2, 150, 25)
        setNewSize(R.id.view3, 150, 25)
        setNewSize(R.id.view4, 150, 25)
      }
    }
  }

  private fun setNewSize(id: Int, width: Int, height: Int) {
    val view = findViewById(id)
    val params = view.layoutParams
    params.width = width
    params.height = height
    view.layoutParams = params
  }
}
