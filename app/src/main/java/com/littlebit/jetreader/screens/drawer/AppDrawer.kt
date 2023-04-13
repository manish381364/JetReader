package com.littlebit.jetreader.screens.drawer

import android.net.Uri
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person2
import androidx.compose.material.icons.rounded.ArrowBack
import androidx.compose.material.icons.rounded.Brightness2
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.material.icons.rounded.WbSunny
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import com.google.firebase.auth.FirebaseAuth
import com.littlebit.jetreader.navigation.JetScreens
import com.littlebit.jetreader.ui.theme.JetFontFamily

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun AppDrawer(
    navController: NavHostController = rememberAnimatedNavController(),
    isDarkTheme: MutableState<Boolean>,
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(start = 50.dp),
                        text = "JetReader",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = MaterialTheme.typography.displayMedium.fontSize,
                            fontStyle = MaterialTheme.typography.displayMedium.fontStyle,
                            fontWeight = MaterialTheme.typography.displayMedium.fontWeight,
                            fontFamily = MaterialTheme.typography.displayMedium.fontFamily
                        ),
                    )
                },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                },
            )
        }
    ) {
        Box(Modifier.padding(it)) {
            val profileImage: Uri? = FirebaseAuth.getInstance().currentUser?.photoUrl
            val userName: String? = FirebaseAuth.getInstance().currentUser?.displayName
            val email: String? = FirebaseAuth.getInstance().currentUser?.email
            AppDrawerContent(profileImage, userName, email, navController, isDarkTheme)
        }
    }
}

@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppDrawerContent(
    profileImage: Uri? = Uri.EMPTY,
    userName: String? = "User Name",
    email: String? = "Email@xyz.com",
    navController: NavHostController = rememberAnimatedNavController(),
    isDarkTheme: MutableState<Boolean>
) {
    Column(
        modifier = Modifier
            .clip(RoundedCornerShape(topStart = 100.dp, topEnd = 100.dp))
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(
                Brush.sweepGradient(
                    listOf(
                        Color.Red.copy(0.5f),
                        Color.Blue.copy(0.5f),
                        Color.Blue.copy(0.5f),
                        Color.Red.copy(0.5f),
                    )
                )
            )
    ) {

        Spacer(Modifier.height(20.dp))
        var loading by remember { mutableStateOf(true) }
        ElevatedCard(
            modifier = Modifier
                .align(Alignment.CenterHorizontally),
            shape = CircleShape,
            elevation = CardDefaults.cardElevation(10.dp)
        ) {

            Box(
                Modifier
                    .size(100.dp)
                    .background(MaterialTheme.colorScheme.onSurfaceVariant)
            ) {
                AsyncImage(
                    model = profileImage ?: Icons.Filled.Person2,
                    modifier = Modifier
                        .size(100.dp)
                        .clip(CircleShape),
                    contentDescription = "Profile Image",
                    onSuccess = { loading = false },
                    onError = { loading = false },
                    onLoading = { loading = true },
                    contentScale = ContentScale.Crop,
                    filterQuality = FilterQuality.High
                )
                if (loading) {
                    CircularProgressIndicator(
                        Modifier
                            .size(30.dp)
                            .align(Alignment.Center)
                    )
                }
            }
        }

        Spacer(Modifier.height(20.dp))

        ElevatedCard(
            Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(Color.Transparent)
        ) {
            Text(
                text = userName ?: "User Name",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontStyle = FontStyle.Italic,
                    fontFamily = JetFontFamily
                ),
                modifier = Modifier.padding(start = 20.dp, top = 5.dp, bottom = 5.dp)
            )
        }
        Spacer(Modifier.height(10.dp))
        ElevatedCard(
            Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(Color.Transparent)
        ) {
            Text(
                text = email ?: "email@xyz.com",
                style = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                    fontStyle = FontStyle.Italic,
                    fontFamily = JetFontFamily
                ),
                modifier = Modifier.padding(start = 20.dp, top = 5.dp, bottom = 5.dp)
            )
        }
        Spacer(Modifier.height(10.dp))
        val theme = isDarkTheme.value
        val currentTheme = remember { mutableStateOf(theme) }
        AnimatedContent(targetState = currentTheme.value) {
            ElevatedCard(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .clickable {
                        currentTheme.value = !currentTheme.value
                        isDarkTheme.value = !isDarkTheme.value
                    }
                    .indication(
                        indication = null,
                        interactionSource = remember { MutableInteractionSource() }),
                shape = RoundedCornerShape(10.dp),
                colors = CardDefaults.cardColors(Color.Transparent)
            ) {

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(5.dp)
                ) {
                    Text(
                        text = if (currentTheme.value) "Light Mode" else "Dark Mode",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onSurface,
                            fontSize = MaterialTheme.typography.titleLarge.fontSize,
                            fontStyle = FontStyle.Italic,
                            fontFamily = JetFontFamily
                        ),
                        modifier = Modifier.padding(start = 20.dp, top = 5.dp, bottom = 5.dp)
                    )
                    Spacer(Modifier.width(10.dp))
                    Icon(
                        imageVector = if (currentTheme.value) Icons.Rounded.WbSunny else Icons.Rounded.Brightness2,
                        contentDescription = "Theme"
                    )
                }
            }
        }
        Spacer(Modifier.height(20.dp))
        ElevatedCard(
            Modifier
                .fillMaxWidth()
                .padding(start = 10.dp, end = 10.dp)
                .clip(RoundedCornerShape(10.dp))
                .clickable {
                    FirebaseAuth
                        .getInstance()
                        .signOut()
                    navController.navigate(JetScreens.LoginSignUpScreen.name) {
                        popUpTo(JetScreens.HomeScreen.name) {
                            inclusive = true
                        }
                    }
                }
                .indication(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }),
            shape = RoundedCornerShape(10.dp),
            colors = CardDefaults.cardColors(Color.Transparent)
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Log Out",
                    style = TextStyle(
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                        fontStyle = FontStyle.Italic,
                        fontFamily = JetFontFamily
                    ),
                    modifier = Modifier.padding(start = 20.dp, top = 5.dp, bottom = 5.dp)
                )
                Spacer(Modifier.width(20.dp))
                Icon(imageVector = Icons.Rounded.Logout, contentDescription = "Log Out")
            }
        }
    }
}