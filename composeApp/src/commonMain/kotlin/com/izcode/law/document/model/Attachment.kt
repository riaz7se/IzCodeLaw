package com.izcode.law.document.model

data class Attachment(
    val fileName: String,
    val fileType: FileType,
    val fileSize: String
)

enum class FileType {
    PDF, WORD
} 