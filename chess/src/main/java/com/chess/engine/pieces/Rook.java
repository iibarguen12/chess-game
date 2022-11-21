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

public class Rook extends Piece{
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES={-8, -1, 1, 8};

    public Rook(final int piecePosition, final Team pieceTeam) {

        super(PieceType.ROOK, piecePosition, pieceTeam, true);
    }

    public Rook(final int piecePosition, final Team pieceTeam, final boolean isFirstMove) {

        super(PieceType.ROOK, piecePosition, pieceTeam, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int candidateCoordinateOffset: CANDIDATE_MOVE_VECTOR_COORDINATES){
            int candidateDestinationCoordinate = this.piecePosition;

            while(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){

                if (isColumnsExclusion(candidateDestinationCoordinate,candidateCoordinateOffset)){
                    break;
                }

                candidateDestinationCoordinate += candidateCoordinateOffset;
                if(BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                    final Tile candidateDestinationTile = board.getTile(candidateDestinationCoordinate);
                    if(!candidateDestinationTile.isTileOccupied()){
                        legalMoves.add(new Move.MajorMove(board, this, candidateDestinationCoordinate));
                    }else{
                        final Piece pieceAtDestination = candidateDestinationTile.getPiece();
                        if (this.pieceTeam != pieceAtDestination.getPieceTeam()){
                            legalMoves.add(new Move.AttackMove(board,this, candidateDestinationCoordinate,pieceAtDestination));
                        }
                        break;
                    }
                }
            }

        }

        return ImmutableList.copyOf(legalMoves);
    }

    private static boolean isColumnsExclusion(final int currentPosition, final int candidateOffset){
        return isFirstColumnExclusion(currentPosition, candidateOffset) ||  isEightColumnExclusion(currentPosition, candidateOffset);
    }

    private static boolean isFirstColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.FIRST_COLUMN[currentPosition] && (candidateOffset == -1);
    }

    private static boolean isEightColumnExclusion(final int currentPosition, final int candidateOffset){
        return BoardUtils.EIGHTH_COLUMN[currentPosition] && (candidateOffset == 1);
    }

    @Override
    public String toString(){
        return PieceType.ROOK.toString();
    }

    @Override
    public Rook movePiece(final Move move) {
        return new Rook(move.getDestinationCoordinate(), move.getMovedPiece().getPieceTeam());
    }
}
