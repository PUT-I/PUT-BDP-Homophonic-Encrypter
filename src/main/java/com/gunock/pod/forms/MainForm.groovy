package com.gunock.pod.forms

import com.gunock.pod.utils.FormUtil

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class MainForm {

    private static JFrame frame

    static void create() {
        frame = new JFrame("Main Menu")
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

        JPanel buttonPanel = new JPanel()

        JButton generateKeyButton = new JButton("Generate key")
        generateKeyButton.addActionListener(generateKeyButtonAction())

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

    static void close() {
        if (frame != null) {
            FormUtil.close(frame)
        }
    }

    static void setVisible(boolean visible) {
        frame.setVisible(visible)
    }

    static ActionListener generateKeyButtonAction() {
        return new ActionListener() {
            @Override
            void actionPerformed(ActionEvent e) {
                GenerateKeyForm.construct()
                setVisible(false)
            }
        }
    }

}
