package com.gunock.pod.forms

import com.gunock.pod.cipher.Encrypter
import com.gunock.pod.cipher.EncryptionKey
import com.gunock.pod.forms.DocumentFilters.KeyFilter
import com.gunock.pod.utils.FormUtil
import com.gunock.pod.utils.HelperUtil
import org.json.JSONObject

import javax.swing.*
import javax.swing.event.DocumentEvent
import javax.swing.event.DocumentListener
import javax.swing.text.AbstractDocument
import java.awt.*
import java.awt.event.ActionListener
import java.awt.event.WindowListener

class EditKeyForm extends AbstractForm {

    private JFrame chartFrame
    private String exampleText
    private KeyFilter keyFilter

    EditKeyForm(AbstractForm parentForm, EncryptionKey encryptionKey, String text) {
        this.parentForm = parentForm
        keyFilter = new KeyFilter(encryptionKey)
        exampleText = text
        create()
        frame.setLocation(parentForm.getFrame().getLocation())
        frame.setVisible(true)
    }

    JPanel createSaveButtonPanel() {
        JPanel saveButtonPanel = new JPanel()
        FormUtil.setBoxLayout(saveButtonPanel, BoxLayout.X_AXIS)
        JTextField filenameField = new JTextField("key.json")
        FormUtil.addButton(saveButtonPanel, "Save Key", saveButtonAction(filenameField))
        saveButtonPanel.add(filenameField)
        return saveButtonPanel
    }

    JPanel createButtonPanel() {
        JPanel saveButtonPanel = createSaveButtonPanel()

        JPanel chartsButtonPanel = new JPanel()
        chartsButtonPanel.setLayout(new FlowLayout())
        FormUtil.addButton(chartsButtonPanel, "Show Frequency Charts", chartsButtonAction())

        JPanel buttonPanel = new JPanel()
        FormUtil.setBoxLayout(buttonPanel, BoxLayout.Y_AXIS)
        FormUtil.addAllComponents(buttonPanel, [saveButtonPanel, chartsButtonPanel])
        return buttonPanel
    }

    @Override
    void create() {
        final JPanel buttonPanel = createButtonPanel()

        frame = new JFrame("Edit key")
        FormUtil.setBoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS)
        frame.getContentPane().add(buttonPanel)
        mapToFrameElements(keyFilter.getEncryptionKey())
        frame.addWindowListener(onCloseAction())
        frame.setSize(400, 800)
        frame.setResizable(false)
    }

    ActionListener chartsButtonAction() {
        return FormUtil.createActionListener {
            final String encryptedText = Encrypter.encrypt(exampleText, keyFilter.getEncryptionKey())
            FormUtil.createCharFrequencyChart(chartFrame, exampleText.toLowerCase(), encryptedText)
        }
    }

    private WindowListener onCloseAction() {
        new FormUtil().onWindowClosingAction {
            FormUtil.close(chartFrame)
            parentForm.close()
        }
    }

    ActionListener saveButtonAction(JTextField filenameField) {
        return FormUtil.createActionListener {
            JSONObject json = new JSONObject(keyFilter.getEncryptionKey().getKey())
            HelperUtil.writeFile(filenameField.getText(), json.toString())
            close()
            parentForm.close()
        }
    }

    void mapToFrameElements(EncryptionKey encryptionKey) {
        JPanel keyPanel = new JPanel()
        keyPanel.setLayout(new GridBagLayout())
        keyPanel.setMaximumSize(new Dimension(10, 100))
        JScrollPane scrollPane = new JScrollPane(keyPanel)

        GridBagConstraints labelConstraints = new GridBagConstraints()
        labelConstraints.anchor = GridBagConstraints.WEST
        labelConstraints.gridx = 0
        GridBagConstraints valuesConstraints = new GridBagConstraints()
        valuesConstraints.anchor = GridBagConstraints.WEST
        valuesConstraints.gridx = 40

        int yPos = 0
        for (Character keyEntry : encryptionKey.keySet()) {
            String keyText = keyEntry.toString()
            if (keyText == ' ') {
                keyText = '\\s'
            } else if (keyText == '\n') {
                keyText = '\\n'
            }
            JLabel keyLabel = new JLabel(keyText)
            JTextField keyValues = new JTextField(
                    encryptionKey.get(keyEntry)
                            .toString()
                            .replace("[", "")
                            .replace("]", "")
            )

            labelConstraints.gridy = yPos
            keyPanel.add(keyLabel, labelConstraints)

            valuesConstraints.gridy = yPos
            keyValues.setPreferredSize(new Dimension(300, 20))
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
            ((AbstractDocument) keyValues.getDocument()).setDocumentFilter(keyFilter)

            keyPanel.add(keyValues, valuesConstraints)
            yPos++
        }
        frame.getContentPane().add(scrollPane)
    }

}
