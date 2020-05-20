package com.kinley.earnings.data

import java.util.*
import java.util.concurrent.ThreadLocalRandom


object DataFactory {

    fun randomString(): String = UUID.randomUUID().toString()

    fun randomInt(): Int = ThreadLocalRandom.current().nextInt(3, 20 + 1)

    fun randomDouble(): Double = randomInt().toDouble()
}
