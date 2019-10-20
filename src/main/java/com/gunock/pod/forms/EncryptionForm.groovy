package com.gunock.pod.forms

import com.gunock.pod.cipher.Encrypter
import com.gunock.pod.cipher.EncryptionKey
import com.gunock.pod.utils.FormUtil
import com.gunock.pod.utils.HelperUtil
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class EncryptionForm extends AbstractForm {

    private JFrame fileChooserFrame
    private JFrame chartFrame
    private JLabel keyStatusLabel
    private JPanel unencryptedTextPanel
    private JPanel encryptedTextPanel
    private JTextField filenameField
    private EncryptionKey encryptionKey

    EncryptionForm(AbstractForm parentForm) {
        this.parentForm = parentForm
        create()
        final Point parentLocation = parentForm.getFrame().getLocation()
        int y = parentLocation.getY() - frame.getHeight() / 2 as int
        y = y < 0 ? 0 : y
        frame.setLocation(parentLocation.getX() as int, y)
        frame.setVisible(true)
    }

    JPanel createUpperButtonPanel() {
        JPanel upperButtonPanel = new JPanel()
        FormUtil.setBoxLayout(upperButtonPanel, BoxLayout.X_AXIS)
        FormUtil.addButton(upperButtonPanel, "Load Encryption Key", loadFileButtonAction(keyFileChooserAction()))
        FormUtil.addButton(upperButtonPanel, "Encrypt Text", encryptTextButtonAction())
        FormUtil.addButton(upperButtonPanel, "Decrypt Text", decryptTextButtonAction())

        keyStatusLabel = new JLabel("    Not loaded    ")
        keyStatusLabel.setBorder(new TitledBorder(new LineBorder(Color.GRAY, 5), "Key Status"))
        keyStatusLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT)
        keyStatusLabel.setAlignmentY(JLabel.CENTER_ALIGNMENT)
        upperButtonPanel.add(keyStatusLabel)

        return upperButtonPanel
    }

    JPanel createLowerButtonPanel() {
        JPanel lowerButtonPanel = new JPanel()
        FormUtil.setBoxLayout(lowerButtonPanel, BoxLayout.X_AXIS)
        FormUtil.addButton(lowerButtonPanel, "Load Unencrypted File", loadFileButtonAction(loadFileChooserAction(false)))
        FormUtil.addButton(lowerButtonPanel, "Load Encrypted File", loadFileButtonAction(loadFileChooserAction(true)))
        FormUtil.addButton(lowerButtonPanel, "Show Frequency Charts", chartsButtonAction())
        FormUtil.addButton(lowerButtonPanel, "Save Encrypted File", saveEncryptedFileButtonAction())

        filenameField = new JTextField()
        filenameField.setPreferredSize(new Dimension(200, filenameField.getPreferredSize().getHeight() as int))
        filenameField.setMaximumSize(filenameField.getPreferredSize())
        lowerButtonPanel.add(filenameField)
        return lowerButtonPanel
    }

    JPanel createTextAreaPanel() {
        JPanel textAreaPanel = new JPanel()
        FormUtil.setBoxLayout(textAreaPanel, BoxLayout.X_AXIS)
        unencryptedTextPanel = FormUtil.createTextAreaWithTitle("Unencrypted Text", true)
        encryptedTextPanel = FormUtil.createTextAreaWithTitle("Encrypted Text", true)
        textAreaPanel.add(unencryptedTextPanel)
        textAreaPanel.add(encryptedTextPanel)
        return textAreaPanel
    }

    @Override
    void create() {
        final JPanel upperButtonPanel = createUpperButtonPanel()
        final JPanel lowerButtonPanel = createLowerButtonPanel()
        final JPanel textAreaPanel = createTextAreaPanel()

        frame = new JFrame("Encryption/Decryption")
        FormUtil.setBoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS)
        FormUtil.addAllComponents(frame.getContentPane(), [upperButtonPanel, lowerButtonPanel, textAreaPanel])
        frame.setSize(1000, 800)
        frame.setResizable(false)
        frame.addWindowListener(FormUtil.onWindowClosingAction({ parentForm.getFrame().setVisible(true) }))
    }

    ActionListener saveEncryptedFileButtonAction() {
        return FormUtil.createActionListener {
            final String text = FormUtil.getTextAreaFromPanelWithTitle(encryptedTextPanel).getText()
            if (text.isBlank()) {
                return
            }
            HelperUtil.writeFile(filenameField.getText(), text)
        }
    }

    private ActionListener loadFileButtonAction(ActionListener fileChooserAction) {
        return FormUtil.createActionListener {
            fileChooserFrame = new JFrame()
            JFileChooser keyFileChooser = new JFileChooser()
            keyFileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")))
            keyFileChooser.addActionListener(fileChooserAction)
            fileChooserFrame.add(keyFileChooser)

            fileChooserFrame.setSize(480, 600)
            fileChooserFrame.addWindowListener(FormUtil.onWindowClosingAction({ frame.setVisible(true) }))
            frame.setVisible(false)
            fileChooserFrame.setLocation(frame.getLocation())
            fileChooserFrame.setVisible(true)
        }
    }

    private ActionListener keyFileChooserAction() {
        return FormUtil.createActionListener { ActionEvent event ->
            JFileChooser fileChooser = (JFileChooser) event.getSource()
            if (event.actionCommand == JFileChooser.APPROVE_SELECTION) {
                try {
                    final String fileText = fileChooser.getSelectedFile().getText()
                    loadKey(fileText)
                    keyStatusLabel.setText("Loaded from: " + fileChooser.getSelectedFile().getName())
                    FormUtil.close(fileChooserFrame)
                } catch (FileNotFoundException ignored) {
                    FormUtil.showMessage("Error", "File could not be opened!")
                } catch (JSONException ignored) {
                    FormUtil.showMessage("Error", "File content is not valid JSON!")
                }
            } else if (event.actionCommand == JFileChooser.CANCEL_SELECTION) {
                FormUtil.close(fileChooserFrame)
            }
        }
    }


    private loadFile(ActionEvent event, boolean encrypted) {
        JFileChooser fileChooser = (JFileChooser) event.getSource()
        try {
            final String fileText = fileChooser.getSelectedFile().getText("UTF-8")
            JTextArea unencryptedTextArea = FormUtil.getTextAreaFromPanelWithTitle(unencryptedTextPanel)
            JTextArea encryptedTextArea = FormUtil.getTextAreaFromPanelWithTitle(encryptedTextPanel)
            unencryptedTextArea.setText("")
            encryptedTextArea.setText("")
            if (encrypted) {
                encryptedTextArea.setText(fileText)
            } else {
                unencryptedTextArea.setText(fileText)
            }

            FormUtil.close(fileChooserFrame)
        } catch (FileNotFoundException ignored) {
            FormUtil.showMessage("Error", "File could not be opened!")
        }
    }

    private ActionListener loadFileChooserAction(boolean encrypted) {
        return FormUtil.createActionListener { ActionEvent event ->
            if (event.actionCommand == JFileChooser.APPROVE_SELECTION) {
                loadFile(event, encrypted)
            } else if (event.actionCommand == JFileChooser.CANCEL_SELECTION) {
                FormUtil.close(fileChooserFrame)
            }
        }
    }

    private ActionListener encryptTextButtonAction() {
        return FormUtil.createActionListener {
            if (!keyStatusLabel.getText().contains("Not loaded")) {
                String originalText = FormUtil.getTextAreaFromPanelWithTitle(unencryptedTextPanel).getText()
                JTextArea encryptedTextArea = FormUtil.getTextAreaFromPanelWithTitle(encryptedTextPanel)

                try {
                    String encryptedText = Encrypter.encrypt(originalText, encryptionKey)
                    encryptedTextArea.setText(encryptedText)
                } catch (NullPointerException ignored) {
                    FormUtil.showMessage("Wrong key", "Some character were not found in key!")
                }
            } else {
                FormUtil.showMessage("Error", "Encryption key not loaded!")
            }
        }
    }

    private ActionListener decryptTextButtonAction() {
        return FormUtil.createActionListener {
            if (!keyStatusLabel.getText().contains("Not loaded")) {
                String encryptedText = FormUtil.getTextAreaFromPanelWithTitle(encryptedTextPanel)
                        .getText()
                JTextArea originalTextArea = FormUtil.getTextAreaFromPanelWithTitle(unencryptedTextPanel)

                String decryptedText = Encrypter.decrypt(encryptedText, encryptionKey)
                originalTextArea.setText(decryptedText)
            } else {
                FormUtil.showMessage("Error", "Encryption key not loaded!")
            }
        }
    }

    private ActionListener chartsButtonAction() {
        return FormUtil.createActionListener {
            final String publicText = FormUtil.getTextAreaFromPanelWithTitle(unencryptedTextPanel).getText()
            final String encryptedText = Encrypter.encrypt(publicText, encryptionKey)

            if (encryptedText.isBlank() || publicText.isBlank()) {
                return
            }

            FormUtil.createCharFrequencyChart(chartFrame, publicText.toLowerCase(), encryptedText)
        }
    }

    private void loadKey(String text) throws JSONException {
        JSONObject json = new JSONObject(text)
        encryptionKey = new EncryptionKey()

        // Map json to correct map
        for (String key : json.keySet()) {
            JSONArray array = json.get(key) as JSONArray
            HashSet<Character> set = new HashSet<>()
            for (Object elem : array) {
                set.add(elem.toString().toCharArray()[0])
            }

            encryptionKey.put(key.toCharArray()[0], set)
        }
    }

}
