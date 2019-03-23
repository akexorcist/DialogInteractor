package com.akexorcist.library.dialoginteractor

import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProviders

abstract class InteractorDialog<MAPPER : EventMapper<LISTENER>, LISTENER : DialogListener, VM : ViewModel> :
    DialogFragment() {
    private lateinit var viewModel: VM

    private var key: String? = null
    private var data: Bundle? = null

    companion object {
        internal const val EXTRA_KEY = "com.akexorcist.library.dialoginteractor.extra_key"
        internal const val EXTRA_DATA = "com.akexorcist.library.dialoginteractor.extra_data"
        internal const val TAG = "com.akexorcist.library.dialoginteractor.default_dialog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (savedInstanceState == null) {
            arguments?.let { onRestoreInstanceState(it) }
        } else {
            onRestoreInstanceState(savedInstanceState)
        }
        activity?.let {
            viewModel = ViewModelProviders.of(it).get(bindViewModel())
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(EXTRA_KEY, key)
        outState.putBundle(EXTRA_DATA, data)
    }

    private fun onRestoreInstanceState(bundle: Bundle) {
        key = bundle.getString(EXTRA_KEY)
        data = bundle.getBundle(EXTRA_DATA)
    }

    abstract fun bindViewModel(): Class<VM>

    abstract fun bindLauncher(viewModel: VM): DialogLauncher<MAPPER, LISTENER>

    fun getListener(): LISTENER = bindLauncher(viewModel).listener

    fun getKey(): String? = key

    fun getData(): Bundle? = data

    fun show(manager: FragmentManager) = run { super.show(manager, TAG) }

    fun show(transaction: FragmentTransaction): Int = super.show(transaction, TAG)

    fun showNow(manager: FragmentManager) = run { super.showNow(manager, TAG) }
}