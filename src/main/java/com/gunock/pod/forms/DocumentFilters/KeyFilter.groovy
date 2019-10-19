package com.gunock.pod.forms.DocumentFilters

import com.gunock.pod.cipher.Encrypter
import com.gunock.pod.cipher.EncryptionKey

import javax.swing.*
import javax.swing.text.AttributeSet
import javax.swing.text.BadLocationException
import javax.swing.text.DocumentFilter

class KeyFilter extends DocumentFilter {
    private EncryptionKey encryptionKey

    KeyFilter(EncryptionKey encryptionKey) {
        this.encryptionKey = encryptionKey
    }

    @Override
    void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
        Map<Character, Character> reversedKey = Encrypter.reverseKey(encryptionKey)
        String documentText = fb.getDocument().getText(0, fb.getDocument().getLength())
        if (reversedKey.containsKey(text.charAt(0)) || documentText.contains(text) || text == ' ') {
            return
        } else if (documentText.length() > 0) {
            super.insertString(fb, offset, ', ' + text, attrs)
        } else {
            super.replace(fb, offset, length, text, attrs)
        }

        String labelText = ((JLabel) fb.getDocument().getProperty("label")).getText()

        updateKey(labelText, fb.getDocument().getText(0, fb.getDocument().getLength()))
    }

    @Override
    void insertString(FilterBypass fb, int offset, String text, AttributeSet attr) throws BadLocationException {
    }

    @Override
    void remove(FilterBypass fb, int offset, int length) throws BadLocationException {
        if (fb.getDocument().getLength() > 1) {
            super.remove(fb, offset - 2, length + 2)
        } else {
            super.remove(fb, offset, length)
        }

        String labelText = ((JLabel) fb.getDocument().getProperty("label")).getText()
        updateKey(labelText, fb.getDocument().getText(0, fb.getDocument().getLength()))
    }


    private void updateKey(String key, String values) {
        Character charKey = key.charAt(0)
        if (key.length() > 1) {
            if (key == "\\n") {
                charKey = '\n'
            } else if (key == "\\s") {
                charKey = ' '
            }
        }

        ArrayList<String> valuesTable = Arrays.asList(values.split(", "))

        encryptionKey.get(charKey).clear()
        if (valuesTable.size() == 0) {
            println("values empty")
            return
        } else if (valuesTable.size() == 1 && valuesTable.get(0) == '') {
            return
        }
        for (String value : valuesTable) {
            encryptionKey.get(charKey).add(value.charAt(0))
        }
    }

    EncryptionKey getEncryptionKey() {
        return encryptionKey
    }
}
