package com.littlebit.jetreader.components

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.littlebit.jetreader.model.Item
import com.littlebit.jetreader.navigation.JetScreens
import com.littlebit.jetreader.screens.search.BooksSearchViewModel

@Composable
fun SearchScreenFullContent(
    it: PaddingValues,
    navController: NavHostController,
    viewModel: BooksSearchViewModel
) {
    Box(
        modifier = Modifier.padding(it),
        contentAlignment = Alignment.TopCenter
    ) {
        Surface {
            SearchForm(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                navController = navController,
            ) { search ->
                Log.d("Search", "SearchScreen: $search")
                viewModel.searchBooks(search)
            }
        }
    }
}

@OptIn(ExperimentalComposeUiApi::class, ExperimentalMaterial3Api::class)
@Composable
fun SearchForm(
    modifier: Modifier = Modifier,
    navController: NavController,
    hint: String = "Search",
    onSearch: (String) -> Unit = { _ -> },
) {
    val searchQuery = rememberSaveable { mutableStateOf("") }
    val keyboardController = LocalSoftwareKeyboardController.current
    Column (modifier){
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = searchQuery.value,
            maxLines = 1,
            onValueChange = {searchQuery.value = it},
            label = { Text("Search") },
            placeholder = { Text(hint) },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Search),
            keyboardActions = KeyboardActions {
                if(searchQuery.value.isNotEmpty())
                    onSearch(searchQuery.value.trim())
                keyboardController?.hide()
            },
            leadingIcon = {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "SearchIcon"
                )
            },

            )
        Spacer(modifier = Modifier.height(16.dp))
        SearchBookList(navController = navController)
    }
}


@Composable
fun SearchBookList(
    navController: NavController,
    viewModel: BooksSearchViewModel = hiltViewModel()
) {

    if (viewModel.isLoading){
        Row(
            modifier = Modifier.padding(end = 2.dp),
            horizontalArrangement = Arrangement.SpaceAround,
            verticalAlignment = Alignment.CenterVertically) {
            LinearProgressIndicator()
            Spacer(modifier = Modifier.width(16.dp))
            Text(text = "Loading...")
        }

    } else {
        LazyColumn {
            items(viewModel.list) { book ->
                SearchBookItem(book = book) {
                    navController.navigate(JetScreens.BookDetailsScreen.name + "/${book.id}")
                }
            }
        }
    }
}

@Composable
fun SearchBookItem(
    book: Item?,
    onClick: () -> Unit = { }
) {
    Card(
        modifier = Modifier
            .padding(top = 8.dp)
            .clickable { onClick() },
        elevation = CardDefaults.cardElevation(4.dp),
        shape = RoundedCornerShape(22.dp),
    ){
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
                model = book?.volumeInfo?.imageLinks?.thumbnail?: "https://picsum.photos/200/300",
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
                Text(
                    text = book?.volumeInfo?.title?: "No Title",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = TextStyle(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    )
                )
                Text(
                    text = book?.volumeInfo?.authors.toString(),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = book?.volumeInfo?.publishedDate?: "No Date",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = book?.volumeInfo?.description?: "No Description",
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}