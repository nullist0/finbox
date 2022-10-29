package com.nullist.finbox.domain

internal class FakeFinboxRepository : FinboxRepository {
    private val directories = mutableListOf<InboxDirectory>()

    override val finbox: Finbox
        get() = Finbox(directories.flatMap { it.files })

    override fun addDirectory(directory: InboxDirectory) {
        directories += directory
    }

    override fun removeDirectory(directory: InboxDirectory) {
        directories -= directory
    }
}
