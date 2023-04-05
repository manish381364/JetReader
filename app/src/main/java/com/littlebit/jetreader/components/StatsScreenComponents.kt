package com.littlebit.jetreader.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Person
import androidx.compose.material.icons.rounded.ThumbUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.google.firebase.auth.FirebaseUser
import com.littlebit.jetreader.model.JetBook
import com.littlebit.jetreader.screens.home.HomeScreenViewModel
import com.littlebit.jetreader.utils.formatDate
import java.util.*

@Composable
fun StatsScreenContent(
    it: PaddingValues,
    currentUser: FirebaseUser?,
    viewModel: HomeScreenViewModel,
    books: List<JetBook>
) {
    Surface(
        modifier = Modifier
            .padding(it)
    ) {
        Column(
            Modifier.padding(10.dp)
        ) {
            Row {
                Box(
                    modifier = Modifier
                        .size(45.dp)
                        .padding(2.dp)
                ) {
                    Icon(imageVector = Icons.Rounded.Person, contentDescription = null)
                }
                val userName =
                    currentUser?.email.toString().split("@")[0].uppercase(Locale.ROOT)
                Text(text = "Hello, $userName", style = MaterialTheme.typography.titleSmall)
            }
            OutlinedCard(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = CircleShape,
                elevation = CardDefaults.cardElevation(5.dp)
            ) {
                val readBooks = if (!viewModel.data.value.data.isNullOrEmpty()) {
                    viewModel.data.value.data!!.filter { it.userId == currentUser?.uid && it.finishedReading != null }
                } else {
                    emptyList()
                }
                val readingBooks = if (!viewModel.data.value.data.isNullOrEmpty()) {
                    viewModel.data.value.data!!.filter { it.userId == currentUser?.uid && it.startedReading != null && it.finishedReading == null }
                } else {
                    emptyList()
                }
                val unreadBooks = if (!viewModel.data.value.data.isNullOrEmpty()) {
                    viewModel.data.value.data!!.filter { it.userId == currentUser?.uid && it.startedReading != null && it.finishedReading == null }
                } else {
                    emptyList()
                }
                val totalBooks = books.size
                Column(
                    modifier = Modifier.padding(10.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text("Books Stats", style = MaterialTheme.typography.titleMedium)
                    Divider(Modifier.padding(10.dp))
                    Text(
                        text = "Total Books: $totalBooks",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "Reading: ${readBooks.size} ",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "Read: ${readingBooks.size} ",
                        style = MaterialTheme.typography.titleSmall
                    )
                    Text(
                        text = "Not Started: ${unreadBooks.size} ",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }

            if (viewModel.data.value.loading == true) {
                LinearProgressIndicator()
            } else {
                Divider(Modifier.padding(2.dp))
                LazyColumn(
                    Modifier
                        .fillMaxWidth()
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(2.dp)
                ) {
                    val readBooks = if (!viewModel.data.value.data.isNullOrEmpty()) {
                        viewModel.data.value.data!!.filter { it.userId == currentUser?.uid && it.finishedReading != null }
                    } else {
                        emptyList()
                    }
                    items(readBooks) { book ->
                        SearchBookItem(book)
                    }
                }
            }
        }
    }
}

@Composable
fun SearchBookItem(
    book: JetBook?,
    onClick: () -> Unit = { }
) {
    Card(
        modifier = Modifier
            .padding(top = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(22.dp),
    ) {
        Row(
            modifier = Modifier
                .padding(end = 2.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                modifier = Modifier
                    .size(60.dp, 100.dp)
                    .clip(RoundedCornerShape(topStart = 22.dp, bottomStart = 22.dp)),
                model = book?.image ?: "https://picsum.photos/200/300",
                contentDescription = "Book Image",
                contentScale = ContentScale.Crop,
            )
            Spacer(modifier = Modifier.width(16.dp))
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(1.dp),
                horizontalAlignment = Alignment.Start,
                verticalArrangement = Arrangement.SpaceBetween
            ) {
                Row(
                    Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                ) {
                    Text(
                        text = book?.title ?: "No Title",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        style = TextStyle(
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    )
                    if (book?.rating!! >= 4) {
                        Icon(
                            imageVector = Icons.Rounded.ThumbUp,
                            contentDescription = "Thumbs Up",
                            tint = Color.Green.copy(alpha = 0.5f),
                            modifier = Modifier
                                .size(20.dp)
                                .padding(2.dp)
                        )
                    } else Box {}
                }
                Text(
                    text = book?.author.toString(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Started: ${formatDate(book?.startedReading.toString())}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = "Finished: ${formatDate(book?.startedReading.toString())}",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}