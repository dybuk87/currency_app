package pl.duch.dybuk87.banksample

import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LifecycleRegistry

class LCOwner : LifecycleOwner {
    private val lifecycle = LifecycleRegistry(this)
        .also {
            it.currentState = Lifecycle.State.RESUMED
        }
    override fun getLifecycle(): Lifecycle = lifecycle
}