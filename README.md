# scrabbleAI
Hasbro's Scrabble, being adapted to include an artificially intelligent opponent.
To set up project locally, create a new javafx project in intellij before cloning. Project uses Java 8.

Rules (https://en.wikibooks.org/wiki/Scrabble/Rules) 
----------------------------------------------------------------------
<h3>Scoring</h3>

Each letter tile has a number next to it that indicates how many points it is worth. Common letters, such as vowels, are worth 1 point, while rare letters such as "Q", "X", and "Z" are worth 8-10 points. Blank tiles are worth zero points. The score of a play is equal to the sum of the scores of all new words formed (including extensions or hook words, see examples below).

Double Letter Score and Triple Letter Score (DLS, TLS) - a letter on this space is doubled or tripled in its point value.
Double Word Score and Triple Word Score (DWS, TWS) - if any letter touches this space, the entire word is doubled in point value. If your word includes both a letter bonus and a word bonus, the letter bonus is applied first, in accordance with order of operations.
Note that the center square is a DWS, so the first play of the game receives a double word score.

The premium letter squares are only applied on the first turn that they are used. On subsequent plays, the premium letter squares are nulled.

If a pre-existing word is added onto with a tile that lands on a double word tile, the original word is not doubled.

A play that covers two DWS is doubled then re-doubled (i.e. the score for the word is 4 times its face value) and is sometimes referred to as a "double-double." Similarly, a play covering two TWS is tripled then re-tripled ("triple-triple.").

If a player uses all seven of their tiles on one play, they receives an extra 50 points, in addition to the score for the word. Such a play is commonly referred to as a bingo or a bonus.

At the end of the game when nobody can make a move or someone goes out of tiles, each player subtracts the amount of points that remain unused on their rack from their total score. If someone goes out of tiles, then the total of points on all other players' racks is added to their score as well. For this reason it's usually advantageous to be the first to dump all your tiles.

<h3>End of the Game</h3>
Under North American tournament rules, the game ends when:

There are no more tiles left to draw, and one player has used up his or her tiles (known as playing out), or
Six consecutive scoreless turns have occurred.
If a player plays out, the sum of the values of his opponents' tiles is added to his score, while each of the opponents' scores is reduced by the sum on his or her rack. In tournament play, the player playing out receives twice the value of his opponent's rack, and the opponent's score is unchanged. This does not affect point spread, but makes scoring slightly easier.