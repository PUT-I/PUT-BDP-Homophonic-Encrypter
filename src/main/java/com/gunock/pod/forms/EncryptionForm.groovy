package com.gunock.pod.forms

import com.gunock.pod.cipher.BarChart
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

class EncryptionForm {

    static JFrame frame
    static JFrame fileChooserFrame
    private static JFrame chartFrame
    static JLabel keyStatusLabel
    static JPanel unencryptedTextPanel
    static JPanel encryptedTextPanel

    private static EncryptionKey encryptionKey

    static void construct(int x, int y) {
        create()
        y = y - frame.getHeight() / 2 as int
        y = y < 0 ? 0 : y
        frame.setLocation(x, y)
        frame.setVisible(true)
    }

    static void create() {
        JPanel upperButtonPanel = new JPanel()
        FormUtil.setBoxLayout(upperButtonPanel, BoxLayout.X_AXIS)
        FormUtil.addButton(upperButtonPanel, "Load Encryption Key", loadFileButtonAction(keyFileChooserAction()))
        FormUtil.addButton(upperButtonPanel, "Encrypt Text", encryptTextButtonAction())
        FormUtil.addButton(upperButtonPanel, "Decrypt Text", decryptTextButtonAction())

        JPanel lowerButtonPanel = new JPanel()
        FormUtil.setBoxLayout(lowerButtonPanel, BoxLayout.X_AXIS)
        FormUtil.addButton(lowerButtonPanel, "Load Unencrypted File", loadFileButtonAction(loadFileChooserAction(false)))
        FormUtil.addButton(lowerButtonPanel, "Load Encrypted File", loadFileButtonAction(loadFileChooserAction(true)))
        FormUtil.addButton(lowerButtonPanel, "Show Frequency Charts", chartsButtonAction())


        keyStatusLabel = new JLabel("    Not loaded    ")
        keyStatusLabel.setBorder(new TitledBorder(new LineBorder(Color.GRAY, 5), "Key Status"))
        keyStatusLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT)
        keyStatusLabel.setAlignmentY(JLabel.CENTER_ALIGNMENT)
        upperButtonPanel.add(keyStatusLabel)

        unencryptedTextPanel = FormUtil.createTextAreaWithTitle("Unencrypted Text", true)
        encryptedTextPanel = FormUtil.createTextAreaWithTitle("Encrypted Text", true)

        JPanel textAreaPanel = new JPanel()
        FormUtil.setBoxLayout(textAreaPanel, BoxLayout.X_AXIS)
        textAreaPanel.add(unencryptedTextPanel)
        textAreaPanel.add(encryptedTextPanel)

        frame = new JFrame("Encryption/Decryption")

        FormUtil.setBoxLayout(frame.getContentPane() as JComponent, BoxLayout.Y_AXIS)

        FormUtil.addAllComponents(
                frame.getContentPane() as JComponent,
                [upperButtonPanel, lowerButtonPanel, textAreaPanel]
        )

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS))
        frame.setSize(1000, 800)
        frame.setResizable(false)
        frame.addWindowListener(FormUtil.onWindowCloseAction(MainForm.getFrame()))
    }

    private static ActionListener loadFileButtonAction(ActionListener fileChooserAction) {
        return new ActionListener() {
            @Override
            void actionPerformed(ActionEvent event) {
                fileChooserFrame = new JFrame()
                JFileChooser keyFileChooser = new JFileChooser()
                keyFileChooser.setCurrentDirectory(new File(System.getProperty("user.dir")))
                keyFileChooser.addActionListener(fileChooserAction)
                fileChooserFrame.add(keyFileChooser)

                fileChooserFrame.setSize(480, 600)
                fileChooserFrame.addWindowListener(FormUtil.onWindowCloseAction(frame))
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
    }

    private static ActionListener loadFileChooserAction(boolean encrypted) {
        return new ActionListener() {
            @Override
            void actionPerformed(ActionEvent event) {
                JFileChooser fileChooser = (JFileChooser) event.getSource()
                if (event.actionCommand == JFileChooser.APPROVE_SELECTION) {
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
                    String originalText = FormUtil.getTextAreaFromPanelWithTitle(unencryptedTextPanel)
                            .getText()
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
    }

    private static ActionListener decryptTextButtonAction() {
        return new ActionListener() {
            @Override
            void actionPerformed(ActionEvent e) {
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
    }

    private static ActionListener chartsButtonAction() {
        return new ActionListener() {
            @Override
            void actionPerformed(ActionEvent e) {
                final String originalText = FormUtil.getTextAreaFromPanelWithTitle(unencryptedTextPanel).getText()
                final String encryptedText = Encrypter.encrypt(originalText, encryptionKey)

                if (encryptedText.isBlank() || originalText.isBlank()) {
                    return
                }

                Map<Character, Integer> analyzedAlphabet = HelperUtil.analyzeCharactersFrequency(originalText.toLowerCase())
                Map<Character, Integer> analyzedAlphabetEncrypted = HelperUtil.analyzeCharactersFrequency(encryptedText)

                new Thread(new Runnable() {
                    @Override
                    void run() {
                        FormUtil.close(chartFrame)
                        JFrame originalTextChart = BarChart.getChart(analyzedAlphabet, "Original Text")
                        JFrame encryptedTextChart = BarChart.getChart(analyzedAlphabetEncrypted, "Encrypted Text")

                        chartFrame = new JFrame()
                        FormUtil.setBoxLayout(chartFrame.getContentPane() as JComponent, BoxLayout.Y_AXIS)

                        chartFrame.add(originalTextChart.getContentPane().getComponent(0))
                        chartFrame.add(encryptedTextChart.getContentPane().getComponent(0))
                        chartFrame.setSize(1000, 800)
                        chartFrame.setTitle("Character frequency chart comparison")
                        chartFrame.setVisible(true)

                        FormUtil.close(originalTextChart)
                        FormUtil.close(encryptedTextChart)
                    }
                }).start()
            }
        }
    }

    private static void loadKey(String text) throws JSONException {
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
