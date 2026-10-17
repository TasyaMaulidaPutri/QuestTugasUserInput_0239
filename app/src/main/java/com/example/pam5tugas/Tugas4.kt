package com.example.pam5tugas

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.TextUnit
import kotlinx.coroutines.delay
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
private fun spResource(id: Int): TextUnit =
    with(LocalDensity.current) { dimensionResource(id).toSp() }

@Composable
private fun FieldHint(id: Int) {
    Text(
        text = stringResource(id),
        fontSize = spResource(R.dimen.field_text_size),
        maxLines = 1,
        overflow = TextOverflow.Ellipsis
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormBiodataScreen() {
    // ---------- State ----------
    var firstName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var birthDate by rememberSaveable { mutableStateOf("") }
    var experience by rememberSaveable { mutableStateOf("") }
    var showDatePicker by rememberSaveable { mutableStateOf(false) }
    var expandExperience by rememberSaveable { mutableStateOf(false) }
    var showResult by rememberSaveable { mutableStateOf(false) }

    val experienceList = stringArrayResource(R.array.experience_list)
    val skillList = stringArrayResource(R.array.skill_list)
    val selectedSkills = remember { mutableStateListOf<String>() }

    val isFormValid = firstName.isNotBlank() && lastName.isNotBlank() &&
            email.isNotBlank() && birthDate.isNotBlank() &&
            experience.isNotBlank() && selectedSkills.isNotEmpty()

    val displayDuration = integerResource(R.integer.result_display_duration).toLong()
    val skillSeparator = stringResource(R.string.skill_separator)
    val dateFormat = stringResource(R.string.date_format)

    // Result is shown, then hidden automatically after the duration
    LaunchedEffect(showResult) {
        if (showResult) {
            delay(displayDuration)
            showResult = false
        }
    }

    // ---------- Resources ----------
    val pinkPrimary = colorResource(R.color.pink_primary)
    val textPrimary = colorResource(R.color.text_primary)
    val fieldShape = RoundedCornerShape(dimensionResource(R.dimen.field_corner))
    val fieldTextStyle = LocalTextStyle.current.copy(fontSize = spResource(R.dimen.field_text_size))
    val cardShape = RoundedCornerShape(
        topStart = dimensionResource(R.dimen.card_corner),
        topEnd = dimensionResource(R.dimen.card_corner)
    )

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = pinkPrimary,
        unfocusedBorderColor = colorResource(R.color.field_border),
        focusedLabelColor = pinkPrimary,
        cursorColor = pinkPrimary,
        focusedTextColor = textPrimary,
        unfocusedTextColor = textPrimary
    )


}