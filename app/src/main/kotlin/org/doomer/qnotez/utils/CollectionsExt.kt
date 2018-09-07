package org.doomer.qnotez.utils

fun IntArray.containsOnly(num: Int): Boolean = filter { it == num }.isNotEmpty()