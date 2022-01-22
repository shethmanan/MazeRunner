package dungeonmodel;

/**
 * This class represent a dangerous and smelly monster called Otyugh.
 */
public class Otyugh implements Monster {
  private int health;
  private final String type;
  private static final int SMELL_RADIUS = 2;
  private static final int DAMAGE_DEALT = 100;

  /**
   * Create an Otyugh by providing its initial health.
   *
   * @param health the health of the monster created.
   */
  public Otyugh(int health) {
    this.health = health;
    type = "otyugh";
  }

  @Override
  public String getType() {
    return this.type;
  }

  @Override
  public int getHealth() {
    return this.health;
  }

  @Override
  public int getSmellRadius() {
    return SMELL_RADIUS;
  }

  @Override
  public int getDamage() {
    return DAMAGE_DEALT;
  }

  @Override
  public Monster getCopy(int health) {
    return new Otyugh(health);
  }

  @Override
  public String toString() {
    return "Otyugh: "
            + "health=" + health
            + ", smell radius=" + SMELL_RADIUS;
  }
}
