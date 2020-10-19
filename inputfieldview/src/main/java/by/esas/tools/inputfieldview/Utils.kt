package by.esas.tools.inputfieldview

import android.text.InputType
import android.widget.EditText

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

fun isInputTypePassword(editText: EditText?): Boolean {
    return (editText != null
            && (editText.inputType == InputType.TYPE_NUMBER_VARIATION_PASSWORD || editText.inputType == InputType.TYPE_TEXT_VARIATION_PASSWORD || editText.inputType == InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD || editText.inputType == InputType.TYPE_TEXT_VARIATION_WEB_PASSWORD))
}

/*java.lang.NoSuchFieldError: ConstraintLayout_Layout_layout_constraintTag
	at androidx.constraintlayout.widget.ConstraintLayout$LayoutParams$Table.<clinit>(ConstraintLayout.java:2593)
	at androidx.constraintlayout.widget.ConstraintLayout$LayoutParams.<init>(ConstraintLayout.java:2603)
	at androidx.constraintlayout.widget.ConstraintLayout.generateLayoutParams(ConstraintLayout.java:1823)
	at androidx.constraintlayout.widget.ConstraintLayout.generateLayoutParams(ConstraintLayout.java:481)
	at android.view.LayoutInflater.rInflate_Original(LayoutInflater.java:1125)
	at android.view.LayoutInflater_Delegate.rInflate(LayoutInflater_Delegate.java:72)
	at android.view.LayoutInflater.rInflate(LayoutInflater.java:1097)
	at android.view.LayoutInflater.rInflateChildren(LayoutInflater.java:1084)
	at android.view.LayoutInflater.inflate(LayoutInflater.java:682)
	at android.view.LayoutInflater.inflate(LayoutInflater.java:501)
	at com.android.layoutlib.bridge.impl.RenderSessionImpl.inflate(RenderSessionImpl.java:328)
	at com.android.layoutlib.bridge.Bridge.createSession(Bridge.java:373)
	at com.android.tools.idea.layoutlib.LayoutLibrary.createSession(LayoutLibrary.java:141)
	at com.android.tools.idea.rendering.RenderTask.createRenderSession(RenderTask.java:678)
	at com.android.tools.idea.rendering.RenderTask.lambda$inflate$8(RenderTask.java:809)
	at java.util.concurrent.CompletableFuture$AsyncSupply.run(CompletableFuture.java:1604)
	at java.util.concurrent.ThreadPoolExecutor.runWorker(ThreadPoolExecutor.java:1149)
	at java.util.concurrent.ThreadPoolExecutor$Worker.run(ThreadPoolExecutor.java:624)
	at java.lang.Thread.run(Thread.java:748)
*/