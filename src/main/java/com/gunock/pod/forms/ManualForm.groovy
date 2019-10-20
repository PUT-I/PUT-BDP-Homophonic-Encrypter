package com.gunock.pod.forms

import com.gunock.pod.utils.FormUtil

import javax.swing.*
import java.awt.*
import java.awt.event.WindowListener

class ManualForm extends AbstractForm {

    ManualForm(AbstractForm parentForm) {
        this.parentForm = parentForm
        create()
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize()
        final int x = (int) (screenSize.getWidth() / 2 - frame.getWidth() / 2)
        final int y = (int) (screenSize.getHeight() / 2 - frame.getHeight() / 2)
        frame.setLocation(x, y)
        frame.setVisible(true)
    }

    JPanel createTextPanel() {
        final String DESCRIPTION_TEXT = 'This application is used to encrypt data with homophonic cipher.' +
                '\nApplication allows to generate and save encryption, encrypt and decrypt data' +
                ' (entered manually or loaded from file).'

        JPanel textPanel = new JPanel()
        FormUtil.setBoxLayout(textPanel, BoxLayout.Y_AXIS)
        JPanel descriptionText = FormUtil.createTextAreaWithTitle("Description", DESCRIPTION_TEXT)

        textPanel.add(descriptionText)
        return textPanel
    }

    void create() {
        JPanel textPanel = createTextPanel()

        frame = new JFrame("Manual")
        FormUtil.setBoxLayout(frame.getContentPane() as JComponent, BoxLayout.Y_AXIS)
        frame.getContentPane().add(new JScrollPane(textPanel))
        FormUtil.addAllComponents(frame.getContentPane() as JComponent, [textPanel])
        frame.setSize(480, 800)
        frame.addWindowListener(onCloseAction())
    }

    private WindowListener onCloseAction() {
        return FormUtil.onWindowClosingAction {
            parentForm.getFrame().setVisible(true)
        }
    }
}
