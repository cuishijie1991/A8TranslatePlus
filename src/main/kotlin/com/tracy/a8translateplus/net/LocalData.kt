package com.tracy.a8translateplus.net

import com.intellij.openapi.diagnostic.Logger
import org.jetbrains.annotations.NonNls
import java.io.File
import java.io.IOException
import java.io.InputStreamReader
import java.nio.charset.Charset
import java.nio.charset.CodingErrorAction
import java.util.*

/**
 *
 * @author cuishijie
 */
const val PREFIX_NAME = "A8Plus"

object LocalData {
    private val LOG = Logger.getInstance("com.tracy.a8translateplus.net.LocalData")
    private val f = File(System.getProperty("user.home"), "a8plus.properties")
    private val p = Properties()
    private var isInitialized = false
    fun store(@NonNls key: String, @NonNls value: String) {
        p[propertyKey(key)] = value
        save()
    }

    init {
        try {
            if (!f.exists()) {
                f.parentFile?.mkdirs()
                f.createNewFile()
            }
            if (f.exists()) {
                // 优先按 UTF-8 读取，失败时回退到系统默认字符集以兼容旧版本写入的 GBK 文件
                val props = readProperties(f, Charsets.UTF_8) ?: readProperties(f, Charset.defaultCharset())
                if (props != null) {
                    p.putAll(props)
                } else {
                    LOG.warn("无法解析配置文件：$f")
                }
                isInitialized = true
            }
        } catch (e: IOException) {
            LOG.warn("初始化配置文件失败：$f", e)
        }
    }

    private fun readProperties(file: File, charset: Charset): Properties? {
        return try {
            val decoder = charset.newDecoder()
                    .onMalformedInput(CodingErrorAction.REPORT)
                    .onUnmappableCharacter(CodingErrorAction.REPORT)
            Properties().apply {
                InputStreamReader(file.inputStream(), decoder).use { load(it) }
            }
        } catch (e: Exception) {
            null
        }
    }

    @Synchronized
    private fun save() {
        if (!isInitialized) return
        try {
            f.writer(Charsets.UTF_8).use { p.store(it, "") }
        } catch (e: IOException) {
            LOG.warn("保存配置文件失败：$f", e)
        }
    }

    fun read(@NonNls key: String): String? = if (isInitialized) p.getProperty(propertyKey(key)) else null

    private fun propertyKey(key: String): String {
        val fullKey = "${PREFIX_NAME}_$key"
        if (fullKey.isEmpty()) return fullKey
        val first = fullKey[0]
        // 与 commons-lang 的 uncapitalize 行为一致：仅首字母大写时转为小写
        return if (first.isUpperCase()) Character.toLowerCase(first) + fullKey.substring(1) else fullKey
    }

}
