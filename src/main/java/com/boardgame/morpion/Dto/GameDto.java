package com.boardgame.morpion.dto;

public class GameDto {
    private String gameId;
    private int playerCount;
    private int boardSize;
    private String opponentIds;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public int getPlayerCount() {
        return playerCount;
    }

    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }

    public int getBoardSize() {
        return boardSize;
    }

    public void setBoardSize(int boardSize) {
        this.boardSize = boardSize;
    }

    public String getOpponentIds() {
        return opponentIds;
    }

    public void setOpponentIds(String opponentIds) {
        this.opponentIds = opponentIds;
    }
}
