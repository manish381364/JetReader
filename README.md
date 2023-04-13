# JetReader: A Modern Book Tracking App
JetReader is a user-friendly book tracking app that helps users keep track of their reading progress with ease. Whether you are an avid reader or just starting, JetReader can help you organize your reading list by categorizing your books as "Currently Reading," "To Read," and "Read."

The app is developed using the latest technologies and architecture patterns such as Hilt Dagger, Retrofit, MVVM Clean Architecture, Material Design 3, Firebase Firestore, Coroutines Lifecycle, and Navigation Graph of Jetpack Compose. With its sleek design and seamless user flow, JetReader provides a modern and efficient book tracking experience.

The Navigation Graph of Jetpack Compose creates a smooth user flow, starting with the SplashScreen and leading to various screens such as HomeScreen, SearchScreen, LoginSignUpScreen, BookDetailsScreen, UpdateScreen, and StatsScreen.

The HomeScreen and SearchScreen are the primary screens, using HomeScreenViewModel and BooksSearchViewModel, respectively. The Hilt Dagger dependency injection framework is used to inject these ViewModels, which communicate with the Firebase Firestore and Google Books API to get the required data.

The StatsScreen displays the statistics of the app's usage, such as the number of books read and the number of books in the library. It uses HomeScreenViewModel and Hilt injection to fetch the required data.

JetReader follows the MVVM Clean Architecture pattern, where the View layer is implemented using Jetpack Compose, the ViewModel layer is implemented using Hilt injection, and the Model layer communicates with Firebase Firestore and Google Books API to provide the data to the ViewModel.

Material Design 3 provides a modern and intuitive design to the app, while Coroutines Lifecycle handles the asynchronous tasks efficiently.




## Different Screens
#### 1. LoginScreen 2. SignUpScreen 3. HomeScreen 4. SearchScreen 5. BookDetailsScreen 6. UpdateScreen 7. BookStatusScreen
<img class="image" src="https://raw.githubusercontent.com/manish381364/JetReader/master/app/src/main/res/drawable/LoginScreen.png" width="300"/>  <img src="https://raw.githubusercontent.com/manish381364/JetReader/master/app/src/main/res/drawable/SignUpScreen.png" width="300"/> <img src="https://raw.githubusercontent.com/manish381364/JetReader/master/app/src/main/res/drawable/HomScreen.png" width="300"/> <img src="https://raw.githubusercontent.com/manish381364/JetReader/master/app/src/main/res/drawable/SearchScreen.png" width="300"/> <img src="https://raw.githubusercontent.com/manish381364/JetReader/master/app/src/main/res/drawable/BookDetailsScreen.png" width="300"/>  <img src="https://raw.githubusercontent.com/manish381364/JetReader/master/app/src/main/res/drawable/UpdateScreen.png" width="300"/>  <img src="https://raw.githubusercontent.com/manish381364/JetReader/master/app/src/main/res/drawable/BookStatusScreen.png" width="300"/>


In summary, JetReader is a modern, efficient, and user-friendly app that helps book enthusiasts organize their reading list with ease. So, whether you're a casual reader or a bookworm, JetReader is the perfect app for you. Development is still ongoing, so stay tuned for more updates!

