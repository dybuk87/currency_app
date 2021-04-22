package pl.duch.dybuk87.banksample.main

import android.util.Log
import androidx.lifecycle.ViewModel

class MainActivityVM  : ViewModel() {
    fun currentView(pageNo: Int) {
        Log.i("MainActivity", "page  $pageNo")
    }

}