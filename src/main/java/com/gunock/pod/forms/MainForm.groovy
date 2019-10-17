package com.gunock.pod.forms

import com.gunock.pod.utils.FormUtil

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class MainForm {

    private static JFrame frame

    static void construct() {
        create()
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize()
        final int x = (int) (screenSize.getWidth() / 2 - frame.getWidth() / 2)
        final int y = (int) (screenSize.getHeight() / 2 - frame.getHeight() / 2)
        frame.setLocation(x, y)
        frame.setVisible(true)
    }

    static void create() {
        JPanel buttonPanel = new JPanel()
        FormUtil.addButton(buttonPanel, "Generate key", generateKeyButtonAction())
        FormUtil.addButton(buttonPanel, "Encryption/Decryption", encryptionButtonAction())
        FormUtil.addButton(buttonPanel, "Manual", manualButtonAction())
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT)

        frame = new JFrame("Main Menu")
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        frame.getContentPane().add(buttonPanel)

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.X_AXIS))
        frame.setSize(400, 80)
        frame.setResizable(false)
    }

    static void setVisible(boolean visible) {
        frame.setVisible(visible)
    }

    static ActionListener generateKeyButtonAction() {
        return new ActionListener() {
            @Override
            void actionPerformed(ActionEvent e) {
                setVisible(false)
                GenerateKeyForm.construct(frame.getX(), frame.getY())
            }
        }
    }

    static ActionListener encryptionButtonAction() {
        return new ActionListener() {
            @Override
            void actionPerformed(ActionEvent e) {
                setVisible(false)
                EncryptionForm.construct(frame.getX(), frame.getY())
            }
        }
    }


    static ActionListener manualButtonAction() {
        return new ActionListener() {
            @Override
            void actionPerformed(ActionEvent e) {
                setVisible(false)
                ManualForm.construct()
            }
        }
    }

}
