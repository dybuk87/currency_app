package pl.duch.dybuk87.banksample.main.home.adapter

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DiffUtil
import androidx.viewpager2.adapter.FragmentStateAdapter
import pl.duch.dybuk87.banksample.main.home.page.HomeAccountPageFragment
import pl.duch.dybuk87.core.kernel.Money
import pl.duch.dybuk87.core.repository.account.Account

class AccountDiffUtil(private val oldList: MutableList<Money>, private val newList: MutableList<Money>) : DiffUtil.Callback() {

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition].currencyType == newList[newItemPosition].currencyType

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean =
        oldList[oldItemPosition] == newList[newItemPosition]

}

class HomeAccountPageAdapter(parent: Fragment) : FragmentStateAdapter(parent) {
    private val data = mutableListOf<Money>()

    override fun getItemCount(): Int = data.size

    override fun createFragment(position: Int): Fragment =
        HomeAccountPageFragment.create(data[position])

    fun updateAccountList(accountList: List<Account>) {
        val oldList = ArrayList<Money>(data)

        data.clear()
        data.addAll(accountList.map { it.balance.value })

        val result= DiffUtil.calculateDiff(AccountDiffUtil(oldList, data), true)
        result.dispatchUpdatesTo(this)
    }

    override fun getItemId(position: Int): Long {
        return data[position].currencyType.ordinal.toLong()
    }
}