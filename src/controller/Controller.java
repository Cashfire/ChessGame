package controller;

import model.Piece;
import model.Pos;

import java.util.Set;
import java.util.stream.Collectors;

public class Controller {
  private ChessBoard chessBoard;

  public Controller() {
    chessBoard = new ChessBoard();
  }

  public ChessBoard getChessBoard() {
    return chessBoard;
  }

  public ViewState clickHandler(Pos clickPosition) {
    if (chessBoard.getCurrentPossibleMoves().isEmpty()) {
      // click some piece to show the possible moves
      if (chessBoard.getBoard().getPiece(clickPosition) != null) {
        chessBoard
            .getCurrentPossibleMoves()
            .addAll(
                chessBoard.getBoard().getPiece(clickPosition).getValidMoves(chessBoard.getBoard()));
        chessBoard.setSelectPosition(clickPosition);
      }
    } else {
      // click some square to move the piece
      if (chessBoard.getCurrentPossibleMoves().contains(clickPosition)) {
        chessBoard
            .getBoard()
            .getPiece(chessBoard.getSelectPosition())
            .move(chessBoard.getBoard(), clickPosition, false);
      } else {
        // If not in possible moves, clear the possible moves
        chessBoard.getCurrentPossibleMoves().clear();
      }
      chessBoard.getCurrentPossibleMoves().clear();
      chessBoard.setSelectPosition(null);
    }
    return new ViewState(
        chessBoard.getCurrentPossibleMoves(),
        Piece.getWhoseTurn(),
        Piece.getTotalRound(),
        getViewPieces());
  }

  public Set<ViewPiece> getViewPieces() {
    return chessBoard.getBoard().getCurrentActivePieces().stream()
        .map(p -> new ViewPiece(p.getPos(), p.getId()))
        .collect(Collectors.toSet());
  }
}
