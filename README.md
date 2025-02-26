# SudokuMaster

**SudokuMaster** is a Java-based Sudoku game application featuring:

- **Light/Dark Mode:** Toggle between light and dark themes.
- **Difficulty Selection:** Choose between Easy, Medium, and Hard levels with dynamic puzzle generation.
- **Cell Selection & Error Highlighting:** Select cells via mouse and see invalid moves highlighted.
- **Guide Highlighting:** Visual cues to show valid numbers for the selected cell.
- **Annotation Mode:** Supports both manual and automatic annotations for candidate numbers.
- **Game Completion Dialog:** On solving the puzzle, a congratulatory message appears with options to restart or start a new game.

---

## Project Structure

```
SudokuMaster/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/sudokumaster/
│   │   │       ├── model/         # Game logic, board management, puzzle generation, annotations
│   │   │       ├── view/          # GUI implemented with Swing
│   │   │       └── controller/    # Controller for UI and model interaction
│   │   └── resources/           # Additional resources (images, config files)
│   └── test/                    # Unit tests using JUnit 5
└── README.md
```

---

## Setup & Installation

1. **Clone the Repository:**

   ```bash
   git clone https://github.com/yourusername/SudokuMaster.git
   ```

2. **Open the Project:**

   Open the project in IntelliJ IDEA.

3. **Configure Maven:**

   Ensure Maven is installed and added to your system's PATH.

4. **Build and Run:**

   Use the `Main` class located in the project to start the application.

---

## Running Tests

Execute the following command in the project root to run the unit tests:

```bash
mvn test
```

---

## Contribution Guidelines

- **Commit Messages:**  
  Use clear, descriptive messages. For example:  
  `[controller] Integrated Annotation Mode in number input, toggling annotations and updating board display to show notes.`

- **Branch Naming:**  
  Name branches following conventions like `feature/<feature-name>` or `bugfix/<bug-description>`.

- **Code Comments:**  
  All code is thoroughly commented to assist future contributors. Please maintain or enhance the commenting when making changes.

---

## Exception Handling

The project includes basic exception handling, particularly around user inputs and board updates. Future iterations may incorporate more robust error logging and handling strategies.

---


