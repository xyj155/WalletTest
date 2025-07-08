package com.fastchar.wallettest.util

import android.content.Context
import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.concurrent.locks.ReentrantLock
import kotlin.concurrent.withLock

object AssetsJsonReader {

    private val lock = ReentrantLock()

    /**
     * 读取 assets 文件夹下的 json 文件
     * @param context 上下文
     * @param fileName 文件名，如 "config.json"
     * @return JSON 字符串，读取失败时返回 null
     */
    fun readJson(context: Context, fileName: String): String? {
        lock.withLock {
            return try {
                val assetManager = context.assets
                assetManager.open(fileName).use { inputStream ->
                    val reader = BufferedReader(InputStreamReader(inputStream))
                    val stringBuilder = StringBuilder()
                    var line: String?
                    while (reader.readLine().also { line = it } != null) {
                        stringBuilder.append(line)
                    }
                    stringBuilder.toString()
                }
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
    }
}