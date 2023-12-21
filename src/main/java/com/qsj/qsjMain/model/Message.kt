package com.qsj.qsjMain.model

import com.qsj.qsjMain.config.EnvConf
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

/**
 * API 接口返回值
 */
class Message<T> private constructor(
    val status: Int,                                     // 状态码
    val message: String,                                 // 状态描述
    val data: T? = null,                                 // 实际返回的数据
    val host: String = EnvConf.address,           // host
    val timestamp: String = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"))   // 当前时间
) {

    private constructor(result: ResultCode, message: String? = null, data: T? = null) : this(
        result.code, message ?: result.message, data
    )

    companion object {

        @JvmStatic
        fun <T> ok(): Message<T> {
            return Message(result = ResultCode.SUCCESS)
        }

        @JvmStatic
        fun <T> ok(data: T?): Message<T> {
            return Message(
                    result = ResultCode.SUCCESS,
                    data = data
            )
        }

        @JvmStatic
        fun <T> error(result: ResultCode): Message<T> {
            return Message(result = result)
        }

        @JvmStatic
        fun <T> error(result: ResultCode, message: String?): Message<T> {
            return Message(result = result, message = message)
        }

        @JvmStatic
        fun <T> error(result: ResultCode, data: T?): Message<T> {
            return Message(result = result, data = data)
        }

        @JvmStatic
        fun <T> error(result: ResultCode, message: String?, data: T?): Message<T> {
            return Message(result = result, message = message, data = data)
        }

    }

}
