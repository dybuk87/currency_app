package pl.duch.dybuk87.core

import androidx.lifecycle.MutableLiveData
import java.lang.Math.random
import java.math.BigDecimal
import java.math.MathContext
import java.math.RoundingMode
import kotlin.random.Random
import kotlin.reflect.KClass

inline fun <reified T : Any?> postInitMutableLiveData(initialValue: T) =
    MutableLiveData<T>().also { it.postValue(initialValue) }

inline fun <reified T : Any?> initMutableLiveData(initialValue: T) =
    MutableLiveData<T>().also { it.value = initialValue }

infix fun <K, V> Iterable<Pair<K, V>>.insertInto(into: MutableMap<K, V>) {
    into.putAll(this)
}