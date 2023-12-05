package com.tracy.a8translateplus.net

import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import com.tracy.a8translateplus.bean.BaiduTranslationBean
import com.tracy.a8translateplus.bean.NiuTranslationBean
import com.tracy.a8translateplus.bean.TranslateResult
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL
import java.net.URLEncoder

/**
 * @author cuishijie
 * *
 */

//翻译类型枚举，小牛
val SourceNiu = "小牛翻译"

//百度
val SourceBaidu = "百度翻译"

//访问使用小牛翻译
private const val NIU_BASE_URL = "https://api.niutrans.com/NiuTransServer/translation?from=en&to=zh&apikey=430cd5a365eca28eec1571a1cd3a57d3&src_text="
private const val BAIDU_BASE_URL = "https://fanyi-api.baidu.com/api/trans/vip/translate?"

var sourceType = LocalData.read("sourceType")
var baiduAppId = LocalData.read("baiduAppId")
var baiduSecret = LocalData.read("baiduSecret")

/**
 * 请求网络数据
 * @author ice1000
 */
fun requestNetData(queryWord: String, callBack: NetCallback<TranslateResult>) {
    try {
        LocalData.read(queryWord)?.let {
            try {
                val bean = Gson().fromJson<TranslateResult>(it, TranslateResult::class.java)
                callBack.onSuccess(bean)
            } catch (e: JsonSyntaxException) {
                callBack.onFail(" 返回解析失败，github issue to me：\n$it")
            }
            return
        }
        var url: URL
        val queryStr = URLEncoder.encode(queryWord.replace(Regex("[*+\\- \r]+"), " "), "UTF-8")
        if (sourceType == SourceNiu) {
            url = URL("$NIU_BASE_URL${queryStr}")
        } else if (sourceType == SourceBaidu) {
            url = URL("$BAIDU_BASE_URL${buildBaiduParams(queryStr, "en", "zh")}")
        } else {
            callBack.onFail("不支持的翻译源类型$sourceType")
            return
        }

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
                var result: TranslateResult? = null;
                if (sourceType == SourceNiu) {
                    result = Gson().fromJson(content, NiuTranslationBean::class.java).toTranslateResult()
                } else if (sourceType == SourceBaidu) {
                    result = Gson().fromJson(content, BaiduTranslationBean::class.java).toTranslateResult()
                }
                callBack.onSuccess(result!!)
                LocalData.store(queryWord, Gson().toJson(result))
            } else callBack.onFail("翻译接口返回为空")
        } else callBack.onFail("错误码：${conn.responseCode}\n错误信息：\n${conn.responseMessage}")
    } catch (e: IOException) {
        callBack.onFail("无法访问：\n${e.message}")
    }
}

private fun buildBaiduParams(query: String, from: String, to: String): String {
    val salt = System.currentTimeMillis().toString()
    val appid = baiduAppId ?: ""
    val src1 = appid + query + salt + baiduSecret
    val sign = MD5.md5(src1)
    return "q=$query&from=$from&to=$to&appid=$appid&salt=$salt&sign=$sign"
}



