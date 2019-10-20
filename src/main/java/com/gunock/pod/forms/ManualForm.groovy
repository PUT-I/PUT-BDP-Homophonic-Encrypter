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
        final String DESCRIPTION_TEXT = 'This application is used to encrypt data with homophonic cipher. ' +
                'Application allows to generate and save encryption and encrypt and decrypt data ' +
                '(entered manually or loaded from file).'

        final String GENERATE_KEY_TEXT = 'Key generation consists of two view. In first view ' +
                'you enter public text (you can load it from file) to be used in generating key.' +
                ' Key generation uses text to calculate character frequency and fit key to it. ' +
                'In second view you can view frequency charts for public and encrypted texts, ' +
                'edit key and save it to file as JSON.'

        final String ENCRYPTION_TEXT = 'In encryption view you have several options. First you need to load ' +
                'encryption key (key status should show that key is loaded) and then enter ' +
                'encrypted/public text (you can load both from file) and then you can decrypt/encrypt it. ' +
                'As in key generation view, you can show frequency charts (public and encrypted text areas must ' +
                'be filled). You can save encrypted text to file by entering filename in text field positioned ' +
                'beside file save button.'

        final String IMPLEMENTATION_TEXT = 'Key is generated on latin alphabet extended by letters from public ' +
                'text passed as example text of given language. Cipher uses letters with ASCII codes 33-126 and 173-254.' +
                'To calculate amount of characters to be assigned to for given character in public text, key generator ' +
                'calculates average frequency of characters in public text and picks random number from 1 to amount of ' +
                'remaining unused cipher alphabet character divided by remaining amount of public unused public text ' +
                'alphabet characters. After picking random number of cipher characters to be assigned to public text ' +
                'character, generator checks if frequency of character is higher than average. If it so, then number ' +
                'of characters to be assigned to it is multiplied by 2 and character frequency divided by average ' +
                'frequency is added to it. After that random characters from cipher alphabet are assigned to character ' +
                'from public text alphabet.'

        JPanel textPanel = new JPanel()
        FormUtil.setBoxLayout(textPanel, BoxLayout.Y_AXIS)
        JPanel descriptionText = FormUtil.createTextAreaWithTitle("Description", DESCRIPTION_TEXT)
        JPanel keyGenerationText = FormUtil.createTextAreaWithTitle("Key Generation", GENERATE_KEY_TEXT)
        JPanel encryptionText = FormUtil.createTextAreaWithTitle("Encryption/Decryption", ENCRYPTION_TEXT)
        JPanel implementationText = FormUtil.createTextAreaWithTitle("Cipher Implementation", IMPLEMENTATION_TEXT)

        FormUtil.addAllComponents(textPanel, [descriptionText, keyGenerationText, encryptionText, implementationText])
        return textPanel
    }

    void create() {
        final JPanel textPanel = createTextPanel()

        frame = new JFrame("Manual")
        FormUtil.setBoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS)
        frame.getContentPane().add(textPanel)
        frame.setSize(480, 800)
        frame.addWindowListener(onCloseAction())
    }

    private WindowListener onCloseAction() {
        return FormUtil.onWindowClosingAction {
            parentForm.getFrame().setVisible(true)
        }
    }
}
