package dev.moru3.compsql.interfaces

interface SendSyntax: Syntax {
    /**
     * SQLと同期します。
     */
    fun send(force: Boolean)
}

interface NonForceSyntax: Syntax {
    /**
     * SQLと同期します。
     */
    fun send()
}