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

        addBishopDiagonalMoves(board, myPosition, validMoves, 1, 1);

        addBishopDiagonalMoves(board, myPosition, validMoves, 1, -1);

        addBishopDiagonalMoves(board, myPosition, validMoves, -1, 1);

        addBishopDiagonalMoves(board, myPosition, validMoves, -1, -1);

        return validMoves;
    }

    private void addBishopDiagonalMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> validMoves, int rowDirection, int colDirection) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        ChessPiece targetPiece;
        do {
            row += rowDirection;
            col += colDirection;

            if (!board.isValidPosition(row, col)) {
                break;
            }

            ChessPosition endPosition = new ChessPosition(row, col);
            targetPiece = board.getPiece(endPosition);

            if (targetPiece == null || targetPiece.getTeamColor() != getTeamColor()) {
                validMoves.add(new ChessMove(myPosition, endPosition, null));
            }

        } while (targetPiece == null);

    }
}
