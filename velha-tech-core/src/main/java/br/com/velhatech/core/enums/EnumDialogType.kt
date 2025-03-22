package br.com.velhatech.core.enums

import br.com.velhatech.core.R

enum class EnumDialogType(val titleResId: Int) {
    ERROR(R.string.error_dialog_title),
    CONFIRMATION(R.string.warning_dialog_title),
    INFORMATION(R.string.information_dialog_title)
}