package com.gunock.pod.features.steps

import com.gunock.pod.cipher.HomophonicCipherEncrypter
import com.gunock.pod.cipher.HomophonicCipherGenerator
import com.gunock.pod.utils.HelperUtil
import cucumber.api.groovy.EN
import cucumber.api.groovy.Hooks
import org.testng.Assert

this.metaClass.mixin(Hooks)
this.metaClass.mixin(EN)

String fileText
String encryptedText
String decryptedText
Map<Character, Set<Character>> encryptionKey

Given(~/^I have loaded text from file "([^"]*)"$/) { String path ->
    fileText = HelperUtil.readFile(path)
    Assert.assertFalse(fileText.isBlank(), "Loaded text should not be empty,")
    println("File " + path + " loaded")
}
When(~/^I create encryption key from loaded text$/) { ->
    encryptionKey = HomophonicCipherGenerator.generateKey(fileText)
    Assert.assertNotNull(encryptionKey)
    Assert.assertFalse(encryptionKey.isEmpty())
    println("Key generated")
}
And(~/^I encrypt loaded text$/) { ->
    encryptedText = HomophonicCipherEncrypter.encrypt(fileText, encryptionKey)
    Assert.assertNotNull(encryptedText)
    Assert.assertFalse(encryptedText.isBlank())
    println("Text encrypted")
}
Then(~/^I get encrypted text$/) { ->
    final Map<Character, Character> reversedKey = HomophonicCipherEncrypter.reverseKey(encryptionKey)
    for (Character c : encryptedText.toCharArray()) {
        Assert.assertTrue(reversedKey.containsKey(c))
    }
    println("Encryption assertion passed")
}
And(~/^I decrypt encrypted text$/) { ->
    decryptedText = HomophonicCipherEncrypter.decrypt(encryptedText, encryptionKey)
    Assert.assertNotNull(encryptedText)
    Assert.assertFalse(encryptedText.isBlank())
    println("Text decrypted")
}
Then(~/^I get decrypted text$/) { ->
    for (Character c : decryptedText.toCharArray()) {
        Assert.assertTrue(encryptionKey.containsKey(c))
    }
    println("Decryption assertion passed")
}