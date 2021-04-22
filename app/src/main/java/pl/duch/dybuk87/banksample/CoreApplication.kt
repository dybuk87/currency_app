package pl.duch.dybuk87.banksample

import android.app.Application
import pl.duch.dybuk87.banksample.di.AppComponent
import pl.duch.dybuk87.banksample.di.DaggerAppComponent
import pl.duch.dybuk87.banksample.di.module.ApiModule
import pl.duch.dybuk87.core.datasource.AccountDataSource
import javax.inject.Inject

class CoreApplication : Application() {

    val component: AppComponent by lazy {
        DaggerAppComponent.builder()
            .apiModule(ApiModule())
            .build()
    }


    @Inject
    lateinit var accountDataSource: AccountDataSource

    override fun onCreate() {
        super.onCreate()

        component.inject(this)
    }
}