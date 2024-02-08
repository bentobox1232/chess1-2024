package chess;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

/**
 * For a class that can manage a chess game, making moves on a board
 * <p>
 * Note: You can add to this class, but you may not alter
 * signature of the existing methods.
 */
public class ChessGame {
    private TeamColor teamColor;
    private ChessBoard chessBoard;
    private ChessBoard tempBoard;
    public ChessGame(TeamColor teamColor,ChessBoard chessBoard) {
        this.teamColor = teamColor;
        this.chessBoard = chessBoard;
    }
    public ChessGame() {
        this.teamColor = TeamColor.WHITE; // Set the default team color if needed
        this.chessBoard = new ChessBoard();
        this.chessBoard.resetBoard();
    }

    /**
     * @return Which team's turn it is
     */
    public TeamColor getTeamTurn() {
        return this.teamColor;
    }

    /**
     * Set's which teams turn it is
     *
     * @param team the team whose turn it is
     */
    public void setTeamTurn(TeamColor team) {
        this.teamColor = team;
    }

    /**
     * Enum identifying the 2 possible teams in a chess game
     */
    public enum TeamColor {
        WHITE,
        BLACK
    }

    /**
     * Gets a valid moves for a piece at the given location
     *
     * @param startPosition the piece to get valid moves for
     * @return Set of valid moves for requested piece, or null if no piece at
     * startPosition
     */
    public Collection<ChessMove> validMoves(ChessPosition startPosition) {
        ChessPiece piece = chessBoard.getPiece(startPosition);
        setTeamColor(piece.getTeamColor());
        if (piece != null && piece.getTeamColor() == teamColor) {
            Collection<ChessMove> moves = piece.pieceMoves(chessBoard, startPosition);

            // Filter out invalid moves (e.g., moves that leave the king in check)
            moves = filterInvalidMoves(startPosition, moves);

            return moves;
        }
        return null;
    }

    /**
     * Makes a move in a chess game
     *
     * @param move chess move to preform
     * @throws InvalidMoveException if move is invalid
     */
    public void makeMove(ChessMove move) throws InvalidMoveException {
        ChessPosition startPosition = move.getStartPosition();
        ChessPosition endPosition = move.getEndPosition();

        ChessPiece piece = chessBoard.getPiece(startPosition);

        if (piece != null && piece.getTeamColor() == teamColor) {
            Collection<ChessMove> validMoves = validMoves(startPosition);

            if (validMoves.contains(move)) {
                chessBoard.makeMove(move, teamColor);
                switchTeamTurn();
            } else {
                throw new InvalidMoveException("Invalid move");
            }
        } else {
            throw new InvalidMoveException("No piece at the starting position");
        }
    }

    private void switchTeamTurn() {
        teamColor = (teamColor == TeamColor.WHITE) ? TeamColor.BLACK : TeamColor.WHITE;
    }

    private Collection<ChessMove> filterInvalidMoves(ChessPosition startPosition, Collection<ChessMove> moves) {
        // Create a list to store valid moves after filtering
        Collection<ChessMove> validMoves = new ArrayList<>(moves);

        ChessPiece originalPiece = chessBoard.getPiece(startPosition);

        // Iterate through the moves and filter out invalid ones
        Iterator<ChessMove> iterator = validMoves.iterator();
        while (iterator.hasNext()) {
            ChessMove move = iterator.next();
            try {
                // Create a temporary board to simulate the move
                tempBoard = new ChessBoard(chessBoard);
                tempBoard.makeMove(move, teamColor);

                // Check if the move puts the king in check
                if (isCheck(teamColor)) {
                    iterator.remove(); // Remove moves that leave the king in check
                }
            } catch (InvalidMoveException e) {
                iterator.remove(); // Remove moves that are invalid
            }
        }


        return validMoves;
    }

    /**
     * Determines if the given team is in check
     *
     * @param teamColor which team to check for check
     * @return True if the specified team is in check
     */
    public boolean isInCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKingPosition(teamColor, "");

        if (kingPosition.getColumn() == -1 & kingPosition.getRow() == -1) {
            return false;
        }

        // Check if any opposing team piece can attack the king
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition currentPosition = new ChessPosition(row, col);
                ChessPiece piece = chessBoard.getPiece(currentPosition);

                if (piece != null && piece.getTeamColor() != teamColor) {

                    Collection<ChessMove> moves = piece.pieceMoves(chessBoard, currentPosition);
                    if (moves != null && moves.stream().anyMatch(move -> move.getEndPosition().equals(kingPosition))) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    public boolean isCheck(TeamColor teamColor) {
        ChessPosition kingPosition = findKingPosition(teamColor, "t");

        if (kingPosition.getColumn() == -1 & kingPosition.getRow() == -1) {
            return false;
        }

        // Check if any opposing team piece can attack the king
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition currentPosition = new ChessPosition(row, col);
                ChessPiece piece = tempBoard.getPiece(currentPosition);

                if (piece != null && piece.getTeamColor() != teamColor) {

                    Collection<ChessMove> moves = piece.pieceMoves(tempBoard, currentPosition);
                    if (moves != null && moves.stream().anyMatch(move -> move.getEndPosition().equals(kingPosition))) {
                        return true;
                    }
                }
            }
        }

        return false;
    }

    // Helper method to find the position of the king of the specified team
    private ChessPosition findKingPosition(TeamColor teamColor, String board) {
        for (int row = 1; row <= 8; row++) {
            for (int col = 1; col <= 8; col++) {
                ChessPosition currentPosition = new ChessPosition(row, col);
                ChessPiece piece;
                if(board == "t") {
                    piece = tempBoard.getPiece(currentPosition);
                } else {
                    piece = chessBoard.getPiece(currentPosition);
                }

                if (piece != null && piece.getTeamColor() == teamColor && piece.getPieceType() == ChessPiece.PieceType.KING) {
                    return currentPosition;
                }
            }
        }

        return new ChessPosition(-1, -1);
    }


    /**
     * Determines if the given team is in checkmate
     *
     * @param teamColor which team to check for checkmate
     * @return True if the specified team is in checkmate
     */
    public boolean isInCheckmate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Determines if the given team is in stalemate, which here is defined as having
     * no valid moves
     *
     * @param teamColor which team to check for stalemate
     * @return True if the specified team is in stalemate, otherwise false
     */
    public boolean isInStalemate(TeamColor teamColor) {
        throw new RuntimeException("Not implemented");
    }

    /**
     * Sets this game's chessboard with a given board
     *
     * @param board the new board to use
     */
    public void setBoard(ChessBoard board) {
        this.chessBoard = board;
    }

    /**
     * Gets the current chessboard
     *
     * @return the chessboard
     */
    public ChessBoard getBoard() {
        return this.chessBoard;
    }
    public void setTeamColor(TeamColor teamColor) {
        this.teamColor = teamColor;
    }
}
