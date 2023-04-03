package com.littlebit.jetreader.screens.home

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.firebase.auth.FirebaseAuth
import com.littlebit.jetreader.model.JetBook
import com.littlebit.jetreader.navigation.JetScreens
import com.littlebit.jetreader.screens.search.BooksSearchViewModel


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController = NavHostController(LocalContext.current),
    viewModel: HomeScreenViewModel = hiltViewModel(),
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            JetReaderAppBar(title = "JetReader", showProfile = true, navController = navController)
        },
        floatingActionButton = {
            FabContent(onClick = { navController.navigate(JetScreens.SearchScreen.name) })
        },
    ) { it ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(it),
        ) {
            var books = emptyList<JetBook>()
            val currentUser = FirebaseAuth.getInstance().currentUser
            if(!viewModel.data.value.data.isNullOrEmpty()){
                books = viewModel.data.value.data!!.toList().filter { jetBook ->
                    jetBook.userId == currentUser?.uid.toString()
                }
                Log.d("FIRESTORE", "HomeScreen: ${books.toString()}")
            }
            HomeContent(books = books, navController = navController)
        }
    }
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun JetReaderAppBar(
    title: String,
    showProfile: Boolean = true,
    navController: NavController,
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
                        ?: Icons.Default.Person else Icons.Default.Person,
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
fun TitleSections(modifier: Modifier, label: String) {
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
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.Start),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TitleSections(modifier = Modifier, label = "Reading Now")
            Spacer(modifier = Modifier.fillMaxWidth(0.7f))
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
        val testBooks = listOf<JetBook>(
            JetBook(
                id = "1",
                title = "The Lord of the Rings",
                author = "J.R.R. Tolkien",
                description = "The Lord of the Rings is an epic high fantasy novel written by English author and scholar J. R. R. Tolkien. The story began as a sequel to Tolkien's 1937 fantasy novel The Hobbit, but eventually developed into a much larger work. Written in stages between 1937 and 1949, The Lord of the Rings is one of the best-selling novels ever written, with over 150 million copies sold.",
                image = "https://picsum.photos/200/300",
                isRead = false,
            ),
            JetBook(
                id = "2",
                title = "The Hobbit",
                author = "J.R.R. Tolkien",
                description = "The Hobbit is a children's fantasy novel by English author J. R. R. Tolkien. It was published on 21 September 1937 to wide critical acclaim, being nominated for the Carnegie Medal and awarded a prize from the New York Herald Tribune for best juvenile fiction. The book remains popular and is recognized as a classic in children's literature.",
                image = "https://picsum.photos/200/300",
                isRead = false,
            ),
            JetBook(
                id = "3",
                title = "The Fellowship of the Ring",
                author = "J.R.R. Tolkien",
                description = "The Fellowship of the Ring is the first volume of J. R. R. Tolkien's epic adventure The Lord of the Rings. It was published on 29 July 1954 in the United Kingdom by George Allen & Unwin, and in the United States by Houghton Mifflin on 21 August 1954. The book is 423 pages long and contains 11 chapters. It is followed by The Two Towers and The Return of the King.",
                image = "https://picsum.photos/200/300",
                isRead = false,
            ),
            JetBook(
                id = "4",
                title = "The Two Towers",
                author = "J.R.R. Tolkien",
                image = "https://picsum.photos/200/300",
                isRead = false,
            ),
            JetBook(
                id = "5",
                title = "The Return of the King",
                author = "J.R.R. Tolkien",
                image = "https://picsum.photos/200/300",
                isRead = false,
            ),
        )
        ListCard(book = if(books.isNotEmpty()) books[0] else testBooks[0]) {
            navController.navigate(JetScreens.UpdateScreen.name + "/$it")
        }
        ReadingCurrentBooks(books, navController)
    }
}

@Composable
fun ReadingCurrentBooks(
    books: List<JetBook>,
    navController: NavController
) {
    TitleSections(modifier = Modifier, label = "Reading List")
    HorizontalScrollableComponent(books, navController)
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
                navController.navigate(JetScreens.BookDetailsScreen.name + "/$it")
                Log.d("ListCard", "HorizontalScrollableComponent: $it")
            }
        }
    }
}


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
                    Icon(
                        imageVector = Icons.Rounded.FavoriteBorder,
                        contentDescription = "Favorite",
                        modifier = Modifier.padding(bottom = 1.dp)
                    )
                    BookRating()
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
            RoundedButton(label = "Reading", radius = 22, onClick = {})
        }
    }
}

@Preview(showBackground = true)
@Composable
fun RoundedButton(
    modifier: Modifier = Modifier
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
    score: Float = 4.5f
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