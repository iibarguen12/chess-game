package com.chess.gui;

import com.chess.engine.board.Board;
import com.chess.engine.board.BoardUtils;
import com.chess.engine.move.Move;
import com.chess.engine.board.Tile;
import com.chess.engine.pieces.Piece;
import com.chess.engine.move.MoveTransition;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class Table {
    private final JFrame gameFrame;
    private final BoardPanel boardPanel;
    private Board chessBoard;

    private Tile sourceTile;
    private Tile destinationTile;
    private Piece humanMovedPiece;

    private final static Dimension OUTER_FRAME_DIMENSION = new Dimension(600,600);
    private final static Dimension BOARD_PANEL_DIMENSION = new Dimension(400,350);
    private final static Dimension TILE_PANEL_DIMENSION = new Dimension(10,10);

    private static String defaultPieceImagesPath = "img/pieces/";

    private final Color lightTileColor = Color.decode("#e0effd");
    private final Color darkTileColor = Color.decode("#cfe2f3");
    public Table(){
        this.gameFrame = new JFrame("Chess");
        this.gameFrame.setLayout(new BorderLayout());
        final JMenuBar tableMenuBar = createTableMenuBar();
        this.gameFrame.setJMenuBar(tableMenuBar);
        this.gameFrame.setSize(OUTER_FRAME_DIMENSION);
        this.gameFrame.setLocationRelativeTo(null);
        this.chessBoard = Board.createStandardBoard();
        this.boardPanel = new BoardPanel();
        this.gameFrame.add(this.boardPanel, BorderLayout.CENTER);
        this.gameFrame.setVisible(true);
    }

    private void newGame(){
        Table table = new Table();
    }
    private JMenuBar createTableMenuBar() {
        final JMenuBar tableMenuBar = new JMenuBar();
        tableMenuBar.add(createFileMenu());
        return tableMenuBar;
    }

    private JMenu createFileMenu() {
        final JMenu fileMenu = new JMenu("Options");
        final JMenuItem newGameItem = new JMenuItem("New Game");
        newGameItem.addActionListener(e -> newGame());
        fileMenu.add(newGameItem);
        final JMenuItem exitMenuItem = new JMenuItem("Exit Game");
        exitMenuItem.addActionListener(e -> System.exit(0));
        fileMenu.add(exitMenuItem);
        return fileMenu;
    }

    private class BoardPanel extends JPanel{
        final List<TilePanel> boardTiles;
        BoardPanel(){
            super(new GridLayout(8,8));
            this.boardTiles = new ArrayList<>();

            for (int i=0; i < BoardUtils.NUM_TILES; i ++){
                final TilePanel tilePanel = new TilePanel(this,  i);
                this.boardTiles.add(tilePanel);
                add(tilePanel);
            }
            setPreferredSize(BOARD_PANEL_DIMENSION);
            validate();
        }

        public void drawBoard(final Board board) {
            removeAll();
            for (final TilePanel tilePanel: boardTiles){
                tilePanel.drawTile(board);
                add(tilePanel);
            }
            validate();
            repaint();
        }
    }

    public static class MoveLog{
        private final List<Move> moves;

        MoveLog(){
            this.moves = new ArrayList<>();
        }

        public List<Move> getMoves(){
            return this.moves;
        }

        public void addMove(final Move move){
            this.moves.add(move);
        }

        public int size(){
            return this.moves.size();
        }

        public void clear(){
            this.moves.clear();
        }

        public Move removeMove(int index){
            return this.moves.remove(index);
        }

        public boolean removeMove(final Move move){
            return this.moves.remove(move);
        }

    }

    private class TilePanel extends JPanel{
        private final int tileId;
        TilePanel(final BoardPanel boardPanel,
                  final int tileId){
            super(new GridBagLayout());
            this.tileId = tileId;
            setPreferredSize(TILE_PANEL_DIMENSION);
            assignTileColor();
            assignTilePieceIcon(chessBoard);

            addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    if(sourceTile == null){
                        sourceTile = chessBoard.getTile(tileId);
                        humanMovedPiece = sourceTile.getPiece();
                        if (humanMovedPiece == null){
                            sourceTile = null;
                        }
                    }else{
                        destinationTile = chessBoard.getTile(tileId);
                        final Move move = Move.MoveFactory.createMove(
                                chessBoard,
                                sourceTile.getTileCoordinate(),
                                destinationTile.getTileCoordinate());
                        final MoveTransition transition = chessBoard.currentPlayer().makeMove(move);
                        if(transition.getMoveStatus().isDone()){
                            chessBoard = transition.getTransitionBoard();
                        }
                        sourceTile = null;
                        destinationTile = null;
                        humanMovedPiece = null;
                    }
                    SwingUtilities.invokeLater(() -> {
                        boardPanel.drawBoard(chessBoard);
                        checkGameStatus();
                    });

                }

                @Override
                public void mousePressed(MouseEvent e) {
                }

                @Override
                public void mouseReleased(MouseEvent e) {
                }

                @Override
                public void mouseEntered(MouseEvent e) {
                }

                @Override
                public void mouseExited(MouseEvent e) {
                }
            });

            validate();
        }

        private void checkGameStatus() {
            if (chessBoard.currentPlayer().isInCheckMate()){
                JOptionPane.showMessageDialog(gameFrame, "Checkmate! Player "+chessBoard.currentPlayer().getOpponent().getTeam().toString() + " Wins!");
                newGame();
            }else if (chessBoard.currentPlayer().isInCheck()){
                JOptionPane.showMessageDialog(gameFrame, "Player "+chessBoard.currentPlayer().getTeam().toString() + " in Check!");
            }
        }

        public void drawTile(final Board board){
            assignTileColor();
            assignTilePieceIcon(board);
            highlightLegals(board);
            validate();
            repaint();
        }
        private void assignTilePieceIcon(final Board board){
            this.removeAll();
            if(board.getTile(this.tileId).isTileOccupied()){
                try {
                    final BufferedImage image =
                            ImageIO.read(new File(defaultPieceImagesPath +
                                    board.getTile(this.tileId).getPiece().getPieceTeam().toString().substring(0,1) +
                                    board.getTile(this.tileId).getPiece().toString() + ".gif"));
                    add(new JLabel(new ImageIcon(image)));
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        private void assignTileColor() {
            if (BoardUtils.FIRST_ROW[this.tileId]||
                    BoardUtils.THIRD_ROW[this.tileId]||
                    BoardUtils.FIFTH_ROW[this.tileId]||
                    BoardUtils.SEVENTH_ROW[this.tileId]){
                setBackground(this.tileId % 2 == 0 ? lightTileColor : darkTileColor);
            }else if(BoardUtils.SECOND_ROW[this.tileId]||
                    BoardUtils.FOURTH_ROW[this.tileId]||
                    BoardUtils.SIXTH_ROW[this.tileId]||
                    BoardUtils.EIGHTH_ROW[this.tileId]){
                setBackground(this.tileId % 2 != 0 ? lightTileColor : darkTileColor);
            }
        }

        private void highlightLegals(final Board board) {
            if (true) {
                for (final Move move : pieceLegalMoves(board)) {
                    if (move.getDestinationCoordinate() == this.tileId) {
                        try {
                            add(new JLabel(new ImageIcon(ImageIO.read(new File("img/game/green_dot.png")))));
                        }
                        catch (final IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

        private Collection<Move> pieceLegalMoves(final Board board) {
            if(humanMovedPiece != null && humanMovedPiece.getPieceTeam() == board.currentPlayer().getTeam()) {
                return humanMovedPiece.calculateLegalMoves(board);
            }
            return Collections.emptyList();
        }

    }

}
