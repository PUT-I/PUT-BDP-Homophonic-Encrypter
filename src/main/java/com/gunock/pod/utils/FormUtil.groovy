package com.gunock.pod.utils

import javax.swing.*
import java.awt.event.WindowEvent

class FormUtil {

    static void close(JFrame frame) {
        frame.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING))
    }

}
