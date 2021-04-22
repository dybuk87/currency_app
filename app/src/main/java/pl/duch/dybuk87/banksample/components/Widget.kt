package pl.duch.dybuk87.banksample.components

import androidx.lifecycle.LifecycleOwner

interface Widget<VM> {
    var vm : VM?

    fun subscribe(owner: LifecycleOwner, vm: VM)
    fun unsubscribe()
}