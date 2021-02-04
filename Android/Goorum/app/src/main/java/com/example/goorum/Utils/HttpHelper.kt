package com.example.goorum.Utils

import android.util.Log
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.*
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL
import java.util.*

class HttpHelper {
    companion object {
        const val SERVER_URL = "http://641718c273dd.ngrok.io"
        const val TEST_MODE = false

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

                        val data = data ?: JsonObject()
                        val list = ArrayList<String>()
                        for (key in data.keySet()) {
                            list.add("${key}=${data[key].asString}")
                        }

                        val outputString = list.joinToString("&")

                        Log.d("HttpOutput", outputString)

                        outputStream.write(outputString.toByteArray())
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

            Log.d("HttpInput", result)
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