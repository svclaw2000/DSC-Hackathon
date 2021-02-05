package com.example.goorum.utils

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
        const val SERVER_URL = "https://98d0e30af79c.ngrok.io"
        const val TEST_MODE = false

        var mSession = false
        var mCookies = ""

        suspend fun request(url: String, method: HttpMethod, data: JsonObject? = null) : JsonObject {
            val result = withContext(Dispatchers.IO) {
                var httpConn : HttpURLConnection? = null
                var ret = ""

                val outputString = if (data != null) jsonToParamString(data) else null

                try {
                    val inputStream : InputStream

                    val fullUrl = if (method == HttpMethod.GET && outputString != null) {
                        "${SERVER_URL}${url}?${outputString}"
                    } else {
                        SERVER_URL + url
                    }

                    val urlConn = URL(fullUrl)
                    httpConn = urlConn.openConnection() as HttpURLConnection

                    httpConn.requestMethod = method.name
                    httpConn.doInput = true

                    if (mSession) {
                        httpConn.setRequestProperty("Cookie", mCookies)
                    }

                    Log.d("HttpInfo", "${method.name} ${fullUrl}")

                    if ((method == HttpMethod.POST || method == HttpMethod.PUT) && outputString != null) {
                        httpConn.doOutput = true
                        val outputStream = httpConn.outputStream

                        Log.d("HttpOutput", outputString)

                        outputStream.write(outputString.toByteArray())
                        outputStream.flush()
                    }

                    val status = httpConn.responseCode

                    if (!mSession) {
                        val header = httpConn.headerFields
                        if (header.containsKey("Set-Cookie")) {
                            val cookie = header.get("Set-Cookie")
                            if (cookie != null) {
                                for (c in cookie.iterator()) {
                                    mCookies += c
                                }
                            }
                            Log.d("HttpCookie", "Got cookie.")
                            mSession = true
                        } else {
                            mSession = false
                        }
                    }

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

        fun jsonToParamString(json: JsonObject) : String {
            val list = ArrayList<String>()
            for (key in json.keySet()) {
                list.add("${key}=${json[key].asString}")
            }

            return list.joinToString("&")
        }
    }
}