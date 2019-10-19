package com.gunock.pod.forms

import com.gunock.pod.cipher.EncryptionKey
import com.gunock.pod.cipher.KeyGenerator
import com.gunock.pod.utils.FormUtil

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class GenerateKeyForm extends AbstractForm {

    private JFrame fileChooserFrame
    private JFileChooser fileChooser
    private JPanel fileTextAreaPanel

    GenerateKeyForm() {
        create()
    }

    GenerateKeyForm(AbstractForm parentForm) {
        this.parentForm = parentForm
        create()
        Point location = parentForm.getFrame().getLocation()
        int y = location.getY() - frame.getHeight() / 2 as int
        y = y < 0 ? 0 : y
        frame.setLocation((int) location.getX(), y)
        frame.setVisible(true)
    }

    @Override
    void create() {
        JPanel buttonPanel = new JPanel()
        FormUtil.setBoxLayout(buttonPanel, BoxLayout.X_AXIS)

        JButton generateKeyButton = new JButton("Generate Key")
        generateKeyButton.addActionListener(generateKeyButtonAction())

        JButton loadFileButton = new JButton("Load file")
        loadFileButton.addActionListener(loadFileButtonAction())


        buttonPanel.add(generateKeyButton)
        buttonPanel.add(loadFileButton)

        fileTextAreaPanel = FormUtil.createTextAreaWithTitle("Example text", true)

        fileChooser = new JFileChooser()
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")))
        fileChooser.addActionListener(fileChooserAction())

        fileChooserFrame = new JFrame("Open language example file")
        fileChooserFrame.getContentPane().add(fileChooser)

        frame = new JFrame("Generate key")
        FormUtil.addAllComponents(frame.getContentPane() as JComponent, [buttonPanel, fileTextAreaPanel])
        frame.addWindowListener(FormUtil.onWindowCloseAction(parentForm.getFrame()))
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS))
        frame.setSize(new Dimension(480, 800))
        frame.setMinimumSize(new Dimension(480, 800))
        frame.setResizable(false)
    }

    private ActionListener loadFileButtonAction() {
        return new ActionListener() {
            @Override
            void actionPerformed(ActionEvent event) {
                fileChooserFrame.setSize(480, 600)
                fileChooserFrame.addWindowListener(FormUtil.onWindowCloseAction(frame))
                frame.setVisible(false)
                fileChooserFrame.setLocation(frame.getX(), frame.getY())
                fileChooserFrame.setVisible(true)
            }
        }
    }

    private ActionListener generateKeyButtonAction() {
        GenerateKeyForm currentForm = this
        return new ActionListener() {
            @Override
            void actionPerformed(ActionEvent event) {
                String fileText = FormUtil.getTextAreaFromPanelWithTitle(fileTextAreaPanel).getText()
                EncryptionKey key = KeyGenerator.generateKey(fileText)
                frame.setVisible(false)
                new EditKeyForm(currentForm, key, fileText)
            }
        }
    }

    private ActionListener fileChooserAction() {
        return new ActionListener() {
            @Override
            void actionPerformed(ActionEvent event) {
                if (event.actionCommand == JFileChooser.APPROVE_SELECTION) {
                    String fileText = ""
                    try {
                        fileText = fileChooser
                                .getSelectedFile()
                                .getText()
                    } catch (FileNotFoundException ignored) {
                        FormUtil.showMessage("Error", "File could not be opened!")
                    }
                    FormUtil.getTextAreaFromPanelWithTitle(fileTextAreaPanel)
                            .setText(fileText)
                    FormUtil.close(fileChooserFrame)
                } else if (event.actionCommand == JFileChooser.CANCEL_SELECTION) {
                    FormUtil.close(fileChooserFrame)
                }
            }
        }
    }

}
