package pl.duch.dybuk87.backend

import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.random.Random

fun random(min: Double, max: Double) =
    Random.nextDouble(min, max).toBigDecimal().setScale(2, RoundingMode.HALF_EVEN)