package com.cencen.luminaluxe.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.cencen.luminaluxe.ui.theme.LuminaLuxeTheme

@Composable
fun ButtonOrder(
    text: String,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    onClick: () -> Unit,
) {
    Button(
        onClick = onClick,
        enabled = enabled,
        modifier = modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(if (enabled) MaterialTheme.colorScheme.primary else Color.Gray),
        shape = RoundedCornerShape(5.dp),
        colors = ButtonDefaults.textButtonColors(contentColor = Color.White),
    ) {
        Text(
            text = text,
            modifier = Modifier
                .align(Alignment.CenterVertically),
            style = MaterialTheme.typography.titleMedium.copy(
                fontWeight = FontWeight.Bold
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ButtonOrderPreview() {
    LuminaLuxeTheme {
        ButtonOrder(
            text = "Order Now",
            onClick = { }
        )
    }
}