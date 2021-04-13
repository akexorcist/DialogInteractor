package com.akexorcist.library.dialoginteractor.sample.dialog.confirm

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.akexorcist.library.dialoginteractor.InteractorDialog
import com.akexorcist.library.dialoginteractor.createBundle
import com.akexorcist.library.dialoginteractor.sample.databinding.DialogConfirmBinding
import com.akexorcist.library.dialoginteractor.sample.dialog.DialogViewModel

class ConfirmDialog : InteractorDialog<ConfirmMapper, ConfirmListener, DialogViewModel>() {
    private lateinit var binding: DialogConfirmBinding
    private var title: String? = null
    private var message: String? = null

    companion object {
        private const val EXTRA_TITLE = "com.akexorcist.library.dialoginteractor.sample.dialog.confirm.extra_title"
        private const val EXTRA_MESSAGE = "com.akexorcist.library.dialoginteractor.sample.dialog.confirm.extra_message"

        fun newInstance(title: String?, message: String?, key: String?, data: Bundle?) = ConfirmDialog().apply {
            arguments = createBundle(key, data).apply {
                putString(EXTRA_TITLE, title)
                putString(EXTRA_MESSAGE, message)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = when (savedInstanceState) {
            null -> arguments?.getString(EXTRA_TITLE)
            else -> savedInstanceState.getString(EXTRA_TITLE)
        }
        message = when (savedInstanceState) {
            null -> arguments?.getString(EXTRA_MESSAGE)
            else -> savedInstanceState.getString(EXTRA_MESSAGE)
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        binding = DialogConfirmBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.textViewTitle.text = title ?: ""
        binding.textViewMessage.text = message ?: ""
        binding.buttonConfirm.setOnClickListener {
            getListener().onConfirmButtonClick(getKey(), getData())
            dismiss()
        }
        binding.buttonCancel.setOnClickListener {
            getListener().onCancelButtonClick(getKey(), getData())
            dismiss()
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_TITLE, title)
        outState.putString(EXTRA_MESSAGE, message)
    }

    override fun bindViewModel() = DialogViewModel::class.java

    override fun bindLauncher(viewModel: DialogViewModel) = viewModel.confirm

    class Builder {
        private var title: String? = null
        private var message: String? = null
        private var key: String? = null
        private var data: Bundle? = null

        fun setTitle(title: String?): Builder = this.apply {
            this.title = title
        }

        fun setMessage(message: String?): Builder = this.apply {
            this.message = message
        }

        fun setKey(key: String?): Builder = this.apply {
            this.key = key
        }

        fun setData(data: Bundle?): Builder = this.apply {
            this.data = data
        }

        fun build(): ConfirmDialog = newInstance(title, message, key, data)
    }
}
