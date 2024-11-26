package com.izcode.law.document

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Lock
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.izcode.law.document.model.Attachment
import com.izcode.law.document.model.Document
import com.izcode.law.document.model.FileType
import com.izcode.law.document.handler.AttachmentHandler
import kotlinx.coroutines.launch

@Composable
fun DetailDocument(
    document: Document,
    onBackPress: () -> Unit,
    attachmentHandler: AttachmentHandler
) {
    val scope = rememberCoroutineScope()
    var showUploadDialog by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Document Details") },
                navigationIcon = {
                    IconButton(onClick = onBackPress) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                CollapsibleSection(
                    title = "Basic Information",
                    initiallyExpanded = true
                ) {
                    Column {
                        DetailField("Document Title", document.title)
                        DetailField("Client Name", document.clientName)
                        DetailField("Location", document.location)
                    }
                }
            }

            item {
                CollapsibleSection(
                    title = "Hearing Dates"
                ) {
                    Column {
                        document.lastHearingDate?.let { DetailField("Last Hearing", it) }
                        document.nextHearingDate?.let { DetailField("Next Hearing", it) }
                    }
                }
            }

            item {
                CollapsibleSection(
                    title = "Hearing History"
                ) {
                    Column {
                        document.hearingHistory?.forEach { date ->
                            Text(
                                text = "• $date",
                                style = MaterialTheme.typography.body1,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            item {
                CollapsibleSection(
                    title = "Jury Members"
                ) {
                    Column {
                        document.juryNames?.forEach { name ->
                            Text(
                                text = "• $name",
                                style = MaterialTheme.typography.body1,
                                modifier = Modifier.padding(vertical = 4.dp)
                            )
                        }
                    }
                }
            }

            item {
                CollapsibleSection(
                    title = "Attachments"
                ) {
                    Column {
                        document.attachments?.forEach { attachment ->
                            AttachmentItem(attachment, attachmentHandler)
                        }
                    }
                }
            }
        }

        if (showUploadDialog) {
            UploadDialog(
                onDismiss = { showUploadDialog = false },
                onUpload = {
                    scope.launch {
                        attachmentHandler.uploadFile()?.let { newAttachment ->
                            // Handle the new attachment
                            showUploadDialog = false
                        }
                    }
                }
            )
        }
    }
}

@Composable
private fun CollapsibleSection(
    title: String,
    initiallyExpanded: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    var isExpanded by remember { mutableStateOf(initiallyExpanded) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = 4.dp,
        backgroundColor = MaterialTheme.colors.surface
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { isExpanded = !isExpanded },
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.h6,
                    color = MaterialTheme.colors.primary,
                    fontWeight = FontWeight.Bold
                )
                IconButton(onClick = { isExpanded = !isExpanded }) {
                    Icon(
                        imageVector = if (isExpanded) 
                            Icons.Default.KeyboardArrowUp 
                        else 
                            Icons.Default.KeyboardArrowDown,
                        contentDescription = if (isExpanded) "Collapse" else "Expand"
                    )
                }
            }
            if (isExpanded) {
                Spacer(modifier = Modifier.height(8.dp))
                Column {
                    content()
                }
            }
        }
    }
}

@Composable
private fun AttachmentItem(
    attachment: Attachment,
    attachmentHandler: AttachmentHandler
) {
    val scope = rememberCoroutineScope()
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        elevation = 2.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = when (attachment.fileType) {
                        FileType.PDF -> Icons.Filled.Lock
                        FileType.WORD -> Icons.Filled.Email
                    },
                    contentDescription = "File type",
                    tint = MaterialTheme.colors.primary
                )
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(
                        text = attachment.fileName,
                        style = MaterialTheme.typography.body1
                    )
                    Text(
                        text = attachment.fileSize,
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                    )
                }
            }
            
            Row {
                IconButton(
                    onClick = { 
                        scope.launch {
                            attachmentHandler.previewFile(attachment)
                        }
                    }
                ) {
                    Icon(
                        Icons.Filled.Favorite,
                        contentDescription = "Preview",
                        tint = MaterialTheme.colors.primary
                    )
                }
                
                IconButton(
                    onClick = { 
                        scope.launch {
                            attachmentHandler.downloadFile(attachment)
                        }
                    }
                ) {
                    Icon(
                        Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Download",
                        tint = MaterialTheme.colors.primary
                    )
                }
            }
        }
    }
}

@Composable
private fun DetailField(
    label: String,
    value: String
) {
    Column(
        modifier = Modifier.padding(vertical = 4.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.caption,
            color = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
        )
        Text(
            text = value,
            style = MaterialTheme.typography.body1,
            color = MaterialTheme.colors.onSurface
        )
    }
}

@Composable
private fun UploadDialog(
    onDismiss: () -> Unit,
    onUpload: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Upload Document") },
        text = { Text("Select a file to upload") },
        confirmButton = {
            TextButton(onClick = onUpload) {
                Text("Upload")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
} 