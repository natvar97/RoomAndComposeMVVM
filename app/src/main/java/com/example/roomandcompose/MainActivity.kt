package com.example.roomandcompose

import android.app.Application
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.roomandcompose.database.Note
import com.example.roomandcompose.database.NoteViewModel
import com.example.roomandcompose.database.ViewModelFactory
import com.example.roomandcompose.ui.theme.RoomAndComposeTheme
import com.example.roomandcompose.utils.Constants
import com.google.accompanist.glide.rememberGlidePainter
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.text.style.TextOverflow

class MainActivity : ComponentActivity() {
    @ExperimentalMaterialApi
    @ExperimentalAnimationApi
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            RoomAndComposeTheme {
                // A surface container using the 'background' color from the theme
                Surface(color = MaterialTheme.colors.background) {
                    NoteApplicationMainLayout()
                }
            }
        }
    }
}

@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Composable
fun NoteApplicationMainLayout() {

    val context = LocalContext.current

    val noteViewModel: NoteViewModel = viewModel(
        factory = ViewModelFactory(context.applicationContext as Application),
        modelClass = NoteViewModel::class.java
    )
    val notes = noteViewModel.allNotes.observeAsState(listOf<Note>()).value
    val showDialog = remember { mutableStateOf(false) }
    val showEditDialog = remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(60.dp)
                .clip(RoundedCornerShape(bottomEnd = 5.dp, bottomStart = 5.dp))
                .background(colorResource(id = R.color.app_bar_color)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = Constants.APP_TITLE,
                style = TextStyle(
                    color = Color.White,
                    fontFamily = FontFamily.SansSerif,
                    textAlign = TextAlign.Start,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                modifier = Modifier.padding(start = 10.dp)
            )

            Image(
                painter = rememberGlidePainter(R.drawable.icon_add_note),
                contentDescription = null,
                modifier = Modifier
                    .height(40.dp)
                    .width(50.dp)
                    .clickable(onClick = { showDialog.value = true })
            )

            if (showDialog.value) {
                CustomAddNoteLayout(showDialog, noteViewModel)
            }

        }

        LazyColumn() {

            itemsIndexed(notes) { index, note ->

                val currentSelectedItem = remember { mutableStateOf(0) }
                CustomNoteListItem(
                    note = notes[index],
                    onClick = { noteViewModel.delete(notes[index]) },
                    onEditClick = {
                        currentSelectedItem.value = index
                        Log.e("current in editclick", "${currentSelectedItem.value}")
                        showEditDialog.value = true
                    }
                )
                CustomEditNoteLayout(
                    showEditDialog,
                    notes[currentSelectedItem.value],
                    noteViewModel
                )
                Log.e("current in editnote", "${currentSelectedItem.value}")

            }

        }


    }
}

@ExperimentalMaterialApi
@Composable
fun CustomAddNoteLayout(
    showDialog: MutableState<Boolean>,
    viewModel: NoteViewModel
) {

    var textTitle by remember { mutableStateOf("") }
    var textDescription by remember { mutableStateOf("") }

    if (showDialog.value) {
        Dialog(
            onDismissRequest = {
                showDialog.value = false
            }, content = {
                LazyColumn() {
                    item {
                        Column(
                            modifier = Modifier
                                .width(500.dp)
                                .height(500.dp)
                                .background(Color.White)
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                text = Constants.APP_TITLE,
                                style = TextStyle(
                                    color = Color.Black,
                                    fontFamily = FontFamily.SansSerif,
                                    textAlign = TextAlign.Start,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(start = 10.dp)
                            )

                            OutlinedTextField(
                                value = textTitle,
                                onValueChange = { newValue -> textTitle = newValue },
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                label = { Text(text = Constants.NOTE_TITLE) },
                                leadingIcon = {
                                    Image(
                                        painter = rememberGlidePainter(R.drawable.icon_title),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .width(20.dp)
                                            .height(20.dp)
                                    )
                                },
                                placeholder = {
                                    Text(
                                        text = "Your ${Constants.NOTE_TITLE}"
                                    )
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                singleLine = false
                            )


                            OutlinedTextField(
                                value = textDescription,
                                onValueChange = { newValue -> textDescription = newValue },
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .align(Alignment.Start),
                                label = { Text(text = Constants.NOTE_DESCRIPTION) },
                                leadingIcon = {
                                    Image(
                                        painter = rememberGlidePainter(R.drawable.icon_description),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .width(20.dp)
                                            .height(20.dp)
                                    )
                                },
                                placeholder = {
                                    Text(
                                        text = "Your ${Constants.NOTE_DESCRIPTION}"
                                    )
                                },
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                singleLine = false
                            )

                            OutlinedButton(
                                shape = RoundedCornerShape(10.dp),
                                elevation = ButtonDefaults.elevation(10.dp),
                                border = BorderStroke(2.dp, color = Color.Cyan),
                                content = {
                                    Text(
                                        text = "Add Note",
                                        color = Color.Blue,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.SansSerif,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .wrapContentWidth()
                                    )
                                },
                                onClick = {
                                    viewModel.insert(note = Note(textTitle, textDescription))
                                    showDialog.value = false
                                },
                            )

                        }
                    }
                }
            })
    }
}


@ExperimentalMaterialApi
@Composable
fun CustomEditNoteLayout(
    showEditDialog: MutableState<Boolean>,
    note: Note,
    viewModel: NoteViewModel
) {

    var textTitle by remember { mutableStateOf(note.title) }
    var textDescription by remember { mutableStateOf(note.note) }
    val id = note.id

    if (showEditDialog.value) {
        Dialog(
            onDismissRequest = {
                showEditDialog.value = false
            }, content = {
                LazyColumn() {
                    item {
                        Column(
                            modifier = Modifier
                                .width(500.dp)
                                .height(500.dp)
                                .background(Color.White)
                                .padding(20.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceEvenly
                        ) {
                            Text(
                                text = Constants.APP_TITLE,
                                style = TextStyle(
                                    color = Color.Black,
                                    fontFamily = FontFamily.SansSerif,
                                    textAlign = TextAlign.Start,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(start = 10.dp)
                            )

                            OutlinedTextField(
                                value = textTitle,
                                onValueChange = { newValue -> textTitle = newValue },
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth(),
                                label = { Text(text = Constants.NOTE_TITLE) },
                                leadingIcon = {
                                    Image(
                                        painter = rememberGlidePainter(R.drawable.icon_title),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .width(20.dp)
                                            .height(20.dp)
                                    )
                                },
                                placeholder = {
                                    Text(
                                        text = "Your ${Constants.NOTE_TITLE}"
                                    )
                                },
                                enabled = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                singleLine = false
                            )


                            OutlinedTextField(
                                value = textDescription,
                                onValueChange = { newValue -> textDescription = newValue },
                                modifier = Modifier
                                    .padding(8.dp)
                                    .fillMaxWidth()
                                    .height(200.dp)
                                    .align(Alignment.Start),
                                label = { Text(text = Constants.NOTE_DESCRIPTION) },
                                leadingIcon = {
                                    Image(
                                        painter = rememberGlidePainter(R.drawable.icon_description),
                                        contentDescription = null,
                                        modifier = Modifier
                                            .width(20.dp)
                                            .height(20.dp)
                                    )
                                },
                                placeholder = {
                                    Text(
                                        text = "Your ${Constants.NOTE_DESCRIPTION}"
                                    )
                                },
                                enabled = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                                singleLine = false
                            )

                            OutlinedButton(
                                shape = RoundedCornerShape(10.dp),
                                elevation = ButtonDefaults.elevation(10.dp),
                                border = BorderStroke(2.dp, color = Color.Cyan),
                                content = {
                                    Text(
                                        text = "Update Note",
                                        color = Color.Blue,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.SansSerif,
                                        fontWeight = FontWeight.Medium,
                                        modifier = Modifier
                                            .wrapContentWidth()
                                            .wrapContentWidth()
                                    )
                                },
                                onClick = {
                                    viewModel.update(note = Note(textTitle, textDescription, id))
                                    showEditDialog.value = false
                                },
                            )

                        }
                    }
                }
            })
    }

}

@ExperimentalAnimationApi
@Composable
fun CustomNoteListItem(note: Note, onClick: () -> Unit, onEditClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(5.dp),
        elevation = 5.dp,
        backgroundColor = colorResource(id = R.color.app_bar_color),
        border = BorderStroke(1.dp, Color.Gray),
        shape = RoundedCornerShape(bottomStart = 10.dp, bottomEnd = 10.dp)
    ) {

        var expanded by remember { mutableStateOf(false) }

        Column(
            modifier = Modifier
                .padding(10.dp)
                .clickable { expanded = !expanded }
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = note.title,
                    style = TextStyle(
                        color = Color.White,
                        fontFamily = FontFamily.SansSerif,
                        textAlign = TextAlign.Start,
                        fontSize = 24.sp,
                        fontWeight = FontWeight.Bold
                    ),
                    modifier = Modifier
                        .width(350.dp)
                        .padding(start = 10.dp, end = 50.dp),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Image(
                    painter = rememberGlidePainter(request = R.drawable.icon_delete),
                    contentDescription = null,
                    modifier = Modifier
                        .height(30.dp)
                        .width(30.dp)
                        .clickable(onClick = onClick),
                )

            }

            Spacer(modifier = Modifier.height(10.dp))

            AnimatedVisibility(visible = expanded) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {

                    Text(
                        text = note.note,
                        style = TextStyle(
                            color = Color.White,
                            fontFamily = FontFamily.SansSerif,
                            textAlign = TextAlign.Start,
                            fontSize = 18.sp,
                        ),
                        modifier = Modifier
                            .width(350.dp)
                            .padding(start = 10.dp, end = 50.dp)
                    )

                    Image(
                        painter = rememberGlidePainter(request = R.drawable.icon_edit_note),
                        contentDescription = null,
                        modifier = Modifier
                            .height(30.dp)
                            .width(30.dp)
                            .clickable(onClick = onEditClick),
                    )

                }
            }

        }
    }
}


@ExperimentalMaterialApi
@ExperimentalAnimationApi
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    RoomAndComposeTheme {
        NoteApplicationMainLayout()
    }
}