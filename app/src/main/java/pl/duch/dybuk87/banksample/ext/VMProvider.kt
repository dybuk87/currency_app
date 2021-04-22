package pl.duch.dybuk87.banksample.ext

import androidx.lifecycle.ViewModel

interface VMProvider<T : ViewModel> {
    fun getVM() : T
}