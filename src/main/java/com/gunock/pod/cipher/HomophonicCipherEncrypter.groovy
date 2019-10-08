package com.gunock.pod.cipher

class HomophonicCipherEncrypter {

    static String encrypt(String source, Map<Character, Set<Character>> cipherKey) {
        String result = ''
        source = source.toLowerCase().replace("\r", "")

        // Iterate over source string
        for (Character c : source.toCharArray()) {
            final int keyCharCount = cipherKey.get(c).size()
            // Get random character from chars stored under key (character from source string)
            final Character randomChar = cipherKey.get(c)[HelperUtil.randomInt(0, keyCharCount - 1)]
            // Append selected character to result string
            result += randomChar
            // Print comparison of original and substituted characters
        }

        return result
    }

    static Map<Character, Character> reverseKey(Map<Character, Set<Character>> cipherKey) {
        Map<Character, Character> result = new HashMap<>()

        for (Character key : cipherKey.keySet()) {
            for (Character c : cipherKey.get(key)) {
                result.put(c, key)
            }
        }

        return result
    }

    static String decrypt(String source, Map<Character, Set<Character>> cipherKey) {
        // Reversed key to simplify implementation of decryption
        final Map<Character, Character> reversedKey = reverseKey(cipherKey)
        String result = ''

        // Iterate over source string
        for (Character c : source.toCharArray()) {
            // Append character from reversed string corresponding to current character from source string
            result += reversedKey.get(c)
        }

        return result
    }

}
