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

    // ---------- Scaffold ----------
    Scaffold(containerColor = colorResource(R.color.white)) { innerPadding ->
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Doll image
            Image(
                painter = painterResource(R.drawable.bg_doll_pink),
                contentDescription = stringResource(R.string.desc_doll_image),
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(dimensionResource(R.dimen.header_height))
            )

            // Dot indicator
            Row(
                modifier = Modifier.padding(
                    top = dimensionResource(R.dimen.dot_padding_top),
                    start = dimensionResource(R.dimen.dot_padding_start)
                ),
                horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.dot_gap))
            ) {
                val activeIndex = integerResource(R.integer.dot_active_index)
                repeat(integerResource(R.integer.dot_count)) { index ->
                    Box(
                        modifier = Modifier
                            .size(dimensionResource(R.dimen.dot_size))
                            .clip(CircleShape)
                            .background(
                                if (index == activeIndex) pinkPrimary
                                else colorResource(R.color.dot_inactive)
                            )
                    )
                }
            }

            // Floating box above Scaffold
            Column(
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .heightIn(max = maxHeight - dimensionResource(R.dimen.header_visible_height))
                    .padding(horizontal = dimensionResource(R.dimen.card_margin_horizontal))
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f, fill = false)
                        .fillMaxWidth()
                        .shadow(
                            elevation = dimensionResource(R.dimen.card_elevation),
                            shape = cardShape,
                            ambientColor = colorResource(R.color.shadow_color),
                            spotColor = colorResource(R.color.shadow_color)
                        )
                        .clip(cardShape)
                        .background(colorResource(R.color.white))
                ) {
                    Column(
                        modifier = Modifier
                            .verticalScroll(rememberScrollState())
                            .padding(dimensionResource(R.dimen.card_padding)),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.title_form),
                            color = pinkPrimary,
                            fontSize = spResource(R.dimen.title_size),
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(Modifier.height(dimensionResource(R.dimen.title_gap)))
                        Text(
                            text = stringResource(R.string.subtitle_form),
                            color = colorResource(R.color.text_secondary),
                            fontSize = spResource(R.dimen.subtitle_size),
                            textAlign = TextAlign.Center
                        )
                        Spacer(Modifier.height(dimensionResource(R.dimen.section_gap)))

                        // First and last name
                        Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.field_gap))) {
                            OutlinedTextField(
                                value = firstName,
                                onValueChange = { firstName = it },
                                placeholder = { FieldHint(R.string.hint_first_name) },
                                singleLine = true,
                                textStyle = fieldTextStyle,
                                shape = fieldShape,
                                colors = fieldColors,
                                modifier = Modifier.weight(1f)
                            )
                            OutlinedTextField(
                                value = lastName,
                                onValueChange = { lastName = it },
                                placeholder = { FieldHint(R.string.hint_last_name) },
                                singleLine = true,
                                textStyle = fieldTextStyle,
                                shape = fieldShape,
                                colors = fieldColors,
                                modifier = Modifier.weight(1f)
                            )
                        }

                        Spacer(Modifier.height(dimensionResource(R.dimen.section_gap)))

                        // Email
                        OutlinedTextField(
                            value = email,
                            onValueChange = { email = it },
                            placeholder = { FieldHint(R.string.hint_email) },
                            singleLine = true,
                            textStyle = fieldTextStyle,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                            shape = fieldShape,
                            colors = fieldColors,
                            modifier = Modifier.fillMaxWidth()
                        )

                        Spacer(Modifier.height(dimensionResource(R.dimen.section_gap)))

                        // Birth date and experience
                        Row(horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.field_gap))) {
                            OutlinedTextField(
                                value = birthDate,
                                onValueChange = {},
                                readOnly = true,
                                placeholder = { FieldHint(R.string.hint_birth_date) },
                                trailingIcon = {
                                    IconButton(onClick = { showDatePicker = true }) {
                                        Icon(
                                            painter = painterResource(R.drawable.ic_calender),
                                            contentDescription = stringResource(R.string.desc_pick_date),
                                            tint = textPrimary
                                        )
                                    }
                                },
                                singleLine = true,
                                textStyle = fieldTextStyle,
                                shape = fieldShape,
                                colors = fieldColors,
                                modifier = Modifier.weight(1f)
                            )

                            ExposedDropdownMenuBox(
                                expanded = expandExperience,
                                onExpandedChange = { expandExperience = it },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = experience,
                                    onValueChange = {},
                                    readOnly = true,
                                    placeholder = { FieldHint(R.string.hint_experience) },
                                    trailingIcon = {
                                        ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandExperience)
                                    },
                                    singleLine = true,
                                    textStyle = fieldTextStyle,
                                    shape = fieldShape,
                                    colors = fieldColors,
                                    modifier = Modifier.menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable)
                                )
                                ExposedDropdownMenu(
                                    expanded = expandExperience,
                                    onDismissRequest = { expandExperience = false }
                                ) {
                                    experienceList.forEach { item ->
                                        DropdownMenuItem(
                                            text = { Text(item) },
                                            onClick = {
                                                experience = item
                                                expandExperience = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(Modifier.height(dimensionResource(R.dimen.section_gap)))

                        // Skill
                        Text(
                            text = stringResource(R.string.label_skill),
                            color = textPrimary,
                            fontWeight = FontWeight.Bold,
                            fontSize = spResource(R.dimen.label_size),
                            modifier = Modifier.fillMaxWidth()
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            skillList.forEach { skill ->
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Checkbox(
                                        checked = skill in selectedSkills,
                                        onCheckedChange = { checked ->
                                            if (checked) selectedSkills.add(skill)
                                            else selectedSkills.remove(skill)
                                        },
                                        colors = CheckboxDefaults.colors(
                                            checkedColor = pinkPrimary,
                                            uncheckedColor = textPrimary
                                        )
                                    )
                                    Text(
                                        text = skill,
                                        color = textPrimary,
                                        fontSize = spResource(R.dimen.skill_text_size)
                                    )
                                }
                            }
                        }

                        Spacer(Modifier.height(dimensionResource(R.dimen.section_gap)))

                        // Proceed button
                        Button(
                            onClick = { showResult = true },
                            enabled = isFormValid,
                            shape = RoundedCornerShape(dimensionResource(R.dimen.button_corner)),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = pinkPrimary,
                                contentColor = colorResource(R.color.white),
                                disabledContainerColor = colorResource(R.color.button_disabled),
                                disabledContentColor = colorResource(R.color.button_disabled_text)
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(dimensionResource(R.dimen.button_height))
                        ) {
                            Text(
                                text = stringResource(R.string.btn_proceed),
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                // Gap under the card (white area + shadow line like the reference)
                Spacer(Modifier.height(dimensionResource(R.dimen.card_bottom_gap)))
            }

            // Summary of the filled data, floating for a few seconds
            if (showResult) {
                Column(
                    modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(
                            horizontal = dimensionResource(R.dimen.card_padding),
                            vertical = dimensionResource(R.dimen.result_margin)
                        )
                        .fillMaxWidth()
                        .shadow(
                            elevation = dimensionResource(R.dimen.card_elevation),
                            shape = fieldShape
                        )
                        .clip(fieldShape)
                        .background(colorResource(R.color.pink_light))
                        .padding(dimensionResource(R.dimen.result_padding))
                ) {
                    val resultSize = spResource(R.dimen.skill_text_size)
                    Text(stringResource(R.string.result_name, firstName, lastName), color = textPrimary, fontSize = resultSize)
                    Text(stringResource(R.string.result_email, email), color = textPrimary, fontSize = resultSize)
                    Text(stringResource(R.string.result_birth, birthDate), color = textPrimary, fontSize = resultSize)
                    Text(stringResource(R.string.result_experience, experience), color = textPrimary, fontSize = resultSize)
                    Text(
                        stringResource(R.string.result_skill, selectedSkills.joinToString(skillSeparator)),
                        color = textPrimary,
                        fontSize = resultSize
                    )
                }
            }
        }
    }

        }
    }
}