package pl.duch.dybuk87.backend.datasource.model

import pl.duch.dybuk87.core.datasource.AccountHistoryRecordDto

data class HistoryPage(val pageId: String, val list: List<AccountHistoryRecordDto>)
