package pl.michalboryczko.exercise.app

import android.content.Context
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.flurry.android.FlurryAgent
import com.google.android.material.snackbar.Snackbar
import com.mixpanel.android.mpmetrics.MixpanelAPI
import com.uxcam.UXCam
import dagger.android.support.DaggerAppCompatActivity
import timber.log.Timber
import javax.inject.Inject


abstract class BaseActivity<T: BaseViewModel>  : DaggerAppCompatActivity() {

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    @Inject
    lateinit var navigator: Navigator

    lateinit var viewModel : T

    lateinit var mixpanel: MixpanelAPI

    inline fun <reified T: BaseViewModel> getGenericViewModel(): T {
        return ViewModelProviders.of(this, viewModelFactory).get(T::class.java)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModel()
        lifecycle.addObserver(viewModel)

        UXCam.startWithKey("em4z8osou79jvb4");
        mixpanel = MixpanelAPI.getInstance(this, "92ae5834dd72dbea8a203026af8a3559")
        observeToastMessage()
    }

    override fun onDestroy() {
        super.onDestroy()
        mixpanel.flush()
    }

    abstract fun initViewModel()

    fun observeUserLoginStatus(){
        viewModel.loggedInStatus.observe(this, Observer {
            it?.let { isLoggedIn ->
                FlurryAgent.logEvent("BaseActivity - isLoggedIn: $isLoggedIn")
                mixpanel.track("BaseActivity - isLoggedIn: $isLoggedIn")
                if(!isLoggedIn){
                    navigator.navigateToLoginActivity(this)
                }
            }
        })
    }

    fun observeToastMessage(){
        viewModel.toastInfo.observe(this, Observer {
            it?.let { msg ->
                Toast.makeText(this, msg.getContentIfNotHandled(), Toast.LENGTH_LONG).show()
            }
        })

        viewModel.toastInfoResource.observe(this, Observer {
            it?.let { msg ->
                val res = msg.getContentIfNotHandled()
                if(res != null)
                    Toast.makeText(this, getString(res), Toast.LENGTH_LONG).show()

            }
        })

    }

    fun enableViews(vararg t: View)
        = t.iterator().forEach { it.isEnabled = true }

    fun disableViews(vararg t: View)
            = t.iterator().forEach { it.isEnabled = false }

    fun hideViews(vararg t: View)
        = t.iterator().forEach { it.visibility = View.GONE }

    fun showViews(vararg  t: View)
        = t.iterator().forEach { it.visibility = View.VISIBLE }

    fun showToast(context: Context, res: String){
        Toast.makeText(context, res, Toast.LENGTH_LONG).show()
    }

    fun showToast(context: Context, res: Int){
        Toast.makeText(context, res, Toast.LENGTH_LONG).show()
    }

    fun showSnackbar(res: String){
        Snackbar.make(window.decorView.rootView, res, Snackbar.LENGTH_LONG).show()
    }

    fun showSnackbar(res: Int){
        Snackbar.make(window.decorView.rootView, res, Snackbar.LENGTH_LONG).show()
    }

}