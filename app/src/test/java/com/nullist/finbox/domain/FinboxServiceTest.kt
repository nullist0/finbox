package com.nullist.finbox.domain

import com.nullist.finbox.domain.service.FinboxService
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.doReturn
import org.mockito.kotlin.mock
import java.io.File
import kotlin.test.assertEquals
import kotlin.test.assertFails

@OptIn(ExperimentalCoroutinesApi::class)
class FinboxServiceTest {
    private val testDispatcher = UnconfinedTestDispatcher()
    private val testScope = TestScope(testDispatcher)

    private val finboxService: FinboxService by lazy {
        FinboxService(finboxRepository, testDispatcher)
    }
    private val finboxRepository: FakeFinboxRepository by lazy {
        FakeFinboxRepository(DUMMY_INBOX_FILES, DUMMY_INBOX_DIRECTORIES)
    }
    private val capturedModels = mutableListOf<Model>()
    private val capturedFiles = mutableListOf<List<InboxFile>>()
    private val capturedDirectories = mutableListOf<List<InboxDirectory>>()

    @Before
    fun setUp() {
        testScope.backgroundScope.launch {
            finboxService.modelFlow.toList(capturedModels)
        }
        testScope.backgroundScope.launch {
            finboxService.getInboxDirectoriesAsFlow().toList(capturedDirectories)
        }
        testScope.backgroundScope.launch {
            finboxService.getInboxFilesAsFlow().toList(capturedFiles)
        }
    }

    @Test
    fun testInitialFlowValues() = testScope.runTest {
        assertEquals(DUMMY_INBOX_FILES, capturedFiles.first())
        assertEquals(DUMMY_INBOX_DIRECTORIES, capturedDirectories.first())
    }

    @Test
    fun testGetDirectories() = testScope.runTest {
        // given
        val expectedInboxFiles = DUMMY_INBOX_DIRECTORIES

        // when
        val inboxFiles = finboxService.getDirectories()

        // then
        assertEquals(expectedInboxFiles, inboxFiles)
    }

    @Test
    fun testGetTemporaryFiles() = testScope.runTest {
        // given
        val expectedInboxFiles = listOf(DUMMY_TEMPORARY_FILE)

        // when
        val temporaries = finboxService.getTemporaryFiles()

        // then
        assertEquals(expectedInboxFiles, temporaries)
    }

    @Test
    fun testMakePermanent() = testScope.runTest {
        // given
        val expected = DUMMY_TEMPORARY_FILE.toPermanent()

        // when
        finboxService.makePermanent(DUMMY_TEMPORARY_FILE)

        // then
        assertEquals(expected, capturedModels.last())
        assertEquals(2, capturedFiles.size)
        assertEquals(2, capturedDirectories.size)
    }

    @Test
    fun testMakeTemporary() = testScope.runTest {
        // given
        val expected = DUMMY_PERMANENT_FILE.toTemporary()

        // when
        finboxService.makeTemporary(DUMMY_PERMANENT_FILE)

        // then
        assertEquals(expected, capturedModels.last())
        assertEquals(2, capturedFiles.size)
        assertEquals(2, capturedDirectories.size)
    }

    @Test
    fun testMakePermanent_failed() = testScope.runTest {
        // then
        assertFails {
            // when
            finboxService.makePermanent(DUMMY_PERMANENT_FILE)
        }
    }

    @Test
    fun testMakeTemporary_failed() = testScope.runTest {
        // then
        assertFails {
            // when
            finboxService.makeTemporary(DUMMY_TEMPORARY_FILE)
        }
    }

    @Test
    fun testAddInboxFile() = testScope.runTest {
        // given
        val filename = "filename"
        val filepath = "path"
        val mockedFile = mock<File> {
            on { name } doReturn filename
            on { path } doReturn filepath
        }

        // when
        finboxService.addInboxFile(mockedFile)

        // then
        assertEquals(InboxFile.from(mockedFile), capturedModels.last())
        assertEquals(2, capturedFiles.size)
        assertEquals(2, capturedDirectories.size)
    }

    @Test
    fun testAddInboxDirectory() = testScope.runTest {
        // given
        val filename = "filename"
        val filepath = "path"
        val mockedFile = mock<File> {
            on { name } doReturn filename
            on { path } doReturn filepath
        }

        // when
        finboxService.addInboxDirectory(mockedFile)

        // then
        assertEquals(InboxDirectory.from(mockedFile), capturedModels.last())
        assertEquals(2, capturedFiles.size)
        assertEquals(2, capturedDirectories.size)
    }

    @Test
    fun testDeleteInboxFile() = testScope.runTest {
        // given
        val expected = DUMMY_INBOX_FILES - DUMMY_PERMANENT_FILE

        // when
        finboxService.deleteInboxFile(DUMMY_PERMANENT_FILE)

        // then
        assertEquals(DUMMY_PERMANENT_FILE, capturedModels.last())
        assertEquals(2, capturedFiles.size)
        assertEquals(2, capturedDirectories.size)
        assertEquals(expected, finboxRepository.getInboxFiles())
    }

    @Test
    fun testDeleteInboxDirectory() = testScope.runTest {
        // given
        val expected = DUMMY_INBOX_DIRECTORIES - DUMMY_INBOX_DIRECTORY

        // when
        finboxService.deleteInboxDirectory(DUMMY_INBOX_DIRECTORY)

        // then
        assertEquals(DUMMY_INBOX_DIRECTORY, capturedModels.last())
        assertEquals(2, capturedFiles.size)
        assertEquals(2, capturedDirectories.size)
        assertEquals(expected, finboxRepository.getInboxDirectories())
    }

    private companion object {
        val DUMMY_PERMANENT_FILE = InboxFile.Permanent(name = "file1", path = "path")
        val DUMMY_TEMPORARY_FILE = InboxFile.Temporary(name = "file2", path = "path")
        val DUMMY_INBOX_DIRECTORY = InboxDirectory(name = "dir", path = "/path/to/dir")
        val DUMMY_INBOX_FILES = listOf(DUMMY_PERMANENT_FILE, DUMMY_TEMPORARY_FILE)
        val DUMMY_INBOX_DIRECTORIES = listOf(DUMMY_INBOX_DIRECTORY)
    }
}
