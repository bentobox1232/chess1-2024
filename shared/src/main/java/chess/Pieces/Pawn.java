package chess.Pieces;

import chess.*;

import java.util.Collection;
import java.util.HashSet;

public class Pawn implements ChessPieceInterface {

    private final ChessGame.TeamColor pieceColor;

    public Pawn(ChessGame.TeamColor pieceColor) {
        this.pieceColor = pieceColor;
    }

    @Override
    public ChessGame.TeamColor getTeamColor() {
        return pieceColor;
    }

    @Override
    public ChessPiece.PieceType getPieceType() {
        return ChessPiece.PieceType.PAWN;
    }

    @Override
    public Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition) {
        Collection<ChessMove> validMoves = new HashSet<>();

        int forwardDirection = (getTeamColor() == ChessGame.TeamColor.WHITE) ? 1 : -1;

        // Check possible single move forward
        addPawnMove(board, myPosition, validMoves, forwardDirection, 0);

        if ((getTeamColor() == ChessGame.TeamColor.WHITE && myPosition.getRow() == 2) ||
                (getTeamColor() == ChessGame.TeamColor.BLACK && myPosition.getRow() == 7)) {
            addPawnDoubleMove(board, myPosition, validMoves, forwardDirection, 0);
        }

        // Check possible diagonal captures
        addPawnCaptureMove(board, myPosition, validMoves, forwardDirection, 1);
        addPawnCaptureMove(board, myPosition, validMoves, forwardDirection, -1);

        return validMoves;
    }

    private void addPawnMove(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> validMoves, int rowDirection, int colDirection) {
        int newRow = myPosition.getRow() + rowDirection;
        int newCol = myPosition.getColumn() + colDirection;

        if (board.isValidPosition(newRow, newCol)) {
            ChessPosition endPosition = new ChessPosition(newRow, newCol);
            ChessPiece targetPiece = board.getPiece(endPosition);

            // Normal move
            if (colDirection == 0) {
                if (targetPiece == null) {
                    // Check for promotion
                    if (isPromotionRow(newRow)) {
                        addPromotionMoves(validMoves, myPosition, endPosition);
                    } else {
                        validMoves.add(new ChessMove(myPosition, endPosition, null));
                    }
                }
            }
        }
    }

    private void addPawnCaptureMove(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> validMoves, int rowDirection, int colDirection) {
        int newRow = myPosition.getRow() + rowDirection;
        int newCol = myPosition.getColumn() + colDirection;

        if (board.isValidPosition(newRow, newCol)) {
            ChessPosition endPosition = new ChessPosition(newRow, newCol);
            ChessPiece targetPiece = board.getPiece(endPosition);

            // Capture move
            if (targetPiece != null && targetPiece.getTeamColor() != getTeamColor()) {
                // Check for promotion
                if (isPromotionRow(newRow)) {
                    addPromotionMoves(validMoves, myPosition, endPosition);
                } else {
                    validMoves.add(new ChessMove(myPosition, endPosition, null));
                }
            }
        }
    }

    private void addPromotionMoves(Collection<ChessMove> validMoves, ChessPosition startPosition, ChessPosition endPosition) {
        validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.ROOK));
        validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.QUEEN));
        validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.BISHOP));
        validMoves.add(new ChessMove(startPosition, endPosition, ChessPiece.PieceType.KNIGHT));
    }

    private boolean isPromotionRow(int row) {
        return (getTeamColor() == ChessGame.TeamColor.WHITE && row == 8) ||
                (getTeamColor() == ChessGame.TeamColor.BLACK && row == 1);
    }

    private void addPawnDoubleMove(ChessBoard board, ChessPosition myPosition, Collection<ChessMove> validMoves, int rowDirection, int colDirection) {
        int newRow = myPosition.getRow() + (2 * rowDirection);
        int newCol = myPosition.getColumn() + (2 * colDirection);

        // Check if the square two steps ahead is empty
        if (board.isValidPosition(newRow, newCol) && board.getPiece(new ChessPosition(newRow, myPosition.getColumn())) == null
            && board.getPiece(new ChessPosition(newRow - rowDirection, myPosition.getColumn())) == null) {
            ChessPosition endPosition = new ChessPosition(newRow, newCol);
            validMoves.add(new ChessMove(myPosition, endPosition, null));
        }
    }
}
