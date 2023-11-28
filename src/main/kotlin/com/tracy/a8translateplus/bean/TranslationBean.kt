package com.tracy.a8translateplus.bean

/**
 * @author cuishijie
 * *
 */

data class TranslationBean(val from: String, val to: String, val tgt_text: String?, val src_text: String?, val error_code: String?, val error_msg: String?) {
    override fun toString(): String {
        return tgt_text ?: "error:${error_msg}"
    }
}


