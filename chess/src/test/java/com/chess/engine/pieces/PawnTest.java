package com.chess.engine.pieces;

import com.chess.engine.Team;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.move.Move;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class PawnTest {
    @Test
    public void testPawnLegalMoves() {

        final Board.Builder builder = new Board.Builder();

        // Black Layout
        builder.setPiece(new King(4, Team.BLACK));
        // White Layout
        builder.setPiece(new King( 60, Team.WHITE));
        builder.setPiece(new Pawn( 52, Team.WHITE));
        // Set the current player
        builder.setMoveMaker(Team.WHITE);

        final Board board = builder.build();

        final List<Move> whiteLegals = (List<Move>) board.whitePlayer().getLegalMoves();
        final List<Move> blackLegals = (List<Move>) board.blackPlayer().getLegalMoves();
        assertEquals(whiteLegals.size(), 6);
        assertEquals(blackLegals.size(), 5);
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e2"), BoardUtils.getCoordinateAtPosition("e3"))));
        assertTrue(whiteLegals.contains(Move.MoveFactory
                .createMove(board, BoardUtils.getCoordinateAtPosition("e2"), BoardUtils.getCoordinateAtPosition("e4"))));

    }

    @Test
    public void testPawnIllegalMoves() {

        final Board.Builder builder = new Board.Builder();

        // Black Layout
        builder.setPiece(new King(4, Team.BLACK));
        // White Layout
        builder.setPiece(new King( 60, Team.WHITE));
        builder.setPiece(new Pawn( 52, Team.WHITE));
        // Set the current player
        builder.setMoveMaker(Team.WHITE);

        final Board board = builder.build();

        final List<Move> whiteLegals = (List<Move>) board.whitePlayer().getLegalMoves();

        NullPointerException exception1 = assertThrows(NullPointerException.class, () ->{
                    //Move the White Pawn three positions to the front
                    whiteLegals.contains(Move.MoveFactory
                            .createMove(board, BoardUtils.getCoordinateAtPosition("e2"), BoardUtils.getCoordinateAtPosition("e5")));
                }
                , "Not a legal move!");

        NullPointerException exception2 = assertThrows(NullPointerException.class, () ->{
                    //Move the White Pawn in diagonal right
                    whiteLegals.contains(Move.MoveFactory
                            .createMove(board, BoardUtils.getCoordinateAtPosition("e2"), BoardUtils.getCoordinateAtPosition("f3")));
                }
                , "Not a legal move!");

        NullPointerException exception3 = assertThrows(NullPointerException.class, () ->{
                    //Move the White Pawn in diagonal left
                    whiteLegals.contains(Move.MoveFactory
                            .createMove(board, BoardUtils.getCoordinateAtPosition("e2"), BoardUtils.getCoordinateAtPosition("d3")));
                }
                , "Not a legal move!");
    }

}