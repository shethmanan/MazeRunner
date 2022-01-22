package dungeonmodel;

/**
 * Represents all weapons that can be found in the dungeon. Also acts as factory to get the rules
 * of each weapon.
 */
public enum Weapon {
  CROOKED_ARROW {
    @Override
    WeaponRuleInterface getRuleClass() {
      return new CrookedArrowRule();
    }

    int getDamage() {
      return 50;
    }
  };

  abstract WeaponRuleInterface getRuleClass();

  abstract int getDamage();

}
