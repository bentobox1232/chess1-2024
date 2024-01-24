package chess;

import java.util.Collection;

public interface ChessPieceInterface {
    ChessGame.TeamColor getTeamColor();

    ChessPiece.PieceType getPieceType();

    Collection<ChessMove> pieceMoves(ChessBoard board, ChessPosition myPosition);
}
