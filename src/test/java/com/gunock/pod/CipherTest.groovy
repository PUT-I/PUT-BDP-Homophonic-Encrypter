package com.gunock.pod

import com.gunock.pod.cipher.BarChart
import com.gunock.pod.cipher.HelperUtil
import com.gunock.pod.cipher.HomophonicCipherEncrypter

import javax.swing.*
import java.awt.*
import java.awt.event.ActionEvent
import java.awt.event.ActionListener
import java.nio.file.Files
import java.nio.file.Paths

class CipherTest {

    static void listenersText() {
        //1. Create the frame.
        JFrame frame = new JFrame("Frame demo")

        //2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

        //3. Create components and put them in the frame.
        JPanel panel1 = new JPanel()
        JPanel panel2 = new JPanel()

        JButton button1 = new JButton("Load File")
        JButton button2 = new JButton("Generate Key")
        JButton button3 = new JButton("Decrypt")
        JButton button4 = new JButton("Encrypt")
        JLabel label1 = new JLabel("Test text", null, JLabel.CENTER)
        JTextField textfield1 = new JTextField("fileName", 20)
        panel1.add(label1)
        panel2.add(button1)
        panel2.add(button1)
        panel2.add(textfield1)
        panel1.setLayout(new GridLayout(1, 2))
        panel2.setLayout(new FlowLayout())
        frame.add(panel1)
        frame.add(panel2)
        button1.addActionListener(new ActionListener() {
            @Override
            void actionPerformed(ActionEvent arg0) {
                label1.setText(textfield1.getText())
            }
        })
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS))

        //4. Size the frame.
        frame.setSize(800, 480)

        //5. Show it.
        frame.setVisible(true)
    }

    static void generateKeyFrame() {
        //1. Create the frame.
        JFrame frame = new JFrame("Frame demo")

        //2. Optional: What happens when the frame closes?
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

        //3. Create components and put them in the frame.
        JPanel panel2 = new JPanel()
        JPanel panel1 = new JPanel()

        JTextField textfield1 = new JTextField("Example File Name", 20)
        TextArea fileTextArea = new TextArea("File text will be shown here")
        JButton button1 = new JButton("Load File")
        JButton button2 = new JButton("Generate key")

        panel1.add(textfield1)
        panel2.add(fileTextArea)
        panel1.add(button1)
        panel1.add(button2)
        panel1.setLayout(new FlowLayout())
        panel2.setLayout(new GridLayout(1, 3))

        frame.add(panel1)
        frame.add(panel2)

        button1.addActionListener(new ActionListener() {
            @Override
            void actionPerformed(ActionEvent arg0) {
                String fileText = Files.readString(Paths.get("src/test/resources/" + textfield1.getText()))
                        .toLowerCase()
                        .replace("\r", "")
                fileTextArea.setText(fileText)
            }
        })
        frame.setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.Y_AXIS))

        //4. Size the frame.
        frame.setSize(800, 480)

        //5. Show it.
        frame.setVisible(true)
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
        textArea2.setLineWrap(true)

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

        def cipherKey = HomophonicCipherEncrypter.generateKey(textAlph, cipherAlph, fileText)

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

    static void main(String[] args) {
        generateKeyFrame()
        // listenersText()
        // frameTest()
    }

}
