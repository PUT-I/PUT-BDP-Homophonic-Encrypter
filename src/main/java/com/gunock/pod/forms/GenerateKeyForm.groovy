package com.gunock.pod.forms

import com.gunock.pod.cipher.EncryptionKey
import com.gunock.pod.cipher.KeyGenerator
import com.gunock.pod.utils.FormUtil

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener

class GenerateKeyForm {

    private static JFrame frame
    private static JFrame fileChooserFrame
    private static JFileChooser fileChooser
    private static JPanel fileTextAreaPanel

    GenerateKeyForm() {
        create()
    }

    static void construct(int x, int y) {
        create()
        y = y - frame.getHeight() / 2 as int
        y = y < 0 ? 0 : y
        frame.setLocation(x, y)
        frame.setVisible(true)
    }

    static void create() {
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
        frame.addWindowListener(FormUtil.onWindowCloseAction(MainForm.getFrame()))
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS))
        frame.setSize(new Dimension(480, 800))
        frame.setMinimumSize(new Dimension(480, 800))
        frame.setResizable(false)
    }

    static void close() {
        if (frame != null) {
            FormUtil.close(frame)
        }
    }

    static void setVisible(boolean visible) {
        frame.setVisible(visible)
    }

    private static ActionListener loadFileButtonAction() {
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

    private static ActionListener generateKeyButtonAction() {
        return new ActionListener() {
            @Override
            void actionPerformed(ActionEvent event) {
                String fileText = FormUtil.getTextAreaFromPanelWithTitle(fileTextAreaPanel).getText()
                EncryptionKey key = KeyGenerator.generateKey(fileText)
                EditKeyForm.construct(frame.getX(), frame.getY(), key, fileText)
                frame.setVisible(false)
            }
        }
    }

    private static ActionListener fileChooserAction() {
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
