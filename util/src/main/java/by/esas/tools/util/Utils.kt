package by.esas.tools.util


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