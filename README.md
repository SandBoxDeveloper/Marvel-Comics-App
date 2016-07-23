[![Build Status](https://travis-ci.org/SandBoxDeveloper/Marvel-Comics-App.svg?branch=master)](https://travis-ci.org/SandBoxDeveloper/Marvel-Comics-App)
[![codecov.io](https://codecov.io/github/SandBoxDeveloper/Marvel-Comics-App/coverage.svg?branch=master)](https://codecov.io/github/SandBoxDeveloper/Marvel-Comics-App?branch=master)

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

# Libraries used in this project

* [Marvel] [marvelApi]
* [OkHttp] [okHttp]
* [JUnit] [junit]
* [Mockito] [mockito]
* [Glide] [glide]


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

