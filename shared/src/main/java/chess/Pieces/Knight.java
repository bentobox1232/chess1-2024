package chess.Pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class Knight implements ChessPieceInterface {

    private ChessGame.TeamColor pieceColor;

    public Knight(ChessGame.TeamColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    @Override
    public ChessPiece.PieceType getPieceType() {
        return ChessPiece.PieceType.KNIGHT;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();

        int[][] moveOffsets = {
                {-2, -1}, {-2, 1},
                {-1, -2}, {-1, 2},
                {1, -2}, {1, 2},
                {2, -1}, {2, 1}
        };

        for (int[] offset : moveOffsets) {
            int newRow = myPosition.getRow() + offset[0];
            int newCol = myPosition.getColumn() + offset[1];

            if (board.isValidPosition(newRow, newCol)) {
                ChessPosition endPosition = new ChessPosition(newRow, newCol);
                ChessPiece targetPiece = board.getPiece(endPosition);

                if (targetPiece == null || targetPiece.getTeamColor() != getTeamColor()) {
                    validMoves.add(new ChessMove(myPosition, endPosition, null));
                }
            }
        }

        return validMoves;
    }
}

