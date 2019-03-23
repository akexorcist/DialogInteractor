package com.akexorcist.library.dialoginteractor.sample.dialog.alert

import android.os.Bundle
import com.akexorcist.library.dialoginteractor.DialogEvent
import com.akexorcist.library.dialoginteractor.EventMapper
import com.akexorcist.library.dialoginteractor.LiveEvent

class AlertMapper : EventMapper<AlertListener>() {
    companion object {
        const val EVENT_OK = "com.akexorcist.library.dialoginteractor.sample.dialog.alert.event_ok"
    }

    override fun toEvent(event: LiveEvent<DialogEvent>): AlertListener =
        object : AlertListener {
            override fun onOkButtonClick(key: String?, data: Bundle?) {
                event.value = DialogEvent.Builder(
                    event = EVENT_OK,
                    key = key,
                    data = data
                ).build()
            }
        }

    override fun toListener(event: DialogEvent, listener: AlertListener) {
        if (event.getEvent() == EVENT_OK) {
            listener.onOkButtonClick(
                key = event.getKey(),
                data = event.getData()
            )
        }
    }
}
