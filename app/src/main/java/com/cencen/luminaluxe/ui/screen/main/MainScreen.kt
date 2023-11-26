package com.cencen.luminaluxe.ui.screen.main

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cencen.luminaluxe.R
import com.cencen.luminaluxe.datainjection.Injection
import com.cencen.luminaluxe.model.OrderSkincare
import com.cencen.luminaluxe.ui.ViewModelFactory
import com.cencen.luminaluxe.ui.commonstate.UiState
import com.cencen.luminaluxe.ui.component.ItemSkincare
import com.cencen.luminaluxe.ui.theme.LuminaLuxeTheme


@Composable
fun MainScreen(
    viewModel: MainViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepositories())
    ),
    navToDetail: (Long) -> Unit,
    modifier: Modifier = Modifier,
) {
    val uiState by viewModel.uiState.collectAsState()

    var query by remember { mutableStateOf("") }

    when (val currentState = uiState) {
            is UiState.Loading -> viewModel.getAllSkincare()
            is UiState.Success -> {
                val orderSkincare = (currentState as UiState.Success<List<OrderSkincare>>).data

                Column (modifier = modifier ) {
                    SearchBar(
                        query = query,
                        onQueryChanged = {
                            query = it
                            viewModel.searchSkincare(it)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp)
                    )
                    MainEntries(
                        orderSkincare = orderSkincare,
                        navToDetail = navToDetail,
                        modifier = modifier)
                }
            }
            is UiState.Error -> { }
        }

}

@Composable
fun MainEntries(
    orderSkincare: List<OrderSkincare>,
    modifier: Modifier = Modifier,
    navToDetail: (Long) -> Unit,
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(160.dp),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
        ) {
        items(orderSkincare) { content ->
            ItemSkincare(
                photo = content.skincare.photo,
                title = content.skincare.title,
                price =content.skincare.price,
                modifier = Modifier.clickable { navToDetail(content.skincare.id) }
                )
        }
    }
}

@Composable
fun SearchBar(
    query: String,
    onQueryChanged: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Surface (
        color = Color.White,
        tonalElevation = 20.dp,
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
    ) {
        val isHintVisible by remember { mutableStateOf(query.isEmpty()) }

        Row (
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null,
                tint = Color.Black
            )
            Spacer(modifier = Modifier.width(8.dp))
            BasicTextField(
                value = query,
                onValueChange = {
                    onQueryChanged(it) },
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Search
                ),
                textStyle = MaterialTheme.typography.titleSmall.copy(color = Color.Black),
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.Transparent),
                )
            if (isHintVisible) {
                Text(
                    text = stringResource(id = R.string.cari),
                    color = Color.Gray,
                    modifier = modifier.padding(8.dp),
                    style = MaterialTheme.typography.titleSmall
                    )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SearchBarPreview() {
    LuminaLuxeTheme {
        SearchBar(query = "avoskin", onQueryChanged = {})
    }
}