package by.esas.tools.logger

import by.esas.tools.error_mapper.AppErrorStatusEnum

class LoggerImpl : ILogger<AppErrorStatusEnum> {
    override fun setTag(tag: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMessage(msg: String, length: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun showMessage(msgRes: Int, length: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun logLocally(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun log(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun log(tag: String, msg: String, level: Int) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun logError(throwable: Throwable) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun logError(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun logInfo(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun sendLogs(msg: String) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun logError(error: IErrorModel<AppErrorStatusEnum>) {
        TODO("Not yet implemented")
    }
}