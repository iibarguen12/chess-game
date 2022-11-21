package com.chess.engine.pieces;

import com.chess.engine.Team;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.move.Move;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class BishopTest {
    @Test
    public void testBishopMoves() {

        final Board.Builder builder = new Board.Builder();

        // Black Layout
        builder.setPiece(new King(4, Team.BLACK));
        // White Layout
        builder.setPiece(new Bishop(35,Team.WHITE));
        builder.setPiece(new King( 60, Team.WHITE));
        // Set the current player
        builder.setMoveMaker(Team.WHITE);

        final Board board = builder.build();

        final List<Move> whiteLegals = (List<Move>) board.whitePlayer().getLegalMoves();
        final List<Move> blackLegals = (List<Move>) board.blackPlayer().getLegalMoves();
        assertEquals(whiteLegals.size(), 18);
        assertEquals(blackLegals.size(), 5);
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("d4"), BoardUtils.getCoordinateAtPosition("a7"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("d4"), BoardUtils.getCoordinateAtPosition("b6"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("d4"), BoardUtils.getCoordinateAtPosition("c5"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("d4"), BoardUtils.getCoordinateAtPosition("e3"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("d4"), BoardUtils.getCoordinateAtPosition("f2"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("d4"), BoardUtils.getCoordinateAtPosition("g1"))));
    }

}