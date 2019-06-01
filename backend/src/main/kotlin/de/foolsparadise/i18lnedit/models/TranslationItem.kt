package de.foolsparadise.i18lnedit.models

data class TranslationItem(
    val key: String,
    val translations: MutableList<TranslationString> = mutableListOf(),
    var id: String?,
    val deleted: Boolean? = false
)