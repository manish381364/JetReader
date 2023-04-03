package com.littlebit.jetreader.screens.details

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.ArrowDropUp
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.graphics.drawscope.DrawContext
import androidx.compose.ui.graphics.drawscope.DrawStyle
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import com.littlebit.jetreader.screens.home.JetReaderAppBar
import org.jsoup.Jsoup

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookDetailsScreen(
    navController: NavHostController,
    bookId: String?,
    viewModel: BookDetailsViewModel
) {
    // Sticky Top Image of book cover then scrollable content
    viewModel.getBookById(bookId ?: "")
    Scaffold(
        topBar = {
            JetReaderAppBar(
                title = "Book Details",
                navController = navController,
                showProfile = false,
                leadingIcon = Icons.Rounded.ArrowBack,
                leadingIconOnClick = { navController.popBackStack() }
            )
        },

        ) {
        Box(modifier = Modifier
            .padding(it)
            .fillMaxSize()
        ) {
            Column(
                modifier = Modifier
                    .padding(7.dp)
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.Top,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box{
                    var isLoading by remember {
                        mutableStateOf(true)
                    }
                    if (isLoading) {
                        LinearProgressIndicator(Modifier.align(Alignment.Center))
                    }
                    AsyncImage(
                        modifier = Modifier
                            .padding(5.dp)
                            .size(200.dp),
                        model = viewModel.bookResource.value.data?.volumeInfo?.imageLinks?.thumbnail
                            ?: "",
                        contentDescription = "Book Cover Image",
                        contentScale = ContentScale.Crop,
                        onLoading = {
                            isLoading = true
                        },
                        onSuccess = {
                            isLoading = false
                        },
                        onError = {

                        }
                    )

                }
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
                        .removeSuffix("]").removePrefix("[")) ?: "Author: null",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = ("Page Count: " + viewModel.bookResource.value.data?.volumeInfo?.pageCount.toString())
                        ?: "",
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Normal,
                    fontStyle = FontStyle.Italic,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
                Text(
                    text = ("Categories: " + viewModel.bookResource.value.data?.volumeInfo?.categories.toString()
                        .removePrefix("[").removeSuffix("]").replace('/', '|')) ?: "",
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
                var expanded by remember { mutableStateOf(false) }
                val targetSize = if (expanded) 400.dp else 100.dp
                val animatedSize by animateDpAsState(
                    targetValue = targetSize,
                    animationSpec = tween(durationMillis = 200)
                )
                Card(
                    modifier = Modifier
                        .padding(start = 2.dp, end = 2.dp, bottom = 100.dp)
                        .height(animatedSize)
                        .clickable { expanded = !expanded }
                        .clip(RoundedCornerShape(10.dp)),
                    elevation = CardDefaults.cardElevation(4.dp),
                ) {

                    val html = viewModel.bookResource.value.data?.volumeInfo?.description ?: ""
                    val doc = Jsoup.parse(html)
                    val paragraphs = doc.select("p")
                    val text = java.lang.StringBuilder("")
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
                            .padding(5.dp)
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
            Row(
                Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(15.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                FloatingActionButton(onClick = { /*TODO*/ }) {
                    Text("Save")
                }
                FloatingActionButton(onClick = { /*TODO*/ }) {
                    Text("Cancel")
                }
            }
        }
    }
}

