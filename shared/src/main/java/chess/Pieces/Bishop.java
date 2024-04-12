package chess.Pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class Bishop implements ChessPieceInterface {

    private final ChessGame.TeamColor pieceColor;

    public Bishop(ChessGame.TeamColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    @Override
    public ChessPiece.PieceType getPieceType() {
        return ChessPiece.PieceType.BISHOP;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();

        addDiagonalMoves(board, myPosition, validMoves, 1, 1);

        addDiagonalMoves(board, myPosition, validMoves, 1, -1);

        addDiagonalMoves(board, myPosition, validMoves, -1, 1);

        addDiagonalMoves(board, myPosition, validMoves, -1, -1);

        return validMoves;
    }

    private void addDiagonalMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> validMoves, int rowDirection, int colDirection) {
        int row = myPosition.getRow() + rowDirection;
        int col = myPosition.getColumn() + colDirection;

        while (board.isValidPosition(row, col)) {
            ChessPosition endPosition = new ChessPosition(row, col);
            ChessPiece targetPiece = board.getPiece(endPosition);

            if (targetPiece == null || targetPiece.getTeamColor() != getTeamColor()) {
                validMoves.add(new ChessMove(myPosition, endPosition, null));
            }

            if (targetPiece != null) {
                break;
            }

            row += rowDirection;
            col += colDirection;
        }
    }
}
