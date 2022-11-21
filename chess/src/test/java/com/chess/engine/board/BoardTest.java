package com.chess.engine.board;

import com.chess.engine.Team;
import com.chess.engine.move.Move;
import com.chess.engine.move.MoveStatus;
import com.chess.engine.move.MoveTransition;
import com.chess.engine.pieces.King;
import com.chess.engine.pieces.Pawn;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BoardTest {
    @Test
    public void testInitialBoard() {
        final Board board = Board.createStandardBoard();
        assertEquals(board.whitePlayer().getLegalMoves().size(), 20);
        assertEquals(board.blackPlayer().getLegalMoves().size(), 20);
        assertFalse(board.currentPlayer().isInCheck());
        assertFalse(board.currentPlayer().isInCheckMate());
        assertFalse(board.currentPlayer().getOpponent().isInCheck());
        assertFalse(board.currentPlayer().getOpponent().isInCheckMate());
        assertEquals(board.currentPlayer(), board.whitePlayer());
        assertEquals(board.currentPlayer().getOpponent(), board.blackPlayer());
    }

    @Test
    public void testSimpleKingMove() {

        final Board.Builder builder = new Board.Builder();
        // Set the King and one Pawn in front the King for each Player
        // Black Layout
        builder.setPiece(new King(4, Team.BLACK));
        builder.setPiece(new Pawn(12,Team.BLACK));
        // White Layout
        builder.setPiece(new Pawn(52,Team.WHITE));
        builder.setPiece(new King(60, Team.WHITE));
        builder.setMoveMaker(Team.WHITE);
        // Build the board
        final Board board = builder.build();

        // Check the board
        assertEquals(board.whitePlayer().getLegalMoves().size(), 6);
        assertEquals(board.blackPlayer().getLegalMoves().size(), 6);
        assertFalse(board.currentPlayer().isInCheck());
        assertFalse(board.currentPlayer().isInCheckMate());
        assertFalse(board.currentPlayer().getOpponent().isInCheck());
        assertFalse(board.currentPlayer().getOpponent().isInCheckMate());
        assertEquals(board.currentPlayer(), board.whitePlayer());
        assertEquals(board.currentPlayer().getOpponent(), board.blackPlayer());

        // Move the King from one position to the right from e1 to f1
        final Move move = Move.MoveFactory.createMove(board,BoardUtils.getCoordinateAtPosition("e1"), BoardUtils.getCoordinateAtPosition("f1"));
        final MoveTransition moveTransition = board.currentPlayer().makeMove(move);

        //Check the move
        assertEquals(MoveStatus.DONE, moveTransition.getMoveStatus());
        assertEquals(moveTransition.getTransitionBoard().whitePlayer().getPlayerKing().getPiecePosition(), 61);
    }
}