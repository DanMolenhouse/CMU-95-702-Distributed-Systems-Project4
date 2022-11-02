# Distributed-Systems-Project4-AndroidApplication
**Project 4 - Distributed Systems**

**Project Objective:**

1. Implement a native Android application
2. Implement a web service deployed to Heroku using Docker that executes all business logic of the Android application.
3. Handle error conditions (ie. invalid inputs, network errors, API unavaibility, API invalid responses, etc.)
4. Log all traffic through webservice and store data in a noSQL database using MongoDB
5. Create HTML webpage dashboard that displays analytics from traffic logs

Diagram of components and interactions:

<img width="500" alt="Project4-Diagram" src="https://user-images.githubusercontent.com/114946651/199077118-e97703ee-1123-44d5-a3b6-dfb0369a4862.png">


**Tasks:**
1. Choose a 3rd party API and create simple Java application that can make a request & receive a JSON response.
2. Create a MongoDB database on Atlas and create a Java application that can:
    - Prompt the user for a string
    - Write the string to the database
    - Read all documents on the database
    - Print the strings in each document. 
4. Create Android application that has the following:
    - Three different types of Views in Layout (ie. TextView, EditText, ImageView, etc)
    - Takes input from user
    - Makes an HTTP request to web service
    - Recieves and parses JSON response
    - Displays new information to user from response
    - Is repeatable (doesn't require restart for another query)
5. Create the webservice that has the following:
    - Implements an API
    - Receives HTTP request from Android application
    - Executes all business logic appropriate to the applicaiton. This includes fetching JSON information from API, and processing the response.
    - Replies to the Android application with JSON response.
    - Deployed to Heroku using Docker. 
    - Must use Servlets.
6. Application and web service should both test for and handle errors in the following:
    - Invalid input in the Android application.
    - Invalid server-side input (in the case that application input validation did not catch something).
    - Mobile app network failure, or inability to reach server.
    - Third-party API unavailability.
    - Third-party API invalid data or response.
7. Implement logging capabilities with the following requirements:
    - Tracks at least 6 metrics (ie time of request, time to reply, information requested, etc)
    - Stores data in noSQL database using MongoDB.
    - Does CRUD ooperations programmatically from a Java program.
8. Create a web-based HTML operations dashboard that:
    - Has a unique URL address.
    - Displays at least three useful and unique operation analytics.
    - Displays the formatted usage logs.

**Topics/Skills covered:**
- Docker
- Cloud-based application / Heroku
- Android mobile applications / Android Studio
- Servlet programming
- Error handling
- HTML web application
- MVC architecture
- HTTP
- RESTful API
- JSON
- noSQL Databases / MongoDB / Atlas
- CRUD operations

**Demonstration of completed tasks:**

For my project, I created an Android application that allows users to search for TV shows. The application makes calls to the iTunes API based on the user input, which for the purposes of this assignment was just TV Show Title. A list of results is then generating, providing the user with the title of the show, the artwork for the show, and the title of the episode. The user can then make another search from the results page, making the application repeatable.

Screenshot of main landing page of app:

<img width="200" alt="Picture1" src="https://user-images.githubusercontent.com/114946651/199109841-8dc4848e-3d8a-4379-a63f-fed237d8403a.png">

Heres a user entering a search for “Avatar”:

<img width="200" alt="Picture1" src="https://user-images.githubusercontent.com/114946651/199109990-345dfd61-411d-436c-a99c-e5eade66da2b.png">

**Heroku implementation:**
The “search” method in “itunesGet.java” is where the HTTP request is made, with the following URL:

https://pure-shore-63134.herokuapp.com/getTvShows/(searchterm)
    
(searchterm) is the user input string
    
The heroku app is still running and a search term can manually be entered to see the JSON reply from the iTunes API! The code that is deployed to Heroku can be found in the Project1Task1 folder.

This tells the web application to enact it’s “doGet” method with that search term, and uses it to get a Json Reply from the itunes API with the given search term.

JSON Reply Example:
![Picture1](https://user-images.githubusercontent.com/114946651/199110542-acf6470a-8394-4ce0-8c94-169c10d390dc.png)

Same result in a JSON viewer:
    
![Picture1](https://user-images.githubusercontent.com/114946651/199110606-62a0e954-cd2f-45ae-b03c-27f46a459bb5.png)

Response from Heroku web service displayed in Android application:
    
<img width="200" alt="Picture1" src="https://user-images.githubusercontent.com/114946651/199110720-69928983-b3bf-496a-90ce-8154573ffa7f.png">

**Information Logging / Analytics Dashboard**

Information logged:

1. Device type used to access DB
2. Browser used to access DB
3. Search term
4. Number of results
5. Date and time DB was accessed
6. Entire JsonReply is stored here

Screenshot from MongoDB:

<img width="468" alt="image" src="https://user-images.githubusercontent.com/114946651/199111269-664271c8-648e-4c2d-b049-978a71734ddf.png">

Screenshot of web-based HTML dashboard:

<img width="468" alt="image" src="https://user-images.githubusercontent.com/114946651/199111415-542cfed4-ed3f-4a9f-8d3f-ae03bee8e5f9.png">

Three analytics presented: last date the DB was accessed, most recent search query, and a list of all search terms. Note that a better implementation of the "list of all search terms" would only display a single instance of each unique search term, rather than showing multiple instances of the same search term. 
