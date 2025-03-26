package br.com.velha.tech.viewmodel.common

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import br.com.velha.tech.R
import br.com.velha.tech.firebase.apis.crashlytics.sendToFirebaseCrashlytics
import com.google.firebase.FirebaseNetworkException
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch

abstract class VelhaTechViewModel(protected val context: Context) : ViewModel() {

    abstract fun onShowError(throwable: Throwable)

    private fun onShowCommonError(throwable: Throwable) {
        when(throwable) {
            is FirebaseNetworkException -> context.getString(R.string.network_error_message)
            else -> onShowError(throwable)
        }
    }

    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        onShowCommonError(throwable)
        onError(throwable)
    }

    protected fun onError(throwable: Throwable) {
        throwable.sendToFirebaseCrashlytics()
        Log.e(TAG, throwable.message, throwable)
    }

    fun launch(block: suspend () -> Unit) = viewModelScope.launch(exceptionHandler) {
        block()
    }

    companion object {
        private const val TAG = "FitnessProViewModel"
    }

}