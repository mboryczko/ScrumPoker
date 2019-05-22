package pl.michalboryczko.exercise.ui.activesession

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.flurry.android.FlurryAgent
import kotlinx.android.synthetic.main.activity_active_session.*
import pl.michalboryczko.exercise.R
import pl.michalboryczko.exercise.app.BaseActivity
import pl.michalboryczko.exercise.model.ActiveSession
import pl.michalboryczko.exercise.model.api.Estimation
import pl.michalboryczko.exercise.model.api.Session
import pl.michalboryczko.exercise.model.api.Story
import pl.michalboryczko.exercise.model.base.Status
import pl.michalboryczko.exercise.utils.Constants

class ActiveSessionActivity : BaseActivity<ActiveSessionViewModel>() {

    companion object {
        fun prepareIntent(activity: Activity, session: Session): Intent{
            val intent = Intent(activity, ActiveSessionActivity::class.java)
            intent.putExtra(Constants.SESSION_BUNDLE, session)
            return intent
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_active_session)
        FlurryAgent.logEvent("ActiveSessionActivity - launched")

        if(intent.hasExtra(Constants.SESSION_BUNDLE)){
            val session = intent.getSerializableExtra(Constants.SESSION_BUNDLE) as Session
            viewModel.initialize(session)
        }

        createStoryButton.setOnClickListener {
            viewModel.createStory(createStoryName.text.toString(), createStoryDescription.text.toString())
        }

        saveEstimationButton.setOnClickListener {

            FlurryAgent.logEvent("ActiveSessionActivity - save estimation clicked")
            viewModel.saveEstimationClicked(pointsEstimation.text.toString().toInt())
        }


        observeUserLoginStatus()

        viewModel.session.observe(this, Observer {
            it?.let { r ->
                when(r.status){
                    Status.INITIAL -> {}
                    Status.LOADING -> showLoading()
                    Status.ERROR -> {}
                    Status.ERROR_ID -> {}
                    Status.SUCCESS -> {
                        val activeSession = r.data
                        if(activeSession != null){
                            showSession(activeSession)
                        }

                    }
                }
            }
        })

        viewModel.story.observe(this, Observer {
            it?.let { r ->
                when(r.status){
                    Status.INITIAL -> {}
                    Status.LOADING -> showLoading()
                    Status.ERROR -> {}
                    Status.ERROR_ID -> {}
                    Status.SUCCESS -> {
                        val activeStory = r.data
                        if(activeStory != null){
                            showStory(activeStory)
                        }
                    }
                }
            }
        })

    }

    fun showLoading(){
        hideViews(activeSession, activeStory, activeStoryDescription)
    }

    fun showSession(session: ActiveSession){
        activeSession.text = session.session.name
        if(!session.isMaster){
            hideViews(createStoryLayout)
        }
    }

    fun showStory(story: Story){
        showViews(activeSession, activeStory, activeStoryDescription)
        activeStory.text = story.story
        activeStoryDescription.text = story.description

        val estimations = story.estimations
        if(estimations != null){
            estimationsRecyclerView.layoutManager = LinearLayoutManager(this, RecyclerView.HORIZONTAL, false)
            estimationsRecyclerView.adapter = EstimationsAdapter(convert(estimations.toList()), this)
            showViews(estimationsRecyclerView)
        }else{
            hideViews(estimationsRecyclerView)
        }

    }

    fun convert(list: List<Pair<String, Estimation>>): List<Estimation>{
        val output = mutableListOf<Estimation>()
        list.forEach {
            output.add(it.second)
        }

        return output
    }


    override fun initViewModel() {
        viewModel = getGenericViewModel()
    }
}
