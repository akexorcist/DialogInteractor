package com.akexorcist.library.dialoginteractor.sample.dialog.alert

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akexorcist.library.dialoginteractor.InteractorDialog
import com.akexorcist.library.dialoginteractor.createBundle
import com.akexorcist.library.dialoginteractor.sample.R
import com.akexorcist.library.dialoginteractor.sample.dialog.DialogViewModel
import kotlinx.android.synthetic.main.dialog_alert.*

class AlertDialog : InteractorDialog<AlertMapper, AlertListener, DialogViewModel>() {
    private var message: String? = null

    companion object {
        const val EXTRA_MESSAGE = "com.akexorcist.library.dialoginteractor.sample.dialog.alert.extra_message"

        fun newInstance(message: String?, key: String?, request: Bundle?): AlertDialog = AlertDialog().apply {
            arguments = createBundle(key, request).apply {
                putString(EXTRA_MESSAGE, message)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        message = when (savedInstanceState) {
            null -> arguments?.getString(EXTRA_MESSAGE)
            else -> savedInstanceState.getString(EXTRA_MESSAGE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.dialog_alert, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        textViewMessage.text = message ?: ""
        buttonOk.setOnClickListener {
            getListener().onOkButtonClick(getKey(), getData())
            dismiss()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_MESSAGE, message)
    }

    override fun bindViewModel() = DialogViewModel::class.java

    override fun bindLauncher(viewModel: DialogViewModel) = viewModel.alert

    class Builder {
        private var message: String? = null
        private var key: String? = null
        private var data: Bundle? = null

        fun setKey(key: String?): Builder = this.apply {
            this.key = key
        }

        fun setData(data: Bundle?): Builder = this.apply {
            this.data = data
        }

        fun setMessage(message: String?): Builder = this.apply {
            this.message = message
        }

        fun build(): AlertDialog = AlertDialog.newInstance(message, key, data)
    }
}