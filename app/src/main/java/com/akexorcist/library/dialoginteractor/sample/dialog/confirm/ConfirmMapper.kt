package com.akexorcist.library.dialoginteractor.sample.dialog.confirm

import android.os.Bundle
import com.akexorcist.library.dialoginteractor.DialogEvent
import com.akexorcist.library.dialoginteractor.EventMapper
import com.akexorcist.library.dialoginteractor.LiveEvent

class ConfirmMapper : EventMapper<ConfirmListener>() {
    companion object {
        const val EVENT_CONFIRM = "com.akexorcist.library.dialoginteractor.sample.dialog.confirm.event_confirm"
        const val EVENT_CANCEL = "com.akexorcist.library.dialoginteractor.sample.dialog.confirm.event_cancel"
    }

    override fun toEvent(event: LiveEvent<DialogEvent>): ConfirmListener = object : ConfirmListener {
        override fun onConfirmButtonClick(key: String?, data: Bundle?) {
            event.value = DialogEvent.Builder(
                event = EVENT_CONFIRM,
                key = key,
                data = data
            ).build()
        }

        override fun onCancelButtonClick(key: String?, data: Bundle?) {
            event.value = DialogEvent.Builder(
                event = EVENT_CANCEL,
                key = key,
                data = data
            ).build()
        }
    }

    override fun toListener(event: DialogEvent, listener: ConfirmListener) {
        when (event.getEvent()) {
            EVENT_CONFIRM -> {
                listener.onConfirmButtonClick(
                    key = event.getKey(),
                    data = event.getData()
                )
            }
            EVENT_CANCEL -> {
                listener.onCancelButtonClick(
                    key = event.getKey(),
                    data = event.getData()
                )
            }
        }
    }
}
