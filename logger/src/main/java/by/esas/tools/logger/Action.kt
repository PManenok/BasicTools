package by.esas.tools.logger

import android.os.Bundle

/**
 * Should be used for every action that requires some actions from different parts of application
 * For example between view model and fragment, or fragment and activity.
 */
open class Action(
    val name: String,
    val parameters: Bundle = Bundle(),
    var handled: Boolean = false
) {
    override fun toString(): String {
        return "Action(name='$name', handled=$handled, params=$parameters)"
    }
}