[![Build Status](https://travis-ci.org/SandBoxDeveloper/Marvel-Comics-App.svg?branch=master)](https://travis-ci.org/SandBoxDeveloper/Marvel-Comics-App)
[![codecov](https://codecov.io/gh/SandBoxDeveloper/Marvel-Comics-App/branch/master/graph/badge.svg)](https://codecov.io/gh/SandBoxDeveloper/Marvel-Comics-App)


# Marvel Comics
A simple Android app that provides easy access to a list of available comics hosted by Marvel, and detailed information about each one of them.

## Features

With the app, you can:
* Discover the latest Marvel comics
* Read details (Date Published, Price, Page Count and Creators)

# Getting Started

This sample uses the Gradle build system. To build this project, use the "gradlew build" command or use "Import Project" in Android Studio.


## How to Work with the Source

This app uses [Marvel API](http://developer.marvel.com/docs) to retrieve comics.
You must provide your own Public API key in order to build the app. When you get it, just paste it to:
    ```
    secrets.properties
    ```
# Tests

Android tests are located under src/androidTest/java

See the test suite MarvelComics/app/src/androidTest/java/com/hulldiscover/zeus/marvelcomics/FullTestSuite.java 
to run all test at once.

# Libraries used in this project

* [Marvel] [marvelApi]
* [OkHttp] [okHttp] - HTTP
* [JUnit] [junit] - Unit Test
* [Mockito] [mockito] - Unit Test
* [Glide] [glide] - Image Libaray


# Notes

How solution could be improved:

* Implement Spinners - to provide a quick way to select "order by/sort by" options from a set [title, issue number, etc]. 
* Include Shared Preferences/ Settings - with the ability toggle the inclusion comic collections or series.
* Implement Pull To Refresh - to update list of comics.
* Add Favorites feature - to provide to ability for users to save their favourite comics.
* Implement some UI Test using Mokito and JUnit.
* Given a budget, the app should be able to find and list the maximum number of comics that you can buy without exceeding that budget from the 100 comics listed previously. Also all the comics combined should give you the maximum number of pages you can read.
* Share button - to share comic details with friends.


What I would do better: 

* Improve loading time to display images in the app.
* Improve offline capabilities [stored data, caching]
* Use [Android-ObservableScrollView] (https://github.com/ksoichiro/Android-ObservableScrollView) for detailed activity. 
* Improve Fluid UI.


Features that need fixes :

* Continuos Integration script currently isnt succesfully building. I need to change the build parameters.
* Code Coverage isn't building. I need to fix the lingage between the app and the code coverage online tool.
* Offline. Currently comic data is stored in SQLite database, and Sync features have been implemented to allow offline viewing. However, this don't not fully function as intended. I will need to investigate what the app does when not internet connectivity is detected.
* Description text in detailed view of comic. The data pulled from the API needs to be formatted for display.
* Poster Image. This image is stretched out, and looks pixalated. I will need to change the aspect ratio.


## License

    Copyright 2016 Andre Hitchman

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.


[marvelApi]: http://developer.marvel.com/
[okHttp]: https://github.com/square/okhttp
[junit]: https://github.com/junit-team/junit
[mockito]: https://github.com/mockito/mockito
[Glide]: https://github.com/bumptech/glide

