package com.littlebit.jetreader.components

import android.annotation.SuppressLint
import android.util.Log
import android.view.MotionEvent
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.outlined.Favorite
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.pointer.pointerInteropFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.littlebit.jetreader.model.JetBook
import com.littlebit.jetreader.navigation.JetScreens
import com.littlebit.jetreader.screens.home.*

@Composable
fun HomeFullContent(
    it: PaddingValues,
    viewModel: HomeScreenViewModel,
    navController: NavController
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(it),
    ) {
        var books = emptyList<JetBook>()
        val currentUser = FirebaseAuth.getInstance().currentUser
        if (!viewModel.data.value.data.isNullOrEmpty()) {
            books = viewModel.data.value.data!!.toList().filter { jetBook ->
                jetBook.userId == currentUser?.uid.toString()
            }
            Log.d("FIREBASE", "HomeScreen: $books")
        }
        HomeContent(books = books, navController = navController)
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JetReaderAppBar(
    title: String,
    showProfile: Boolean = true,
    leadingIcon: ImageVector? = null,
    leadingIconOnClick: () -> Unit = {},
    trailingIcon: ImageVector? = null,
    trailingIconOnClick: () -> Unit = {},
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = Color.Red.copy(0.7f),
                style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 20.sp)
            )

        },
        navigationIcon = {
            IconButton(onClick = { leadingIconOnClick() }) {
                Icon(
                    modifier = Modifier.clip(RoundedCornerShape(12.dp)),
                    imageVector = if (!showProfile) leadingIcon
                        ?: Icons.Outlined.Favorite else Icons.Outlined.Favorite,
                    contentDescription = "leading icon"
                )
            }
        },
        actions = {
            if (showProfile) {
                IconButton(onClick = {
                    trailingIconOnClick()
                }) {
                    Icon(
                        imageVector = trailingIcon ?: Icons.Default.Logout,
                        contentDescription = "trailing icon"
                    )
                }
            }
        },
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = Color.Transparent
        ),

        )
}


@Composable
fun FabContent(onClick: () -> Unit = {}) {
    FloatingActionButton(
        onClick = { onClick() },
        shape = RoundedCornerShape(50.dp),
        elevation = FloatingActionButtonDefaults.elevation(4.dp),
    ) {
        Icon(
            imageVector = Icons.Default.Add,
            contentDescription = "Add",
        )
    }
}

@Composable
fun TitleSections(label: String) {
    Surface(
        modifier = Modifier.padding(start = 5.dp, top = 1.dp)
    ) {
        Column {
            Text(
                text = label,
                style = TextStyle(
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Start,
                modifier = Modifier.padding(start = 5.dp, top = 5.dp)
            )
        }
    }
}


@Composable
fun HomeContent(
    books: List<JetBook>,
    navController: NavController
) {
    val userName = FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0)
    Column(
        modifier = Modifier
            .padding(2.dp)
            .verticalScroll(rememberScrollState()),
        verticalArrangement = Arrangement.SpaceEvenly,
    ) {

        val readingBooks = books.filter { it.startedReading != null && it.finishedReading == null }
        val finishedBooks = books.filter { it.finishedReading != null }
        val notStartedBooks = books.filter { it.startedReading == null }
        if (readingBooks.isNotEmpty()) {
            BooksRowList(
                books = readingBooks,
                navController,
                label = "Reading Now",
                showProfile = true
            )
        } else {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                TitleSections("Not Reading Any Book!")
                ProfileIcon(navController, userName)
            }
        }
        if (notStartedBooks.isNotEmpty()) {
            BooksRowList(books = notStartedBooks, navController)
        } else {
            Text(
                "No books in reading list! Add Books", modifier = Modifier
                    .padding(5.dp)
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
            )
        }
        if (finishedBooks.isNotEmpty()) BooksRowList(
            books = finishedBooks,
            navController,
            label = "Finished Books"
        )
    }
}

@Composable
fun BooksRowList(
    books: List<JetBook>,
    navController: NavController,
    label: String = "Reading List",
    showProfile: Boolean = false,
) {
    val userName = FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0)
    Row(
        modifier = Modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        TitleSections(label)
        if (showProfile) {
            ProfileIcon(navController, userName)
        }
    }
    HorizontalScrollableComponent(books, navController)
}

@Composable
fun ProfileIcon(
    navController: NavController,
    userName: String? = FirebaseAuth.getInstance().currentUser?.email?.split("@")?.get(0)
) {
    Column(
        modifier = Modifier.width(70.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Icon(
            imageVector = Icons.Filled.AccountCircle,
            contentDescription = "Profile",
            modifier = Modifier
                .clickable { navController.navigate(JetScreens.StatsScreen.name) }
                .padding(start = 5.dp, top = 5.dp)
                .size(35.dp),
            tint = MaterialTheme.colorScheme.secondary
        )
        Text(
            modifier = Modifier
                .padding(2.dp)
                .align(Alignment.CenterHorizontally),
            text = userName ?: "null",
            style = MaterialTheme.typography.bodySmall,
            textAlign = TextAlign.Start,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun HorizontalScrollableComponent(
    books: List<JetBook>,
    navController: NavController
) {
    LazyRow(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp),
        contentPadding = PaddingValues(5.dp)
    ) {
        items(books) { book ->
            ListCard(book = book) {
                navController.navigate(JetScreens.UpdateScreen.name + "/$it")
                Log.d("ListCard", "HorizontalScrollableComponent: $it")
            }
        }
    }
}


@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun ListCard(
    book: JetBook = JetBook(
        title = "My Book",
        author = "Authors",
        image = "https://picsum.photos/200/300",
        isRead = false
    ),
    onClickDetails: (bookId: String) -> Unit = {}
) {
    val context = LocalContext.current
    val resources = context.resources
    val displayMetrics = resources.displayMetrics
    val screenWidth = displayMetrics.widthPixels / displayMetrics.density
    val spacing = 10.dp
    Card(
        modifier = Modifier
            .padding(16.dp)
            .height(242.dp)
            .width(202.dp)
            .clip(RoundedCornerShape(22.dp))
            .clickable { onClickDetails(book.id.toString()) },
        elevation = CardDefaults.cardElevation(6.dp),
    ) {
        Column(
            modifier = Modifier.width(
                screenWidth.dp - (spacing * 2)
            ),
            horizontalAlignment = Alignment.Start
        ) {
            Row(
                horizontalArrangement = Arrangement.Center,
            ) {
                Image(
                    painter = rememberAsyncImagePainter(
                        ImageRequest.Builder(LocalContext.current).data(data = book.image).build(),
                    ),
                    contentDescription = "Book Image",
                    modifier = Modifier
                        .height(140.dp)
                        .width(100.dp)
                        .clip(RoundedCornerShape(topStart = 22.dp)),
                    contentScale = ContentScale.Crop,
                )
                Spacer(modifier = Modifier.width(50.dp))
                Column(
                    modifier = Modifier.padding(top = 25.dp),
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    var favorite by remember { mutableStateOf(book.isFavorite) }

                    var selected by remember {
                        mutableStateOf(false)
                    }
                    val size by animateDpAsState(
                        targetValue = if (selected) 42.dp else 34.dp,
                        spring(Spring.DampingRatioMediumBouncy)
                    )
                    val iconColor = if (favorite) Color.Red.copy(0.6f) else Color.Gray
                    val icon =
                        if (favorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder
                    Icon(
                        imageVector = icon,
                        contentDescription = "Favorite",
                        modifier = Modifier
                            .padding(bottom = 1.dp)
                            .width(size)
                            .height(size)
                            .pointerInteropFilter {
                                when (it.action) {
                                    MotionEvent.ACTION_DOWN -> {
                                        selected = true
                                        val fireStore = Firebase.firestore
                                        val favValue = !favorite
                                        fireStore
                                            .collection("books")
                                            .document(book.id.toString())
                                            .update("favorite", favValue)
                                            .addOnSuccessListener {
                                                Log.d(
                                                    "ListCard",
                                                    "ListCard: ${book.title} is now a favorite"
                                                )
                                                favorite = favValue
                                            }
                                            .addOnFailureListener(Exception::printStackTrace)
                                    }
                                    MotionEvent.ACTION_UP -> {
                                        selected = false
                                    }
                                }
                                true
                            },
                        tint = iconColor,
                    )
                    BookRating(book.rating)
                }

            }
            Text(
                modifier = Modifier.padding(5.dp),
                text = book.title.toString(),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
            Text(
                modifier = Modifier.padding(5.dp),
                text = book.author.toString(),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Normal
                ),
                textAlign = TextAlign.Center,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
            )
        }
        Row(
            modifier = Modifier
                .align(Alignment.End),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.Bottom
        ) {
            val readingStatus =
                if (book.startedReading != null && book.finishedReading == null) "Reading" else if (book.finishedReading != null && book.startedReading != null) "Read" else "Not Started"
            RoundedButton(label = readingStatus, radius = 22, onClick = {})
        }
    }
}


@Composable
fun RoundedButton(
    @SuppressLint("ModifierParameter") modifier: Modifier = Modifier
        .padding(5.dp)
        .height(70.dp),
    label: String = "Reading",
    radius: Int = 29,
    isClickable: Boolean = false,
    onClick: () -> Unit = {}
) {
    Surface(
        modifier = if (isClickable) Modifier.clickable { onClick() } else Modifier,
        shadowElevation = 4.dp,
        shape = RoundedCornerShape(bottomEnd = radius.dp, topStart = radius.dp),
        color = MaterialTheme.colorScheme.secondary
    ) {
        Column(
            modifier = modifier,
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                modifier = Modifier.padding(5.dp),
                text = label,
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}

@Composable
fun BookRating(
    score: Int? = 4
) {
    Surface(
        modifier = Modifier
            .padding(5.dp)
            .height(70.dp),
        shadowElevation = 4.dp,
        shape = RoundedCornerShape(50),
        color = MaterialTheme.colorScheme.secondary
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Rounded.Star,
                contentDescription = "Favorite",
                modifier = Modifier.padding(bottom = 1.dp)
            )
            Text(
                modifier = Modifier.padding(5.dp),
                text = score.toString(),
                style = TextStyle(
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                ),
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSecondary
            )
        }
    }
}