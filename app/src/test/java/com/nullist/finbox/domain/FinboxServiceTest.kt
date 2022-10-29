package com.nullist.finbox.domain

import org.junit.Before
import org.junit.Test

internal class FinboxServiceTest {
    lateinit var finboxService: FinboxService
    lateinit var finboxRepository: FakeFinboxRepository

    @Before
    fun setup() {
        finboxRepository = FakeFinboxRepository()
        finboxService = FinboxService(finboxRepository)
    }

    @Test
    fun `test get all inbox files with files`() {
        // given
        val expectedInboxFiles = listOf(
            PermanentInboxFile("file1"),
            TemporaryInboxFile("file2"),
        )
        val directory = InboxDirectory(
            "/path/to/dir1",
            expectedInboxFiles
        )
        finboxRepository.addDirectory(directory)

        // when
        val inboxFiles = finboxService.allInboxFiles

        // then
        assert(inboxFiles == expectedInboxFiles)
    }

    @Test
    fun `test get all inbox files with empty files`() {
        // given
        val expectedInboxFiles = listOf<InboxFile>()

        // when
        val inboxFiles = finboxService.allInboxFiles

        // then
        assert(inboxFiles == expectedInboxFiles)
    }
}
