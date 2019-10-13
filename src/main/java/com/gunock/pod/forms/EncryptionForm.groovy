package com.gunock.pod.forms

import com.gunock.pod.cipher.HomophonicCipherEncrypter
import com.gunock.pod.utils.FormUtil
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

import javax.swing.*
import javax.swing.border.LineBorder
import javax.swing.border.TitledBorder
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.awt.event.WindowEvent
import java.awt.event.WindowListener

class EncryptionForm {

    static JFrame frame
    static JFrame fileChooserFrame
    static JLabel keyStatusLabel
    static JPanel originalTextPanel
    static JPanel encryptedTextPanel


    private static Map<Character, Set<Character>> encryptionKey

    static void construct(int x, int y) {
        create()
        frame.setLocation(x, y)
        frame.setVisible(true)
    }

    static void create() {
        fileChooserFrame = new JFrame()

        JPanel buttonPanel = new JPanel()
        FormUtil.setBoxLayout(buttonPanel, BoxLayout.X_AXIS)
        FormUtil.addButton(buttonPanel, "Load Encryption Key", loadKeyButtonAction())
        FormUtil.addButton(buttonPanel, "Encrypt Text", encryptTextButtonAction())
        FormUtil.addButton(buttonPanel, "Decrypt Text", decryptTextButtonAction())

        keyStatusLabel = new JLabel("    Not loaded    ")
        keyStatusLabel.setBorder(new TitledBorder(new LineBorder(Color.GRAY, 5), "Key Status"))
        keyStatusLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT)
        keyStatusLabel.setAlignmentY(JLabel.CENTER_ALIGNMENT)
        buttonPanel.add(keyStatusLabel)

        originalTextPanel = FormUtil.createTextAreaWithTitle("Original Text", true)
        encryptedTextPanel = FormUtil.createTextAreaWithTitle("Encrypted Text", true)

        JPanel textAreaPanel = new JPanel()
        FormUtil.setBoxLayout(textAreaPanel, BoxLayout.X_AXIS)
        textAreaPanel.add(originalTextPanel)
        textAreaPanel.add(encryptedTextPanel)

        frame = new JFrame("Encryption/Decryption")

        FormUtil.setBoxLayout(frame.getContentPane() as JComponent, BoxLayout.Y_AXIS)

        FormUtil.addAllComponents(frame.getContentPane() as JComponent, [buttonPanel, textAreaPanel])

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS))
        frame.setSize(1000, 800)
        frame.setResizable(false)
    }

    private static ActionListener loadKeyButtonAction() {
        return new ActionListener() {
            @Override
            void actionPerformed(ActionEvent event) {
                JFileChooser keyFileChooser = new JFileChooser()
                keyFileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")))
                keyFileChooser.addActionListener(keyFileChooserAction())
                fileChooserFrame.add(keyFileChooser)

                fileChooserFrame.setSize(480, 600)
                fileChooserFrame.addWindowListener(fileChooserOnCloseAction(frame))
                frame.setVisible(false)
                fileChooserFrame.setLocation(frame.getX(), frame.getY())
                fileChooserFrame.setVisible(true)
            }
        }
    }

    private static ActionListener keyFileChooserAction() {
        return new ActionListener() {
            @Override
            void actionPerformed(ActionEvent event) {
                JFileChooser fileChooser = (JFileChooser) event.getSource()
                if (event.actionCommand == JFileChooser.APPROVE_SELECTION) {
                    try {
                        String fileText = fileChooser.getSelectedFile().getText("UTF-8")
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
    }

    private static ActionListener encryptTextButtonAction() {
        return new ActionListener() {
            @Override
            void actionPerformed(ActionEvent e) {
                if (!keyStatusLabel.getText().contains("Not loaded")) {
                    String originalText = FormUtil.getTextAreaFromPanelWithTitle(originalTextPanel)
                            .getText()
                    JTextArea encryptedTextArea = FormUtil.getTextAreaFromPanelWithTitle(encryptedTextPanel)

                    String encryptedText = HomophonicCipherEncrypter.encrypt(originalText, encryptionKey)
                    encryptedTextArea.setText(encryptedText)
                } else {
                    FormUtil.showMessage("Error", "Encryption key not loaded!")
                }
            }
        }
    }

    private static ActionListener decryptTextButtonAction() {
        return new ActionListener() {
            @Override
            void actionPerformed(ActionEvent e) {
                if (!keyStatusLabel.getText().contains("Not loaded")) {
                    String encryptedText = FormUtil.getTextAreaFromPanelWithTitle(encryptedTextPanel)
                            .getText()
                    JTextArea originalTextArea = FormUtil.getTextAreaFromPanelWithTitle(originalTextPanel)

                    String decryptedText = HomophonicCipherEncrypter.decrypt(encryptedText, encryptionKey)
                    originalTextArea.setText(decryptedText)
                } else {
                    FormUtil.showMessage("Error", "Encryption key not loaded!")
                }
            }
        }
    }

    private static void loadKey(String text) throws JSONException {
        JSONObject json = new JSONObject(text)
        encryptionKey = new HashMap<>()
        for (String key : json.keySet()) {
            JSONArray array = json.get(key) as JSONArray
            HashSet<Character> set = new HashSet<>()
            for (Object elem : array) {
                set.add(elem.toString().toCharArray()[0])
            }

            encryptionKey.put(key.toCharArray()[0], set)
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

}
