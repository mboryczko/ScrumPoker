package pl.michalboryczko.exercise.ui.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.lifecycle.Observer
import com.uxcam.UXCam
import kotlinx.android.synthetic.main.activity_login.*
import pl.michalboryczko.exercise.R
import pl.michalboryczko.exercise.app.BaseActivity
import pl.michalboryczko.exercise.model.api.call.LoginCall
import pl.michalboryczko.exercise.model.base.Status

class LoginActivity : BaseActivity<LoginViewModel>() {


    companion object {
        fun prepareIntent(activity: Activity) = Intent(activity, LoginActivity::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        UXCam.startWithKey("em4z8osou79jvb4");

        emailEditText.setText("a@b.pl")
        passwordEditText.setText("testtest1")

        loginButton.setOnClickListener { viewModel.loginClicked(
                LoginCall(
                        emailEditText.text.toString(),
                        passwordEditText.text.toString())
        )}

        registerHereTextView.setOnClickListener{
            navigator.navigateToRegisterActivity(this)
        }

        viewModel.status.observe(this, Observer {
            it?.let { r ->
                when(r.status){
                    Status.INITIAL -> showInitial()
                    Status.LOADING -> showLoading()
                    Status.ERROR -> showError(r.message)
                    Status.ERROR_ID -> showError(r.resourceIdMessage)
                    Status.SUCCESS -> { navigator.navigateToCreateSessionActivity(this) }
                }
            }
        })
    }

    private fun showInitial(){
        showViews()
        hideViews(progressBar)
        enableViews(loginButton)
    }

    private fun showLoading(){
        showViews(progressBar)
        hideViews()
        disableViews(loginButton)
    }

    private fun showError(errorMsg: String){
        showViews()
        hideViews(progressBar)
        showToast(this, errorMsg)
        enableViews(loginButton)
    }

    private fun showError(error: Int){
        showViews()
        hideViews(progressBar)
        showToast(this, error)
        enableViews(loginButton)
    }

    override fun initViewModel() {
        viewModel = getGenericViewModel()
    }
}
