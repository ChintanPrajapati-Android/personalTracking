package com.example.personaltracking.presentation.list

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.personaltracking.domain.model.DayWiseLocations
import com.example.personaltracking.R
import com.example.personaltracking.presentation.navigation.LocationListActions
import com.example.personaltracking.ui.theme.PersonalTrackingTheme

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LocationListScreen(
    viewModel: LocationListViewModel = hiltViewModel(),
    onAction: (LocationListActions) -> Unit
) {
    val state by viewModel.state.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.actions.collect { action ->
            onAction(action)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Saved Locations") },
                navigationIcon = {
                    IconButton(onClick = viewModel::onBackClick) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Column(Modifier.padding(padding)) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(state.daysWiselocations, key = { it.date.orEmpty() }) { dayWise ->
                    LocationItem(dayWise)
                }
            }
        }
    }
}

@Composable
fun LocationItem(dayWiseLocations: DayWiseLocations) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp, 8.dp)
            .background(Color.White),
        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.elevatedCardColors(
            containerColor = colorResource(id = R.color.white)
        )
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = dayWiseLocations.date.orEmpty(),
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
                Image(
                    painter = painterResource(R.drawable.baseline_play_circle_24),
                    contentScale = ContentScale.Fit,
                    contentDescription = null,
                    modifier = Modifier
                        .padding( end = 8.dp),
                )
            }
            Spacer(Modifier.height(8.dp))
            Row {
                Image(
                    painter = painterResource(R.drawable.outline_circle_24),
                    contentScale = ContentScale.Fit,
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp)
                        .padding(top = 6.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Source  : ${dayWiseLocations.source}")
            }

            Row {
                Image(
                    painter = painterResource(R.drawable.outline_circle_24),
                    contentScale = ContentScale.Fit,
                    contentDescription = null,
                    modifier = Modifier
                        .size(16.dp)
                        .padding(top = 6.dp)
                )
                Spacer(Modifier.width(8.dp))
                Text("Destination  : ${dayWiseLocations.destination}")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LocationItemPreview() {
    PersonalTrackingTheme {
        LocationItem(
            DayWiseLocations(
                date = "2023-10-01",
                source = "New York",
                destination = "Los Angeles"
            )
        )
    }
}




