package com.chess.engine.pieces;

import com.chess.engine.Team;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.move.Move;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class KingTest {
    @Test
    public void testKingLegalMoves() {

        final Board.Builder builder = new Board.Builder();

        // Black Layout
        builder.setPiece(new King(4, Team.BLACK));
        // White Layout
        builder.setPiece(new King( 60, Team.WHITE));
        // Set the current player
        builder.setMoveMaker(Team.WHITE);

        final Board board = builder.build();

        final List<Move> whiteLegals = (List<Move>) board.whitePlayer().getLegalMoves();
        final List<Move> blackLegals = (List<Move>) board.blackPlayer().getLegalMoves();
        assertEquals(whiteLegals.size(), 5);
        assertEquals(blackLegals.size(), 5);
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e1"), BoardUtils.getCoordinateAtPosition("d1"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e1"), BoardUtils.getCoordinateAtPosition("d2"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e1"), BoardUtils.getCoordinateAtPosition("e2"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e1"), BoardUtils.getCoordinateAtPosition("f2"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e1"), BoardUtils.getCoordinateAtPosition("f1"))));

    }

    @Test
    public void testKingIllegalMoves() {

        final Board.Builder builder = new Board.Builder();

        // Black Layout
        builder.setPiece(new King(4, Team.BLACK));
        // White Layout
        builder.setPiece(new King( 60, Team.WHITE));
        // Set the current player
        builder.setMoveMaker(Team.WHITE);

        final Board board = builder.build();

        final List<Move> whiteLegals = (List<Move>) board.whitePlayer().getLegalMoves();

        NullPointerException exception = assertThrows(NullPointerException.class, () ->{
            //Move the White King two positions to the front
            whiteLegals.contains(Move.MoveFactory
                            .createMove(board, BoardUtils.getCoordinateAtPosition("e1"), BoardUtils.getCoordinateAtPosition("e3")));
                }
                , "Not a legal move!");
    }
}