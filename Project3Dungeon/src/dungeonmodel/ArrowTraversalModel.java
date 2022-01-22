package dungeonmodel;

import java.util.ArrayList;
import java.util.List;

/**
 * This model is used to store the results of firing an arrow. This acts as a read-only model
 * outside the package and has several methods which can be used to read the status of firing an
 * arrow.
 */
class ArrowTraversalModel implements ArrowTraversalModelInterface {
  private List<DungeonLocationInterface> locationListTravelledByArrow;
  private boolean hitMonsterSuccessful;
  private boolean killMonsterSuccessful;
  private boolean arrowTravelledCompleteDistance;

  public ArrowTraversalModel() {
    locationListTravelledByArrow = new ArrayList<>();
    hitMonsterSuccessful = false;
    killMonsterSuccessful = false;
    arrowTravelledCompleteDistance = true;
  }

  void setLocationListTravelledByArrow(List<DungeonLocationInterface>
                                               locationListTravelledByArrow) {
    this.locationListTravelledByArrow = locationListTravelledByArrow;
  }

  void setHitMonsterSuccessful(boolean hitMonsterSuccessful) {
    this.hitMonsterSuccessful = hitMonsterSuccessful;
  }

  void setKillMonsterSuccessful(boolean killMonsterSuccessful) {
    this.killMonsterSuccessful = killMonsterSuccessful;
  }

  void setArrowTravelledCompleteDistance(boolean arrowTravelledCompleteDistance) {
    this.arrowTravelledCompleteDistance = arrowTravelledCompleteDistance;
  }

  ArrowTraversalModelInterface getDeepCopy() {
    ArrowTraversalModel arrowTraversalModel = new ArrowTraversalModel();
    arrowTraversalModel.setHitMonsterSuccessful(this.isHitMonsterSuccessful());
    arrowTraversalModel.setKillMonsterSuccessful(this.isKillMonsterSuccessful());
    arrowTraversalModel.setArrowTravelledCompleteDistance(this.isArrowTravelledCompleteDistance());
    List<DungeonLocationInterface> listOfLocationVisitedByArrow = new ArrayList<>();
    for (DungeonLocationInterface dungeonLocation : getLocationListTravelledByArrow()) {
      listOfLocationVisitedByArrow.add(((DungeonLocation) dungeonLocation).getDeepCopy());
    }
    arrowTraversalModel.setLocationListTravelledByArrow(listOfLocationVisitedByArrow);
    return arrowTraversalModel;
  }

  /**
   * Gets the locations that the arrow travelled. No DeepCopy required as this model is used for
   * holding data only.
   *
   * @return the locations that the arrow travelled.
   */
  @Override
  public List<DungeonLocationInterface> getLocationListTravelledByArrow() {
    return locationListTravelledByArrow;
  }

  @Override
  public boolean isHitMonsterSuccessful() {
    return hitMonsterSuccessful;
  }

  @Override
  public boolean isKillMonsterSuccessful() {
    return killMonsterSuccessful;
  }

  @Override
  public boolean isArrowTravelledCompleteDistance() {
    return arrowTravelledCompleteDistance;
  }

  @Override
  public String toString() {
    return "ArrowTraversalModel{"
            + "locationListTravelledByArrow=" + locationListTravelledByArrow
            + ", arrowHitMonster=" + hitMonsterSuccessful
            + ", arrowKillMonster=" + killMonsterSuccessful
            + ", arrowTravelledCompleteDistance=" + arrowTravelledCompleteDistance + '}';
  }
}
