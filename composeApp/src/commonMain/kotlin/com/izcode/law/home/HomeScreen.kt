import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.rounded.List
import androidx.compose.material.icons.automirrored.rounded.Send
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.izcode.law.auth.UserProfile
import com.izcode.law.auth.state.UserState
import com.izcode.law.document.DetailDocument
import com.izcode.law.document.model.Document
import com.izcode.law.document.handler.AttachmentHandler
import com.izcode.law.home.ViewDocumentsTab
import com.izcode.law.profile.UserProfileScreen

@Composable
fun HomeScreen(
    attachmentHandler: AttachmentHandler,
    userProfile: UserProfile,
    onSignOut: () -> Unit,
    onProfileClick: () -> Unit
) {
    val currentUser by UserState.user.collectAsState()
    var selectedTabIndex by remember { mutableStateOf(0) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchFocused by remember { mutableStateOf(false) }
    var recentSearches by remember { mutableStateOf(listOf<String>()) }
    var selectedDocument by remember { mutableStateOf<Document?>(null) }
    var showProfile by remember { mutableStateOf(false) }

    if (selectedDocument != null) {
        DetailDocument(
            document = selectedDocument!!,
            onBackPress = { selectedDocument = null },
            attachmentHandler = attachmentHandler
        )
    } else {
        Scaffold(
            topBar = {
                Column {
                    // Top bar with search and profile
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Search Bar
                        OutlinedTextField(
                            value = searchQuery,
                            onValueChange = { searchQuery = it },
                            modifier = Modifier
                                .weight(1f)
                                .padding(end = 8.dp),
                            placeholder = { Text("Search documents...") },
                            leadingIcon = { 
                                Icon(
                                    Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = MaterialTheme.colors.primary
                                )
                            },
                            singleLine = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = MaterialTheme.colors.primary,
                                unfocusedBorderColor = MaterialTheme.colors.onSurface.copy(alpha = 0.12f)
                            )
                        )

                        // Profile Icon

                        IconButton(onClick = onProfileClick) {
                            Icon(
                                Icons.Default.Person,
                                contentDescription = "Profile",
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape),
                                tint = MaterialTheme.colors.primary
                            )
                        }
                    }

                    // Recent searches
                    if (isSearchFocused && recentSearches.isNotEmpty()) {
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 8.dp),
                            elevation = 4.dp
                        ) {
                            Column {
                                recentSearches.forEach { search ->
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable { 
                                                searchQuery = search
                                                isSearchFocused = false 
                                            }
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Icon(
                                            Icons.Filled.MoreVert,
                                            contentDescription = "Recent",
                                            tint = MaterialTheme.colors.onSurface.copy(alpha = 0.6f)
                                        )
                                        Spacer(modifier = Modifier.width(16.dp))
                                        Text(search)
                                    }
                                }
                            }
                        }
                    }
                }
            },
            bottomBar = {
                BottomNavigation(
                    backgroundColor = Color(0xFF9C27B0),//MaterialTheme.colors.primary,
                    contentColor = Color.White
                ) {
                    BottomNavigationItem(
                        icon = { Icon(Icons.AutoMirrored.Rounded.Send, "Create Document") },
                        label = { Text("Create") },
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.White.copy(alpha = 0.6f)
                    )

                    BottomNavigationItem(
                        icon = { Icon(Icons.AutoMirrored.Rounded.List, "View Documents") },
                        label = { Text("Documents") },
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.White.copy(alpha = 0.6f)
                    )

                    BottomNavigationItem(
                        icon = { Icon(Icons.Filled.MoreVert, "Calender") },
                        label = { Text("Calender") },
                        selected = selectedTabIndex == 2,
                        onClick = { selectedTabIndex = 2 },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.White.copy(alpha = 0.6f)
                    )

                    BottomNavigationItem(
                        icon = { Icon(Icons.Filled.MoreVert, "Others") },
                        label = { Text("Others") },
                        selected = selectedTabIndex == 3,
                        onClick = { selectedTabIndex = 3 },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.White.copy(alpha = 0.6f)
                    )

                    BottomNavigationItem(
                        icon = { Icon(Icons.Default.Person, "User Profile") },
                        label = { Text("User") },
                        selected = selectedTabIndex == 4,
                        onClick = { selectedTabIndex = 4 },
                        selectedContentColor = Color.White,
                        unselectedContentColor = Color.White.copy(alpha = 0.6f)
                    )

                }
            }
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                when (selectedTabIndex) {
                    0 -> CreateDocumentTab()
                    1 -> ViewDocumentsTab(
                        searchQuery,
                        onDocumentClick = { document ->
                            selectedDocument = document
                        }
                    )
                    2 -> OthersTab()
                    3 -> currentUser?.let {
                        UserProfileScreen(
                            userProfile = it,
                            onBackPress = { showProfile = false }
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun OthersTab() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        Text(
            text = "Others",
            style = MaterialTheme.typography.h6
        )
        // Add other content here
    }
}