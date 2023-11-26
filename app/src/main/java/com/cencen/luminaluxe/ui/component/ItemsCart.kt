package com.cencen.luminaluxe.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cencen.luminaluxe.R
import com.cencen.luminaluxe.ui.theme.LuminaLuxeTheme
import com.cencen.luminaluxe.ui.theme.Shapes

@Composable
fun ItemsCart(
    skincareId: Long,
    photo: Int,
    title: String,
    totalPrice: Int,
    count: Int,
    onItemCountChanged: (id: Long, count: Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row (
        modifier = modifier.fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = photo),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .size(90.dp)
                .clip(Shapes.small)
            )
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
                .weight(1.0f)
        ) {
            Text(
                text = title,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.titleMedium.copy(
                    fontWeight = FontWeight.Bold
                )
            )
            Text(
                text = stringResource(R.string.price, totalPrice),
                color = MaterialTheme.colorScheme.secondary,
                style = MaterialTheme.typography.titleSmall.copy(
                    fontWeight = FontWeight.Normal
                )
            )
        }
        CounterItems(
            orderId = skincareId,
            orderCounter = count,
            onItemIncreased = { onItemCountChanged(skincareId, count + 1)},
            onItemDecreased = { onItemCountChanged(skincareId, count - 1)},
            modifier = Modifier.padding(8.dp)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ItemsCartPreview() {
    LuminaLuxeTheme {
        ItemsCart(
            skincareId = 1,
            photo = R.drawable.skin_1,
            title = "Emina Bright Stuff Serum",
            totalPrice = 53500,
            count = 0,
            onItemCountChanged = { skincareId, count -> }
        )
    }
}