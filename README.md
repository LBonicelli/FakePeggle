# FakePeggle

* FakePeggle is a project that aims to faithfully replicate the game **Peggle**, described on the internet as a *casual puzzle video games*.
* This project was built using the libGDX game development framework.  
* libGDX is a powerful and versatile framework that provides a wide range of features for developing games in Java.  
* The utilization of libGDX has greatly contributed to the development and functionality of this project.

>Note: The project was concerned only as desktop application.  

## Game Design
* Basically there is a 2D map filled with blue and orange pieces (circle and rectangle) called in the game **pegs**.
* A **cannon** placed high-center fires a **ball** that fall and hit the pegs.
* A **basket** in the bottom of the board moves horizontally.
* A scoreboard in the high part of the screen keep the count of the **balls left** and **orange pegs hit/total orange number**.

## Rules
* I established some basic rules from the original games.
  
* **Win Condition**:  
  the main goal of the game is to hit all the orange pegs with the white balls.
* **Lose Condition**:  
  the game end if all the available balls have been shot.
* If the ball hit the basket you gain a bonus ball (+1 on the available ball counter).

## Screenshots

### How the real Peggle looks like ###
![realPeggle](https://github.com/LBonicelli/FakePeggle/blob/master/assets/realPeggle.jpg?raw=true)
### How FakePeggle looks like ###
![gameScreenshot](https://github.com/LBonicelli/FakePeggle/blob/master/assets/gameScreenshot.png?raw=true)

## Features

* Every game you play the position of the orange pegs is choosen **randomically**.  
* When it is hit, every peg **lights** and disappear at the end of each throw.  
* Game music, effects sound, Win and Lose screen.  

## Game Map Implementation

* The pegs game map was generated using Tiled(a video games level editor).  
* The use of this application allows for easy and fast modification of the arrangement of pegs.  
* Additionally, creating new maps with new arrangements would not require much time.  

## Requirements
Java 17+.  
The proper JDK or JRE should be installed in your system to run this application.

## License
This software is licensed under MIT License.


