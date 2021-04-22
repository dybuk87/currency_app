package pl.duch.dybuk87.banksample.di

import dagger.Component
import pl.duch.dybuk87.banksample.CoreApplication
import pl.duch.dybuk87.banksample.di.module.ApiModule
import pl.duch.dybuk87.banksample.main.home.HomeFragment
import pl.duch.dybuk87.banksample.main.home.page.HomeAccountPageFragment
import javax.inject.Singleton


@Singleton
@Component(modules = [ApiModule::class])
interface AppComponent {
    fun inject(coreApplication: CoreApplication)
    fun inject(coreApplication: HomeFragment)
    fun inject(homeAccountPageFragment: HomeAccountPageFragment)
}