package com.boardgame.morpion.Plugin;

import fr.le_campus_numerique.square_games.engine.tictactoe.TicTacToeGame;

import java.util.Locale;
import java.util.OptionalInt;



public interface GamePlugin {
    String getName(Locale locale);
    TicTacToeGame createGame(OptionalInt playerCount, OptionalInt boardSize);
}