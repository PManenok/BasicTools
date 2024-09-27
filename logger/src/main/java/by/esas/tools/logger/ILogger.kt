/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.logger

import android.util.Log

open class ILogger<M : BaseErrorModel> {
    companion object {
        /**
         *  Category for logging any logs, which may be needed in debug mode,
         *  but must not be logged at all in release builds
         **/
        const val CATEGORY_DEBUG: String = "CATEGORY_DEBUG"

        /**
         *  Category for logging any logs with error level, which may be needed in debug mode,
         *  but must not be logged at all in release builds
         **/
        const val CATEGORY_DEBUG_ERROR: String = "CATEGORY_DEBUG_ERROR"

        /**
         *  Category for logging functions order or any logic checkpoints
         **/
        const val CATEGORY_ORDER: String = "CATEGORY_ORDER"

        /**
         *  Category for logging messages with info level
         **/
        const val CATEGORY_INFO: String = "CATEGORY_INFO"

        /**
         *  Category for logging actions
         *  @see Action
         **/
        const val CATEGORY_ACTION: String = "CATEGORY_ACTION"

        /**
         *  Category for logging events
         *  @see Event
         **/
        const val CATEGORY_EVENT: String = "CATEGORY_EVENT"

        /**
         *  Category for logging messages with warn level
         **/
        const val CATEGORY_WARN: String = "CATEGORY_WARN"

        /**
         *  Category for logging messages with error level
         **/
        const val CATEGORY_ERROR: String = "CATEGORY_ERROR"

        /**
         *  Category for logging user info, which may be logged in debug mode,
         *  but must not be logged in app's local logs in release builds
         **/
        const val CATEGORY_USER: String = "CATEGORY_USER"
    }

    protected var _tag: String = ""

    /**
     * Set a tag to use in simplified methods
     */
    open fun setTag(tag: String) {
        _tag = tag
    }

    /**
     * Get a tag to use in simplified methods
     */
    open fun getTag(): String {
        return _tag
    }

    open fun categoriesList(): List<String> {
        return mutableListOf(
            CATEGORY_DEBUG,
            CATEGORY_DEBUG_ERROR,
            CATEGORY_ORDER,
            CATEGORY_INFO,
            CATEGORY_ACTION,
            CATEGORY_EVENT,
            CATEGORY_WARN,
            CATEGORY_ERROR,
            CATEGORY_USER
        )
    }

    open fun getLevelFromCategory(category: String): Int {
        return when (category) {
            CATEGORY_ORDER, CATEGORY_INFO, CATEGORY_ACTION, CATEGORY_EVENT -> Log.INFO
            CATEGORY_WARN, CATEGORY_USER -> Log.WARN
            CATEGORY_ERROR, CATEGORY_DEBUG_ERROR -> Log.ERROR
            else -> Log.DEBUG
        }
    }

    /**
     * All logs must go through this method to be logged
     */
    open fun logCategory(category: String, tag: String, msg: String) {
        // do not log logs locally in release builds
        if (BuildConfig.DEBUG) {
            logLocally(tag, msg, getLevelFromCategory(category))
        }
    }

    /**
     * Simply logging any logs locally to see them in Logcat
     */
    open fun logLocally(tag: String, msg: String, level: Int = Log.DEBUG) {
        if (BuildConfig.DEBUG)
            when (level) {
                Log.INFO -> Log.i(tag, msg)
                Log.ERROR -> Log.e(tag, msg)
                Log.WARN -> Log.w(tag, msg)
                else -> Log.d(tag, msg)
            }
    }

    /**
     * Log logs with Debug level for development stage
     * @see CATEGORY_DEBUG
     */
    open fun d(tag: String, msg: String) {
        logCategory(CATEGORY_DEBUG, tag, msg)
    }

    /**
     * @see ILogger.d(tag: String, msg: String)
     */
    open fun d(msg: String) {
        d(getTag(), msg)
    }

    /**
     * Log logs with Debug Error level for development stage
     * @see CATEGORY_DEBUG_ERROR
     */
    open fun dE(tag: String, msg: String) {
        logCategory(CATEGORY_DEBUG_ERROR, tag, msg)
    }

    /**
     * @see ILogger.dE(tag: String, msg: String)
     */
    open fun dE(msg: String) {
        dE(getTag(), msg)
    }

    /**
     * Log order logs
     * @see CATEGORY_ORDER
     */
    open fun order(tag: String, msg: String) {
        logCategory(CATEGORY_ORDER, tag, msg)
    }

    /**
     * @see ILogger.order(tag: String, msg: String)
     */
    open fun order(msg: String) {
        order(getTag(), msg)
    }

    /**
     * Log info logs
     * @see CATEGORY_INFO
     */
    open fun i(tag: String, msg: String) {
        logCategory(CATEGORY_INFO, tag, msg)
    }

    /**
     * @see ILogger.i(tag: String, msg: String)
     */
    open fun i(msg: String) {
        i(getTag(), msg)
    }

    /**
     * Log action
     * @see Action
     * @see CATEGORY_ACTION
     */
    open fun action(tag: String, action: Action) {
        logCategory(CATEGORY_ACTION, tag, action.toString())
    }

    /**
     * @see ILogger.action(tag: String, action: Action)
     */
    open fun action(action: Action) {
        action(getTag(), action)
    }

    /**
     * Log event
     * @see Event
     * @see CATEGORY_EVENT
     */
    open fun event(tag: String, event: Event) {
        logCategory(CATEGORY_EVENT, tag, event.toMessage())
    }

    /**
     * @see ILogger.event(tag: String, event: Event)
     */
    open fun event(event: Event) {
        event(getTag(), event)
    }

    /**
     * Log warning logs
     * @see CATEGORY_WARN
     */
    open fun w(tag: String, msg: String) {
        logCategory(CATEGORY_WARN, tag, msg)
    }

    /**
     * @see ILogger.w(tag: String, msg: String)
     */
    open fun w(msg: String) {
        w(getTag(), msg)
    }

    /**
     * Log logs with user specific info
     * @see CATEGORY_USER
     */
    open fun userInfo(tag: String, msg: String) {
        logCategory(CATEGORY_USER, tag, msg)
    }

    /**
     * @see ILogger.userInfo(tag: String, msg: String)
     */
    open fun userInfo(msg: String) {
        userInfo(getTag(), msg)
    }

    /**
     * Log error logs
     * @see CATEGORY_ERROR
     */
    open fun e(tag: String, msg: String) {
        logCategory(CATEGORY_ERROR, tag, msg)
    }

    /**
     * @see ILogger.e(tag: String, msg: String)
     */
    open fun e(msg: String) {
        e(getTag(), msg)
    }

    /**
     * Log throwable
     * @see CATEGORY_ERROR
     */
    open fun throwable(tag: String, throwable: Throwable) {
        logCategory(CATEGORY_ERROR, tag, throwable.toString())
    }

    /**
     * @see ILogger.throwable(tag: String, throwable: Throwable)
     */
    open fun throwable(throwable: Throwable) {
        throwable(getTag(), throwable)
    }

    /**
     * Log BaseErrorModel
     * @see BaseErrorModel
     * @see CATEGORY_ERROR
     */
    open fun errorModel(tag: String, error: M) {
        logCategory(CATEGORY_ERROR, tag, error.toString())
    }


    /**
     * @see ILogger.errorModel(tag: String, error: M)
     */
    open fun errorModel(error: M) {
        errorModel(getTag(), error)
    }
}