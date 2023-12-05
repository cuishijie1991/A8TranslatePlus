package com.tracy.a8translateplus.bean

import com.tracy.a8translateplus.net.SourceBaidu
import com.tracy.a8translateplus.net.SourceNiu

/**
 * 翻译结果的bean
 * @author cuishijie
 * *
 */

data class TranslateResult(val source: String, val from: String, val to: String, val src: String, val result: String?, val error: String?) {
    override fun toString(): String {
        return "$source: ${result ?: "error:${error}"}"
    }
}

/**
 * 小牛翻译Bean
 * https://niutrans.com/documents/contents/trans_text#accessMode
 */
data class NiuTranslationBean(val from: String, val to: String, val tgt_text: String?, val src_text: String?, val error_code: String?, val error_msg: String?) {
    override fun toString(): String {
        return tgt_text ?: "error:${error_msg}"
    }

    fun toTranslateResult(): TranslateResult {
        return TranslateResult(SourceNiu, from, to, src_text ?: "", tgt_text, error_code);
    }
}

/**
 * 百度翻译Bean
 * https://api.fanyi.baidu.com/doc/21
 */
data class BaiduTranslationResultBean(val scr: String, val dst: String)
data class BaiduTranslationBean(val from: String?, val to: String?, val trans_result: List<BaiduTranslationResultBean>?, val src_text: String?, val error_code: String?, val error_msg: String?) {
    fun toTranslateResult(): TranslateResult {
        var result = ""
        if (!trans_result.isNullOrEmpty()) {
            trans_result.forEach { it -> result += "${it.dst};" }
            if (result.length > 1) {
                result = result.substring(IntRange(0, result.length - 1))
            }
        }
        return TranslateResult(SourceBaidu, from ?: "zh", to ?: "en", src_text ?: "", result, error_code);
    }
}


