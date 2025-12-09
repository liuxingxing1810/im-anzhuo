package com.aurora.wave.connections

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.aurora.wave.connections.ui.AlphabetIndex
import com.aurora.wave.connections.ui.ContactItem
import com.aurora.wave.connections.ui.FriendRequestItem
import com.aurora.wave.connections.ui.SectionHeader
import com.aurora.wave.connections.ui.rememberAlphabetLetters
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConnectionsRootScreen(
    padding: PaddingValues,
    onContactClick: (String) -> Unit = {},
    viewModel: ContactsViewModel = viewModel()
) {
    val state by viewModel.state.collectAsState()
    val scrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior()
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    
    Scaffold(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = padding.calculateBottomPadding())
            .nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = "Connections",
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    IconButton(onClick = { /* TODO: Search */ }) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = "Search"
                        )
                    }
                    IconButton(onClick = { /* TODO: Menu */ }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = "More"
                        )
                    }
                },
                scrollBehavior = scrollBehavior,
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background,
                    scrolledContainerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { /* TODO: Add contact */ },
                containerColor = MaterialTheme.colorScheme.primary
            ) {
                Icon(
                    imageVector = Icons.Default.PersonAdd,
                    contentDescription = "Add contact",
                    tint = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Tab bar
            TabRow(
                selectedTabIndex = state.selectedTab.ordinal,
                containerColor = MaterialTheme.colorScheme.background
            ) {
                Tab(
                    selected = state.selectedTab == ContactsTab.CONTACTS,
                    onClick = { viewModel.onTabSelected(ContactsTab.CONTACTS) },
                    text = { 
                        Text(
                            "Contacts",
                            fontWeight = if (state.selectedTab == ContactsTab.CONTACTS) FontWeight.Bold else FontWeight.Normal
                        )
                    }
                )
                Tab(
                    selected = state.selectedTab == ContactsTab.NEW_FRIENDS,
                    onClick = { viewModel.onTabSelected(ContactsTab.NEW_FRIENDS) },
                    text = {
                        BadgedBox(
                            badge = {
                                if (state.friendRequests.isNotEmpty()) {
                                    Badge {
                                        Text(state.friendRequests.size.toString())
                                    }
                                }
                            }
                        ) {
                            Text(
                                "New Friends",
                                fontWeight = if (state.selectedTab == ContactsTab.NEW_FRIENDS) FontWeight.Bold else FontWeight.Normal
                            )
                        }
                    }
                )
            }
            
            when {
                state.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                
                state.selectedTab == ContactsTab.CONTACTS -> {
                    ContactsTabContent(
                        groupedContacts = state.groupedContacts,
                        listState = listState,
                        onContactClick = onContactClick,
                        onLetterSelected = { letter ->
                            // Find index of section header
                            val keys = state.groupedContacts.keys.toList()
                            var index = 0
                            for (key in keys) {
                                if (key == letter) break
                                index += 1 + (state.groupedContacts[key]?.size ?: 0)
                            }
                            scope.launch {
                                listState.animateScrollToItem(index)
                            }
                        }
                    )
                }
                
                state.selectedTab == ContactsTab.NEW_FRIENDS -> {
                    NewFriendsTabContent(
                        friendRequests = state.friendRequests,
                        onAccept = viewModel::acceptFriendRequest,
                        onReject = viewModel::rejectFriendRequest
                    )
                }
            }
        }
    }
}

@Composable
private fun ContactsTabContent(
    groupedContacts: Map<Char, List<com.aurora.wave.connections.model.ContactUiModel>>,
    listState: androidx.compose.foundation.lazy.LazyListState,
    onContactClick: (String) -> Unit,
    onLetterSelected: (Char) -> Unit
) {
    val letters = rememberAlphabetLetters(groupedContacts)
    
    if (groupedContacts.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "👥",
                    style = MaterialTheme.typography.displayLarge
                )
                Text(
                    text = "No contacts yet",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Add friends to start chatting",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    } else {
        Row(modifier = Modifier.fillMaxSize()) {
            LazyColumn(
                state = listState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 12.dp,
                    end = 4.dp,
                    top = 8.dp,
                    bottom = 80.dp
                ),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                groupedContacts.forEach { (letter, contacts) ->
                    item(key = "header_$letter") {
                        SectionHeader(letter = letter)
                    }
                    
                    items(
                        items = contacts,
                        key = { it.id }
                    ) { contact ->
                        ContactItem(
                            contact = contact,
                            onClick = { onContactClick(contact.id) }
                        )
                    }
                }
            }
            
            // Alphabet index
            AlphabetIndex(
                letters = letters,
                onLetterSelected = onLetterSelected,
                modifier = Modifier.padding(end = 4.dp, top = 8.dp, bottom = 80.dp)
            )
        }
    }
}

@Composable
private fun NewFriendsTabContent(
    friendRequests: List<com.aurora.wave.connections.model.FriendRequestUiModel>,
    onAccept: (String) -> Unit,
    onReject: (String) -> Unit
) {
    if (friendRequests.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "🤝",
                    style = MaterialTheme.typography.displayLarge
                )
                Text(
                    text = "No pending requests",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Text(
                    text = "Friend requests will appear here",
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.outline
                )
            }
        }
    } else {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(
                horizontal = 12.dp,
                vertical = 12.dp
            ),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(
                items = friendRequests,
                key = { it.id }
            ) { request ->
                FriendRequestItem(
                    request = request,
                    onAccept = { onAccept(request.id) },
                    onReject = { onReject(request.id) }
                )
            }
        }
    }
}
