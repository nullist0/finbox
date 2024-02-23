package com.nullist.finbox.domain.service

import com.nullist.finbox.domain.InboxDirectory
import com.nullist.finbox.domain.InboxFile

interface FinboxRepository {
    suspend fun getInboxFiles(): List<InboxFile>
    suspend fun getInboxDirectories(): List<InboxDirectory>
    suspend fun deleteDirectory(directory: InboxDirectory)
    suspend fun deleteFile(file: InboxFile)
}
