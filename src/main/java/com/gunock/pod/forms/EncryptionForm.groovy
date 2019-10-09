package com.gunock.pod.forms

import javax.swing.*

class EncryptionForm {

    JFrame frame

    EncryptionForm() {
        frame = new JFrame()

        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS))
        frame.setSize(400, 800)
        frame.setResizable(false)
        frame.setVisible(true)
    }

}
