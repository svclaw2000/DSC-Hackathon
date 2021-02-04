package com.example.goorum.utils

import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL

class HttpHelper {
    companion object {
        const val SERVER_URL = "http://641718c273dd.ngrok.io"

        suspend fun request(url: String, method: HttpMethod, data: JsonObject? = null) : JsonObject {
            val result = withContext(Dispatchers.IO) {
                var httpConn : HttpURLConnection? = null
                var ret = ""

                try {
                    val inputStream : InputStream
                    val urlConn = URL(SERVER_URL + url)
                    httpConn = urlConn.openConnection() as HttpURLConnection

                    httpConn.requestMethod = method.name
                    httpConn.doInput = true

                    if (method == HttpMethod.POST) {
                        httpConn.doOutput = true
                        val outputStream = httpConn.outputStream
                        outputStream.write(data.toString().toByteArray())
                        outputStream.flush()
                    }

                    val status = httpConn.responseCode
                    try {
                        inputStream = if (status != HttpURLConnection.HTTP_OK) {
                            httpConn.errorStream
                        } else {
                            httpConn.inputStream
                        }

                        if (inputStream != null) {
                            ret = convertInputStreamToString(inputStream)
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                } catch (e: Exception) {
                    e.printStackTrace()
                } finally {
                    httpConn?.disconnect()
                }
                ret
            }

            return JsonParser.parseString(result).asJsonObject
        }

        @Throws(IOException::class)
        fun convertInputStreamToString(inputStream: InputStream): String {
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            var ret = ""

            var line = bufferedReader.readLine()
            while (line != null) {
                ret += line
                line = bufferedReader.readLine()
            }
            inputStream.close()
            return ret
        }
    }
}