package com.example.goorum.Utils

import java.text.SimpleDateFormat
import java.util.*

class SDF {
    companion object {
        val datetimeSlash = SimpleDateFormat("yy/MM/dd HH:mm", Locale.KOREA)
        val datetimeBar = SimpleDateFormat("yyyy-MM-dd HH:mm", Locale.KOREA)
    }
}