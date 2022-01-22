package view;

import java.awt.BorderLayout;
import java.awt.HeadlessException;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.WindowConstants;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.HashMap;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

import controller.ControllerFeatures;
import dungeonmodel.GameReadonlyInterface;
import dungeonmodel.SoundType;

/**
 * Uses swing library and renders a Gui to play the dungeon game.
 */
public class GameViewSwingUi extends JFrame implements GuiView {
  private GameReadonlyInterface gameInterface;
  private MazePanel maze;
  private static final String title = "Maze Game";
  private CustomMenuBar menuBar;
  private NewGameParameterPanel newGameParameterPanel;
  private HashMap<SoundType, String> soundFile;
  private static String audioExtension = ".wav";
  private static final String pathForAudioFile = "audio/";

  /**
   * Create a new view using the model.
   *
   * @param model the model which will be used to create the view
   * @throws HeadlessException when the input or output device is not supported
   */
  public GameViewSwingUi(GameReadonlyInterface model)
          throws HeadlessException, IllegalArgumentException {
    super(title);
    if (model == null) {
      throw new IllegalArgumentException("Model cannot be null");
    }
    this.setSize(800, 600);
    this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    this.setResizable(false);
    maze = new MazePanel(model);
    gameInterface = model;
    JScrollPane jScrollPane = new JScrollPane(maze, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,
            JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
    GameStatusPanel gameStatusPanel = new GameStatusPanel(model);

    this.add(gameStatusPanel, BorderLayout.LINE_END);
    this.add(jScrollPane, BorderLayout.CENTER);
    menuBar = new CustomMenuBar();
    this.add(menuBar, BorderLayout.NORTH);
    newGameParameterPanel = new NewGameParameterPanel();
    generateSoundMapping();


  }

  private void generateSoundMapping() {
    soundFile = new HashMap<>();
    soundFile.put(SoundType.PLAYER_LOSE, "lose");
    soundFile.put(SoundType.PLAYER_WON, "win");
    soundFile.put(SoundType.MONSTER_DEAD, "kill");
    soundFile.put(SoundType.PLAYER_MOVE, "move");
    soundFile.put(SoundType.PICK, "pick");
  }

  @Override
  public void setListener(ControllerFeatures controller) {
    MazeListener listener = new MazeListener(controller, gameInterface);
    this.addKeyListener(listener);
    this.addMouseListener(listener);
    this.maze.addMouseListener(listener);
    menuBar.setController(controller);
    newGameParameterPanel.setController(controller);
  }

  @Override
  public void showPopupMessage(String message) {
    JOptionPane.showMessageDialog(this, message);
  }

  @Override
  public void openGameParameterInputPanel() {
    newGameParameterPanel.showPanel();
  }

  @Override
  public void playSound(SoundType soundType) {
    try {
      playFile(soundFile.get(soundType));
    } catch (NullPointerException e) {
      System.out.println("Audio file for " + soundType + " could not be read");
    }
  }

  private void playFile(String fileName) throws NullPointerException {
    if (fileName == null) {
      return;
    }
    AudioInputStream audioInputStream = null;
    try {
      audioInputStream = AudioSystem
              .getAudioInputStream(
                      new BufferedInputStream(
                              ClassLoader.getSystemResourceAsStream(
                                      pathForAudioFile + fileName + audioExtension)));
    } catch (UnsupportedAudioFileException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    Clip clip = null;
    try {
      clip = AudioSystem.getClip();
    } catch (LineUnavailableException e) {
      e.printStackTrace();
    }
    try {
      clip.open(audioInputStream);
    } catch (LineUnavailableException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
    clip.start();
  }

  @Override
  public void setVisible() {
    this.setVisible(true);
    this.requestFocus();
  }
}
