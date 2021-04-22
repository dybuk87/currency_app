package pl.duch.dybuk87.banksample.components.account.tab

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer
import kotlinx.android.synthetic.main.widget_account_tab.view.*
import pl.duch.dybuk87.banksample.R
import pl.duch.dybuk87.banksample.components.Widget

class AccountTabWidget @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0,
) : LinearLayout(context, attrs, defStyleAttr), Widget<AccountTabWidgetVM> {

    init {
        LayoutInflater.from(context).inflate(R.layout.widget_account_tab, this, true)
        setOnClickListener { vm?.tabClicked() }
    }

    override var vm: AccountTabWidgetVM? = null
    private val observerData = Observer<AccountTabData> { setAccountData(it) }
    private val observeSelected = Observer<Boolean> { setSelectedView(it) }

    private fun setSelectedView(selected: Boolean) {
        if (selected) {
            setBackgroundColor(ContextCompat.getColor(context, R.color.content_detail))
        } else {
            setBackgroundColor(ContextCompat.getColor(context, android.R.color.transparent))
        }
    }

    private fun setAccountData(data: AccountTabData) {
        widget_account_tab_flag.setImageResource(data.currencyIcon)
        widget_account_tab_amount.text = data.description
    }

    override fun subscribe(owner: LifecycleOwner, vm: AccountTabWidgetVM) {
        this.vm = vm
        vm.data.observe(owner, observerData)
        vm.selected.observe(owner, observeSelected)
    }

    override fun unsubscribe() {
        vm?.data?.removeObserver(observerData)
        vm?.selected?.removeObserver(observeSelected)
        vm = null
    }

}