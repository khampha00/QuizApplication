Of course. Here is a `README.md` file for your project.

# Spring Boot Quiz Application

This project is a web-based Quiz Application developed using Java and the Spring Boot framework. It functions as a traditional server-side rendered application using Spring MVC to manage user interactions and serve dynamic web pages. The application provides functionalities for both administrators to manage questions and for users to take quizzes.

## Features

*   **User Authentication**: Secure user registration and login.
*   **Admin Panel**:
    *   View all quiz questions.
    *   Add new questions with options and a correct answer.
    *   Edit existing questions.
    *   Delete questions.
*   **User Quiz Interface**:
    *   Take a quiz with questions loaded from the database.
    *   Submit answers for scoring.
    *   View the final score upon completion.

## Technologies Used

*   **Backend**: Java, Spring Boot, Spring MVC, Spring Security
*   **Frontend**: Thymeleaf, HTML, CSS
*   **Build Tool**: Maven
*   **Database**: In-memory H2 Database (or can be configured for others like MySQL/PostgreSQL)

## Getting Started

### Prerequisites

*   Java JDK 17 or newer
*   Apache Maven

### Installation & Running the Application

1.  **Clone the repository:**
    ```bash
    git clone https://github.com/khampha00/QuizApplication.git
    cd QuizApplication
    ```

2.  **Build and run the project using Maven:**
    ```bash
    mvn spring-boot:run
    ```

3.  **Access the application:**
    Open your web browser and navigate to `http://localhost:8080`. You will be redirected to the login page.

## Application Routes

The application exposes the following web pages:

*   `/login`: User login page.
*   `/register`: User registration page.
*   `/quiz-list`: (Admin) View, add, edit, and delete quiz questions.
*   `/add-quiz`: (Admin) Form to add a new question.
*   `/edit-quiz/{id}`: (Admin) Form to edit an existing question.
*   `/quiz`: (User) The main quiz page where users can answer questions.
*   `/submit`: (User) Endpoint to handle quiz submission and calculate the score.
*   `/result`: (User) Displays the final score to the user.
