package pl.duch.dybuk87.banksample.di.module

import dagger.Module
import dagger.Provides
import pl.duch.dybuk87.backend.datasource.AccountDataSourceImpl
import pl.duch.dybuk87.backend.datasource.AccountHistoryDataSourceImpl
import pl.duch.dybuk87.backend.event.AppEventsImpl
import pl.duch.dybuk87.core.datasource.AccountDataSource
import pl.duch.dybuk87.core.datasource.AccountHistoryDataSource
import pl.duch.dybuk87.core.event.AppEvents
import pl.duch.dybuk87.core.repository.account.AccountRepository
import pl.duch.dybuk87.core.repository.account.AccountRepositoryImpl
import javax.inject.Singleton

@Module
class ApiModule {

    @Provides
    @Singleton
    fun accountDataSource(): AccountDataSource = AccountDataSourceImpl()

    @Provides
    @Singleton
    fun accountHistoryDataSource(): AccountHistoryDataSource = AccountHistoryDataSourceImpl()

    @Provides
    @Singleton
    fun appEvents(accountDataSource: AccountDataSource) : AppEvents = AppEventsImpl(accountDataSource, AccountHistoryDataSourceImpl())

    @Provides
    @Singleton
    fun accountRepository(
        accountDataSource: AccountDataSource,
        accountHistoryDataSource: AccountHistoryDataSource,
        appEvents: AppEvents
    ): AccountRepository =
        AccountRepositoryImpl(accountDataSource, accountHistoryDataSource, appEvents)

}