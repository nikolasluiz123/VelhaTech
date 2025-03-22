package br.com.velhatech.firebase.apis.crashlytics

import br.com.velhatech.core.exceptions.NoLoggingException
import com.google.firebase.crashlytics.ktx.crashlytics
import com.google.firebase.ktx.Firebase

fun Throwable.sendToFirebaseCrashlytics() {
    if (this !is NoLoggingException) {
        Firebase.crashlytics.recordException(this)
    }
}