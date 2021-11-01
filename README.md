# scrabbleAI
Hasbro's Scrabble, being adapted to include an artificially intelligent opponent.
To set up project locally, download Intellij Community Edition and clone this repo. Building should prompt for JDK configuration, IDE will allow you to choose from various providers. I built this a long time ago with 1.8. Maybe Ill update to Kotlin and finish some missing features like blank tiles. Theres defintely a few majors bugs (like the human player only gets to play one turn, and can put down whatever word they want?), but it demonstrates how a scrabble bot can use a trie to find and place the best possible word on a board given a set of random tiles, from a provided dictionary (included in the project files).

Rules (https://en.wikibooks.org/wiki/Scrabble/Rules) 

<h5 Dictionary <5>
Word list taken from: https://raw.githubusercontent.com/jonbcard/scrabble-bot/master/src/dictionary.txt
