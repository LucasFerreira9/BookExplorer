# Description

This project brings together common features and challenges in Android application development, such as:

* External API search
* CRUD of data in an internal database
* MVVM Architecture pattern
* Efficient results listing 
* Intuitive interface in line with UI design patterns

The application aims to provide a comprehensive example of building a robust Android app, showcasing best practices and modern development techniques.

# Main Used Libraries

* **ViewModel and LiveData:** Part of the Android Architecture Components developed by the Android developer team. These libraries help create an architecture that decouples 
business logic from UI logic, respecting the lifecycle of components and preventing memory leaks. <br>

Combined with the repository design pattern, the following architecture recommended by Google was implemented: 

![](https://miro.medium.com/v2/resize:fit:720/format:webp/1*-yY0l4XD3kLcZz0rO1sfRA.png)

* **ROOM:** Provides an abstraction layer on top of Android's standard SQLite for persistent, structured data saving.

* **okHttp:** Provides an efficient and practical way to perform HTTP communications

* **gson:** A parsing library to deserialize and serialize data in JSON format.


# Build Instructions

Before building the app, paste your Google API key into the **api_keys.properties** file located in the root of the project
