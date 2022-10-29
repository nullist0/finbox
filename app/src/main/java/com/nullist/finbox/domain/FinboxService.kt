package com.nullist.finbox.domain

class FinboxService(
    private val repository: FinboxRepository
) {
    val allInboxFiles: List<InboxFile>
        get() = repository.finbox.inboxFiles
}
