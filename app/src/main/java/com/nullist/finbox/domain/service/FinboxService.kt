package com.nullist.finbox.domain.service

import com.nullist.finbox.domain.InboxDirectory
import com.nullist.finbox.domain.InboxFile
import com.nullist.finbox.domain.Model
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.io.File

class FinboxService(
    private val repository: FinboxRepository,
    private val serviceCoroutineDispatcher: CoroutineDispatcher,
) {
    private val serviceScope: CoroutineScope = CoroutineScope(serviceCoroutineDispatcher)
    private val mutableModelFlow = MutableSharedFlow<Model>()
    val modelFlow: Flow<Model> = mutableModelFlow

    suspend fun getTemporaryFiles(): List<InboxFile> = withContext(serviceCoroutineDispatcher) {
        repository.getInboxFiles().filterIsInstance<InboxFile.Temporary>()
    }

    suspend fun getDirectories(): List<InboxDirectory> = withContext(serviceCoroutineDispatcher) {
        repository.getInboxDirectories()
    }

    fun getInboxFilesAsFlow(): Flow<List<InboxFile>> = flow {
        emit(repository.getInboxFiles())
        modelFlow.collect {
            emit(repository.getInboxFiles())
        }
    }

    fun getInboxDirectoriesAsFlow(): Flow<List<InboxDirectory>> = flow {
        emit(repository.getInboxDirectories())
        modelFlow.collect {
            emit(repository.getInboxDirectories())
        }
    }

    fun addInboxDirectory(file: File) {
        serviceScope.launch {
            mutableModelFlow.emit(InboxDirectory.from(file))
        }
    }

    fun addInboxFile(file: File) {
        serviceScope.launch {
            mutableModelFlow.emit(InboxFile.from(file))
        }
    }

    fun deleteInboxDirectory(directory: InboxDirectory) {
        serviceScope.launch {
            mutableModelFlow.emit(directory)
            repository.deleteDirectory(directory)
        }
    }

    fun deleteInboxFile(file: InboxFile) {
        serviceScope.launch {
            mutableModelFlow.emit(file)
            repository.deleteFile(file)
        }
    }

    fun makePermanent(file: InboxFile) {
        require(file is InboxFile.Temporary)
        serviceScope.launch {
            mutableModelFlow.emit(file.toPermanent())
        }
    }

    fun makeTemporary(file: InboxFile) {
        require(file is InboxFile.Permanent)
        serviceScope.launch {
            mutableModelFlow.emit(file.toTemporary())
        }
    }
}
