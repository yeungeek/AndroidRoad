package com.yeungeek.kmpohsample

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kmpohsample.composeapp.generated.resources.*
import org.jetbrains.compose.resources.stringResource
import org.jetbrains.compose.ui.tooling.preview.Preview

@Composable
fun CustomLayoutSample(modifier: Modifier = Modifier) {
    Column(modifier = modifier.fillMaxSize()) {
        // Row 1: STOP | RESTART
        Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFFFF3377)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(Res.string.stop),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 48.sp
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFF00FF00)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(Res.string.restart),
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 48.sp
                )
            }
        }

        // Row 2: CUSTOMADAPT(INTERNAL)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFFFFD600)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(Res.string.custom_adapt_internal),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
        }

        // Row 3: CUSTOMADAPT(EXTERNAL)
        Box(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .background(Color(0xFFFF0000)),
            contentAlignment = Alignment.Center,
        ) {
            Text(
                text = stringResource(Res.string.custom_adapt_external),
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
            )
        }

        // Row 4: W-250dp | W-109dp
        Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(250.dp)
                    .fillMaxHeight()
                    .background(Color(0xFF3F51B5)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(Res.string.w_250dp_total_360dp),
                    color = Color.Green,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFFFF00FF)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(Res.string.w_109dp),
                    color = Color.Green,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }

        // Row 5: W-120dp | W-90dp | W-150dp
        Row(modifier = Modifier.weight(1f).fillMaxWidth()) {
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .fillMaxHeight()
                    .background(Color(0xFFA0E7FF)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(Res.string.w_120dp),
                    color = Color.Green,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Box(
                modifier = Modifier
                    .width(90.dp)
                    .fillMaxHeight()
                    .background(Color(0xFFADC6FF)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(Res.string.w_90dp),
                    color = Color.Green,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxHeight()
                    .background(Color(0xFFFFE1FF)),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = stringResource(Res.string.w_150dp),
                    color = Color.Green,
                    fontWeight = FontWeight.Bold,
                    fontSize = 20.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun CustomLayoutSamplePreview() {
    androidx.compose.material3.MaterialTheme {
        CustomLayoutSample()
    }
}
