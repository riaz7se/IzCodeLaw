import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.izcode.law.document.DetailDocument
import com.izcode.law.document.model.Document
import com.izcode.law.document.handler.AttachmentHandler
import com.izcode.law.home.ViewDocumentsTab

@Composable
fun HomeScreen(
    attachmentHandler: AttachmentHandler
) {
    var selectedTabIndex by remember { mutableStateOf(0) }
    var selectedDocument by remember { mutableStateOf<Document?>(null) }

    if (selectedDocument != null) {
        DetailDocument(
            document = selectedDocument!!,
            onBackPress = { selectedDocument = null },
            attachmentHandler = attachmentHandler
        )
    } else {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text("IzCode Law") },
                    navigationIcon = {
                        IconButton(onClick = { /* Handle menu click */ }) {
                            Icon(Icons.Default.Menu, contentDescription = "Menu")
                        }
                    },
                    actions = {
                        IconButton(onClick = { /* Handle settings click */ }) {
                            Icon(Icons.Default.Settings, contentDescription = "Settings")
                        }
                    }
                )
            }
        ) { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                TabRow(selectedTabIndex = selectedTabIndex) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = { Text("Create Document") }
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = { Text("View Documents") }
                    )
                }

                when (selectedTabIndex) {
                    0 -> CreateDocumentTab()
                    1 -> ViewDocumentsTab(
                        onDocumentClick = { document ->
                            selectedDocument = document
                        }
                    )
                }
            }
        }
    }
} 