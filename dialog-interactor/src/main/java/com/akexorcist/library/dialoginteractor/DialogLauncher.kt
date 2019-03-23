package com.akexorcist.library.dialoginteractor

import androidx.lifecycle.LifecycleOwner

class DialogLauncher<MAPPER : EventMapper<LISTENER>, LISTENER : DialogListener>(private val cls: Class<MAPPER>) :
        Launch<LISTENER> {
    private val event = LiveEvent<DialogEvent>()
    val listener: LISTENER = event.event(cls, event)

    override fun observe(owner: LifecycleOwner, listener: LISTENER) {
        event.observe(cls, owner, listener)
    }
}
