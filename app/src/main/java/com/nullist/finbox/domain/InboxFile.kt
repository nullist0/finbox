package com.nullist.finbox.domain

import java.io.File

abstract class InboxFile: Model() {
    abstract val name: String
    abstract val path: String

    data class Temporary(
        override val name: String,
        override val path: String,
    ) : InboxFile() {
        fun toPermanent(): Permanent = Permanent(
            name = name,
            path = path,
        )

        companion object {
            fun from(file: File): Temporary = Temporary(file.name, file.path)
        }
    }

    data class Permanent(
        override val name: String,
        override val path: String,
    ) : InboxFile() {
        fun toTemporary(): Temporary = Temporary(
            name = name,
            path = path,
        )

        companion object {
            fun from(file: File): Temporary = Temporary(file.name, file.path)
        }
    }

    companion object {
        fun from(file: File) : InboxFile = Temporary.from(file)
    }
}
