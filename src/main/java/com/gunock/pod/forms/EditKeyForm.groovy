package com.gunock.pod.forms

import com.gunock.pod.cipher.HomophonicCipherEncrypter

import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.AbstractDocument
import javax.swing.text.AttributeSet
import javax.swing.text.BadLocationException
import javax.swing.text.DocumentFilter
import java.awt.*

class EditKeyForm {

    private JFrame frame
    private Map<Character, Set<Character>> cipherKey

    EditKeyForm(Map<Character, Set<Character>> key) {
        cipherKey = key
        frame = new JFrame("Edit key")

        mapToFrameElements(key)

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS))
        frame.setSize(400, 800)
        frame.setResizable(false)
        frame.setVisible(true)
    }

    void mapToFrameElements(Map<Character, Set<Character>> key) {
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

    void updateKey(String key, String values) {
        Character charKey = key.charAt(0)
        if (key.length() > 1) {
            if (key == "\\n") {
                charKey = '\n'
            } else if (key == "\\s") {
                charKey = ' '
            }
        }

        ArrayList<String> valuesTable = Arrays.asList(values.split(", "))

        cipherKey.get(charKey).clear()
        if (valuesTable.size() == 0) {
            println("values empty")
            return
        } else if (valuesTable.size() == 1 && valuesTable.get(0) == '') {
            return
        }
        for (String value : valuesTable) {
            cipherKey.get(charKey).add(value.charAt(0))
        }
    }

    class KeyFilter extends DocumentFilter {
        @Override
        void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
            Map<Character, Character> reversedKey = HomophonicCipherEncrypter.reverseKey(cipherKey)
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
