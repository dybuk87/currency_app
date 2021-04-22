package pl.duch.dybuk87.banksample.ext

import android.os.Build
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import pl.duch.dybuk87.banksample.CoreApplication

inline fun <reified T> Fragment.getParentVM() : T {
    if (parentFragment is VMProvider<*>) {
        val prov = parentFragment as VMProvider<*>
        val vm = prov.getVM()

        if (vm is T) {
            return vm
        } else {
            throw RuntimeException("Parent activity VMProvider return invalid vm model type")
        }
    } else if(activity is VMProvider<*>) {
        val prov = activity as VMProvider<*>
        val vm = prov.getVM()

        if (vm is T) {
            return vm
        } else {
            throw RuntimeException("Parent activity VMProvider return invalid vm model type")
        }
    }
    throw RuntimeException("Parent activity does not implements VMProvider interface")
}


inline fun <reified T : ViewModel> Fragment.getViewModel(noinline creator: (() -> T)? = null): T {
    return if (creator == null)
        ViewModelProvider(this).get(T::class.java)
    else
        ViewModelProvider(this, BaseViewModelFactory(creator)).get(T::class.java)
}

inline fun <reified T : ViewModel> Fragment.getViewModelFromActivity(noinline creator: (() -> T)? = null): T {
    return if (creator == null)
        ViewModelProvider(activity!!).get(T::class.java)
    else
        ViewModelProvider(activity!!, BaseViewModelFactory(creator)).get(T::class.java)
}

fun Fragment.setBottomBarColor(background : Int) {
    activity ?. let {
        it.window.navigationBarColor = background
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            it.window.navigationBarDividerColor = background
        }
    }
}

fun Fragment.di() = (requireActivity().application as CoreApplication).component

