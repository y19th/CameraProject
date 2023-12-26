package com.example.cameraproject.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cameraproject.R
import com.example.cameraproject.domain.events.MainEvents
import com.example.cameraproject.domain.models.DoorModel
import com.example.cameraproject.domain.viewmodel.MainViewModel
import com.example.cameraproject.presentation.components.EditSheet
import com.example.cameraproject.presentation.components.LazyRefresh
import com.example.cameraproject.ui.theme.StarYellow
import de.charlex.compose.RevealDirection
import de.charlex.compose.RevealSwipe
import de.charlex.compose.rememberRevealState
import de.charlex.compose.reset
import kotlinx.coroutines.launch

@Composable
fun DoorScreen(
    viewModel: MainViewModel
) {
    val state = viewModel.state.collectAsState()

    LazyRefresh(
        isRefreshing = state.value.isRefreshing,
        onRefresh = viewModel::refreshDoors
    ) {
        items(state.value.doorsList.size) { index ->
            DoorItem(
                item = state.value.doorsList[index],
                onFavourite = {
                    viewModel.onEvent(MainEvents.OnDoorFavouriteChange(
                        newData = state.value.doorsList[index].favorites.not(),
                        index = index
                    ))
                },
                onEdit = {
                    viewModel.onEvent(MainEvents.OnDoorNameChange(
                        newName = it,
                        index = index
                    ))
                }
            )
        }
    }
}

@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun DoorItem(
    item: DoorModel,
    onFavourite: () -> Unit,
    onEdit: (String) -> Unit
) {

    var showBottomSheet by remember { mutableStateOf(false) }

    val state = rememberRevealState()
    val scope = rememberCoroutineScope()

    if(showBottomSheet) {
        EditSheet(
            onConfirm = onEdit,
            onDismiss = {
                showBottomSheet = false
            }
        )
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        if(!item.room.isNullOrEmpty()) {
            Text(
                text = item.room,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
            Spacer(modifier = Modifier.height(16.dp))
        }
        RevealSwipe(
            modifier = Modifier,
            directions = setOf(RevealDirection.EndToStart),
            backgroundCardEndColor = MaterialTheme.colorScheme.background,
            state = state,
            hiddenContentEnd = {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Image(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable {
                                showBottomSheet = true
                                scope.launch {  state.reset() }
                            },
                        painter = painterResource(id = R.drawable.ic_edit_door),
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Image(
                        modifier = Modifier.clickable {
                            onFavourite.invoke()
                            scope.launch {  state.reset() }
                        },
                        painter = painterResource(id = R.drawable.ic_rounded_star),
                        contentDescription = null
                    )
                }
            },
            maxRevealDp = 90.dp
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = MaterialTheme.colorScheme.onBackground,
                        shape = RoundedCornerShape(16.dp)
                    )
            ) {
                Column {
                    AsyncImage(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)),
                        model = item.snapshot,
                        contentDescription = null,
                        contentScale = ContentScale.Crop
                    )
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .weight(0.6f),
                            text = item.name,
                            color = MaterialTheme.colorScheme.onSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Row(
                            modifier = Modifier.weight(0.4f),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.End
                        ) {
                            if (item.favorites && item.snapshot.isNullOrEmpty()) {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_favourite_cam),
                                    tint = StarYellow,
                                    contentDescription = null
                                )

                                Spacer(modifier = Modifier.width(8.dp))
                            }
                            Image(
                                painter = painterResource(id = R.drawable.ic_door_lock),
                                contentDescription = null
                            )
                        }
                    }
                }
                if (item.favorites && !item.snapshot.isNullOrEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp),
                        verticalAlignment = Alignment.Top,
                        horizontalArrangement = Arrangement.End
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.ic_favourite_cam),
                            tint = StarYellow,
                            contentDescription = null
                        )
                    }
                }
            }
        }
    }
}