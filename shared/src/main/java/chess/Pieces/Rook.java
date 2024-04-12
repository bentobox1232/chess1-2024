package chess.Pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class Rook implements ChessPieceInterface {

    private final ChessGame.TeamColor pieceColor;

    public Rook(ChessGame.TeamColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    @Override
    public ChessPiece.PieceType getPieceType() {
        return ChessPiece.PieceType.ROOK;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();
        
        addSideMoves(board, myPosition, validMoves, 0, 1);
        
        addSideMoves(board, myPosition, validMoves, 0, -1);

        addSideMoves(board, myPosition, validMoves, 1, 0);

        addSideMoves(board, myPosition, validMoves, -1, 0);

        return validMoves;
    }

    private void addSideMoves(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> validMoves, int rowDirection, int colDirection) {
        int row = myPosition.getRow();
        int col = myPosition.getColumn();

        while (true) {
            row += rowDirection;
            col += colDirection;

            if (!board.isValidPosition(row, col)) {
                break;
            }

            ChessPosition endPosition = new ChessPosition(row, col);
            ChessPiece targetPiece = board.getPiece(endPosition);

            if (targetPiece == null || targetPiece.getTeamColor() != getTeamColor()) {
                validMoves.add(new ChessMove(myPosition, endPosition, null));
            }

            if (targetPiece != null) {
                break;
            }
        }
    }
}

