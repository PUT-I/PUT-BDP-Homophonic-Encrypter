package com.gunock.pod.utils

import com.gunock.pod.cipher.BarChart

import javax.swing.*
import javax.swing.border.BevelBorder
import java.awt.event.ActionListener
import java.awt.event.WindowEvent
import java.awt.event.WindowListener

class FormUtil {

    static void close(JFrame frame) {
        if (frame != null) {
            frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING))
        }
    }

    static JPanel createTextAreaWithTitle(String title, String text, boolean editable) {
        JPanel textAreaPanel = new JPanel()
        textAreaPanel.setBorder(new BevelBorder(BevelBorder.LOWERED))
        setBoxLayout(textAreaPanel, BoxLayout.Y_AXIS)

        JLabel textLabel = new JLabel(title)
        textLabel.setAlignmentX(JLabel.CENTER_ALIGNMENT)
        textLabel.setAlignmentY(JLabel.CENTER_ALIGNMENT)

        JTextArea textArea = new JTextArea()
        textArea.setLineWrap(true)
        textArea.setText(text)
        textArea.setEditable(editable)

        JScrollPane scrollPane = new JScrollPane(textArea)

        addAllComponents(textAreaPanel, [textLabel, scrollPane])
        return textAreaPanel
    }

    static JPanel createTextAreaWithTitle(String title, boolean editable) {
        return createTextAreaWithTitle(title, '', editable)
    }

    static JPanel createTextAreaWithTitle(String title, String text) {
        return createTextAreaWithTitle(title, text, false)
    }

    static JTextArea getTextAreaFromPanelWithTitle(JPanel panel) {
        JScrollPane scrollPane = (JScrollPane) panel.getComponent(1)
        JViewport viewport = (JViewport) scrollPane.getComponent(0)
        return (JTextArea) viewport.getComponent(0)
    }

    static void showMessage(String title, String message) {
        JOptionPane.showMessageDialog(
                null,
                message,
                title,
                JOptionPane.INFORMATION_MESSAGE
        )
    }

    static void addButton(JComponent component, String text, ActionListener listener) {
        JButton button = new JButton(text)
        button.addActionListener(listener)
        component.add(button)
    }

    static void addButton(JComponent component, String text) {
        addButton(component, text, null)
    }

    static void setBoxLayout(JComponent component, int axis) {
        component.setLayout(new BoxLayout(component, axis))
    }

    static void addAllComponents(JComponent parent, List<JComponent> components) {
        for (JComponent component : components) {
            parent.add(component)
        }
    }

    static WindowListener onWindowCloseAction(JFrame previousFrame) {
        return new WindowListener() {
            @Override
            void windowOpened(WindowEvent e) {
            }

            @Override
            void windowClosing(WindowEvent e) {
                previousFrame.setVisible(true)
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

    static void createCharFrequencyChart(JFrame chartFrame, String publicText, String encryptedText) {
        Map<Character, Integer> analyzedAlphabet = HelperUtil.analyzeCharactersFrequency(publicText.toLowerCase())
        Map<Character, Integer> analyzedAlphabetEncrypted = HelperUtil.analyzeCharactersFrequency(encryptedText)

        new Thread(new Runnable() {
            @Override
            void run() {
                close(chartFrame)
                JFrame originalTextChart = BarChart.getChart(analyzedAlphabet, "Original Text")
                JFrame encryptedTextChart = BarChart.getChart(analyzedAlphabetEncrypted, "Encrypted Text")

                chartFrame = new JFrame()
                setBoxLayout(chartFrame.getContentPane() as JComponent, BoxLayout.Y_AXIS)

                chartFrame.add(originalTextChart.getContentPane().getComponent(0))
                chartFrame.add(encryptedTextChart.getContentPane().getComponent(0))
                chartFrame.setSize(1000, 800)
                chartFrame.setTitle("Character frequency chart comparison")
                chartFrame.setVisible(true)

                close(originalTextChart)
                close(encryptedTextChart)
            }
        }).start()
    }

}
