package com.gunock.pod.forms

import com.gunock.pod.utils.FormUtil

import javax.swing.*
import java.awt.*
import java.awt.event.ActionListener

class MainForm extends AbstractForm {

    MainForm() {
        create()
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize()
        final int x = (int) (screenSize.getWidth() / 2 - frame.getWidth() / 2)
        final int y = (int) (screenSize.getHeight() / 2 - frame.getHeight() / 2)
        frame.setLocation(x, y)
        frame.setVisible(true)
    }

    JPanel createButtonPanel() {
        JPanel buttonPanel = new JPanel()

        FormUtil.addButton(buttonPanel, "Generate key", generateKeyButtonAction())
        FormUtil.addButton(buttonPanel, "Encryption/Decryption", encryptionButtonAction())
        FormUtil.addButton(buttonPanel, "Manual", manualButtonAction())
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT)
        return buttonPanel
    }

    @Override
    void create() {
        final JPanel buttonPanel = createButtonPanel()

        frame = new JFrame("Main Menu")
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
        frame.getContentPane().add(buttonPanel)
        FormUtil.setBoxLayout(frame.getContentPane(), BoxLayout.X_AXIS)
        frame.setSize(400, 80)
        frame.setResizable(false)
    }

    ActionListener newFormButtonAction(Closure newForm) {
        return FormUtil.createActionListener {
            frame.setVisible(false)
            newForm()
        }
    }

    ActionListener generateKeyButtonAction() {
        return newFormButtonAction { new GenerateKeyForm(this) }
    }

    ActionListener encryptionButtonAction() {
        return newFormButtonAction { new EncryptionForm(this) }
    }

    ActionListener manualButtonAction() {
        return newFormButtonAction { new ManualForm(this) }
    }

}
