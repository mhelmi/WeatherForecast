# WeatherForecast

Simple app utilize a weather API http://openweathermap.org/API to search for a city and get the forecast.

## Architecture
I choose to use Clean architecture based on MVVM (Model-View-ViewModel)
The following diagram shows all the layers and how each layer interact with each other. This architecture using a layered software architecture.
![MVVM](https://github.com/mhelmi/WeatherForecast/blob/master/art/mvvm_architecture.png)
![Clean Architecture](https://github.com/mhelmi/WeatherForecast/blob/master/art/clean_architecture.jpg)
## Built With 🛠
* [Kotlin](https://kotlinlang.org/) - official programming language for Android development.
* [Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html) - for asynchronous or non-blocking programming.
* [Android Architecture Components](https://developer.android.com/topic/libraries/architecture) - Part of Jetpack it's a set of libraries that help you design robust, testable, and maintainable apps.
  - [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) - an observable data holder class.
  - [ViewModel](https://developer.android.com/topic/libraries/architecture/viewmodel) - store and manage UI-related data in a lifecycle conscious way. 
  - [Room](https://developer.android.com/topic/libraries/architecture/room) - persistence library provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.
  - [Navigation](https://developer.android.com/guide/navigation) - Navigation refers to the interactions that allow users to navigate across, into, and back out from the different pieces of content within your app.
* [Dagger2](https://github.com/google/dagger) - Dependency Injection Framework.
* [Retrofit](https://square.github.io/retrofit/) - A type-safe HTTP client for Android.
* [Gson](https://github.com/google/gson) A Java serialization/deserialization library to convert Java Objects into JSON and back.
* [sdp](https://github.com/intuit/sdp) - size unit scales with the screen size.
