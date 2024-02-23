package com.nullist.finbox.domain

import java.io.File

data class InboxDirectory(
    val name: String,
    val path: String,
): Model() {
    companion object {
        fun from(file: File): InboxDirectory = InboxDirectory(file.name, file.path)
    }
}
