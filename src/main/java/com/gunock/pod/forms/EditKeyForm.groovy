package com.gunock.pod.forms

import com.gunock.pod.cipher.BarChart
import com.gunock.pod.cipher.HomophonicCipherEncrypter
import com.gunock.pod.utils.FormUtil
import com.gunock.pod.utils.HelperUtil
import org.json.JSONObject

import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.AbstractDocument
import javax.swing.text.AttributeSet
import javax.swing.text.BadLocationException
import javax.swing.text.DocumentFilter
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.WindowEvent
import java.awt.event.WindowListener

class EditKeyForm {

    private static JFrame frame
    private static JFrame chartFrame
    private static Map<Character, Set<Character>> encryptionKey
    private static String exampleText

    static void construct(int x, int y, Map<Character, Set<Character>> key, String text) {
        encryptionKey = key
        exampleText = text
        create()
        frame.setLocation(x, y)
        frame.setVisible(true)
    }

    static void create() {
        JTextField filenameField = new JTextField("key.json")

        JPanel saveButtonPanel = new JPanel()
        FormUtil.setBoxLayout(saveButtonPanel, BoxLayout.X_AXIS)
        FormUtil.addButton(saveButtonPanel, "Save Key", saveButtonAction(filenameField))
        saveButtonPanel.add(filenameField)

        JPanel chartsButtonPanel = new JPanel()
        chartsButtonPanel.setLayout(new FlowLayout())
        FormUtil.addButton(chartsButtonPanel, "Show Frequency Charts", chartsButtonAction())

        JPanel buttonPanel = new JPanel()
        FormUtil.setBoxLayout(buttonPanel, BoxLayout.Y_AXIS)
        FormUtil.addAllComponents(buttonPanel, [saveButtonPanel, chartsButtonPanel])

        frame = new JFrame("Edit key")
        frame.getContentPane().add(buttonPanel)
        frame.addWindowListener(onCloseAction())

        mapToFrameElements(encryptionKey)

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS))
        frame.setSize(400, 800)
        frame.setResizable(false)
    }

    static ActionListener chartsButtonAction() {
        return new ActionListener() {
            @Override
            void actionPerformed(ActionEvent e) {
                final String encryptedText = HomophonicCipherEncrypter.encrypt(exampleText, encryptionKey)

                Map<Character, Integer> analyzedAlphabet = HelperUtil.analyzeCharactersFrequency(exampleText.toLowerCase())
                Map<Character, Integer> analyzedAlphabetEncrypted = HelperUtil.analyzeCharactersFrequency(encryptedText)

                new Thread(new Runnable() {
                    @Override
                    void run() {
                        FormUtil.close(chartFrame)
                        JFrame originalTextChart = BarChart.getChart(analyzedAlphabet, "Original Text")
                        JFrame encryptedTextChart = BarChart.getChart(analyzedAlphabetEncrypted, "Encrypted Text")

                        chartFrame = new JFrame()
                        FormUtil.setBoxLayout(chartFrame.getContentPane() as JComponent, BoxLayout.Y_AXIS)

                        chartFrame.add(originalTextChart.getContentPane().getComponent(0))
                        chartFrame.add(encryptedTextChart.getContentPane().getComponent(0))
                        chartFrame.setSize(1000, 800)
                        chartFrame.setTitle("Character frequency chart comparison")
                        chartFrame.setVisible(true)

                        FormUtil.close(originalTextChart)
                        FormUtil.close(encryptedTextChart)
                    }
                }).start()
            }
        }
    }

    static void close() {
        if (frame != null) {
            FormUtil.close(frame)
        }
    }

    private static WindowListener onCloseAction() {
        new WindowListener() {
            @Override
            void windowOpened(WindowEvent e) {
            }

            @Override
            void windowClosing(WindowEvent e) {
                FormUtil.close(chartFrame)
                GenerateKeyForm.setVisible(true)
            }

            @Override
            void windowClosed(WindowEvent e) {
            }

            @Override
            void windowIconified(WindowEvent e) {
            }

            @Override
            void windowDeiconified(WindowEvent e) {
            }

            @Override
            void windowActivated(WindowEvent e) {
            }

            @Override
            void windowDeactivated(WindowEvent e) {
            }
        }
    }

    static ActionListener saveButtonAction(JTextField filenameField) {
        return new ActionListener() {
            @Override
            void actionPerformed(ActionEvent e) {
                JSONObject json = new JSONObject(encryptionKey)
                HelperUtil.writeFile(filenameField.getText(), json.toString())
                close()
                GenerateKeyForm.close()
                MainForm.setVisible(true)
            }
        }
    }

    static void mapToFrameElements(Map<Character, Set<Character>> key) {
        JPanel keyPanel = new JPanel()
        keyPanel.setLayout(new GridBagLayout())
        GridBagConstraints labelConstraints = new GridBagConstraints()
        GridBagConstraints valuesConstraints = new GridBagConstraints()

        int yPos = 0

        keyPanel.setMaximumSize(new Dimension(10, 100))
        JScrollPane scrollPane = new JScrollPane(keyPanel)
        for (Character keyEntry : key.keySet()) {
            String keyText = keyEntry.toString()
            if (keyText == ' ') {
                keyText = '\\s'
            } else if (keyText == '\n') {
                keyText = '\\n'
            }
            JLabel keyLabel = new JLabel(keyText)
            JTextField keyValues = new JTextField(
                    key.get(keyEntry)
                            .toString()
                            .replace("[", "")
                            .replace("]", "")
            )

            labelConstraints.gridy = yPos
            labelConstraints.gridx = 0
            labelConstraints.anchor = GridBagConstraints.WEST
            keyPanel.add(keyLabel, labelConstraints)

            valuesConstraints.gridy = yPos
            valuesConstraints.gridx = 40
            valuesConstraints.anchor = GridBagConstraints.WEST
            keyValues.setPreferredSize(new Dimension(250, 20))
            keyValues.getDocument().addDocumentListener(new DocumentListener() {
                @Override
                void insertUpdate(DocumentEvent e) {
                }

                @Override
                void removeUpdate(DocumentEvent e) {
                    Runnable remove = new Runnable() {
                        @Override
                        void run() {
                            final String text = keyValues.getText()
                            if (text.length() > 3) {
                                keyValues.setText(text.substring(0, text.length() - 2))
                            }
                        }
                    }
                    SwingUtilities.invokeLater(remove)
                }

                @Override
                void changedUpdate(DocumentEvent e) {
                }
            })
            keyValues.getDocument().putProperty("label", keyLabel)
            ((AbstractDocument) keyValues.getDocument()).setDocumentFilter(new KeyFilter())
            keyPanel.add(keyValues, valuesConstraints)

            yPos++
        }
        frame.getContentPane().add(scrollPane)
    }

    private static void updateKey(String key, String values) {
        Character charKey = key.charAt(0)
        if (key.length() > 1) {
            if (key == "\\n") {
                charKey = '\n'
            } else if (key == "\\s") {
                charKey = ' '
            }
        }

        ArrayList<String> valuesTable = Arrays.asList(values.split(", "))

        encryptionKey.get(charKey).clear()
        if (valuesTable.size() == 0) {
            println("values empty")
            return
        } else if (valuesTable.size() == 1 && valuesTable.get(0) == '') {
            return
        }
        for (String value : valuesTable) {
            encryptionKey.get(charKey).add(value.charAt(0))
        }
    }

    private static class KeyFilter extends DocumentFilter {
        @Override
        void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            Map<Character, Character> reversedKey = HomophonicCipherEncrypter.reverseKey(encryptionKey)
            String documentText = fb.getDocument().getText(0, fb.getDocument().getLength())
            if (reversedKey.containsKey(text.charAt(0)) || documentText.contains(text) || text == ' ') {
                return
            } else if (documentText.length() > 0) {
                super.insertString(fb, offset, ', ' + text, attrs)
            } else {
                super.replace(fb, offset, length, text, attrs)
            }

            String labelText = ((JLabel) fb.getDocument().getProperty("label")).getText()

            updateKey(labelText, fb.getDocument().getText(0, fb.getDocument().getLength()))
        }

        @Override
        void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
        }

        @Override
        void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
            if (fb.getDocument().getLength() > 1) {
                super.remove(fb, offset - 2, length + 2)
            } else {
                super.remove(fb, offset, length)
            }

            String labelText = ((JLabel) fb.getDocument().getProperty("label")).getText()
            updateKey(labelText, fb.getDocument().getText(0, fb.getDocument().getLength()))
        }
    }
}
