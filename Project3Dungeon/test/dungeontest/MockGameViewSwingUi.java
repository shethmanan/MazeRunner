package dungeontest;

import java.io.IOException;

import controller.ControllerFeatures;
import view.GuiView;
import dungeonmodel.SoundType;

/**
 * Mock View used to test the Ui Controller.
 */
public class MockGameViewSwingUi implements GuiView {
  Appendable out;

  /**
   * Create the instance of the Mock View.
   *
   * @param out the output stream which will be used to append the log and be tested.
   */
  public MockGameViewSwingUi(Appendable out) {
    this.out = out;
  }

  @Override
  public void setVisible() {
    try {
      out.append("setVisible() called.");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void setListener(ControllerFeatures controller)
          throws IllegalArgumentException {
    if (controller == null) {
      throw new IllegalArgumentException("Controller features cannot be null");
    } else {
      try {
        out.append("setListener() called with controller features.");
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }

  @Override
  public void showPopupMessage(String message) {
    try {
      out.append("showPopupMessage() called with message = " + message);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void openGameParameterInputPanel() {
    try {
      out.append("openGameParameterInputPanel() called");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void repaint() {
    try {
      out.append("repaint() called");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void dispose() {
    try {
      out.append("dispose() called");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void playSound(SoundType soundType) {
    try {
      out.append("playSound() called");
    } catch (IOException e) {
      e.printStackTrace();
    }
  }
}
