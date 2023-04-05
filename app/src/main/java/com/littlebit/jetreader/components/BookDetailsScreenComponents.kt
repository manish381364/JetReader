package com.littlebit.jetreader.components

import android.util.Log
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.tween
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.littlebit.jetreader.model.JetBook
import com.littlebit.jetreader.navigation.JetScreens
import com.littlebit.jetreader.screens.details.BookDetailsViewModel
import org.jsoup.Jsoup

@Composable
fun ExpandableCard(
    viewModel: BookDetailsViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    ElevatedCard(
        modifier = Modifier
            .padding(start = 5.dp, end = 5.dp, bottom = 20.dp)
            .clickable { expanded = !expanded }
            .clip(RoundedCornerShape(10.dp))
            .animateContentSize(animationSpec = tween(2000, easing = EaseInOut)),
        elevation = CardDefaults.cardElevation(4.dp),
    ) {

        val html = viewModel.bookResource.value.data?.volumeInfo?.description ?: ""
        val doc = Jsoup.parse(html)
        val paragraphs = doc.select("p")
        val text = StringBuilder("")
        for (paragraph in paragraphs) {
            text.append(paragraph.text() + "\n")
            println(paragraph.text() + "\n")
        }

        Text(
            text = text.toString(),
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.Normal,
            maxLines = if (expanded) (Int.MAX_VALUE) else 3,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier
                .padding(top = 5.dp, bottom = 2.dp, start = 5.dp, end = 5.dp)
                .fillMaxWidth()
        )
        IconButton(modifier = Modifier
            .align(Alignment.End),
            onClick = { expanded = !expanded }
        ) {
            Icon(
                imageVector = if (!expanded) Icons.Default.ArrowDropDown else Icons.Default.ArrowDropUp,
                contentDescription = "Expand"
            )
        }

    }
}

fun floatingActionOnClick(
    viewModel: BookDetailsViewModel,
    navController: NavHostController
) {
    val book = JetBook(
        title = viewModel.bookResource.value.data?.volumeInfo?.title ?: "",
        author = viewModel.bookResource.value.data?.volumeInfo?.authors.toString()
            .removeSuffix("]").removePrefix("["),
        pageCount = viewModel.bookResource.value.data?.volumeInfo?.pageCount.toString(),
        categories = viewModel.bookResource.value.data?.volumeInfo?.categories.toString()
            .removePrefix("[").removeSuffix("]").replace('/', '|'),
        publishedDate = viewModel.bookResource.value.data?.volumeInfo?.publishedDate
            ?: "",
        description = viewModel.bookResource.value.data?.volumeInfo?.description
            ?: "",
        image = viewModel.bookResource.value.data?.volumeInfo?.imageLinks?.thumbnail
            ?: "",
        id = viewModel.bookResource.value.data?.id ?: "",
        notes = "",
        isFavorite = false,
        isRead = false,
        userId = FirebaseAuth.getInstance().currentUser?.uid.toString(),
        rating = 2,
    )
    saveToDataBase(book, navController)
}

@Composable
fun AboutBook(viewModel: BookDetailsViewModel) {
    Spacer(modifier = Modifier.height(20.dp))
    Text(
        text = viewModel.bookResource.value.data?.volumeInfo?.title ?: "",
        style = MaterialTheme.typography.titleMedium,
        fontWeight = FontWeight.Bold,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
    Spacer(modifier = Modifier.height(4.dp))
    val authorSize = viewModel.bookResource.value.data?.volumeInfo?.authors?.size
    val author = if (authorSize != null && authorSize > 1) "Authors" else "Author"
    Text(
        text = ("$author: " + viewModel.bookResource.value.data?.volumeInfo?.authors.toString()
            .removeSuffix("]").removePrefix("[")),
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Normal,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
    Text(
        text = ("Page Count: " + viewModel.bookResource.value.data?.volumeInfo?.pageCount.toString()),
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Italic,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
    Text(
        text = ("Categories: " + viewModel.bookResource.value.data?.volumeInfo?.categories.toString()
            .removePrefix("[").removeSuffix("]").replace('/', '|')),
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Normal,
        fontStyle = FontStyle.Italic,
        maxLines = 3,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier
            .padding(start = 2.dp, end = 2.dp)
            .fillMaxWidth()
    )
    Spacer(modifier = Modifier.height(5.dp))
    Text(
        text = viewModel.bookResource.value.data?.volumeInfo?.publishedDate ?: "",
        style = MaterialTheme.typography.bodyMedium,
        fontWeight = FontWeight.Normal,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
    )
    Spacer(modifier = Modifier.height(30.dp))
}

@Composable
fun BookDetailsImage(
    viewModel: BookDetailsViewModel,
    isLoading: MutableState<Boolean>
) {
    AsyncImage(
        modifier = Modifier
            .padding(5.dp)
            .size(200.dp),
        model = viewModel.bookResource.value.data?.volumeInfo?.imageLinks?.thumbnail
            ?: "",
        contentDescription = "Book Cover Image",
        contentScale = ContentScale.Crop,
        onLoading = {
            isLoading.value = true
        },
        onSuccess = {
            isLoading.value = false
        },
        onError = {

        }
    )
}

fun saveToDataBase(
    book: JetBook,
    navController: NavController
) {

    val db = FirebaseFirestore.getInstance()
    val dbCollection = db.collection("books")
    if (book.id != "") {
        dbCollection.add(book).addOnSuccessListener {
            val docId = it.id
            db.document("books/$docId").update(hashMapOf("id" to docId) as Map<String, Any>)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        Log.d("FIREBASE", "DocumentSnapshot successfully updated!")
                        navController.navigate(JetScreens.HomeScreen.name)
                    } else {
                        Log.d("FIREBASE ADD", "Error updating document", task.exception)

                    }
                }
        }
    }
}