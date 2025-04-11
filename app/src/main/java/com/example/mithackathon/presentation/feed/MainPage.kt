package com.example.mithackathon.presentation.feed

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.QrCodeScanner
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.mithackathon.InternalFun.getUserInfoFromPrefs
import com.example.mithackathon.data.models.Event
import com.example.mithackathon.navigation.AppScreens

@Composable
fun MainFeedScreen(viewModel: FeedViewModel , navController: NavController) {
    val context = LocalContext.current
    val (_, _, userType) = getUserInfoFromPrefs(context)
    var selectedTab by remember { mutableIntStateOf(0) }

    LaunchedEffect(Unit) {
        viewModel
    }


    Scaffold(
        topBar = { TopAppBar() },
        bottomBar = {
            when (userType) {
                "Student" -> StudentBottomNavigation(selectedTab , navController)
                "Club" -> ClubBottomNavigation(selectedTab , navController)
                else -> {}
            }
        }
    ) { paddingValues ->

        EventsScreen(viewModel, paddingValues , navController)

    }
}

@Composable
fun StudentBottomNavigation(selectedTab: Int, navController: NavController) {
    NavigationBar(containerColor = Color(0xFFFFFFFF) , contentColor = Color(0xFF607D8B)) {
        NavigationBarItem(
            icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
            label = { Text("Home") },
            selected = selectedTab == 0,
            onClick = { navController.navigate(AppScreens.MainScreen.name) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Groups, contentDescription = "Clubs") },
            label = { Text("Clubs") },
            selected = selectedTab == 1,
            onClick = { navController.navigate(AppScreens.ClubsListScreen.name) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.QrCodeScanner, contentDescription = "Scan QR") },
            label = { Text("Attendance") },
            selected = selectedTab == 2,
            onClick = { navController.navigate(AppScreens.ScanQrScreen.name) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Default.Person, contentDescription = "Profile") },
            label = { Text("Profile") },
            selected = selectedTab == 3,
            onClick = { navController.navigate(AppScreens.UserProfileScreen.name) }
        )
    }
}

@Composable
fun ClubBottomNavigation(selectedTab: Int,navController: NavController) {
    NavigationBar(containerColor = Color(0xFFFFFFFF) , contentColor = Color(0xFF607D8B)) {
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Home, contentDescription = "Home") },
            label = { Text("Home" , fontSize = 12.sp , fontWeight = FontWeight.SemiBold) },
            selected = selectedTab == 0,
            onClick = { AppScreens.MainScreen.name }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Groups, contentDescription = "Clubs" , modifier = Modifier.size(28.dp)) },
            label = { Text("Clubs",fontSize = 12.sp , fontWeight = FontWeight.SemiBold) },
            selected = selectedTab == 1,
            onClick = { navController.navigate(AppScreens.ClubsListScreen.name) }
        )
        NavigationBarItem(
            icon = { Icon(Icons.Outlined.Person, contentDescription = "Profile") },
            label = { Text("Profile",fontSize = 12.sp , fontWeight = FontWeight.SemiBold) },
            selected = selectedTab == 3,
            onClick = { navController.navigate(AppScreens.ClubDashBoardScreen.name) }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar() {
    TopAppBar(
        modifier = Modifier.padding(horizontal = 20.dp),
        title = {
            Text(
                text = "VIIT Pune",
                fontWeight = FontWeight.Bold
            )
        },
        actions = {
            IconButton(onClick = { /* Handle notifications */ }) {
                Icon(
                    imageVector = Icons.Default.Notifications,
                    contentDescription = "Notifications"
                )
            }
        }
    )
}

@Composable
fun EventsScreen(viewModel: FeedViewModel, paddingValues: PaddingValues , navController: NavController) {
    val events = viewModel.events
    Column(
        modifier = Modifier
            .padding(paddingValues)
            .fillMaxSize()
            .background(color = Color(0xFFECEFF1))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Events",
                fontWeight = FontWeight.Bold,
                fontSize = 20.sp
            )

            IconButton(onClick = { /* Handle filter/menu */ }) {
                Icon(
                    imageVector = Icons.Default.Menu,
                    contentDescription = "Filter"
                )
            }
        }

        LazyColumn(
            Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            items(events) { event ->
                EventCard(event , navController)
            }
        }
    }
}

@Composable
fun EventCard(event: Event , navController: NavController) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            if (event.imageUrl.isNotEmpty()) {
                AsyncImage(
                    model = event.imageUrl,
                    contentDescription = "Event Poster",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp)
                        .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp))
                )
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text(
                    text = event.name,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = event.description,
                    fontSize = 14.sp,
                    color = Color.Gray,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )


                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = event.tag,
                        fontSize = 12.sp,
                        color = Color.Gray
                    )

                    Button(
                        onClick = { navController.navigate("${AppScreens.EventDetailsScreenU.name}/${event.id}") },
                        shape = RoundedCornerShape(50),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF1A237E)
                        ),
                        modifier = Modifier.height(36.dp)
                    ) {
                        Text(
                            text = "See details",
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }
    }
}

//@Preview(showBackground = true)
//@Composable
//fun VIITPuneAppPreview() {
//    MaterialTheme {
//        MainFeedScreen(viewModel())
//    }
//}