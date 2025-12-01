# 🎮 Tic-Tac-Toe GUI Game

![Java](https://img.shields.io/badge/Language-Java-red) ![Swing](https://img.shields.io/badge/GUI-Swing-blue) ![Gradle](https://img.shields.io/badge/Build-Gradle-brightgreen) ![License](https://img.shields.io/badge/License-MIT-yellow)

A **GUI-based Tic-Tac-Toe game** developed in **Java** using **Swing** and **Gradle**.  
Supports **two players**, keeps track of **wins and losses**, and saves the scores to a file after every match.

---

## **Features**

- ✅ Play Tic-Tac-Toe between **two players**.  
- ✅ Prompt for **player nicknames** before each match.  
- ✅ **Scoreboard** tracks wins and losses for each player.  
- ✅ **Persistent storage**: Scores saved to `scoreboard.txt`.  
- ✅ **Start New Match** button.  
- ✅ **Quit Game** option.  
- ✅ Improved **GUI** with styled X and O buttons and highlighted winning lines.  

---

## **Technologies Used**

- Java 25  
- Swing (GUI)  
- Gradle (build and dependency management)  
- Java Collections Framework  
- File I/O for persistent scoreboard  

---

## **Project Structure**
ttt/

├─ src/

│ └─ main/

│ └─ java/

│ └─ org/

│ └─ example/

│ ├─ Main.java # Launches the application

│ ├─ TicTacToeGame.java # GUI and game logic

│ ├─ Board.java # Game board handling

│ └─ Scoreboard.java # Tracks scores, reads/writes file

├─ build.gradle.kts

├─ gradlew

├─ gradlew.bat

├─ settings.gradle.kts

├─ scoreboard.txt

└─ README.md



---

---

## **How to Run**

### ** Clone Repository**


git clone https://github.com/Sarbocha/OOP_Project.git

cd OOP_Project

./gradlew run      # Linux / Mac

gradlew.bat run    # Windows


## Gameplay Instructions

1. Click **Start Game**.  
2. Enter nicknames for **Player 1** and **Player 2**.  
3. Click cells to place **X** or **O**.  
4. Board highlights the winning line automatically.  
5. Use **New Match** to start again.  
6. Quit safely using **Quit**.  

---

## OOP Principles Applied

- **Encapsulation**: `Board` and `Scoreboard` hide internal data; access via methods.  
- **Abstraction**: GUI interacts with classes via simple method calls.  
- **Modularity**: Separate classes for GUI, board logic, and score handling.  
- **Clean Code**: Clear variable names, methods, slight comments, proper formatting.  

---

## Persistent Scoreboard

- Scores are stored in `scoreboard.txt`.  
- File updates **after every match**, ensuring scores persist between sessions.  

---

## Author

**Sarbocha Pandey**  
CS-3354 Course Project – Tic-Tac-Toe GUI Game


