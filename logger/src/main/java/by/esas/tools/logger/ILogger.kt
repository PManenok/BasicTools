/*
 * Copyright 2021 Electronic Systems And Services Ltd.
 * SPDX-License-Identifier: Apache-2.0
 */

package by.esas.tools.logger

import android.util.Log

interface ILogger<M : BaseErrorModel> {
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

    fun categoriesList(): List<String> {
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

    fun getLevelFromCategory(category: String): Int {
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
    fun logCategory(category: String, tag: String, msg: String) {
        // do not log logs locally in release builds
        if (BuildConfig.DEBUG) {
            logLocally(tag, msg, getLevelFromCategory(category))
        }
    }

    /**
     * Simply logging any logs locally to see them in Logcat
     */
    fun logLocally(tag: String, msg: String, level: Int = Log.DEBUG) {
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
    fun d(tag: String, msg: String) {
        logCategory(CATEGORY_DEBUG, tag, msg)
    }

    /**
     * Log logs with Debug Error level for development stage
     * @see CATEGORY_DEBUG_ERROR
     */
    fun dE(tag: String, msg: String) {
        logCategory(CATEGORY_DEBUG_ERROR, tag, msg)
    }

    /**
     * Log order logs
     * @see CATEGORY_ORDER
     */
    fun order(tag: String, msg: String) {
        logCategory(CATEGORY_ORDER, tag, msg)
    }

    /**
     * Log info logs
     * @see CATEGORY_INFO
     */
    fun i(tag: String, msg: String) {
        logCategory(CATEGORY_INFO, tag, msg)
    }

    /**
     * Log action
     * @see Action
     * @see CATEGORY_ACTION
     */
    fun action(tag: String, action: Action) {
        logCategory(CATEGORY_ACTION, tag, action.toString())
    }

    /**
     * Log event
     * @see Event
     * @see CATEGORY_EVENT
     */
    fun event(tag: String, event: Event) {
        logCategory(CATEGORY_EVENT, tag, event.toMessage())
    }

    /**
     * Log warning logs
     * @see CATEGORY_WARN
     */
    fun w(tag: String, msg: String) {
        logCategory(CATEGORY_WARN, tag, msg)
    }

    /**
     * Log logs with user specific info
     * @see CATEGORY_USER
     */
    fun userInfo(tag: String, msg: String) {
        logCategory(CATEGORY_USER, tag, msg)
    }

    /**
     * Log error logs
     * @see CATEGORY_ERROR
     */
    fun e(tag: String, msg: String) {
        logCategory(CATEGORY_ERROR, tag, msg)
    }

    /**
     * Log throwable
     * @see CATEGORY_ERROR
     */
    fun throwable(tag: String, throwable: Throwable) {
        logCategory(CATEGORY_ERROR, tag, throwable.toString())
    }

    /**
     * Log BaseErrorModel
     * @see BaseErrorModel
     * @see CATEGORY_ERROR
     */
    fun errorModel(tag: String, error: M) {
        logCategory(CATEGORY_ERROR, tag, error.toString())
    }
}