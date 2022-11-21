package com.chess.engine.move;

import com.chess.engine.board.Board;
import com.chess.engine.board.Board.Builder;
import com.chess.engine.pieces.Pawn;
import com.chess.engine.pieces.Piece;

public abstract class Move {
    protected final Board board;
    protected final Piece movedPiece;
    protected final int destinationCoordinate;

    public static final Move NULL_MOVE = new NullMove();

    private Move(final Board board, final Piece movedPiece, final int destinationCoordinate) {
        this.board = board;
        this.movedPiece = movedPiece;
        this.destinationCoordinate = destinationCoordinate;
    }


    @Override
    public int hashCode(){
        final int prime = 31;
        int result = 1;
        result = prime * result + this.destinationCoordinate;
        result = prime * result + this.movedPiece.hashCode();
        result = prime * result + this.movedPiece.getPiecePosition();
        return result;
    }

    @Override
    public boolean equals(final Object other){
        if (this == other){
            return true;
        }
        if (!(other instanceof Move)){
            return false;
        }
        final Move otherMove = (Move) other;
        return this.getCurrentCoordinate() == otherMove.getCurrentCoordinate() &&
               this.getDestinationCoordinate() == otherMove.getDestinationCoordinate() &&
               this.getMovedPiece().equals(otherMove.getMovedPiece());
    }
    public int getCurrentCoordinate(){
        return this.movedPiece.getPiecePosition();
    }
    public int getDestinationCoordinate() {
        return this.destinationCoordinate;
    }

    public Piece getMovedPiece(){
        return this.movedPiece;
    }

    public boolean isAttack(){
        return false;
    }

    public boolean isNull(){ return false;}

    public Piece getAttackedPiece(){
        return null;
    }

    public Board execute() {
        final Builder builder = new Builder();

        for(final Piece piece : this.board.currentPlayer().getActivePieces()){
            if (!this.movedPiece.equals(piece)){
                builder.setPiece(piece);
            }
        }
        for(final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
            builder.setPiece(piece);
        }
        builder.setPiece(this.movedPiece.movePiece(this));
        builder.setMoveMaker(this.board.currentPlayer().getOpponent().getTeam());
        return builder.build();
    }

    public static final class MajorMove extends Move{
        public MajorMove(Board board,
                         Piece movedPiece,
                         int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

    }

    public static class AttackMove extends Move{
        final Piece attackedPiece;

        public AttackMove(final Board board,
                          final Piece movedPiece,
                          final int destinationCoordinate,
                          final Piece attackedPiece) {
            super(board,movedPiece,destinationCoordinate);
            this.attackedPiece = attackedPiece;
        }

        @Override
        public int hashCode(){
            return this.attackedPiece.hashCode() + super.hashCode();
        }

        @Override
        public boolean equals(final Object other){
            if (this == other){
                return true;
            }
            if(!(other instanceof AttackMove)){
                return false;
            }
            final AttackMove otherAttackMove = (AttackMove) other;
            return super.equals(otherAttackMove) && getAttackedPiece().equals(otherAttackMove.getAttackedPiece());
        }
        @Override
        public Board execute(){
            final Board.Builder builder = new Builder();
            for (final Piece piece : board.currentPlayer().getActivePieces()) {
                if (!this.movedPiece.equals(piece)) {
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : board.currentPlayer().getOpponent().getActivePieces()) {
                builder.setPiece(piece);
            }
            builder.setPiece(this.movedPiece.movePiece(this));
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getTeam());
            return builder.build();
        }

        @Override
        public boolean isAttack(){
            return true;
        }

        @Override
        public Piece getAttackedPiece(){
            return this.attackedPiece;
        }

    }

    public static final class PawnMove extends Move{
        public PawnMove(Board board,
                        Piece movedPiece,
                        int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }
    }

    public static class PawnAttackMove extends AttackMove{
        public PawnAttackMove(final Board board,
                              final Piece movedPiece,
                              final int destinationCoordinate,
                              final Piece attackedPiece) {
            super(board,movedPiece,destinationCoordinate, attackedPiece);
        }
    }

    public static final class PawnJump extends Move{
        public PawnJump(Board board,
                        Piece movedPiece,
                        int destinationCoordinate) {
            super(board, movedPiece, destinationCoordinate);
        }

        @Override
        public Board execute(){
            final Builder builder = new Builder();
            for (final Piece piece : this.board.currentPlayer().getActivePieces()){
                if(!this.movedPiece.equals(piece)){
                    builder.setPiece(piece);
                }
            }
            for (final Piece piece : this.board.currentPlayer().getOpponent().getActivePieces()){
                builder.setPiece(piece);
            }
            final Pawn movedPawn = (Pawn) this.movedPiece.movePiece(this);
            builder.setPiece(movedPawn);
            builder.setMoveMaker(this.board.currentPlayer().getOpponent().getTeam());
            return builder.build();
        }

    }

    public static final class NullMove extends Move{
        public NullMove() {
            super(null, null, -1);
        }

        @Override
        public boolean isNull(){
            return true;
        }
        @Override
        public Board execute(){
            throw new RuntimeException("cannot execute the null move!");
        }

    }

    public static class MoveFactory {
        private MoveFactory(){
            throw new RuntimeException("Not instantiable!");
        }

        public static Move createMove (final Board board,
                                       final int currentCoordinate,
                                       final int destinationCoordinate){
            for (final Move move : board.geAllLegalMoves()){
                if(move.getCurrentCoordinate() == currentCoordinate &&
                   move.getDestinationCoordinate() == destinationCoordinate){
                    return move;
                }
            }
            return NULL_MOVE;
        }
    }

}
