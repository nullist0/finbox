package com.nullist.finbox.domain

data class InboxDirectory(
    val name: String,
    val files: List<InboxFile>,
)
