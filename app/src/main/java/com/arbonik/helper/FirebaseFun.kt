package com.arbonik.helper

import android.net.Uri
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.HttpURLConnection
import java.net.URL

class FirebaseFun {
    val API_PATH = "https://us-central1-helper-681f0.cloudfunctions.net/"
    val HELLO_FUN = "hello"
    val ENDPOINT = Uri.parse("https://us-central1-helper-681f0.cloudfunctions.net/helloWorld")
        .buildUpon()
        .build()

    fun getFirst():String{
        return getUrlString(ENDPOINT.toString())
    }

    fun getUrlString(string : String) : String{
        return String(getUrlBytes(string))
    }

    fun getUrlBytes(urlSpec : String): ByteArray {
        var url = URL(urlSpec)
        var connection = url.openConnection() as HttpURLConnection

        try {
            var out = ByteArrayOutputStream()
            var input = connection.getInputStream()
            if (connection.responseCode != HttpURLConnection.HTTP_OK) {
                throw IOException(connection.responseMessage + url)
            }
            var readBytes = 0
            var buffer = ByteArray(1024)
            while ({readBytes = input.read(buffer)
                    readBytes}() > 0) {
                out.write(buffer, 0, readBytes)
            }
            out.close()
            return out.toByteArray()
        } finally {
            connection.disconnect()
        }
    }

}