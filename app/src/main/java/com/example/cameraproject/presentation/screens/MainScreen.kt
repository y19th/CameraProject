package com.example.cameraproject.presentation.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.cameraproject.R
import com.example.cameraproject.domain.viewmodel.MainViewModel
import com.example.cameraproject.presentation.components.StandardText
import org.koin.androidx.compose.getViewModel

@Composable
fun MainScreen() {

    val viewModel: MainViewModel = getViewModel()

    var selectedItem by remember {
        mutableIntStateOf(0)
    }

    val selectedColor = MaterialTheme.colorScheme.primary
    val disabledColor = MaterialTheme.colorScheme.secondary


    Column {

        Text(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 24.dp),
            text = stringResource(id = R.string.my_house),
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.bodyLarge,
            color = MaterialTheme.colorScheme.onPrimary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier.fillMaxWidth()
        ) {
            StandardText(
                modifier = Modifier
                    .weight(0.5f)
                    .drawBehind {
                        drawLine(
                            color = if (selectedItem == 0) selectedColor else disabledColor,
                            strokeWidth = 8f,
                            start = Offset(0f, this.size.height + 8.dp.toPx()),
                            end = Offset(this.size.width, this.size.height + 8.dp.toPx())
                        )
                    }
                    .clickable {
                        selectedItem = 0
                    }
                    .clip(RoundedCornerShape(16.dp)),
                text = stringResource(id = R.string.cameras),
                textAlign = TextAlign.Center
            )
            StandardText(
                modifier = Modifier
                    .weight(0.5f)
                    .drawBehind {
                        drawLine(
                            color = if (selectedItem == 1) selectedColor else disabledColor,
                            strokeWidth = 8f,
                            start = Offset(0f, this.size.height + 8.dp.toPx()),
                            end = Offset(this.size.width, this.size.height + 8.dp.toPx())
                        )
                    }
                    .clickable {
                        selectedItem = 1
                    }
                    .clip(RoundedCornerShape(16.dp)),
                text = stringResource(id = R.string.doors),
                textAlign = TextAlign.Center
            )
        }
        when(selectedItem) {
            0 -> {
                CameraScreen(viewModel = viewModel)
            }
            1 -> {
                DoorScreen(viewModel = viewModel)
            }
            else -> {
                CameraScreen(viewModel = viewModel)
            }
        }
    }
}