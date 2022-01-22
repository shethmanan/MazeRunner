package view;

import java.awt.Color;
import java.io.BufferedReader;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.BorderFactory;
import javax.swing.UIManager;

import javax.swing.border.Border;


/**
 * Panel used to display game instructions.
 */
public class InstructionPanel extends JPanel {
  private final String instructionFileName = "instruction";
  private String basePathForFile = "text/";
  private String fileExtension = ".txt";

  /**
   * Construct the game panel.
   */
  public InstructionPanel() {
    JTextArea instructionArea = new JTextArea(3, 15);
    Border border = BorderFactory.createLineBorder(Color.BLACK);
    instructionArea.setBorder(BorderFactory.createCompoundBorder(border,
            BorderFactory.createEmptyBorder(10, 10, 10, 5)));
    instructionArea.setWrapStyleWord(true);
    instructionArea.setLineWrap(true);
    instructionArea.setEditable(false);
    instructionArea.setFocusable(false);
    instructionArea.setBackground(UIManager.getColor("Label.background"));

    StringBuilder sb = new StringBuilder();
    try (InputStream in = ClassLoader.getSystemResourceAsStream(basePathForFile
            + instructionFileName + fileExtension);
         BufferedReader reader = new BufferedReader(new InputStreamReader(in))) {
      String s;
      while ((s = reader.readLine()) != null) {
        sb.append(s);
      }
    } catch (IOException e) {
      e.printStackTrace();
    }

    String instructionText = sb.toString();
    instructionText = instructionText.replaceAll("\\\\n", System.lineSeparator());
    instructionText = instructionText.replaceAll("zzendzz", "");
    instructionArea.setText(instructionText);
    this.add(instructionArea);
  }


}
