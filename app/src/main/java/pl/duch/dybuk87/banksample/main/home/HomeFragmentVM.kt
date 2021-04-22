package pl.duch.dybuk87.banksample.main.home

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import pl.duch.dybuk87.core.initMutableLiveData
import pl.duch.dybuk87.core.kernel.RemoteDataState
import pl.duch.dybuk87.core.repository.account.Account
import pl.duch.dybuk87.core.repository.account.AccountRepository

class HomeFragmentVM(
    var context: Context,
    private val accountRepository: AccountRepository
) : ViewModel() {

    private val _accountList = initMutableLiveData<List<Account>>(emptyList())
    val accountList : LiveData<List<Account>> = _accountList

    private val _loaderVisible = initMutableLiveData(true)
    val loaderVisible : LiveData<Boolean> = _loaderVisible

    init {
        viewModelScope.launch {
            accountRepository.ownedAccounts.collect {
                _accountList.postValue(it.data)

                val loadingState = it.state == RemoteDataState.Loading || it.state == RemoteDataState.NotReady

                _loaderVisible.postValue(
                    loadingState && it.data.isEmpty()
                )
            }
        }
        viewModelScope.launch {
            accountRepository.loadAccountList()
        }
    }

    override fun onCleared() {
        super.onCleared()
    }
}