Tic-Tac-Toe GUI Game (Java / Swing / Gradle)
Overview

This project is a GUI-based Tic-Tac-Toe game developed in Java using Swing and Gradle.
The application allows two players to play Tic-Tac-Toe, keeps track of their wins and losses, and saves the data to a file after each game. The project follows object-oriented programming (OOP) principles, clean code practices, and modular design.

Functionalities

Play a Tic-Tac-Toe match between two players.

Before each match, players are prompted to enter their nicknames.

Scoreboard keeps track of wins and losses for each player.

Persistent storage: Scores are saved to a file (scoreboard.txt) after every game.

New Match option: Start a new match without restarting the application.

Quit option: Exit the game safely.

GUI Improvements:

Color-coded winning line highlight.

Stylish X and O buttons.

Clear scoreboard panel.

Technologies Used

Java 25

Swing for GUI

Gradle for project management and build

Collections Framework (for storing and updating scores)

File I/O for persistent scoreboard

Project Structure
ttt/
├─ src/
│  └─ main/
│     └─ java/
│        └─ org/
│           └─ example/
│              ├─ Main.java          # Launches the application and shows start menu
│              ├─ TicTacToeGame.java # Main game GUI and logic
│              ├─ Board.java         # Handles the game board state
│              └─ Scoreboard.java    # Tracks player wins/losses and saves to file
├─ build.gradle.kts
├─ gradlew
├─ gradlew.bat
├─ settings.gradle.kts
└─ README.md

How to Run

Clone the repository:

git clone https://github.com/Sarbocha/OOP_Project.git
cd OOP_Project


Run with Gradle:

./gradlew run   # On Linux / Mac
gradlew.bat run # On Windows


Gameplay:

Click Start Game on the main menu.

Enter nicknames for Player 1 and Player 2.

Play Tic-Tac-Toe by clicking the cells.

After a win, loss, or draw, the board resets automatically.

Use Options → New Match to start another game.

Use Options → Quit to exit the application.

Screenshots

(Optional: Add screenshots of the GUI here if you like)

OOP Principles Followed

Encapsulation: Classes Board and Scoreboard hide internal data and provide methods for safe access.

Abstraction: GUI interacts with Board and Scoreboard via simple methods.

Modularity: Separate classes for GUI, board logic, and score handling.

Clean Code: Meaningful variable names, slight comments, modular methods, and proper formatting.

Persistent Scoreboard

All player scores are saved in scoreboard.txt.

The file updates after every game, ensuring scores are not lost when the application closes.

Author

Sarbocha Pandey
CS-3354 Course Project – Tic-Tac-Toe GUI Game
