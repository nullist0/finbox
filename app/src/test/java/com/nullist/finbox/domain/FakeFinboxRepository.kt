package com.nullist.finbox.domain

import com.nullist.finbox.domain.service.FinboxRepository

internal class FakeFinboxRepository(
    inboxFiles: List<InboxFile> = emptyList(),
    inboxDirectories: List<InboxDirectory> = emptyList(),
): FinboxRepository {
    private val mutableInboxFiles = inboxFiles.toMutableList()
    private val mutableInboxDirectories = inboxDirectories.toMutableList()

    override suspend fun getInboxFiles(): List<InboxFile> = mutableInboxFiles

    override suspend fun getInboxDirectories(): List<InboxDirectory> = mutableInboxDirectories

    override suspend fun deleteDirectory(directory: InboxDirectory) {
        mutableInboxDirectories.remove(directory)
    }

    override suspend fun deleteFile(file: InboxFile) {
        mutableInboxFiles.remove(file)
    }
}
