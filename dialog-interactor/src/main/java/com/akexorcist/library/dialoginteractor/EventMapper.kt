package com.akexorcist.library.dialoginteractor

abstract class EventMapper<T : DialogListener> {
    abstract fun toEvent(event: LiveEvent<DialogEvent>): T

    abstract fun toListener(event: DialogEvent, listener: T)
}