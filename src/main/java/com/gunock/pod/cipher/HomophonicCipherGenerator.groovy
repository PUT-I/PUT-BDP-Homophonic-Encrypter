package com.gunock.pod.cipher

import org.json.JSONObject

class HomophonicCipherGenerator {

    final static String ENG_ALPHABET = "abcdefghijklmnopqrstuvwxyz,.!?&\"';()1234567890 \n"
    static Set<Character> cipherAlphabet

    static {
        Set<Character> alph1 = HelperUtil.generateAlphabet(33, 126)
        Set<Character> alph2 = HelperUtil.generateAlphabet(173, 254)
        cipherAlphabet = alph1 + alph2
    }

    private static int generateRandomCharacterCount(Character c, Map<Character, Integer> analyzedAlphabet, int maxCharacters) {
        // Average of frequency of every character in analyzed alphabet
        final int analysisAverage = analyzedAlphabet.values().sum() / analyzedAlphabet.size()

        if(maxCharacters < 1){
            maxCharacters = 1
        }

        int randomCharNumber = HelperUtil.randomInt(1, maxCharacters)

        // For characters that are more frequent than average, increase random character number
        if (analyzedAlphabet.get(c) >= analysisAverage) {
            randomCharNumber *= 2 * (analyzedAlphabet.get(c) / analysisAverage)
        }
        return randomCharNumber
    }

    static Map<Character, Set<Character>> generateKey(String textExample) {
        final Set<Character> textAlphabet = HelperUtil.deduceAlphabet(ENG_ALPHABET)
        return generateKey(textAlphabet, cipherAlphabet, textExample)
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

    static String keyToJson(Map<Character, Set<Character>> key) {
        return new JSONObject(key).toString(0)
    }

}