package com.nullist.finbox.domain

interface FinboxRepository {
    val finbox: Finbox

    fun addDirectory(directory: InboxDirectory)
    fun removeDirectory(directory: InboxDirectory)
}
