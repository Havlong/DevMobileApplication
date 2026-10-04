package ru.pnzgu.devmobile.model

enum class SignGateUserRole {
    ADMIN, SIGNER;

    val infraInfoAvailable
        get() = when (this) {
            ADMIN -> true
            else -> false
        }

    fun switch() = when (this) {
        ADMIN -> SIGNER
        SIGNER -> ADMIN
    }
}
