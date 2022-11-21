package com.chess.engine.board;

import com.chess.engine.Team;
import com.chess.engine.move.Move;
import com.chess.engine.pieces.*;
import com.chess.engine.player.BlackPlayer;
import com.chess.engine.player.Player;
import com.chess.engine.player.WhitePlayer;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;

import java.util.*;

public class Board {

    private final List<Tile> gameBoard;
    private final Collection<Piece> blackPieces;
    private final Collection<Piece> whitePieces;

    private final BlackPlayer blackPlayer;
    private final WhitePlayer whitePlayer;
    private final Player currentPlayer;
    private Board(final Builder builder){
        this.gameBoard = createGameBoard(builder);
        this.blackPieces = calculateActivePieces(this.gameBoard, Team.BLACK);
        this.whitePieces = calculateActivePieces(this.gameBoard, Team.WHITE);

        final Collection<Move> blackStandardLegalMoves = calculateLegalMoves(this.blackPieces);
        final Collection<Move> whiteStandardLegalMoves = calculateLegalMoves(this.whitePieces);

        this.blackPlayer = new BlackPlayer(this, whiteStandardLegalMoves,blackStandardLegalMoves);
        this.whitePlayer = new WhitePlayer(this, whiteStandardLegalMoves,blackStandardLegalMoves);

        this.currentPlayer = builder.nextMoveMaker.choosePlayer(this.whitePlayer, this.blackPlayer);
    }

    @Override
    public String toString(){
        final StringBuilder builder = new StringBuilder();
        for (int i=0; i < BoardUtils.NUM_TILES; i++){
            final String titleText = this.gameBoard.get(i).toString();
            builder.append(String.format("%3s",titleText));
            if ((i+1) % BoardUtils.NUM_TILES_PER_ROW == 0){
                builder.append("\n");
            }
        }
        return builder.toString();
    }

    public Player whitePlayer(){
        return this.whitePlayer;
    }

    public Player blackPlayer(){
        return this.blackPlayer;
    }

    public Player currentPlayer() {
        return this.currentPlayer;
    }
    public Collection<Piece> getBlackPieces(){
        return this.blackPieces;
    }

    public Collection<Piece> getWhitePieces(){
        return this.whitePieces;
    }

    private Collection<Move> calculateLegalMoves(final Collection<Piece> pieces) {
        final List<Move> legalMoves = new ArrayList<>();
        for (final Piece piece: pieces){
            legalMoves.addAll(piece.calculateLegalMoves(this));
        }
        return ImmutableList.copyOf(legalMoves);
    }

    private static Collection<Piece> calculateActivePieces(final List<Tile> gameBoard, final Team team) {
        final List<Piece> activePieces = new ArrayList<>();

        for (final Tile tile: gameBoard){
            if (tile.isTileOccupied()){
                final Piece piece = tile.getPiece();
                if (piece.getPieceTeam() == team){
                    activePieces.add(piece);
                }
            }
        }
        return ImmutableList.copyOf(activePieces);
    }

    public Tile getTile(final int tileCoordinate){
        return gameBoard.get(tileCoordinate);
    }

    private static List<Tile> createGameBoard (final Builder builder){
        final Tile[] tiles = new Tile[BoardUtils.NUM_TILES];
        for (int i = 0; i < BoardUtils.NUM_TILES; i++){
            tiles[i] = Tile.createTile(i,builder.boardConfig.get(i));
        }

        return ImmutableList.copyOf(tiles);
    }

    public static Board createStandardBoard(){
        final Builder builder = new Builder();
        builder.setPiece(new Rook(0,Team.BLACK));
        builder.setPiece(new Knight(1,Team.BLACK));
        builder.setPiece(new Bishop(2,Team.BLACK));
        builder.setPiece(new Queen(3,Team.BLACK));
        builder.setPiece(new King(4,Team.BLACK));
        builder.setPiece(new Bishop(5,Team.BLACK));
        builder.setPiece(new Knight(6,Team.BLACK));
        builder.setPiece(new Rook(7,Team.BLACK));
        builder.setPiece(new Pawn(8,Team.BLACK));
        builder.setPiece(new Pawn(9,Team.BLACK));
        builder.setPiece(new Pawn(10,Team.BLACK));
        builder.setPiece(new Pawn(11,Team.BLACK));
        builder.setPiece(new Pawn(12,Team.BLACK));
        builder.setPiece(new Pawn(13,Team.BLACK));
        builder.setPiece(new Pawn(14,Team.BLACK));
        builder.setPiece(new Pawn(15,Team.BLACK));

        builder.setPiece(new Pawn(48,Team.WHITE));
        builder.setPiece(new Pawn(49,Team.WHITE));
        builder.setPiece(new Pawn(50,Team.WHITE));
        builder.setPiece(new Pawn(51,Team.WHITE));
        builder.setPiece(new Pawn(52,Team.WHITE));
        builder.setPiece(new Pawn(53,Team.WHITE));
        builder.setPiece(new Pawn(54,Team.WHITE));
        builder.setPiece(new Pawn(55,Team.WHITE));
        builder.setPiece(new Rook(56,Team.WHITE));
        builder.setPiece(new Knight(57,Team.WHITE));
        builder.setPiece(new Bishop(58,Team.WHITE));
        builder.setPiece(new Queen(59,Team.WHITE));
        builder.setPiece(new King(60,Team.WHITE));
        builder.setPiece(new Bishop(61,Team.WHITE));
        builder.setPiece(new Knight(62,Team.WHITE));
        builder.setPiece(new Rook(63,Team.WHITE));

        builder.setMoveMaker(Team.WHITE);

        return builder.build();
    }

    public Iterable<Move> geAllLegalMoves() {
        return Iterables.unmodifiableIterable(
                Iterables.concat(this.whitePlayer.getLegalMoves(), this.blackPlayer.getLegalMoves()));
    }

    public static class Builder{

        Map<Integer, Piece> boardConfig;
        Team nextMoveMaker;

        public Builder (){
            this.boardConfig = new HashMap<>();
        }

        public Builder setPiece(final Piece piece){
            this.boardConfig.put(piece.getPiecePosition(),piece);
            return this;
        }

        public Builder setMoveMaker(final Team nextMoveMaker){
            this.nextMoveMaker = nextMoveMaker;
            return this;
        }

        public Board build(){
            return new Board(this);
        }
    }
}
