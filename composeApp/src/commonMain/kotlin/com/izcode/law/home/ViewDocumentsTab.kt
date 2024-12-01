package com.izcode.law.home

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.izcode.law.document.model.Document
import com.izcode.law.home.document.repository.DocumentRepository

@Composable
fun ViewDocumentsTab(
    searchQuery: String,
    onDocumentClick: (Document) -> Unit) {
    val documents = DocumentRepository.documents
    val filteredDocuments = remember(searchQuery, documents) {
        if (searchQuery.isBlank()) {
            documents
        } else {
            documents.filter { doc ->
                doc.title.contains(searchQuery, ignoreCase = true) ||
                doc.clientName.contains(searchQuery, ignoreCase = true)
            }
        }
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        items(filteredDocuments) { document ->
            DocumentItem(
                document,
                onClick = { onDocumentClick(document) }
            )
        }
    }
}

@Composable
private fun DocumentItem(
    document: Document,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = 4.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = document.title,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold
            )
            
            Spacer(modifier = Modifier.height(8.dp))
            
            Text(
                text = "Client: ${document.clientName}",
                fontSize = 14.sp,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
            
            Spacer(modifier = Modifier.height(4.dp))
            
            Text(
                text = "Hearing Date: ${document.nextHearingDate}",
                fontSize = 14.sp,
                color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
            )
        }
    }
} 