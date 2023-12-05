package com.tracy.a8translateplus.net

import org.apache.commons.lang.StringUtils
import org.jetbrains.annotations.NonNls
import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.util.*

/**
 *
 * @author cuishijie
 */
const val PREFIX_NAME = "A8Plus"

object LocalData {
    private val f = File(System.getProperty("user.home") + "/a8plus.properties")
    private val p = Properties()
    private var isInitialized = false
    fun store(@NonNls key: String, @NonNls value: String) {
        p[StringUtils.uncapitalize("${PREFIX_NAME}_$key")] = value
        save()
    }

    init {
        if (!f.exists()) {
            f.createNewFile()
        }
        if (f.exists()) {
            p.load(FileReader(f))
            isInitialized = true
        }
    }


    private fun save() {
        if (isInitialized) {
            p.store(FileWriter(f), "")
        }
    }

    fun read(@NonNls key: String): String? = if (isInitialized) p.getProperty(StringUtils.uncapitalize("${PREFIX_NAME}_$key")) else null

}
