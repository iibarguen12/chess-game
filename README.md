# Chess

This Java program implements the basic logic of the Chess game for two human players.

The game ignores the following rules:
* Castling
* Piece promotion
* En passant

The architectural design approach implemented is the **Immutable Design Pattern**, 
which helps us to define an immutable board as initial point of reference as the game board
and subsequents boards with the desire moves to evaluate if the move belongs to the different legal possible moves.

These **boards are composed by a list of 64 tiles**, each one representing a position in the Chess board 
mimic the representation of a 8x8 two-dimensional array, and each Tile could be occupied by a Piece or be empty.

So based in the above declaration, the calculation of every possible move is based in the addition or subtraction 
(depending on the direction of the game) of the desire new position to the current one, and validate it in a possible
range of vectors defined for each different Piece.

Therefore, the normal representation of an Algebraic Notation of the Chess board below:

```
a8  b8  c8  d8  e8  f8  g8  h8
a7  b7  c7  d7  e7  f7  g7  h7
a6  b6  c6  d6  e6  f6  g6  h6
a5  b5  c5  d5  e5  f5  g5  h5
a4  b4  c4  d4  e4  f4  g4  h4
a3  b3  c3  d3  e3  f3  g3  h3
a2  b2  c2  d2  e2  f2  g2  h2
a1  b1  c1  d1  e1  f1  g1  h1
```

Is treated as an immutable list of 64 positions like this:

```
0    1   2   3   4   5   6   7
8    9  10  11  12  13  14  15
16  17  18  19  20  21  22  23
24  25  26  27  28  29  30  31
32  33  34  35  36  37  38  39
40  41  42  43  44  45  46  47
48  49  50  51  52  53  54  55
56  57  58  59  60  61  62  63
```

The GUI is made using java.awt and javax.swing, creating a simple 8x8 Tile grid and assigning to each Tile 
the corresponding images, first the light or dark board images and then the black and white images corresponding 
to each Piece, this plus adding an event listener for the mouse click, redrawing the board at each click making the 
effect of movement possible.
