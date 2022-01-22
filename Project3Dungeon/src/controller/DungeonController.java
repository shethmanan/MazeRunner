package controller;

import dungeonmodel.GameInterface;

/**
 * Represents a controller for Dungeon game: handles user moves by executing them using model;
 * convey move outcomes to the user in some form.
 */
public interface DungeonController {
  /**
   * Execute a single game of Dungeon maze given a Dungeon Model. When the game is over,
   * the playGame method ends.
   *
   * @param m a non-null Dungeon Model
   */
  void playGame(GameInterface m);
}
