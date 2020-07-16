package by.esas.tools.inputfieldview

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