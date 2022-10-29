package com.nullist.finbox.domain

open class InboxFile protected constructor(
    open val name: String
)

data class TemporaryInboxFile(
    override val name: String
): InboxFile(name)

data class PermanentInboxFile(
    override val name: String
): InboxFile(name)
