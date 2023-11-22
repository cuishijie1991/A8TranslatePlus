@file:JvmName("HttpUtils")

package com.tracy.a8translateplus.net

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.tracy.a8translateplus.bean.TranslationBean
import org.intellij.lang.annotations.Language
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

/**
 * @author cuishijie
 * *
 */
//访问使用小牛翻译
private const val BASE_URL = "https://api.niutrans.com/NiuTransServer/translation?from=zh&to=en&apikey=430cd5a365eca28eec1571a1cd3a57d3&src_text="

/**
 * 请求网络数据
 * @author ice1000
 */
fun requestNetData(queryWord: String, callBack: NetCallback<TranslationBean>) {
    try {
        LocalData.read(queryWord)?.let {
            try {
                callBack.onSuccess(Gson().fromJson<TranslationBean>(it, TranslationBean::class.java))
            } catch (e: JsonSyntaxException) {
                callBack.onFail(" 返回解析失败，github issue to me：\n$it")
            }
            return
        }

        @Language("RegExp")
        val url = URL("$BASE_URL${URLEncoder.encode(queryWord.replace(Regex("[*+\\- \r]+"), " "), "UTF-8")}")
        val conn = url.openConnection() as HttpURLConnection

        conn.connectTimeout = 3000
        conn.readTimeout = 3000
        conn.requestMethod = conn.requestMethod

        // 连接成功
        if (conn.responseCode == 200) {
            val ins = conn.inputStream

            // 获取到Json字符串
            val content = StreamUtils.getStringFromStream(ins)
            if (content.isNotBlank()) {
                println(content);
                callBack.onSuccess(Gson().fromJson(content, TranslationBean::class.java))
                LocalData.store(queryWord, content)
            } else callBack.onFail("翻译接口返回为空")
        } else callBack.onFail("错误码：${conn.responseCode}\n错误信息：\n${conn.responseMessage}")
    } catch (e: IOException) {
        callBack.onFail("无法访问：\n${e.message}")
    }
}
