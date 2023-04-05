package com.littlebit.jetreader.components

import android.content.Context
import android.util.Log
import android.view.MotionEvent
import android.widget.Toast
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.StarRate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FirebaseFirestore
import com.littlebit.jetreader.model.JetBook
import com.littlebit.jetreader.navigation.JetScreens
import com.littlebit.jetreader.screens.update.*
import com.littlebit.jetreader.utils.formatDate

@Composable
fun BookInfoCard(bookInfo: JetBook?) {
    ElevatedCard(
        modifier = Modifier
            .fillMaxWidth(0.9f)
            .height(200.dp),
        shape = RoundedCornerShape(80.dp)
    ) {
        Column(
            Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            LoadImage(bookInfo)
            Row(
                modifier = Modifier.padding(12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                Column(
                    verticalArrangement = Arrangement.SpaceEvenly,
                ) {
                    Text(
                        text = bookInfo?.title.toString(),
                        style = MaterialTheme.typography.titleMedium,
                        fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                    )
                    Text(
                        text = bookInfo?.author.toString().removePrefix("[")
                            .removeSuffix("]"),
                        style = MaterialTheme.typography.bodySmall
                    )
                    Text(
                        text = bookInfo?.publishedDate.toString(),
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Form(
    book: JetBook?,
    navController: NavController
) {
    var notesText by remember {
        mutableStateOf(book?.notes.toString())
    }
    UpdateForm(
        notesText = remember { mutableStateOf(notesText) },
        book = book,
        navController = navController,
    ) {
        notesText = it
    }
}

@ExperimentalMaterial3Api
@Composable
@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
private fun UpdateForm(
    notesText: MutableState<String>,
    book: JetBook?,
    navController: NavController,
    onDone: (String) -> Unit = {}
) {
    val keyBoardController = LocalSoftwareKeyboardController.current
    val valid = remember(notesText.value) { mutableStateOf(notesText.value.trim().isNotEmpty()) }
    OutlinedTextField(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(12.dp)
            .requiredHeightIn(min = 25.dp, max = 140.dp),
        value = notesText.value,
        onValueChange = { updatedValue ->
            notesText.value = updatedValue
        },
        keyboardActions = KeyboardActions(
            onDone = {
                if (valid.value) {
                    onDone(notesText.value.trim())
                }
                keyBoardController?.hide()
            }
        ),
        label = { Text(text = "Review") },
        keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
    )
    Spacer(Modifier.height(10.dp))
    val isFinishedReading = remember { mutableStateOf(false) }
    val isStartedReading = remember { mutableStateOf(false) }
    if (book != null)
        BookStatusTexts(book, isFinishedReading, isStartedReading)
    else LinearProgressIndicator()
    Spacer(modifier = Modifier.height(25.dp))
    Text("Rating")
    Spacer(modifier = Modifier.height(5.dp))
    val rating = if (book?.rating != null) book.rating!!.toInt() else 2
    val ratingVal = remember { mutableStateOf(rating) }
    RatingBar(
        rating = rating, maxRating = 5
    ) { rated ->
        ratingVal.value = rated
    }
    Spacer(modifier = Modifier.height(25.dp))
    Row(
        Modifier
            .fillMaxWidth()
            .padding(bottom = 30.dp),
        horizontalArrangement = Arrangement.SpaceAround
    ) {
        val changedNotes = book?.notes != notesText.value
        val changedRating = book?.rating != ratingVal.value
        val isFinishedTimeStamp = if (isFinishedReading.value) {
            Timestamp.now().toString()
        } else {
            book?.finishedReading
        }
        val isStartedTimeStamp = if (isStartedReading.value) {
            Timestamp.now().toString()
        } else {
            book?.startedReading
        }
        val bookUpdate =
            changedNotes || changedRating || isFinishedReading.value || isStartedReading.value
        val bookToUpdate = hashMapOf(
            "finished_reading_at" to isFinishedTimeStamp,
            "started_reading_at" to isStartedTimeStamp,
            "rating" to ratingVal.value,
            "notes" to notesText.value
        ).toMap()
        val context = LocalContext.current
        RoundedButton(
            label = "Update",
            modifier = Modifier.padding(8.dp),
            isClickable = true
        ) {
            if (bookUpdate) {
                FirebaseFirestore.getInstance().collection("books")
                    .document(book?.id.toString())
                    .update(bookToUpdate)
                    .addOnSuccessListener {
                        showToast(context, "Book Updated!")
                        navController.navigate(JetScreens.HomeScreen.name)
                        Log.d("Update", "DocumentSnapshot successfully updated!")
                    }
                    .addOnFailureListener { e ->
                        Log.w("Update", "Error updating document", e)
                        showToast(context, "Error Updating Book")
                    }
            }
        }
        val openDialog = remember { mutableStateOf(false) }
        if (openDialog.value) {
            ShowAlertDialog(message = "Are you sure you want to delete this book?", onDismiss = {
                openDialog.value = false
            }, onConfirm = {
                FirebaseFirestore.getInstance().collection("books")
                    .document(book?.id.toString())
                    .delete()
                    .addOnSuccessListener {
                        showToast(context, "Book Deleted!")
                        navController.navigate(JetScreens.HomeScreen.name)
                        Log.d("Delete", "DocumentSnapshot successfully deleted!")
                    }
                    .addOnFailureListener { e ->
                        showToast(context, "Error Deleting Book")
                        Log.w("Delete", "Error deleting document", e)
                    }
            })
        }
        RoundedButton(
            label = "Delete",
            modifier = Modifier.padding(8.dp),
            isClickable = true
        ) {
            openDialog.value = true
        }
    }
}

@Composable
fun ShowAlertDialog(message: String, onDismiss: () -> Unit, onConfirm: () -> Unit) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Delete Book") },
        text = { Text(message) },
        confirmButton = {
            Button(onClick = onConfirm) {
                Text("Yes")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("No")
            }
        }
    )
}

fun showToast(context: Context, message: String) {
    Toast.makeText(context, "$message!", Toast.LENGTH_SHORT).show()
}

@Composable
private fun BookStatusTexts(
    book: JetBook?,
    isFinishedReading: MutableState<Boolean>,
    isStartedReading: MutableState<Boolean>,
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround
    ) {

        TextButton(
            onClick = {
                isStartedReading.value = true
            },
            enabled = book?.startedReading == null
        ) {
            Log.d("BOOK", "BookStatusTexts: ${book?.startedReading.toString()}")
            if (book?.startedReading == null) {
                if (!isStartedReading.value)
                    Text(text = "Start Reading")
                else
                    Text(
                        text = "Started Reading!",
                        modifier = Modifier.alpha(0.6f),
                        color = Color.Red.copy(0.5f)
                    )
            } else {
                Text(
                    text = "Started on: ${formatDate(book.startedReading.toString())}",
                )
            }

        }
        TextButton(
            onClick = {
                isFinishedReading.value = true
            },
            enabled = book?.finishedReading == null
        ) {
            if (book?.finishedReading == null) {
                if (!isFinishedReading.value)
                    Text(text = "Mark As Read")
                else
                    Text(
                        text = "Finished Reading",
                        modifier = Modifier.alpha(0.6f),
                        color = Color.Red.copy(0.5f)
                    )
            } else {
                Text(
                    text = "Finished on: ${formatDate(book.finishedReading.toString())}",
                )
            }
        }
    }
}

@Composable
fun LoadImage(
    book: JetBook? = null
) {
    AsyncImage(
        model = book?.image,
        contentDescription = null,
        contentScale = ContentScale.Crop,
        modifier = Modifier
            .clip(RoundedCornerShape(8.dp))
            .size(100.dp),
    )
}

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun RatingBar(
    modifier: Modifier = Modifier,
    rating: Int,
    maxRating: Int,
    onRatingChanged: (Int) -> Unit
) {
    var ratingState by remember {
        mutableStateOf(rating)
    }
    var selected by remember {
        mutableStateOf(false)
    }
    val size by animateDpAsState(
        targetValue = if (selected) 42.dp else 34.dp,
        spring(Spring.DampingRatioMediumBouncy)
    )
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        for (i in 1..maxRating) {
            val drawableRes = if (i <= ratingState) {
                Icons.Filled.Star
            } else {
                Icons.Outlined.StarRate
            }
            Icon(
                imageVector = drawableRes,
                contentDescription = null,
                modifier = modifier
                    .width(size)
                    .height(size)
                    .pointerInteropFilter {
                        when (it.action) {
                            MotionEvent.ACTION_DOWN -> {
                                selected = true
                                onRatingChanged(i)
                                ratingState = i

                            }
                            MotionEvent.ACTION_UP -> {
                                selected = false
                            }
                        }
                        true
                    },
                tint = if (i <= ratingState) Color.Yellow else Color.Gray.copy(0.5f)
            )
        }
    }
}