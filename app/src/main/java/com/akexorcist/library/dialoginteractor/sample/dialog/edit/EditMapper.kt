package com.akexorcist.library.dialoginteractor.sample.dialog.edit

import android.os.Bundle
import com.akexorcist.library.dialoginteractor.DialogEvent
import com.akexorcist.library.dialoginteractor.EventMapper
import com.akexorcist.library.dialoginteractor.LiveEvent
import com.akexorcist.library.dialoginteractor.sample.vo.User

class EditMapper : EventMapper<EditListener>() {
    companion object {
        const val EVENT_EDIT_SUCCESS = "com.akexorcist.library.dialoginteractor.sample.dialog.edit.event_edit_success"
        const val EVENT_EDIT_FAILURE = "com.akexorcist.library.dialoginteractor.sample.dialog.edit.event_edit_failure"
    }

    override fun toEvent(event: LiveEvent<DialogEvent>): EditListener = object : EditListener {
        override fun onEditSuccess(user: User?, key: String?, data: Bundle?) {
            event.value = DialogEvent.Builder(
                event = EVENT_EDIT_SUCCESS,
                key = key,
                data = data?.apply {
                    putParcelable(EditDialog.EXTRA_EDITED_USER, user)
                }
            ).build()
        }

        override fun onEditFailure(error: String?, key: String?, data: Bundle?) {
            event.value = DialogEvent.Builder(
                event = EVENT_EDIT_FAILURE,
                key = key,
                data = data?.apply {
                    putString(EditDialog.EXTRA_ERROR_MESSAGE, error)
                }
            ).build()
        }
    }

    override fun toListener(event: DialogEvent, listener: EditListener) {
        when (event.getEvent()) {
            EVENT_EDIT_SUCCESS -> listener.onEditSuccess(
                    user = event.getData()?.getParcelable<User?>(EditDialog.EXTRA_EDITED_USER),
                    key = event.getKey(),
                    data = event.getData()
            )
            EVENT_EDIT_FAILURE -> listener.onEditFailure(
                    error = event.getData()?.getString(EditDialog.EXTRA_ERROR_MESSAGE),
                    key = event.getKey(),
                    data = event.getData()
            )
        }
    }
}