package org.jbomberman.model.listener;

/** The package type enum */
public enum PackageType {
  /** The load map package type */
  LOADMAP,
  /** The player movement package type */
  MOVE_PLAYER,
  /** The player bomb spawn package type */
  SPAWN_BOMB,
  /** The bomb explosion package type */
  BOMB_EXPLOSION,
  /** The tile destruction package type */
  TILE_DESTRUCTION,
  /** The mob movement package type */
  MOB_MOVEMENT,
  /** The remove mob spawn package type */
  REMOVE_MOB,
  /** The player lives update package type */
  PLAYER_LIVES_UPDATE,

  /** The player initial position package type */
  PLAYER_INITIAL_POSITION,
  /** The mob initial position package type */
  MOB_INITIAL_POSITION,
  /** The powerup spawn package type */
  SPAWN_POWERUP,
  /** The powerup application package type */
  POWERUP_APPLICATION,
  /** The powerup despawn package type */
  POWERUP_DESPAWN,
  /** the player score update package type */
  PLAYER_SCORE_UPDATE,
  /** The bomb spawn data package type */
  DRAW_PLAYER_LIVES_UPDATE,
  /** The spawn exit tile package type */
  SPAWN_EXIT_TILE,
  /** The game over update package type */
  GAME_OVER,
  /** The denkyun respawn package type */
  DENKYUN_RESPAWN,
  /** The level update package type */
  LEVEL_UPDATE,
  /** The youwin package type */
  YOU_WIN
}
