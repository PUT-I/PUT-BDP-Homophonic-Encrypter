package com.gunock.pod.forms

import com.gunock.pod.cipher.HomophonicCipherEncrypter

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.KeyEvent
import java.awt.event.KeyListener

class EditKeyForm {

    private JFrame frame
    private Map<Character, Set<Character>> key

    EditKeyForm(Map<Character, Set<Character>> key) {
        this.key = key
        frame = new JFrame("Edit key")

        mapToFrameElements(key)

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS))
        frame.setSize(800, 800)
        frame.setMinimumSize(new Dimension(480, 800))
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
            JLabel keyLabel = new JLabel(' ' + keyText)
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
            keyValues.addKeyListener(new KeyListener() {
                @Override
                void keyTyped(KeyEvent e) {
                }

                @Override
                void keyPressed(KeyEvent e) {
                    editTextFieldAction(e)
                }

                @Override
                void keyReleased(KeyEvent e) {

                }
            })
            keyPanel.add(keyValues, valuesConstraints)

            yPos++
        }
        frame.getContentPane().add(scrollPane)
    }

    void editTextFieldAction(KeyEvent keyEvent) {
        JTextField textField = (JTextField) keyEvent.getComponent()
        if (keyEvent.getKeyChar() == (char) 8) {
            if (textField.getText().length() > 3) {
                String text = textField.getText()
                textField.setText(text.substring(0, text.length() - 2))
            }
        } else if (keyEvent.getKeyCode() >= 37 && keyEvent.getKeyCode() <= 40) {
        } else if (keyEvent.getKeyChar() >= '!'.toCharacter()) {
            String text = textField.getText()
            String newText = text
            if (!text.isBlank()) {
                newText = text + ", "
            }

            textField.setText(newText)
        }
    }

}
