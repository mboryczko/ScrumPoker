package pl.michalboryczko.exercise.app

import androidx.multidex.MultiDex
import com.flurry.android.FlurryAgent
import com.google.firebase.FirebaseApp
import dagger.android.AndroidInjector
import dagger.android.DaggerApplication
import leakcanary.LeakCanary
import pl.michalboryczko.exercise.di.DaggerAppComponent
import timber.log.Timber
import com.mixpanel.android.mpmetrics.MixpanelAPI




class MainApplication: DaggerApplication() {


    override fun onCreate() {
        super.onCreate()
        MultiDex.install(this)
        FirebaseApp.initializeApp(this)
        FlurryAgent.Builder()
                .withLogEnabled(true)
                .build(this, "S46QBVY86W4W8CPNBW6Z")


        Timber.plant(CustomLoggingTree())
    }

    override fun applicationInjector(): AndroidInjector<out DaggerApplication> {
        return DaggerAppComponent.builder().create(this)
    }


}