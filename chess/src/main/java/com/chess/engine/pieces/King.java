package com.chess.engine.pieces;

import com.chess.engine.Team;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.move.Move;
import com.chess.engine.board.Tile;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class King extends Piece{
    private final static int[] CANDIDATE_MOVE_COORDINATES={-9 ,-8 ,-7 ,-1 ,1 ,7 ,8 ,9};

    public King(final int piecePosition, final Team pieceTeam) {

        super(PieceType.KING,piecePosition, pieceTeam, true);
    }

    public King(final int piecePosition, final Team pieceTeam, final boolean isFirstMove) {

        super(PieceType.KING,piecePosition, pieceTeam, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : CANDIDATE_MOVE_COORDINATES){
            final int candidateDestinationCoordinate = this.piecePosition + currentCandidateOffset;

            if (isColumnsExclusion(this.piecePosition, currentCandidateOffset)){
                continue;
            }

            if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                if(!candidateDestinationTile.isTileOccupied()){
                    legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                }else{
                    final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                    if (this.pieceTeam != pieceAtDestination.getPieceTeam()){
                        legalMoves.add(new Move.AttackMove(board,this, candidateDestinationCoordinate,pieceAtDestination));
                    }
                }
            }
        }

        return ImmutableList.copyOf(legalMoves);
    }

    private static boolean isColumnsExclusion(final int currentPosition, final int candidateOffset){
        return isFirstColumnExclusion(currentPosition, candidateOffset) || isEightColumnExclusion(currentPosition, candidateOffset) ;
    }
    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -9 || candidateOffset == -1 ||
                candidateOffset == 7);
    }

    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == -7 || candidateOffset == 1||
                candidateOffset == 9);
    }

    @Override
    public String toString(){
        return PieceType.KING.toString();
    }

    @Override
    public King movePiece(final Move move) {
        return new King(move.getDestinationCoordinate(), move.getMovedPiece().getPieceTeam());
    }
}
