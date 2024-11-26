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
    onDocumentClick: (Document) -> Unit
) {
    var searchQuery by remember { mutableStateOf("") }
    
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Documents",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Search TextField
        OutlinedTextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            modifier = Modifier.fillMaxWidth(),
            placeholder = { Text("Search documents...") },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Search") },
            singleLine = true
        )
        
        Spacer(modifier = Modifier.height(16.dp))
        
        // Filtered Documents List
        val filteredDocuments = DocumentRepository.documents.filter { document ->
            document.title.contains(searchQuery, ignoreCase = true) ||
            document.clientName.contains(searchQuery, ignoreCase = true)
        }
        
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(filteredDocuments) { document ->
                DocumentItem(
                    document = document,
                    onClick = { onDocumentClick(document) }
                )
            }
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