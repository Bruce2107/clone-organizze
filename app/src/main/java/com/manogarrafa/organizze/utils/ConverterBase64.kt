package com.manogarrafa.organizze.utils

import android.util.Base64

class ConverterBase64 {
    companion object {
        fun decode(value: String): String =
            Base64.decode(value.toByteArray(), Base64.NO_WRAP).toString()

        fun encode(value: String) =
            Base64.encodeToString(value.toByteArray(), Base64.NO_WRAP).toString()
                .replace("(\\n|\\r)", "")

    }
}