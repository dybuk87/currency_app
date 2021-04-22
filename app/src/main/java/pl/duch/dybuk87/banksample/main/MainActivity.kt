package pl.duch.dybuk87.banksample.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.ui.NavigationUI
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.flow.collect
import pl.duch.dybuk87.banksample.R
import pl.duch.dybuk87.banksample.ext.getViewModel
import pl.duch.dybuk87.banksample.ext.navHostFragment
import pl.duch.dybuk87.banksample.ext.setBottomBarColor
import pl.duch.dybuk87.core.repository.account.Account
import pl.duch.dybuk87.core.kernel.CurrencyType
import pl.duch.dybuk87.core.kernel.Money


class MainActivity : AppCompatActivity() {

    private val navHostFragment by lazy { navHostFragment()!! }
    private val vm by lazy { getViewModel { MainActivityVM() } }

    private val navIndex = listOf<Int>(R.id.home, R.id.exchange, R.id.transfer, R.id.finances)

    private val navControllerListener = NavController.OnDestinationChangedListener {
            controler: NavController, destination: NavDestination, arguments: Bundle? ->
            vm.currentView(navIndex.indexOf(destination.id))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        setBottomBarColor(ContextCompat.getColor(this, R.color.top_bar_background))
        setUpNavigation()
    }


    private fun setUpNavigation() {
        NavigationUI.setupWithNavController(
            bttm_nav,
            navHostFragment.navController
        )
        navHostFragment.navController.addOnDestinationChangedListener(navControllerListener)
    }


    override fun onDestroy() {
        super.onDestroy()
        teardownNavigation()
    }

    private fun teardownNavigation() {
        navHostFragment.navController.removeOnDestinationChangedListener(navControllerListener)
    }
}