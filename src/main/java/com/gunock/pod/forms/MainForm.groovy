package com.gunock.pod.forms

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class MainForm {

    JFrame frame

    MainForm() {
        frame = new JFrame()

        JPanel buttonPanel = new JPanel()

        JButton generateKeyButton = new JButton("Generate key")
        generateKeyButton.addActionListener(new ActionListener() {
            @Override
            void actionPerformed(ActionEvent e) {
                new GenerateKeyForm()
            }
        })

        JButton encryptionButton = new JButton("Encryption/Decryption")

        buttonPanel.add(generateKeyButton)
        buttonPanel.add(encryptionButton)
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT)
        frame.getContentPane().add(buttonPanel)

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS))
        frame.setSize(350, 80)
        frame.setResizable(false)
        frame.setVisible(true)
    }

}
