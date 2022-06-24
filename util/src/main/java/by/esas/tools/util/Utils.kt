/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.util

import kotlin.reflect.KClass

val Any.TAGk: String
    get() {
        val tag = javaClass.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }

val KClass<*>.TAGk: String
    get() {
        val tag = java.simpleName
        return if (tag.length <= 23) tag else tag.substring(0, 23)
    }

internal const val UNBREAKABLE_SPACE = "\u00A0"
fun Double.toFormattedInput(): String {
    val temp = this.toString()
    val dotIndex = temp.indexOf(".")
    return when {
        dotIndex == -1 -> temp//.plus(".00")
        dotIndex == temp.length - 2 -> {
            if (temp.last() == "0".toCharArray()[0])
                temp.substring(0, temp.length - 2)
            else
                temp.plus("0")
            //temp.substring(0, temp.length - 2)
        }
        dotIndex < temp.length - 3 -> {
            temp.substring(0, dotIndex + 3)
        }
        else -> temp
    }
}

fun Double.toFormattedString(): String {
    val temp = this.toString()
    return when (temp.indexOf(".")) {
        -1 -> temp.plus(".00")
        temp.length - 2 -> temp.plus("0")
        else -> temp
    }
}

fun getOnlyNumbers(value: String): String {
    val builder = StringBuilder()
    value.forEach {
        if (it.isDigit() || ".".contains(it)) {
            builder.append(it)
        }
    }
    return builder.toString()
}

fun luhnCheck(value: String): Boolean {
    // this only works if you are certain all input will be at least 10 characters
    return if (value.length < 10) {
        false
    } else {
        var sum = 0
        value.toCharArray().forEachIndexed { index, c ->
            val num = c - '0'
            var product: Int = if (index % 2 != 0) {
                num * 1
            } else {
                num * 2
            }
            if (product > 9)
                product -= 9
            sum += product
        }
        sum % 10 == 0
    }
}

fun getCardType(mask: String): String {
    if (mask.isBlank()) return ""
    val firstFour = mask.subSequence(0, 4)
    val number = firstFour.toString().toInt()
    val firstNum = number / 1000
    val secondNum = number / 100 - number / 1000 * 10
    when (firstNum) {
        2 -> {
            if (number in (2221..2720) && mask.length == 16)
                return "MasterCard"
        }
        3 -> {
            if ((secondNum == 4 || secondNum == 7) && mask.length == 15)
                return "AmericanExpress"
        }
        4 -> if (mask.length in (13..16)) return "Visa"
        5 -> {
            if (secondNum in (1..5) && mask.length == 16)
                return "MasterCard"
        }
        6 -> when (secondNum) {
            2 -> return "Unionpay"
        }
    }
    return ""
}