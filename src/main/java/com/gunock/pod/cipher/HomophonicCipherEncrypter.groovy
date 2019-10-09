package com.gunock.pod.cipher

import com.gunock.pod.utils.HelperUtil

class HomophonicCipherEncrypter {

    static String encrypt(String source, Map<Character, Set<Character>> encryptionKey) {
        String result = ''
        source = source.toLowerCase().replace("\r", "")

        // Iterate over source string
        for (Character c : source.toCharArray()) {
            final int keyCharCount = encryptionKey.get(c).size()
            // Get random character from chars stored under key (character from source string)
            final Character randomChar = encryptionKey.get(c)[HelperUtil.randomInt(0, keyCharCount - 1)]
            // Append selected character to result string
            result += randomChar
            // Print comparison of original and substituted characters
        }

        return result
    }

    static Map<Character, Character> reverseKey(Map<Character, Set<Character>> encryptionKey) {
        Map<Character, Character> result = new HashMap<>()

        for (Character key : encryptionKey.keySet()) {
            for (Character c : encryptionKey.get(key)) {
                result.put(c, key)
            }
        }

        return result
    }

    static String decrypt(String source, Map<Character, Set<Character>> encryptionKey) {
        // Reversed key to simplify implementation of decryption
        final Map<Character, Character> reversedKey = reverseKey(encryptionKey)
        String result = ''

        // Iterate over source string
        for (Character c : source.toCharArray()) {
            // Append character from reversed string corresponding to current character from source string
            result += reversedKey.get(c)
        }

        return result
    }

}
