package by.esas.tools.customswitch

/**
 * Interface for handling CustomSwitch view on/off status changing.
 * Override [onSwitchChange] method to do something when checked status is changed.
 * Override [prepareToSwitchOn]/[prepareToSwitchOff] methods to do logics before
 * changing switcher status.
 * So if [prepareToSwitchOn] returns false switcher will not become on
 * and will stay off.
 * If [prepareToSwitchOff] returns false switcher will not become off and will stay on.
 * By default [prepareToSwitchOn]/[prepareToSwitchOff] methods return true.
 *
 * @see CustomSwitch
 */
interface ISwitchHandler {

    fun onSwitchChange(isChecked: Boolean)
    fun prepareToSwitchOn(): Boolean {
        return true
    }

    fun prepareToSwitchOff(): Boolean {
        return true
    }
}