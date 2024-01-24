package chess.Pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class Bishop implements ChessPieceInterface {

    private ChessGame.TeamColor pieceColor;

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

        // Diagonal moves (top-left to bottom-right)
        addDiagonalMoves(board, myPosition, validMoves, 1, 1);

        // Diagonal moves (top-right to bottom-left)
        addDiagonalMoves(board, myPosition, validMoves, 1, -1);

        // Diagonal moves (bottom-left to top-right)
        addDiagonalMoves(board, myPosition, validMoves, -1, 1);

        // Diagonal moves (bottom-right to top-left)
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
                // Empty square or square with an opponent's piece - valid move
                validMoves.add(new ChessMove(myPosition, endPosition, null));
            }

            if (targetPiece != null) {
                // Stop if there's a piece blocking the diagonal
                break;
            }

            // Move to the next diagonal position
            row += rowDirection;
            col += colDirection;
        }
    }
}
