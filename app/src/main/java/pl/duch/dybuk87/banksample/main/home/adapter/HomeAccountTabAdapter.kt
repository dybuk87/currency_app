package pl.duch.dybuk87.banksample.main.home.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.vh_home_account_tab.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import pl.duch.dybuk87.banksample.R
import pl.duch.dybuk87.banksample.components.DataBoundViewHolder
import pl.duch.dybuk87.banksample.components.account.tab.AccountTabWidgetVM
import pl.duch.dybuk87.core.kernel.CurrencyType
import pl.duch.dybuk87.core.kernel.Money
import pl.duch.dybuk87.core.repository.account.Account
import pl.duch.dybuk87.core.repository.account.AccountRepository

class AccountTabDiffUtil(private val oldList: MutableList<Account>, private val newList: MutableList<Account>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].balance.value.currencyType == newList[newItemPosition].balance.value.currencyType

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]

}

class HomeAccountTabVH(
    view: View,
    click: (CurrencyType) -> Unit
) : DataBoundViewHolder(view) {
    private val widget = view.account_tab_widget
    private val vm = AccountTabWidgetVM(view.context)

    init {
        widget.subscribe(this, vm)
        view.setOnClickListener {
            click(vm.currency)
            vm.tabClicked()
        }
    }

    fun watchAccount(account: Account) {
        vm.pushModel(account.balance.value)
        lifecycleScope.launch {
            account.balance.collect {
                withContext(Dispatchers.Main) {
                    vm.pushModel(it)
                }
            }
        }
    }

}

class HomeAccountTabAdapter(
    private val click : (CurrencyType) -> Unit) : RecyclerView.Adapter<HomeAccountTabVH>() {

    private val data: MutableList<Account> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeAccountTabVH {
        val vh = HomeAccountTabVH(
            LayoutInflater.from(parent.context).inflate(R.layout.vh_home_account_tab, parent, false), click
        )
        vh.markCreated()
        return vh
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: HomeAccountTabVH, position: Int) {
        holder.watchAccount(data[position])
    }

    override fun onViewAttachedToWindow(holder: HomeAccountTabVH) {
        super.onViewAttachedToWindow(holder)
        holder.markAttach()
    }

    override fun onViewDetachedFromWindow(holder: HomeAccountTabVH) {
        super.onViewDetachedFromWindow(holder)
        holder.markDetach()
    }

    fun updateAccountList(accountList: List<Account>) {

        val oldList = ArrayList<Account>(data)

        data.clear()
        data.addAll(accountList)

        val result= DiffUtil.calculateDiff(AccountTabDiffUtil(oldList, data), true)
        result.dispatchUpdatesTo(this)
    }

}