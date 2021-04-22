package pl.duch.dybuk87.banksample.ext

import android.app.Activity
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.NavHostFragment
import pl.duch.dybuk87.banksample.CoreApplication
import pl.duch.dybuk87.banksample.R

fun AppCompatActivity.navHostFragment() = supportFragmentManager
    .findFragmentById(R.id.nav_host_fragment) as NavHostFragment?

inline fun <reified T : ViewModel> AppCompatActivity.getViewModel(noinline creator: (() -> T)? = null): T {
    return if (creator == null)
        ViewModelProvider(this).get(T::class.java)
    else
        ViewModelProvider(this, BaseViewModelFactory(creator)).get(T::class.java)
}

inline fun <reified T : ViewModel> FragmentActivity.getViewModel(noinline creator: (() -> T)? = null): T {
    return if (creator == null)
        ViewModelProvider(this).get(T::class.java)
    else
        ViewModelProvider(this, BaseViewModelFactory(creator)).get(T::class.java)
}


class BaseViewModelFactory<T0 : ViewModel?>(val creator: () -> T0) : ViewModelProvider.Factory {
    override fun <T1 : ViewModel> create(modelClass: Class<T1>): T1 {
        return creator() as T1
    }
}

fun Activity.setBottomBarColor(background: Int) {
    window.navigationBarColor = background
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
        window.navigationBarDividerColor = background
    }
}

fun Activity.di() = (application as CoreApplication).component

