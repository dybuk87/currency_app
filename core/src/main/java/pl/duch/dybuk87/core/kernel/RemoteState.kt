package pl.duch.dybuk87.core.kernel

enum class RemoteDataState {
    NotReady,
    Loading,
    Error,
    Success
}

class RemoteData<T>(val state: RemoteDataState, val data: T)