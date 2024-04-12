package chess.Pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class Queen implements ChessPieceInterface {

    private final ChessGame.TeamColor pieceColor;

    public Queen(ChessGame.TeamColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    @Override
    public ChessPiece.PieceType getPieceType() {
        return ChessPiece.PieceType.QUEEN;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();

        addDiagonalMoves(board, myPosition, validMoves, 1, 1);

        addDiagonalMoves(board, myPosition, validMoves, 1, -1);

        addDiagonalMoves(board, myPosition, validMoves, -1, 1);

        addDiagonalMoves(board, myPosition, validMoves, -1, -1);

        addDiagonalMoves(board, myPosition, validMoves, 0, 1);

        addDiagonalMoves(board, myPosition, validMoves, 0, -1);

        addDiagonalMoves(board, myPosition, validMoves, 1, 0);

        addDiagonalMoves(board, myPosition, validMoves, -1, 0);

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

