package chiukingyee.animationsample

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

  override fun onCreate(savedInstanceState: Bundle?) {
    super.onCreate(savedInstanceState)
    setContentView(R.layout.activity_main)

    findViewById(R.id.activity_transition)
       .setOnClickListener { startActivity(Intent(this, ActivityTransition::class.java)) }

    findViewById(R.id.animation_cloning)
       .setOnClickListener { startActivity(Intent(this, AnimationCloning::class.java)) }

    findViewById(R.id.animation_loading)
       .setOnClickListener { startActivity(Intent(this, AnimationLoading::class.java)) }

    findViewById(R.id.animation_seeking)
       .setOnClickListener { startActivity(Intent(this, AnimationSeeking::class.java)) }

    findViewById(R.id.animator_events)
       .setOnClickListener { startActivity(Intent(this, AnimatorEvents::class.java)) }

    findViewById(R.id.bouncing_balls)
       .setOnClickListener { startActivity(Intent(this, BouncingBalls::class.java)) }

    findViewById(R.id.custom_evaluator)
       .setOnClickListener { startActivity(Intent(this, CustomEvaluator::class.java)) }

    findViewById(R.id.layout_animations)
       .setOnClickListener { startActivity(Intent(this, LayoutAnimations::class.java)) }

    findViewById(R.id.layout_animations_by_default)
       .setOnClickListener { startActivity(Intent(this, LayoutAnimationsByDefault::class.java)) }

    findViewById(R.id.layout_animations_hide_show)
       .setOnClickListener { startActivity(Intent(this, LayoutAnimationsHideShow::class.java)) }

    findViewById(R.id.list_flipper)
       .setOnClickListener { startActivity(Intent(this, ListFlipper::class.java)) }

    findViewById(R.id.multi_property_animation)
       .setOnClickListener { startActivity(Intent(this, MultiPropertyAnimation::class.java)) }

    findViewById(R.id.path_animations)
       .setOnClickListener { startActivity(Intent(this, PathAnimations::class.java)) }

    findViewById(R.id.reversing_animations)
       .setOnClickListener { startActivity(Intent(this, ReversingAnimation::class.java)) }

    findViewById(R.id.transition_3d)
       .setOnClickListener { startActivity(Intent(this, Transition3d::class.java)) }

    findViewById(R.id.transitions)
       .setOnClickListener { startActivity(Intent(this, Transitions::class.java)) }
  }
}
