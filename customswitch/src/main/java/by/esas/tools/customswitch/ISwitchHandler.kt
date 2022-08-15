package by.esas.tools.customswitch

interface ISwitchHandler {
    fun onSwitchChange(isChecked: Boolean)
    fun prepareToSwitchOn(): Boolean {
        return true
    }
    fun prepareToSwitchOff(): Boolean {
        return true
    }
}