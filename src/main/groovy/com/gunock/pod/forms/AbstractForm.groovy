package com.gunock.pod.forms

import com.gunock.pod.utils.FormUtil

import javax.swing.*

abstract class AbstractForm {

    protected JFrame frame
    protected AbstractForm parentForm

    protected abstract void create()

    JFrame getFrame() {
        return frame
    }

    void close() {
        if (frame != null) {
            FormUtil.close(frame)
            parentForm.getFrame().setVisible(true)
        }
    }
}
