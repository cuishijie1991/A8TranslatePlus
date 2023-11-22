package com.tracy.a8translateplus.net

import org.apache.commons.lang.StringUtils
import org.jetbrains.annotations.NonNls
import java.util.*

/**
 *
 * @author cuishijie
 */
const val PREFIX_NAME = "A8Plus"

object LocalData {
    private val p = Properties()
    fun store(@NonNls key: String, @NonNls value: String) {
        p.put(StringUtils.uncapitalize("$PREFIX_NAME-$key"), value)
        save()
    }

    fun clear() {
        p.clear()
        save()
    }

    private fun save() = Unit

    fun read(@NonNls key: String): String? = p.getProperty(StringUtils.uncapitalize("$PREFIX_NAME-$key"))
}
