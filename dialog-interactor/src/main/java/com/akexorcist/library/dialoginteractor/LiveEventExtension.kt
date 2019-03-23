package com.akexorcist.library.dialoginteractor

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.Observer

fun <MAPPER : EventMapper<LISTENER>, LISTENER : DialogListener> LiveEvent<DialogEvent>.observe(
        cls: Class<MAPPER>,
        owner: LifecycleOwner,
        listener: LISTENER?
) {
    this.observe(owner, Observer { event: DialogEvent? ->
        if (event != null && listener != null) {
            cls.newInstance().toListener(event, listener)
        }
    })
}

fun <MAPPER : EventMapper<LISTENER>, LISTENER : DialogListener> LiveEvent<DialogEvent>.event(
        cls: Class<MAPPER>,
        event: LiveEvent<DialogEvent>
): LISTENER = cls.newInstance().toEvent(event)