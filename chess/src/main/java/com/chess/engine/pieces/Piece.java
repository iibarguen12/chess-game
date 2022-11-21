package com.chess.engine.pieces;

import com.chess.engine.Team;
import com.chess.engine.board.Board;
import com.chess.engine.move.Move;

import java.util.Collection;

public abstract class Piece {
    protected final PieceType pieceType;
    protected final int piecePosition;
    protected final Team pieceTeam;
    protected final boolean isFirstMove;
    private final int cachedHashCode;

    Piece(final PieceType pieceType, final int piecePosition, final Team pieceTeam, final boolean isFirstMove){
        this.pieceType = pieceType;
        this.piecePosition = piecePosition;
        this.pieceTeam = pieceTeam;
        this.isFirstMove = isFirstMove;
        this.cachedHashCode = computeHashCode();
    }

    private int computeHashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + this.pieceType.hashCode();
        result = prime * result + this.pieceTeam.hashCode();
        result = prime * result + this.piecePosition;
        result = prime * result + (this.isFirstMove? 1: 0);
        return result;
    }

    @Override
    public int hashCode(){
        return this.cachedHashCode;
    }

    @Override
    public boolean equals (final Object other){
        if (this == other){
            return true;
        }
        if (!(other instanceof Piece)){
            return false;
        }
        final Piece otherPiece = (Piece) other;

        return this.piecePosition == otherPiece.getPiecePosition() && this.pieceType == otherPiece.getPieceType() &&
               this.pieceTeam == otherPiece.pieceTeam && this.isFirstMove == otherPiece.isFirstMove;

    }

    public int getPiecePosition(){
        return this.piecePosition;
    }
    public Team getPieceTeam() {
        return pieceTeam;
    }

    public boolean isFirstMove(){
        return this.isFirstMove;
    }
    public PieceType getPieceType() {
        return this.pieceType;
    }
    public abstract Collection<Move> calculateLegalMoves(final Board board);

    public abstract Piece movePiece(Move move);
    public  enum PieceType{
        PAWN("P"){
            @Override
            public boolean isKing() {
                return false;
            }
        },
        ROOK("R") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        KNIGHT("N") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        BISHOP("B") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        QUEEN("Q") {
            @Override
            public boolean isKing() {
                return false;
            }
        },
        KING("K") {
            @Override
            public boolean isKing() {
                return true;
            }
        };
        private final String pieceName;

        PieceType(final String pieceName){
            this.pieceName = pieceName;
        }

        @Override
        public String toString(){
            return this.pieceName;
        }

        public abstract boolean isKing();

    }
}
