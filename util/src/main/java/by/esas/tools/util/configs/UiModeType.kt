package by.esas.tools.util.configs

enum class UiModeType {
    DAY,
    NIGHT,
    SYSTEM;

    companion object {
        fun getMode(value: String?): UiModeType? {
            return try {
                UiModeType.valueOf(value ?: "")
            } catch (e: IllegalStateException) {
                e.printStackTrace()
                null
            }
        }
    }
}