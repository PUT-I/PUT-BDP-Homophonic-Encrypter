package com.gunock.pod.forms


import com.gunock.pod.utils.FormUtil

import javax.swing.*
import java.awt.*
import java.awt.event.WindowEvent
import java.awt.event.WindowListener

class ManualForm {

    static JFrame frame

    static void construct() {
        create()
        final Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize()
        final int x = (int) (screenSize.getWidth() / 2 - frame.getWidth() / 2)
        final int y = (int) (screenSize.getHeight() / 2 - frame.getHeight() / 2)
        frame.setLocation(x, y)
        frame.setVisible(true)
    }

    static void create() {
        final String DESCRIPTION_TEXT = '''This application is used to encrypt data with homophonic cipher.
Application allows to generate and save encryption, encrypt and decrypt data (entered manually or loaded from file).
'''

        JPanel textPanel = new JPanel()
        FormUtil.setBoxLayout(textPanel, BoxLayout.Y_AXIS)
        JPanel descriptionText = FormUtil.createTextAreaWithTitle("Description", DESCRIPTION_TEXT)

        textPanel.add(descriptionText)

        frame = new JFrame("Manual")

        FormUtil.setBoxLayout(frame.getContentPane() as JComponent, BoxLayout.Y_AXIS)

        frame.getContentPane().add(new JScrollPane(textPanel))

        FormUtil.addAllComponents(frame.getContentPane() as JComponent, [textPanel])

        frame.setSize(480, 800)
        frame.setResizable(false)
        frame.addWindowListener(onCloseAction())
    }

    private static WindowListener onCloseAction() {
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
}
