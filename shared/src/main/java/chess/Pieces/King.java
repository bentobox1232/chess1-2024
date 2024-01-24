package chess.Pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class King implements ChessPieceInterface {

    private ChessGame.TeamColor pieceColor;

    public King(ChessGame.TeamColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    @Override
    public ChessPiece.PieceType getPieceType() {
        return ChessPiece.PieceType.KING;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();

        addKingMoves(board, myPosition, validMoves, 1, 0);
        addKingMoves(board, myPosition, validMoves, -1, 0);
        addKingMoves(board, myPosition, validMoves, 0, 1);
        addKingMoves(board, myPosition, validMoves, 0, -1);
        addKingMoves(board, myPosition, validMoves, 1, 1);
        addKingMoves(board, myPosition, validMoves, 1, -1);
        addKingMoves(board, myPosition, validMoves, -1, 1);
        addKingMoves(board, myPosition, validMoves, -1, -1);

        return validMoves;
    }

    private void addKingMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> validMoves, int rowDirection, int colDirection) {
        int row = myPosition.getRow() + rowDirection;
        int col = myPosition.getColumn() + colDirection;

        if (board.isValidPosition(row, col)) {
            ChessPosition endPosition = new ChessPosition(row, col);
            ChessPiece targetPiece = board.getPiece(endPosition);

            if (targetPiece == null || targetPiece.getTeamColor() != getTeamColor()) {
                validMoves.add(new ChessMove(myPosition, endPosition, null));
            }
        }
    }
}
