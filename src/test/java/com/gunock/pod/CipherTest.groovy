package com.gunock.pod

import com.gunock.pod.cipher.BarChart
import com.gunock.pod.cipher.HelperUtil
import com.gunock.pod.cipher.HomophonicCipherEncrypter
import com.gunock.pod.cipher.HomophonicCipherGenerator
import com.gunock.pod.forms.EditKeyForm
import com.gunock.pod.forms.GenerateKeyForm

import javax.swing.*
import java.nio.file.Files
import java.nio.file.Paths

class CipherTest {

    static void generateKeyFrame() {
        new GenerateKeyForm()
    }

    static void frameTest(String text1, String text2) {
        //1. Create the frame.
        JFrame frame = new JFrame("Homophonic Encrypter")

        //2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

        //3. Create components and put them in the frame.
        JLabel label1 = new JLabel("File Text", null, JLabel.CENTER)
        JLabel label2 = new JLabel("Encrypted Text", null, JLabel.CENTER)
        JTextArea textArea1 = new JTextArea(text1)
        textArea1.setLineWrap(true)
        JTextArea textArea2 = new JTextArea(text2)

        BoxLayout layout = new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS)
        frame.setLayout(layout)
        frame.getContentPane().add(label1)
        frame.getContentPane().add(textArea1)
        frame.getContentPane().add(label2)
        frame.getContentPane().add(textArea2)

        //4. Size the frame.
        frame.setSize(800, 480)

        //5. Show it.
        frame.setVisible(true)
    }

    static void encryptionTest() {
        final String ENG_ALPHABET = "abcdefghijklmnopqrstuvwxyz,.!?&\"';()1234567890 \n"

        Set<Character> textAlph = HelperUtil.deduceAlphabet(ENG_ALPHABET)

        Set<Character> alph1 = HelperUtil.generateAlphabet(33, 126)
        Set<Character> alph2 = HelperUtil.generateAlphabet(173, 254)
        Set<Character> cipherAlph = alph1 + alph2

        String currentDir = System.getProperty("user.dir")
        System.out.println("Current dir using System:" + currentDir)

        String fileText = Files.readString(Paths.get("src/test/resources/lorem-ipsum.txt"))
                .toLowerCase()
                .replace("\r", "")

        def cipherKey = HomophonicCipherGenerator.generateKey(textAlph, cipherAlph, fileText)

        println("Key:")
        println(cipherKey)

        final String encryptedText = HomophonicCipherEncrypter.encrypt(fileText, cipherKey).toLowerCase()

        def analyzedAlphabet = HelperUtil.analyzeCharactersFrequency(fileText)
        def analyzedAlphabetEncrypted = HelperUtil.analyzeCharactersFrequency(encryptedText)
        println(analyzedAlphabet)
        println(analyzedAlphabetEncrypted)

        BarChart.displayChart(analyzedAlphabet, "File text")
        BarChart.displayChart(analyzedAlphabetEncrypted, "Encrypted text")

        frameTest(fileText, encryptedText)
    }

    static void editKeyTest(){
        String fileText = Files.readString(Paths.get("src/test/resources/lorem-ipsum.txt"))
        Map<Character, Set<Character>> key = HomophonicCipherGenerator.generateKey(fileText)
        new EditKeyForm(key)
    }

    static void main(String[] args) {
        editKeyTest()
        // listenersText()
        // frameTest()
    }

}
