package com.izcode.law.document.handler

import com.izcode.law.document.model.Attachment
import kotlinx.coroutines.flow.Flow

sealed class AttachmentResult {
    data class Success(val message: String) : AttachmentResult()
    data class Error(val message: String) : AttachmentResult()
    data class Progress(val progress: Float) : AttachmentResult()
}

expect class AttachmentHandler {
    suspend fun downloadFile(attachment: Attachment): Flow<AttachmentResult>
    suspend fun previewFile(attachment: Attachment): AttachmentResult
    suspend fun uploadFile(): Flow<AttachmentResult>
} 