package by.esas.tools

import androidx.databinding.ViewDataBinding
import by.esas.tools.basedaggerui.simple.SimpleActivity
import by.esas.tools.error_mapper.AppErrorStatusEnum

abstract class AppActivity<VM : AppVM, B : ViewDataBinding> : SimpleActivity<VM, B, AppErrorStatusEnum>() {

}