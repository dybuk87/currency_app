package pl.duch.dybuk87.banksample.main.home.page

import android.os.Bundle
import android.os.Handler
import android.os.Parcelable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.android.parcel.Parcelize
import kotlinx.android.synthetic.main.fragment_home_account_page.*
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import pl.duch.dybuk87.banksample.R
import pl.duch.dybuk87.banksample.components.account.widget.HomeAccountWidgetVM
import pl.duch.dybuk87.banksample.components.listener.RecyclerViewScrollListener
import pl.duch.dybuk87.banksample.ext.di
import pl.duch.dybuk87.banksample.main.home.adapter.HomeAccountHistoryAdapter
import pl.duch.dybuk87.core.kernel.CurrencyType
import pl.duch.dybuk87.core.kernel.Money
import pl.duch.dybuk87.core.repository.account.AccountRepository
import java.math.BigDecimal
import javax.inject.Inject


class HomeAccountPageFragment : Fragment() {

    @Inject
    lateinit var accountRepository: AccountRepository

    private val currency: CurrencyType by lazy {
        CurrencyType.valueOf(requireArguments().getString(CURRENCY)!!)
    }

    val vm : HomeAccountWidgetVM by lazy { HomeAccountWidgetVM(requireContext(), currency, accountRepository) }

    private val historyAdapter : HomeAccountHistoryAdapter by lazy { HomeAccountHistoryAdapter(requireContext()) }

    private var scrollListener : RecyclerViewScrollListener? = null

    private val observeNewHistoryCount = Observer<Int> { updateHistoryCount(it) }

    private fun updateHistoryCount(count: Int) {
        home_account_history_new_events.visibility = if (count > 0) View.VISIBLE else View.GONE
        home_account_history_new_events.text = requireContext().getString(R.string.new_history_events, count)
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
        return inflater.inflate(R.layout.fragment_home_account_page, container, false)
    }

    @ExperimentalCoroutinesApi
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        home_account_widget.subscribe(this, vm)

        val layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        home_account_history.layoutManager = layoutManager
        home_account_history.adapter = historyAdapter

        historyAdapter.subscribe(this,
            accountRepository.ownedAccounts.value.data.find {
                    account -> account.balance.value.currencyType == currency
            }!!.history)

        scrollListener = object : RecyclerViewScrollListener(layoutManager) {
            override fun onScrolling(scroll: Int) {  }
            override fun nextPage(){ Handler().post { vm.nextHistoryPage() } }
            override fun safeNotifyVisible(first: Int, last: Int) {}
        }

        scrollListener ?. let { home_account_history.addOnScrollListener(it) }

        vm.newHistoryCount.observe(viewLifecycleOwner, observeNewHistoryCount)

        home_account_history_new_events.setOnClickListener {
            vm.onNewEventClicked()
        }

        lifecycleScope.launch {
            vm.scrollToTop.openSubscription().consumeEach {
                home_account_history.smoothScrollToPosition(0)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        home_account_widget.unsubscribe()
        vm.newHistoryCount.removeObserver(observeNewHistoryCount)
    }

    companion object {
        const val CURRENCY = "currency"

        fun create(money: Money) = HomeAccountPageFragment().apply {
            arguments = bundleOf(
                Pair(CURRENCY, money.currencyType.toString())
            )
        }
    }
}