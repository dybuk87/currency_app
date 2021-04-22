package pl.duch.dybuk87.core.kernel

data class AppError(
    val errorCode: Int = -1,
    val errorMessage: String="",
    val exception : Throwable? = null,
    val errors : List<Pair<String, String>> = emptyList()
) {
    constructor(throwable: Throwable) :
            this(-1, throwable.localizedMessage ?: "Unknown error", throwable, emptyList())
}