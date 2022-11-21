package com.chess.engine.pieces;

import com.chess.engine.Team;
import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.move.Move;
import com.google.common.collect.ImmutableList;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class Pawn extends Piece{
    private final static int[] CANDIDATE_MOVE_VECTOR_COORDINATES={7, 8, 9, 16};

    public Pawn(final int piecePosition, final Team pieceTeam) {
        super(PieceType.PAWN, piecePosition, pieceTeam, true);
    }

    public Pawn(final int piecePosition, final Team pieceTeam, final boolean isFirstMove) {
        super(PieceType.PAWN, piecePosition, pieceTeam, isFirstMove);
    }

    @Override
    public Collection<Move> calculateLegalMoves(final Board board) {
        final List<Move> legalMoves = new ArrayList<>();

        for (final int currentCandidateOffset : CANDIDATE_MOVE_VECTOR_COORDINATES){
            final int candidateDestinationCoordinate = this.piecePosition + (this.getPieceTeam().getDirection() * currentCandidateOffset);

            if (!BoardUtils.isValidTileCoordinate(candidateDestinationCoordinate)){
                continue;
            }

            if (currentCandidateOffset == 8 && !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                legalMoves.add(new Move.MajorMove(board,this, candidateDestinationCoordinate));
            }else if (currentCandidateOffset == 16 && this.isFirstMove() &&
                    ((BoardUtils.SECOND_ROW[this.piecePosition] && this.pieceTeam.isBlack()) ||
                    (BoardUtils.SEVENTH_ROW[this.piecePosition] && this.pieceTeam.isWhite()))){
                final int behindCandidateDestinationCoordinate = this.piecePosition + (this.pieceTeam.getDirection() * 8);

                if (!board.getTile(behindCandidateDestinationCoordinate).isTileOccupied() &&
                    !board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    legalMoves.add(new Move.PawnJump(board,this, candidateDestinationCoordinate));
                }
            }else if (currentCandidateOffset == 7 &&
                     !((BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceTeam.isWhite()) ||
                      (BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceTeam.isBlack()))){
                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceTeam != pieceOnCandidate.getPieceTeam()){
                        legalMoves.add(new Move.PawnAttackMove(board,this, candidateDestinationCoordinate, pieceOnCandidate));
                    }
                }
            }else if (currentCandidateOffset == 9 &&
                    !((BoardUtils.FIRST_COLUMN[this.piecePosition] && this.pieceTeam.isWhite()) ||
                            (BoardUtils.EIGHTH_COLUMN[this.piecePosition] && this.pieceTeam.isBlack()))){
                if (board.getTile(candidateDestinationCoordinate).isTileOccupied()){
                    final Piece pieceOnCandidate = board.getTile(candidateDestinationCoordinate).getPiece();
                    if (this.pieceTeam != pieceOnCandidate.getPieceTeam()){
                        legalMoves.add(new Move.PawnAttackMove(board,this, candidateDestinationCoordinate, pieceOnCandidate));
                    }
                }
            }
        }
        return ImmutableList.copyOf(legalMoves);
    }

    @Override
    public String toString(){
        return PieceType.PAWN.toString();
    }

    @Override
    public Pawn movePiece(final Move move) {
        return new Pawn(move.getDestinationCoordinate(), move.getMovedPiece().getPieceTeam());
    }
}
