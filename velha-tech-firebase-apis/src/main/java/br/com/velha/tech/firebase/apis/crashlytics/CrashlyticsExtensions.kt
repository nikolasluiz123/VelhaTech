package br.com.velha.tech.firebase.apis.crashlytics

import br.com.velha.tech.core.exceptions.NoLoggingException
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

fun Throwable.sendToFirebaseCrashlytics() {
    if (this !is NoLoggingException) {
        Firebase.crashlytics.recordException(this)
    }
}