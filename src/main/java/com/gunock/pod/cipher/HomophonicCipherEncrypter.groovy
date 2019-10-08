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

    private static int generateRandomCharacterCount(Character c, Map<Character, Integer> analyzedAlphabet, int maxCharacters) {
        // Average of frequency of every character in analyzed alphabet
        final int analysisAverage = analyzedAlphabet.values().sum() / analyzedAlphabet.size()

        int randomCharNumber = HelperUtil.randomInt(1, maxCharacters)

        // For characters that are more frequent than average, increase random character number
        if (analyzedAlphabet.get(c) >= analysisAverage) {
            randomCharNumber *= 2 * (analyzedAlphabet.get(c) / analysisAverage)
        }
        return randomCharNumber
    }

    static Map<Character, Set<Character>> generateKey(Set<Character> textAlphabet, Set<Character> cipherAlphabet, String textExample) {
        // Analyzed character frequency to improve generated key
        final Map<Character, Integer> analyzedAlphabet = HelperUtil.analyzeCharactersFrequency(textExample)
        Map<Character, Set<Character>> result = new HashMap<>()

        // Iterate over given alphabet
        for (Character c : textAlphabet) {
            final int maxCharacters = (int) Math.floor(cipherAlphabet.size() / textAlphabet.size())

            // Initialize key in result map
            result.put(c, new HashSet<Character>())

            // Generate number of characters to be added under current key in map
            int randomCharNumber = generateRandomCharacterCount(c, analyzedAlphabet, maxCharacters)

            // Add random number of characters to key
            for (int i = 0; i < randomCharNumber; i++) {
                // Generate random index value of random character
                final int randomCharIndex = HelperUtil.randomInt(0, cipherAlphabet.size() - 1)
                // Random character to be added to key
                final Character randomChar = cipherAlphabet[randomCharIndex]
                // Safeguard to not to use same characters as key
                if (randomChar == c) {
                    i--
                    continue
                }
                // Add character to key
                result.get(c).add(randomChar)
                // Remove character from alphabet of cipher
                cipherAlphabet.remove(randomChar)
            }

            // Sort characters added to key
            result.get(c).sort()
        }

        return result
    }

}
