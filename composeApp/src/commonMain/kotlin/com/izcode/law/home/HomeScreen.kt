import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.izcode.law.auth.UserProfile
import com.izcode.law.document.DetailDocument
import com.izcode.law.document.model.Document
import com.izcode.law.document.handler.AttachmentHandler
import com.izcode.law.home.ViewDocumentsTab

@Composable
fun HomeScreen(
    attachmentHandler: AttachmentHandler,
    userProfile: UserProfile,
    onSignOut: () -> Unit
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
                        IconButton(onClick = onSignOut) {
                            Icon(Icons.AutoMirrored.Default.ExitToApp, contentDescription = "Sign Out")
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
                // User info bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(MaterialTheme.colors.surface)
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = userProfile.displayName ?: "User",
                        style = MaterialTheme.typography.caption,
                        color = MaterialTheme.colors.onSurface.copy(alpha = 0.7f)
                    )
                }

                // Tab content
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