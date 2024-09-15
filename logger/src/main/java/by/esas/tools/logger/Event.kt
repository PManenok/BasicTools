package by.esas.tools.logger

open class Event(
    val info: String,
    val type: String = CLICK_EVENT,
    val extra: String = ""
) {
    companion object {
        const val USER_ACTION_EVENT: String = "USER_ACTION_EVENT"
        const val CLICK_EVENT: String = "CLICK_EVENT"
        const val START_ACTIVITY_EVENT: String = "START_ACTIVITY_EVENT"
        const val FINISH_AFFINITY_EVENT: String = "FINISH_AFFINITY_EVENT"
        const val FINISH_EVENT: String = "FINISH_EVENT"
        const val OPEN_DIALOG_EVENT: String = "OPEN_DIALOG_EVENT"
        const val DIALOG_FINISHED_EVENT: String = "DIALOG_FINISHED_EVENT"
        const val ACTION_REQUESTED_EVENT: String = "ACTION_REQUESTED_EVENT"
    }

    fun toMessage(): String {
        return "{\"type\":\"$type\", \"info\":\"$info\", \"extra\":\"$extra\"}"
    }
}