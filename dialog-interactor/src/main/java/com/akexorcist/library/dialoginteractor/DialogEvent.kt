package com.akexorcist.library.dialoginteractor

import android.os.Bundle

class DialogEvent(private val bundle: Bundle) {
    companion object {
        private const val EXTRA_EVENT = "com.akexorcist.library.dialoginteractor.extra_event"
        private const val EXTRA_KEY = "com.akexorcist.library.dialoginteractor.extra_key"
        private const val EXTRA_DATA = "com.akexorcist.library.dialoginteractor.extra_data"
    }

    fun getEvent(): String? = bundle.getString(EXTRA_EVENT)

    fun getKey(): String? = bundle.getString(EXTRA_KEY)

    fun getData(): Bundle? = bundle.getBundle(EXTRA_DATA)

    class Builder(
            var event: String? = null,
            var key: String? = null,
            var data: Bundle? = null
    ) {
        fun build() = DialogEvent(Bundle().apply {
            putString(EXTRA_EVENT, event)
            putString(EXTRA_KEY, key)
            putBundle(EXTRA_DATA, data)
        })
    }
}