package com.gunock.pod.forms

import com.gunock.pod.cipher.HomophonicCipherGenerator
import com.gunock.pod.utils.FormUtil

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.WindowEvent
import java.awt.event.WindowListener

class GenerateKeyForm {

    private static JFrame frame
    private static JFrame fileChooserFrame
    private static JFileChooser fileChooser
    private static JTextArea fileTextArea

    GenerateKeyForm() {
        create()
    }

    static void construct() {
        create()
        frame.setVisible(true)
    }

    static void create() {
        JPanel buttonPanel = new JPanel()
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS))

        JButton generateKeyButton = new JButton("Generate Key")
        generateKeyButton.addActionListener(generateKeyButtonAction())

        JButton loadFileButton = new JButton("Load file")
        loadFileButton.addActionListener(loadFileButtonAction())

        buttonPanel.add(generateKeyButton)
        buttonPanel.add(loadFileButton)

        fileTextArea = new JTextArea("Test text")
        fileTextArea.setLineWrap(true)
        JScrollPane scrollPane = new JScrollPane(fileTextArea)

        fileChooser = new JFileChooser()
        fileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")))
        fileChooser.addActionListener(fileChooserAction())

        fileChooserFrame = new JFrame("Open language example file")
        fileChooserFrame.getContentPane().add(fileChooser)

        frame = new JFrame("Generate key")
        frame.getContentPane().add(buttonPanel)
        frame.getContentPane().add(scrollPane)
        frame.addWindowListener(onCloseAction())
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

    private static WindowListener onCloseAction(JFrame mainFrame) {
        new WindowListener() {
            @Override
            void windowOpened(WindowEvent e) {
            }

            @Override
            void windowClosing(WindowEvent e) {
                MainForm.setVisible(true)
            }

            @Override
            void windowClosed(WindowEvent e) {
            }

            @Override
            void windowIconified(WindowEvent e) {
            }

            @Override
            void windowDeiconified(WindowEvent e) {
            }

            @Override
            void windowActivated(WindowEvent e) {
            }

            @Override
            void windowDeactivated(WindowEvent e) {
            }
        }
    }

    private static WindowListener fileChooserOnCloseAction(JFrame mainFrame) {
        new WindowListener() {
            @Override
            void windowOpened(WindowEvent e) {
            }

            @Override
            void windowClosing(WindowEvent e) {
                mainFrame.setVisible(true)
            }

            @Override
            void windowClosed(WindowEvent e) {
            }

            @Override
            void windowIconified(WindowEvent e) {
            }

            @Override
            void windowDeiconified(WindowEvent e) {
            }

            @Override
            void windowActivated(WindowEvent e) {
            }

            @Override
            void windowDeactivated(WindowEvent e) {
            }
        }
    }

    private static ActionListener loadFileButtonAction() {
        return new ActionListener() {
            @Override
            void actionPerformed(ActionEvent event) {
                fileChooserFrame.setSize(480, 600)
                fileChooserFrame.addWindowListener(fileChooserOnCloseAction(frame))
                frame.setVisible(false)
                fileChooserFrame.setVisible(true)
            }
        }
    }

    private static ActionListener generateKeyButtonAction() {
        return new ActionListener() {
            @Override
            void actionPerformed(ActionEvent event) {
                Map<Character, Set<Character>> key = HomophonicCipherGenerator.generateKey(fileTextArea.getText())
                EditKeyForm.construct(key)
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
                        fileText = fileChooser.getSelectedFile().getText("UTF-8")
                    } catch (FileNotFoundException ignored) {
                        JOptionPane.showMessageDialog(
                                null,
                                "File not found!",
                                "Error",
                                JOptionPane.INFORMATION_MESSAGE
                        )
                    }
                    fileTextArea.setText(fileText)
                    FormUtil.close(fileChooserFrame)
                } else if (event.actionCommand == JFileChooser.CANCEL_SELECTION) {
                    FormUtil.close(fileChooserFrame)
                }
            }
        }
    }

}
