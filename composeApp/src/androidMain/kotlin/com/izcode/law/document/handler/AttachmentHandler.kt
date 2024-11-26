package com.izcode.law.document.handler

import android.app.DownloadManager
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Environment
import android.webkit.MimeTypeMap
import androidx.activity.result.ActivityResultLauncher
import androidx.core.content.FileProvider
import co.touchlab.kermit.Logger
import com.izcode.law.document.model.Attachment
import com.izcode.law.document.model.FileType
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import java.io.File

actual class AttachmentHandler(
    private val context: Context,
    private val filePickerLauncher: ActivityResultLauncher<String>? = null
) {
    companion object {
        private const val TAG = "AttachmentHandler"
    }
    
    actual suspend fun downloadFile(attachment: Attachment): Flow<AttachmentResult> = flow {
        try {
            Logger.i( "Starting download for file: ${attachment.fileName}")
            emit(AttachmentResult.Progress(0f))
            
            val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            val mimeType = getMimeType(attachment.fileType)
            Logger.d( "MimeType for ${attachment.fileName}: $mimeType")
            
            // In real app, get actual file URL from your backend
            val fileUrl = "https://your-api.com/files/${attachment.fileName}"
            
            val request = DownloadManager.Request(Uri.parse(fileUrl)).apply {
                setTitle(attachment.fileName)
                setDescription("Downloading ${attachment.fileName}")
                setMimeType(mimeType)
                setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                setDestinationInExternalPublicDir(
                    Environment.DIRECTORY_DOWNLOADS,
                    "IzCodeLaw/${attachment.fileName}"
                )
            }
            
            val downloadId = downloadManager.enqueue(request)
            Logger.d( "Download enqueued with ID: $downloadId")
            
            monitorDownload(downloadManager, downloadId) { result ->
                when (result) {
                    is AttachmentResult.Success -> {
                        Logger.i( "Download completed: ${attachment.fileName}")
                    }
                    is AttachmentResult.Error -> {
                        Logger.e( "Download failed: ${result.message}")
                    }
                    is AttachmentResult.Progress -> {
                        Logger.d( "Download progress: ${result.progress * 100}%")
                    }
                }
                emit(result)
            }
            
        } catch (e: Exception) {
            Logger.e( "Download failed with exception", e)
            emit(AttachmentResult.Error("Download failed: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)

    actual suspend fun previewFile(attachment: Attachment): AttachmentResult {
        return try {
            Logger.d( "Attempting to preview file: ${attachment.fileName}")
            
            val file = File(
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                "IzCodeLaw/${attachment.fileName}"
            )
            
            if (!file.exists()) {
                Logger.e( "File not found: ${file.absolutePath}")
                return AttachmentResult.Error("File not found")
            }
            
            Logger.d( "File exists at: ${file.absolutePath}")
            
            val uri = FileProvider.getUriForFile(
                context,
                "${context.packageName}.fileprovider",
                file
            )
            
            val intent = Intent(Intent.ACTION_VIEW).apply {
                setDataAndType(uri, getMimeType(attachment.fileType))
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
            }
            
            // Verify there's an app that can handle this file type
            if (intent.resolveActivity(context.packageManager) != null) {
                context.startActivity(
                    Intent.createChooser(intent, "Open ${attachment.fileName}")
                )
                AttachmentResult.Success("Opening file")
            } else {
                AttachmentResult.Error("No app found to open this file type")
            }
        } catch (e: Exception) {
            Logger.e( "Preview failed", e)
            AttachmentResult.Error("Cannot open file: ${e.message}")
        }
    }

    actual suspend fun uploadFile(): Flow<AttachmentResult> = flow {
        try {
            Logger.i( "Starting file upload process")
            emit(AttachmentResult.Progress(0f))
            
            if (filePickerLauncher == null) {
                Logger.e( "File picker not initialized")
                emit(AttachmentResult.Error("File picker not initialized"))
                return@flow
            }
            
            // Launch file picker
            Logger.d( "Launching file picker")
            filePickerLauncher.launch("*/*")
            
            // Simulated upload progress
            repeat(10) {
                val progress = (it + 1) / 10f
                Logger.d( "Upload progress: ${progress * 100}%")
                kotlinx.coroutines.delay(500)
                emit(AttachmentResult.Progress(progress))
            }
            
            Logger.d( "Upload completed successfully")
            emit(AttachmentResult.Success("File uploaded successfully"))
        } catch (e: Exception) {
            Logger.e( "Upload failed", e)
            emit(AttachmentResult.Error("Upload failed: ${e.message}"))
        }
    }.flowOn(Dispatchers.IO)

    private fun getMimeType(fileType: FileType): String = when (fileType) {
        FileType.PDF -> "application/pdf"
        FileType.WORD -> "application/msword"
    }

    private suspend fun monitorDownload(
        downloadManager: DownloadManager,
        downloadId: Long,
        progressCallback: suspend (AttachmentResult) -> Unit
    ) {
        var downloading = true
        while (downloading) {
            val query = DownloadManager.Query().setFilterById(downloadId)
            downloadManager.query(query).use { cursor ->
                if (cursor.moveToFirst()) {
                    // Safe column index retrieval
                    val statusIndex = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_STATUS)
                    val status = cursor.getInt(statusIndex)
                    
                    when (status) {
                        DownloadManager.STATUS_SUCCESSFUL -> {
                            downloading = false
                            progressCallback(AttachmentResult.Success("File downloaded successfully"))
                        }
                        DownloadManager.STATUS_FAILED -> {
                            downloading = false
                            val reasonIndex = cursor.getColumnIndexOrThrow(DownloadManager.COLUMN_REASON)
                            val reason = cursor.getInt(reasonIndex)
                            progressCallback(AttachmentResult.Error("Download failed: Error code $reason"))
                        }
                        DownloadManager.STATUS_RUNNING -> {
                            val downloadedIndex = cursor.getColumnIndexOrThrow(
                                DownloadManager.COLUMN_BYTES_DOWNLOADED_SO_FAR
                            )
                            val totalIndex = cursor.getColumnIndexOrThrow(
                                DownloadManager.COLUMN_TOTAL_SIZE_BYTES
                            )
                            
                            val bytesDownloaded = cursor.getLong(downloadedIndex)
                            val bytesTotal = cursor.getLong(totalIndex)
                            
                            if (bytesTotal > 0) {
                                val progress = bytesDownloaded.toFloat() / bytesTotal.toFloat()
                                progressCallback(AttachmentResult.Progress(progress))
                            }
                        }
                    }
                }
            }
            kotlinx.coroutines.delay(100)
        }
    }
} 