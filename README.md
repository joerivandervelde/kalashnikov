# Kalashnikov: the card game
A Java implementation of the card game 'Kalashnikov' made by Life of Boris. 
Check it out at: https://www.youtube.com/watch?v=IiRk-yGfAjc.

### In a nutshell
Players take 
turns in drawing cards from either scrap (i.e. deck) or shelf to assemble a 
gun (of course AK47 - Ace, King, 4, 7), which they may use to shoot at other
 players. Duplicate gun parts may be shelved and picked up later or drawn by other 
players. A gun comprised of same-suit cards is a golden gun and deals maximum
 double damage. A game ends when someone has attacked, and the series end 
 when only one player is left alive. If no players survive, it is a draw.

### This code repository
The current implementation lets AI players battle each other and 
does not allow human players to join the game. Also there is no fancy 
graphical user interface. But all this can all be added later. For now, it is 
already useful for analyzing some interesting game mechanics.

# Analysis results

### How many players should I invite?
The game is most suitable for 2-4 players. By adding more 
players, the number of games that must be played in order to reach a series 
winner seems to increase logarithmically.

![NrOfGamesVsNrOfPlayers](https://raw.githubusercontent.com/joerivandervelde/kalashnikov/master/analysis/NrOfGamesVsNrOfPlayers/NrOfGamesVsNrOfPlayers.png "NrOfGamesVsNrOfPlayers")

### Is there win bias?
If every game starts with the same player, that player has a slight increased
 chance of winning the series. It would be best to rotate the starting 
 position to keep things fair. This type of bias increases with the number of
  players invited to the series.

![WinBiasVsNrOfPlayers](https://raw.githubusercontent.com/joerivandervelde/kalashnikov/master/analysis/NrOfGamesVsNrOfPlayers/NrOfGamesVsNrOfPlayers.png "WinBiasVsNrOfPlayers")

### Golden strategy worth it?
If you are very close to building golden gun, then surely go for it! However,
 it would seem that deliberately waiting for the golden gun as a general 
 strategy for any game decreases your chances of winning.

![WaitForGoldenGun](https://raw.githubusercontent.com/joerivandervelde/kalashnikov/master/analysis/WinChanceOfRandomAIs/WaitForGoldenGun.png "WaitForGoldenGun")

### More golden damage?
What is the effect of the golden gun damage on win chance? This depends on
 the likelihood to wait for the golden gun. The higher this likelihood, the 
 more gun damage matter, as could be expected. However, even an overpowered 
 gun does not seem to provide an advantage over a player that shoots 
 immediately upon obtaining a normal gun. So default damage of 8 is fine.

![BetterGoldenGun](https://raw.githubusercontent.com/joerivandervelde/kalashnikov/master/analysis/BetterGoldenGun/BetterGoldenGun.png "BetterGoldenGun")

### Scrap over shelf?
Is it better to draw a card from scrap instead of an unknown card from 
another player's shelf? Evidence seems to suggest not. So, if you can, pick up
that mystery shelf card!

![ScrapOverShelf](https://raw.githubusercontent.com/joerivandervelde/kalashnikov/master/analysis/WinChanceOfRandomAIs/ScrapOverShelf.png "ScrapOverShelf")

### Duplicates over useless cards?
Is it better to discard useless cards (i.e. non gun parts) before starting to
 shelf duplicate cards? From the results we may conclude that it does not 
 matter what you do.

![DuplicatesBeforeUseless](https://raw.githubusercontent.com/joerivandervelde/kalashnikov/master/analysis/WinChanceOfRandomAIs/DuplicatesBeforeUseless.png "DuplicatesBeforeUseless")
