package com.tracy.a8translateplus.net;

import org.jetbrains.annotations.NotNull;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer
import java.nio.charset.Charset

/**
 * Created by Pinger on 2016/12/11.
 * 将流转换成字符串
 */
class StreamUtils {

    companion object {
        fun getStringFromStream(ins: InputStream): String {
            // 内层流读取数据
            val outputStream = ByteArrayOutputStream();
            var len = 0
            var buffer = ByteArray(1024)
            // 写入数据到输出流
            do {
                len = ins.read(buffer)
                if (len > 0) {
                    outputStream.write(buffer, 0, len)
                }
            } while (len != -1)
            // 返回字符串
            return String(outputStream.toByteArray(), Charset.forName("UTF-8"));
        }
    }

}
