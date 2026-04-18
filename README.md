Whack-a-Mole Game (Java)

Description:
This project is a Java-based Whack-a-Mole game built using a graphical user interface. The game allows users to interact with a grid of holes where different elements such as moles, bonus moles, and bombs appear randomly. The player earns or loses points based on their actions.

Features:

Interactive GUI built using Java Swing
Random spawning of moles, bonus moles, and bombs
Score tracking and countdown timer
High score saving and loading using file handling
Game over conditions and replay option
Menu controls for starting, stopping, and viewing scores

How to run:
Compile all Java files:
javac *.java

Run the program:
java GameBoard

How it works:
The game uses a central game board to manage the interface and user interactions. A game engine runs in a separate thread to handle spawning of objects and timing. Different types of hole occupants such as moles, bonus moles, and bombs have different behaviors and scoring rules. High scores are stored using file serialization.

Limitations:

Fixed grid size
No difficulty levels
Requires local images to run properly

Author:
Diggaj Rupani
