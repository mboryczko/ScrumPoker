package pl.michalboryczko.exercise.di

import dagger.Component
import dagger.android.AndroidInjector
import dagger.android.support.AndroidSupportInjectionModule
import pl.michalboryczko.exercise.di.modules.NetworkModule
import pl.michalboryczko.exercise.app.MainApplication
import pl.michalboryczko.exercise.di.modules.InteractorModule
import pl.michalboryczko.exercise.di.modules.ActivityModule
import pl.michalboryczko.exercise.di.modules.FragmentModule
import javax.inject.Singleton


@Singleton
@Component(
        modules = arrayOf(
                AndroidSupportInjectionModule::class,
                AppModule::class,
                NetworkModule::class,
                ViewModelBuilder::class,
                ActivityModule::class,
                FragmentModule::class,
                InteractorModule::class
        ))
interface AppComponent : AndroidInjector<MainApplication> {

    @Component.Builder
    abstract class Builder : AndroidInjector.Builder<MainApplication>()
}