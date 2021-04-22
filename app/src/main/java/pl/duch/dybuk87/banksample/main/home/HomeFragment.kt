package pl.duch.dybuk87.banksample.main.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import kotlinx.android.synthetic.main.fragment_home.*
import pl.duch.dybuk87.banksample.R
import pl.duch.dybuk87.banksample.ext.di
import pl.duch.dybuk87.banksample.ext.getViewModel
import pl.duch.dybuk87.banksample.main.home.adapter.HomeAccountPageAdapter
import pl.duch.dybuk87.banksample.main.home.adapter.HomeAccountTabAdapter
import pl.duch.dybuk87.core.kernel.CurrencyType
import pl.duch.dybuk87.core.repository.account.Account
import pl.duch.dybuk87.core.repository.account.AccountRepository
import javax.inject.Inject

class TabMediator(
    val viewPagerTab: RecyclerView,
    private val viewPager: ViewPager2,
) {
    fun updateAccountList(accountList: List<Account>) {
        data.clear()
        data.addAll(accountList)
    }

    val data: MutableList<Account> = mutableListOf()

    val tabClick: (CurrencyType) -> Unit = { currencyType ->
        viewPager.currentItem = data.indexOfFirst { it.balance.value.currencyType == currencyType }
    }

    private val vpMediator = object: ViewPager2.OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            viewPagerTab.smoothScrollToPosition(position)
        }
    }

    init {
        viewPager.registerOnPageChangeCallback(vpMediator)
    }

}

class PageTransformer : ViewPager2.PageTransformer {
    override fun transformPage(page: View, position: Float) {
        page.pivotX = if (position < 0f) page.width.toFloat() else 0f
        page.pivotY = page.height * 0.5f
        page.rotationY = 90f * position
    }

}

class HomeFragment : Fragment() {

    private lateinit var tabMediator : TabMediator

    @Inject
    lateinit var accountRepository: AccountRepository

    private val vm by lazy { getViewModel { HomeFragmentVM(requireContext(), accountRepository) } }

    private val pageAdapter : HomeAccountPageAdapter by lazy { HomeAccountPageAdapter(this) }

    private val tabAdapter : HomeAccountTabAdapter by lazy { HomeAccountTabAdapter(tabMediator.tabClick) }

    private val observerAccountList = Observer<List<Account>> { updateAccountList(it) }
    private val observerLoader = Observer<Boolean> { updateLoader(it) }

    private fun updateLoader(loaderVisible: Boolean) {
        fragment_home_loader.visibility = if (loaderVisible) View.VISIBLE else View.GONE
    }

    private fun updateAccountList(accountList: List<Account>) {
        tabMediator.updateAccountList(accountList)
        tabAdapter.updateAccountList(accountList)
        pageAdapter.updateAccountList(accountList)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        di().inject(this)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view_pager.adapter = pageAdapter
        view_pager.setPageTransformer(PageTransformer())

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        view_pager_tab.layoutManager = layoutManager

        tabMediator = TabMediator(view_pager_tab, view_pager)

        view_pager_tab.adapter = tabAdapter

        vm.accountList.observe(viewLifecycleOwner, observerAccountList)
        vm.loaderVisible.observe(viewLifecycleOwner, observerLoader)
    }

    override fun onDestroyView() {
        super.onDestroyView()

        vm.accountList.removeObserver(observerAccountList)
        vm.loaderVisible.removeObserver(observerLoader)
    }
}