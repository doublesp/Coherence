# Garçon de cuisine

An app for dinner makers to search for recipe, create a shopping list, and send it to their significant others to have them buy the ingredients back.

## User Stories

* [ ] User can create a grocery list.
  * [x] User can add items to a grocery.
  * [ ] User can crossout/un-crossout an item in grocery list by swiping horizontally.
  * [ ] User can remove an item from the grocery list by long press.
  * [ ] User can see auto-complete suggestions when creating the grocery list.
  * [x] User can save a grocery list on server.  
* [ ] User can share the grocery list.
  * [x] User can share the grocery list as a link. Tapping on the link would direct them back to our app with the grocery list shown.
  * [ ] If a user didn't install our app, he should see a web-version of the grocery list, with smart banner which prompts the user to download the app.
  * [x] User can share the grocery list internally with another user.
  * [ ] User will get push notification if a list is shared with him/her internally.
* [ ] User can search for recipes.
  * [x] User can search for a recipe contains a specific keyword.
  * [ ] User can see auto-complete suggestions when searching for a recipe.
  * [x] User can see trending recipes suggested by the system.
  * [x] User can see detail instruction of a recipe.
  * [x] User can bookmark a recipe.
* [x] User can create a grocery list from a recipe.
* [ ] User can reversely search for a recipe that contains as many ingredients on a grocery list, and as little missing ingredients as possible.

 
## Video Walkthrough

Here's a walkthrough of implemented user stories:

![Video Walkthrough](garcon.gif)

GIF created with [LiceCap](http://www.cockos.com/licecap/).

## Architecture

![Architecture](architecture.png)

## Open-source libraries used

- [Retrofit](https://github.com/square/retrofit) - Type-safe HTTP client for Android and Java by Square, Inc.
- [RxJava](https://github.com/ReactiveX/RxJava) - Reactive Extensions for the JVM – a library for composing asynchronous and event-based programs using observable sequences for the Java VM.
- [Stetho](http://facebook.github.io/stetho/) - A debug bridge for Android applications.
- [Signpost](https://github.com/pakerfeldt/okhttp-signpost) - A Signpost extension for signing OkHttp requests.
- [GSON](https://github.com/google/gson) - A Java serialization/deserialization library that can convert Java Objects into JSON and back.
- [Glide](https://github.com/bumptech/glide) - An image loading and caching library for Android focused on smooth scrolling.
- [RetroLambda](https://github.com/evant/gradle-retrolambda) - A gradle plugin for getting java lambda support in java 6, 7 and android.
- [Parceler](http://parceler.org/) - Android Parcelable code generator for Google Android.
- [scribe-java](https://github.com/fernandezpablo85/scribe-java) - Simple OAuth library for handling the authentication flow.
- [codepath-oauth](https://github.com/thecodepath/android-oauth-handler) - Custom-built library for managing OAuth authentication and signing of requests
- [DBFlow](https://github.com/Raizlabs/DBFlow) - Simple ORM for persisting a local SQLite database on the Android device
- [Dagger](http://google.github.io/dagger/) - A fully static, compile-time dependency injection framework for both Java and Android.

## License

    Copyright [2016] [pinyaoting]

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.

