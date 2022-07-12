package io.syslogic.colorpicker.compose

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close

import androidx.compose.runtime.*

import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.window.Dialog

import io.syslogic.colorpicker.OnColorChangedListener
import io.syslogic.colorpicker.R

/**
 * Jetpack Compose Color-Picker Dialog
 *
 * @author Martin Zeitler
 */
@Composable
fun ColorPickerDialog(
    dialogTitle: String = LocalContext.current.getString(R.string.text_select_color),
    initialColor: Color = Color.Unspecified,
    onColorChanged: OnColorChangedListener?,
    showAlpha: Boolean = true,
    showHSV: Boolean = true,
    showARGB: Boolean = true,
    showHex: Boolean = true,
    showDialog: (Boolean) -> Unit
) {

    /* The value is being initialized by the `initialColor`. */
    val currentColor: Int by remember { mutableStateOf(initialColor.hashCode()) }

    Dialog(onDismissRequest = { showDialog(false) }) {
        Surface(
            shape = RoundedCornerShape(dimensionResource(R.dimen.compose_dialog_border_radius)),
            color = Color.White
        ) {
            Box(
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .padding(dimensionResource(R.dimen.compose_dialog_column_padding))
                ) {

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = dialogTitle,
                            style = TextStyle(
                                fontSize = toSp(dp = dimensionResource(R.dimen.compose_dialog_title_font_size)),
                                fontFamily = FontFamily.Default,
                                fontWeight = FontWeight.Bold
                            )
                        )
                        Icon(
                            imageVector = Icons.Filled.Close,
                            contentDescription = "",
                            tint = Color.DarkGray,
                            modifier = Modifier
                                .width(dimensionResource(R.dimen.compose_dialog_close_icon_size))
                                .height(dimensionResource(R.dimen.compose_dialog_close_icon_size))
                                .clickable { showDialog(false) }
                        )
                    }

                    Spacer(
                        modifier = Modifier.height(dimensionResource(R.dimen.compose_dialog_spacer_height))
                    )

                    ColorPickerComponent(
                        initialColor = Color(currentColor),
                        onColorChanged = onColorChanged,
                        showAlpha = showAlpha,
                        showARGB = showARGB,
                        showHSV = showHSV,
                        showHex = showHex
                    )
                }
            }
        }
    }
}

@Composable
fun toSp(dp: Dp) = with(LocalDensity.current) { dp.toSp() }
