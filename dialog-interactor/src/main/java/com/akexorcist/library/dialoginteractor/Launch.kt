package com.akexorcist.library.dialoginteractor

import androidx.lifecycle.LifecycleOwner

interface Launch<LISTENER : DialogListener> {
    fun observe(owner: LifecycleOwner, listener: LISTENER)
}
