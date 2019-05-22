package pl.michalboryczko.exercise.ui.session

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.flurry.android.FlurryAgent
import kotlinx.android.synthetic.main.activity_session.*
import pl.michalboryczko.exercise.R
import pl.michalboryczko.exercise.app.BaseActivity
import pl.michalboryczko.exercise.model.base.Status

class SessionActivity : BaseActivity<SessionViewModel>() {

    companion object {
        fun prepareIntent(activity: Activity) = Intent(activity, SessionActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_session)

        FlurryAgent.logEvent("SessionActivity - launched")
        mixpanel.track("SessionActivity - launched")

        createSessionButton.setOnClickListener {
            viewModel.createSession(sessionNameEditText.text.toString(), sessionPassword.text.toString())
        }

        joinSessionButton.setOnClickListener {
            viewModel.joinSession(joinSessionIdText.text.toString(), joinSessionPassword.text.toString())
        }

        logoutButton.setOnClickListener {
            viewModel.logout()
        }

        observeUserLoginStatus()

        viewModel.session.observe(this, Observer {
            it?.let { r ->
                when(r.status){
                    Status.INITIAL -> initial()
                    Status.LOADING -> showLoading()
                    Status.ERROR -> {hideViews(progressBar)}
                    Status.ERROR_ID -> {hideViews(progressBar)}
                    Status.SUCCESS -> {
                        val session = r.data
                        if(session != null){
                            navigator.navigateToActiveSessionActivity(this, session)
                        }
                    }
                }
            }
        })
    }

    fun initial(){
        enableViews(createSessionButton, joinSessionButton)
        hideViews(progressBar)
    }

    fun showLoading(){
        disableViews(createSessionButton, joinSessionButton)
        showViews(progressBar)
    }

    override fun initViewModel() {
        viewModel = getGenericViewModel()
    }
}
