package com.cencen.luminaluxe.ui.screen.cart

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cencen.luminaluxe.R
import com.cencen.luminaluxe.datainjection.Injection
import com.cencen.luminaluxe.ui.ViewModelFactory
import com.cencen.luminaluxe.ui.commonstate.UiState
import com.cencen.luminaluxe.ui.component.ButtonOrder
import com.cencen.luminaluxe.ui.component.ItemsCart

@Composable
fun CartScreen(
    viewModel: CartViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.provideRepositories()
        )
    ),
    onOrderBtnClicked: (String) -> Unit,
) {
    viewModel.uiState.collectAsState(
        initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getAddedOrderSkincare()
            }
            is UiState.Success -> {
                CartEntries(
                    states = uiState.data,
                    onItemCountChanged = { skincareId, count ->
                        viewModel.updateOrderSkincare(skincareId, count)
                    } , onOrderBtnClicked = onOrderBtnClicked)
            }
            is UiState.Error -> {}
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CartEntries(
    states: CartState,
    onItemCountChanged: (id: Long, count: Int) -> Unit,
    onOrderBtnClicked: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val shareOrderMessage = stringResource(id = R.string.share_buy_message)

    Column(
        modifier = modifier.fillMaxSize()
    ) {
        CenterAlignedTopAppBar(
            title = {
                Text(
                    text = stringResource(id = R.string.cart),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    textAlign = TextAlign.Center
                )
            }
        )
        LazyColumn(
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier
                .weight(weight = 1f)
        ) {
            items(states.orderSkincare, key = { it.skincare.id}) { item ->
                ItemsCart(
                    skincareId = item.skincare.id,
                    photo = item.skincare.photo,
                    title = item.skincare.title,
                    totalPrice = item.skincare.price * item.count,
                    count = item.count,
                    onItemCountChanged = onItemCountChanged,
                )
                Divider()
            }
        }
        ButtonOrder(
            text = stringResource(id = R.string.total_ordered, states.totalPrice),
            enabled = states.orderSkincare.isNotEmpty(),
            onClick = {
                onOrderBtnClicked(shareOrderMessage)
            },
            modifier = Modifier.padding(16.dp)
            )
    }
}

