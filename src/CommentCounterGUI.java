import java.awt.*;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

/**
 * The window for the JavaDoc Comment Counter. It has four tabs that show the
 * parsed parts of a file, a summary of the character counts, and a button
 * that opens a file chooser and starts the analysis.
 */
public class CommentCounterGUI extends JFrame {
    //the state machine that does the counting
    private CommentCounter counter = new CommentCounter();

    //one text area per tab
    private JTextArea codeArea = makeTextArea();
    private JTextArea commentsArea = makeTextArea();
    private JTextArea javadocArea = makeTextArea();
    private JTextArea originalArea = makeTextArea();

    //summary labels
    private JLabel fileLabel = new JLabel("No file selected");
    private JLabel codeLabel = new JLabel();
    private JLabel singleLineLabel = new JLabel();
    private JLabel multiLineLabel = new JLabel();
    private JLabel javadocLabel = new JLabel();

    //Builds the window
    public CommentCounterGUI() {
        super("JavaDoc Comment Counter");

        //the four tabs
        JTabbedPane tabs = new JTabbedPane();
        tabs.addTab("Code Only", new JScrollPane(codeArea));
        tabs.addTab("Comments", new JScrollPane(commentsArea));
        tabs.addTab("JavaDoc", new JScrollPane(javadocArea));
        tabs.addTab("Original", new JScrollPane(originalArea));

        //the summary under the tabs
        JPanel summaryPanel = new JPanel(new GridLayout(5, 1, 0, 4));
        summaryPanel.add(fileLabel);
        summaryPanel.add(codeLabel);
        summaryPanel.add(singleLineLabel);
        summaryPanel.add(multiLineLabel);
        summaryPanel.add(javadocLabel);
        updateSummary();

        //the button that picks a file and analyzes it
        JButton analyzeButton = new JButton("Analyze File");
        analyzeButton.addActionListener(e -> chooseAndAnalyze());
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        buttonPanel.add(analyzeButton);

        JPanel bottomPanel = new JPanel(new BorderLayout());
        bottomPanel.setBorder(BorderFactory.createEmptyBorder(8, 10, 8, 10));
        bottomPanel.add(summaryPanel, BorderLayout.CENTER);
        bottomPanel.add(buttonPanel, BorderLayout.SOUTH);

        //put it all together
        setLayout(new BorderLayout());
        add(tabs, BorderLayout.CENTER);
        add(bottomPanel, BorderLayout.SOUTH);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 650);
        setLocationRelativeTo(null);
    }

    /**
     * Makes a read only text area with a fixed width font so the redacted
     * text lines up with the original
     * @return The new text area
     */
    private static JTextArea makeTextArea() {
        JTextArea retVal = new JTextArea();
        retVal.setEditable(false);
        retVal.setFont(new Font(Font.MONOSPACED, Font.PLAIN, 13));
        retVal.setTabSize(4);
        return retVal;
    }

    //Shows a file chooser and, if a file is picked, analyzes it
    private void chooseAndAnalyze() {
        JFileChooser chooser = new JFileChooser(new File(System.getProperty("user.dir"), "testfiles"));
        chooser.setFileFilter(new FileNameExtensionFilter("Java files (*.java)", "java"));

        if(chooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();

            try {
                String text = new String(Files.readAllBytes(file.toPath()), StandardCharsets.UTF_8);
                counter.analyze(text);

                //fill in the tabs
                setText(codeArea, counter.getCodeText());
                setText(commentsArea, counter.getCommentsText());
                setText(javadocArea, counter.getJavadocText());
                setText(originalArea, counter.getOriginalText());

                fileLabel.setText("File: " + file.getName());
                updateSummary();
            }
            catch(IOException ex) {
                JOptionPane.showMessageDialog(this, "Could not read the file:\n" + ex.getMessage(),
                    "Error", JOptionPane.ERROR_MESSAGE);
            }
        }
    }

    /**
     * Puts text in a text area and scrolls back to the top
     * @param area The text area
     * @param text The text to show
     */
    private void setText(JTextArea area, String text) {
        area.setText(text);
        area.setCaretPosition(0);
    }

    //Updates the summary labels from the counter
    private void updateSummary() {
        codeLabel.setText("Code Characters: " + counter.getNumCodeChars());
        singleLineLabel.setText("Single Line Characters: " + counter.getNumSingleLineChars());
        multiLineLabel.setText("Multi Line Characters: " + counter.getNumMultiLineChars());
        javadocLabel.setText("JavaDoc Characters: " + counter.getNumJavadocChars());
    }
}