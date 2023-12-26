package com.example.cameraproject.presentation.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.cameraproject.R
import com.example.cameraproject.domain.events.MainEvents
import com.example.cameraproject.domain.models.CameraModel
import com.example.cameraproject.domain.viewmodel.MainViewModel
import com.example.cameraproject.presentation.components.LazyRefresh
import com.example.cameraproject.ui.theme.StarYellow
import de.charlex.compose.RevealDirection
import de.charlex.compose.RevealSwipe

@Composable
fun CameraScreen(viewModel: MainViewModel) {

    val state = viewModel.state.collectAsState()

    LazyRefresh(
        isRefreshing = state.value.isRefreshing,
        onRefresh = viewModel::refreshCameras
    ) {
        items(state.value.cameraList.size) { index ->
            CameraItem(item = state.value.cameraList[index]) {
                viewModel.onEvent(
                    MainEvents.OnCameraFavouriteChange(
                        newData = state.value.cameraList[index].favorites.not(),
                        index = index
                    )
                )
            }
        }
    }
}


@OptIn(ExperimentalMaterialApi::class)
@Composable
private fun CameraItem(
    item: CameraModel,
    onSwipe: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)

    ) {
        if (!item.room.isNullOrEmpty()) {
            Text(
                text = item.room,
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


        RevealSwipe(
            modifier = Modifier,
            directions = setOf(RevealDirection.EndToStart),
            backgroundCardEndColor = MaterialTheme.colorScheme.background,
            hiddenContentEnd = {
                Image(
                    modifier = Modifier.padding(start = 25.dp, end = 9.dp),
                    painter = painterResource(id = R.drawable.ic_rounded_star),
                    contentDescription = null
                )
            },
            onBackgroundEndClick = onSwipe
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
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            modifier = Modifier
                                .padding(24.dp)
                                .weight(0.8f),
                            text = item.name,
                            color = MaterialTheme.colorScheme.onSecondary,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        if (item.rec) {
                            Icon(
                                modifier = Modifier.weight(0.2f),
                                painter = painterResource(id = R.drawable.ic_cam_guard),
                                contentDescription = null,
                                tint = Color(0xFFB6BABF)
                            )
                        }
                    }
                }
                if (item.favorites && item.snapshot.isNotEmpty()) {
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