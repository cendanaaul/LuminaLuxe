package com.cencen.luminaluxe.ui.screen.details

import androidx.annotation.DrawableRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Devices
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.cencen.luminaluxe.R
import com.cencen.luminaluxe.datainjection.Injection
import com.cencen.luminaluxe.ui.ViewModelFactory
import com.cencen.luminaluxe.ui.commonstate.UiState
import com.cencen.luminaluxe.ui.component.ButtonOrder
import com.cencen.luminaluxe.ui.component.CounterItems
import com.cencen.luminaluxe.ui.theme.LuminaLuxeTheme

@Composable
fun DetailsScreen(
    skincareId: Long,
    viewModel: DetailSkincareViewModel = viewModel(
        factory = ViewModelFactory(
            Injection.provideRepositories()
        )
    ),
    navToBack: () -> Unit,
    navToCart: () -> Unit,
) {
    viewModel.uiState.collectAsState(
        initial = UiState.Loading
    ).value.let { state ->
        when (state) {
            is UiState.Loading -> viewModel.getSkincareById(skincareId)
            is UiState.Success -> {
                val content = state.data
                DetailEntries(
                    photo = content.skincare.photo,
                    title = content.skincare.title,
                    price = content.skincare.price,
                    count = content.count,
                    description = content.skincare.description,
                    onClickBack = { navToBack },
                    onAddCart = { counter ->
                        viewModel.addSkincareToCart(content.skincare, counter)
                        navToCart()
                    }
                )
            }
            is UiState.Error -> { }
        }
    }
}

@Composable
fun DetailEntries(
    @DrawableRes photo: Int,
    title: String,
    price: Int,
    count: Int,
    description: String,
    onClickBack: () -> Unit,
    onAddCart: (count: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    var totalPrice by rememberSaveable { mutableStateOf(0) }
    var orderCounter by rememberSaveable { mutableStateOf(count) }

    Column (modifier = modifier) {
        Column(
            modifier = Modifier
                .verticalScroll(rememberScrollState())
                .weight(1f)
        ) {
            Box {
                Image(
                    painter = painterResource(id = photo),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = modifier
                        .height(400.dp)
                        .fillMaxWidth()
                )
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = stringResource(id = R.string.arrow_back),
                    modifier = Modifier
                        .padding(16.dp)
                        .clickable { onClickBack }
                    )
            }
            Column (
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = title,
                    textAlign = TextAlign.Center,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 22.sp
                    ),
                    modifier = Modifier.padding(vertical = 8.dp)
                    )
                Text(
                    text = stringResource(id = R.string.price, price),
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    ),
                    color = MaterialTheme.colorScheme.secondary,
                    modifier = Modifier.padding(vertical = 8.dp)
                    )
                Text(
                    text = description,
                    textAlign = TextAlign.Justify,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(8.dp)
                )
                Text(
                    text = stringResource(id = R.string.order_description),
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Justify,
                    modifier = Modifier.padding(8.dp)
                    )
            }
        }
        Spacer(modifier = Modifier
            .fillMaxWidth()
            .height(4.dp)
            .background(Color.LightGray))
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            CounterItems(
                orderId = 1,
                orderCounter = orderCounter,
                onItemIncreased = { orderCounter++ },
                onItemDecreased = { if (orderCounter>0) orderCounter--},
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(bottom = 16.dp)
                )
            totalPrice = (price * orderCounter).toInt()
            ButtonOrder(
                text = stringResource(id = R.string.add_cart, totalPrice),
                enabled = orderCounter > 0,
                onClick = { onAddCart(orderCounter) }
                )
        }
    }

}

@Preview(showBackground = true, device = Devices.PIXEL_4)
@Composable
fun DetailEntriesPreview() {
    LuminaLuxeTheme {
        DetailEntries(
            photo = R.drawable.skin_12,
            title = "Skintific MSH Niacinamide Moisturizer",
            price = 331800,
            count = 1,
            description = "MSH Niacinamide Brightening Moisture Gel dengan tekstur seringan udara, dapat menyerap dengan cepat dan mengontrol minyak dalam 24 jam. Diformulasikan dengan Novel MSH Niacinamide ekslusif SKINTIFIC yang dikombinasikan dengan dua bahan pencerah yang ringan dan paling efektif yaitu Alpha Arbutin dan Tranexamic Acid, yang mampu mencerahkan dengan signifikan dalam waktu 14 hari. ",
            onClickBack = { },
            onAddCart = { }
        )
    }
}