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
import androidx.compose.material3.Button
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cameraproject.R
import com.example.cameraproject.domain.events.MainEvents
import com.example.cameraproject.domain.models.DoorModel
import com.example.cameraproject.domain.viewmodel.MainViewModel
import com.example.cameraproject.presentation.components.LazyRefresh
import com.example.cameraproject.presentation.components.StandardText
import com.example.cameraproject.ui.theme.StarYellow
import de.charlex.compose.RevealDirection
import de.charlex.compose.RevealSwipe

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

                }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun EditSheet(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val sheetState = rememberModalBottomSheetState()
    var itemName by remember {
        mutableStateOf("")
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            OutlinedTextField(
                modifier = Modifier.fillMaxWidth(),
                value = itemName,
                onValueChange = {
                    itemName = it
                }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(color = MaterialTheme.colorScheme.primary),
                onClick = { onConfirm.invoke(itemName) }
            ) {
                StandardText(
                    text = stringResource(id = R.string.done),
                    textAlign = TextAlign.Center
                )
            }
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
            hiddenContentEnd = {
                Row (
                    verticalAlignment = Alignment.CenterVertically
                ){
                    Image(
                        modifier = Modifier
                            .padding(start = 16.dp)
                            .clickable { showBottomSheet = true },
                        painter = painterResource(id = R.drawable.ic_edit_door),
                        contentDescription = null
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Image(
                        modifier = Modifier.clickable { onFavourite.invoke() },
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