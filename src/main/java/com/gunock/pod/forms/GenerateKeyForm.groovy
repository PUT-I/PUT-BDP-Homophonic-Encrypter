package com.gunock.pod.forms

import com.gunock.pod.cipher.HomophonicCipherGenerator

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class GenerateKeyForm {

    private JFrame frame
    private JFileChooser fileChooser
    private JButton generateKeyButton
    private JTextArea fileTextArea

    GenerateKeyForm() {
        frame = new JFrame("Generate key")
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

        // Add elements
        generateKeyButton = new JButton("Generate Key")
        generateKeyButton.addActionListener(new ActionListener() {
            @Override
            void actionPerformed(ActionEvent e) {
                generateKeyButtonAction(e)
            }
        })

        fileTextArea = new JTextArea("Test text")
        fileTextArea.setLineWrap(true)
        JScrollPane scrollPane = new JScrollPane(fileTextArea)

        fileChooser = new JFileChooser()
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")))
        fileChooser.addActionListener(new ActionListener() {
            @Override
            void actionPerformed(ActionEvent e) {
                fileChooserAction(e)
            }
        })

        frame.getContentPane().add(fileChooser)
        frame.getContentPane().add(generateKeyButton)
        frame.getContentPane().add(scrollPane)
        // Finish adding elements

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS))
        frame.setSize(800, 800)
        frame.setMinimumSize(new Dimension(480, 800))
        frame.setVisible(true)
    }

    private fileChooserAction(ActionEvent event) {
        if (event.actionCommand == JFileChooser.APPROVE_SELECTION) {
            String fileText = ""
            try {
                fileText = fileChooser.getSelectedFile().getText("UTF-8")
            } catch (FileNotFoundException ignored) {
                JOptionPane.showMessageDialog(
                        null,
                        "File not found!",
                        "Error",
                        JOptionPane.INFORMATION_MESSAGE
                )
            }
            fileTextArea.setText(fileText)
        }
    }

    private generateKeyButtonAction(ActionEvent event) {
        Map<Character, Set<Character>> key = HomophonicCipherGenerator.generateKey(fileTextArea.getText())
        new EditKeyForm(frame, key)
    }

}
