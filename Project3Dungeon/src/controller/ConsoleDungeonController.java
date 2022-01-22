package controller;

import dungeonmodel.ArrowTraversalModelInterface;
import dungeonmodel.Direction;
import dungeonmodel.GameInterface;
import dungeonmodel.SmellLevel;
import dungeonmodel.TreasureEnum;
import dungeonmodel.Weapon;

import java.io.IOException;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;


/**
 * Console controller uses the Dungeon model to play the dungeon game as a text based computer
 * game. The input can be any appendable and output can be any readable.
 */
public class ConsoleDungeonController implements DungeonController {
  private final Appendable out;
  private final Scanner scan;

  /**
   * Constructor for the controller.
   *
   * @param in  the source to read from
   * @param out the target to print to
   */
  public ConsoleDungeonController(Readable in, Appendable out) {
    if (in == null || out == null) {
      throw new IllegalArgumentException("Readable and Appendable can't be null");
    }
    this.out = out;
    scan = new Scanner(in);
  }

  @Override
  public void playGame(GameInterface m)
          throws IllegalArgumentException, IllegalStateException {
    if (m == null) {
      throw new IllegalArgumentException("Model cannot be NULL");
    }
    //Play the entire game here
    try {
      while (!m.isGameOver()) {
        out.append("\nYou are in a "
                + m.getDungeon().getType(m.getPlayer().getCurrentLocationOfPlayer()) + "\n");
        //show treasure
        if (m.getTreasureCountAtPlayerLocation() > 0) {
          out.append("You find " + printHashMap(m.getPlayer().getCurrentLocationOfPlayer()
                  .getAllTreasure()) + " here.\n");
        }
        if (m.getPlayer().getCurrentLocationOfPlayer().getAllWeapon().size() > 0) {
          out.append("You find " + printHashMap(m.getPlayer().getCurrentLocationOfPlayer()
                  .getAllWeapon()) + " here.\n");
        }
        SmellLevel currentSmellLevel =
                m.getDungeon().getSmellLevel(m.getPlayer().getCurrentLocationOfPlayer());
        if (currentSmellLevel.equals(SmellLevel.STRONG)) {
          out.append("You smell something terrible nearby\n");
        } else if (currentSmellLevel.equals(SmellLevel.LIGHT)) {
          out.append("You smell a mild stench nearby\n");
        }

        //show weapon
        //show smell if any
        out.append("Possible exits: " + m.getPlayerPossibleMoves().toString().substring(1,
                m.getPlayerPossibleMoves().toString().length() - 1) + "\n");

        out.append("\nMove, Pickup, Shoot, Player Details (M-P-S-D)?: ");
        String input = scan.next();

        while (!isOneOfIgnoreCase(input, "M", "P", "S", "D")) {
          out.append("Enter M - Move, P - Pickup, S - Shoot, D - Player Details: ");
          input = scan.next();
        }
        input = input.toUpperCase();
        //Now we have a user selection
        if (input.equals("M")) {
          movePlayer(m);
        } else if (input.equals("P")) {
          pick(m);
        } else if (input.equals("S")) {
          attack(m);
        } else {
          displayPlayerDetails(m);
        }
      }
      if (m.isGameOver() && m.getPlayer().getPlayerHealth() == 100) {
        out.append("Congratulations! You won the game.\n");
      } else {
        out.append("Nom, nom, nomm.. An Otyugh ate you!\n");
      }
    } catch (IOException e) {
      throw new IllegalStateException("Append failed", e);
    }

  }

  private String printHashMap(HashMap hashMap) {
    StringBuilder sb = new StringBuilder();
    Iterator hmIterator = hashMap.entrySet().iterator();
    while (hmIterator.hasNext()) {
      Map.Entry mapElement = (Map.Entry) hmIterator.next();
      sb.append("," + mapElement.getValue() + " " + mapElement.getKey());
    }
    if (sb.length() < 1) {
      return "NONE";
    } else {
      return sb.substring(1);
    }
  }

  private void displayPlayerDetails(GameInterface m) throws IOException {
    out.append("Treasure collected: " + printHashMap(m.getPlayer().getAllTreasure()) + "\n");
    out.append("Available weapons: " + printHashMap(m.getPlayer().getAllWeapon()) + "\n");
  }

  private void movePlayer(GameInterface m) throws IOException {
    out.append("Where? (N,S,E,W): ");
    String playerMove = scan.next();
    boolean validInput = false;
    try {
      do {
        validInput = isOneOfIgnoreCase(playerMove, "N", "S", "E", "W");
        if (!validInput) {
          out.append("(N - NORTH, S - SOUTH, E - EAST, W - WEST) Where?: ");
          playerMove = scan.next();
          continue;
        }
        if (!m.movePlayerToDirection(playerMove.charAt(0))) {
          out.append("Kindly enter a valid move.\n");
        }
        validInput = true;
      }
      while (!validInput);
    } catch (IllegalArgumentException e) {
      out.append("Kindly enter a valid move.\n");
    }
  }

  private boolean isOneOfIgnoreCase(String input, String... option) {
    boolean result = false;
    for (String v : option) {
      if (v.equalsIgnoreCase(input)) {
        result = true;
        break;
      }
    }
    return result;
  }

  private boolean isNumeric(String input) {
    try {
      Integer.parseInt(input);
      return true;
    } catch (NumberFormatException e) {
      return false;
    }
  }

  private void attack(GameInterface m) throws IOException {

    String strDistance;
    boolean validInput;
    out.append("No of caves (1-5): ");
    strDistance = scan.next();
    validInput = isNumeric(strDistance);
    while (!validInput || (validInput && !(Integer.parseInt(strDistance) <= 5
            && Integer.parseInt(strDistance) > 0))) {
      out.append("Enter valid distance. No of caves (1-5): ");
      strDistance = scan.next();
      validInput = isNumeric(strDistance);
    }
    String direction;
    out.append("Where? (N,S,E,W): ");
    direction = scan.next();
    validInput = isOneOfIgnoreCase(direction, "N", "S", "E", "W");
    while (!validInput) {
      out.append("Enter valid direction.(n - NORTH, s - SOUTH, e - EAST, W - WEST) : ");
      direction = scan.next();
      validInput = isOneOfIgnoreCase(direction, "N", "S", "E", "W");
    }
    Direction attackDirection = getDirection(direction);
    try {
      ArrowTraversalModelInterface arrow = m.killMonster(Weapon.CROOKED_ARROW, attackDirection,
              Integer.parseInt(strDistance));
      out.append("You shot an arrow!\n");
      if (arrow.isKillMonsterSuccessful()) {
        out.append("Bravo! You killed a monster\n");
      }
    } catch (IllegalArgumentException e) {
      out.append("Kindly select available weapon and direction");
    }
  }

  private Direction getDirection(String direction) {
    Direction d;
    switch (direction.toLowerCase()) {
      case "e":
        d = Direction.EAST;
        break;
      case "w":
        d = Direction.WEST;
        break;
      case "n":
        d = Direction.NORTH;
        break;
      default:
        d = Direction.SOUTH;
        break;
    }
    return d;
  }

  private void pick(GameInterface m) throws IOException {
    out.append("What? (Ruby - r, Diamond - d, Sapphire - s, Arrow - a): ");
    String input = scan.next().toLowerCase();
    while (!isOneOfIgnoreCase(input, "s", "r", "a", "d")) {
      out.append("Kindly enter valid input. Pick what? (Ruby - r, Diamond - d, Sapphire - s, "
              + "Arrow - a): ");
      input = scan.next();
    }
    TreasureEnum treasureEnum;
    if (isOneOfIgnoreCase(input, "s", "r", "d")) {
      switch (input) {
        case "s":
          treasureEnum = TreasureEnum.SAPPHIRE;
          break;
        case "r":
          treasureEnum = TreasureEnum.RUBY;
          break;
        default:
          treasureEnum = TreasureEnum.DIAMOND;
          break;
      }
      try {
        m.collectATreasure(treasureEnum, 1);
        out.append("You picked a treasure.\n");
      } catch (IllegalArgumentException e) {
        out.append("You can only pick treasure at current location\n");
      }

    }

    if (isOneOfIgnoreCase(input, "a")) {
      try {
        m.collectAWeapon(Weapon.CROOKED_ARROW, 1);
        out.append("You picked an arrow.\n");
      } catch (IllegalArgumentException e) {
        out.append("You can only pick weapon at current location\n");
      }

    }
  }


}
