package com.gunock.pod.cipher

import java.util.concurrent.ThreadLocalRandom

class HelperUtil {

    static Set<Character> deduceAlphabet(String input) {
        Set<Character> uniqueSymbols = new HashSet<>()
        for (Character c : input.toCharArray()) {
            uniqueSymbols.add(c)
        }
        return uniqueSymbols.sort()
    }

    static Set<Character> generateAlphabet(int asciiStart, int asciiEnd) {
        Set<Character> result = new HashSet<>()
        for (char c = asciiStart; c <= asciiEnd; c++) {
            result.add(c)
        }
        return result
    }

    static Map<Character, Integer> analyzeCharactersFrequency(String source) {
        Map<Character, Integer> result = new HashMap<>()

        for (Character c : source.toCharArray()) {
            if (result.containsKey(c)) {
                result.put(c, result.get(c) + 1)
            } else {
                result.put(c, 1)
            }
        }

        return result.sort { a, b -> b.value <=> a.value }
    }

    static int randomInt(int min, int max) {
        return ThreadLocalRandom.current().nextInt(min, max + 1)
    }

    static List<String> characterSetToStringList(Set<Character> charSet) {
        List<String> result = new ArrayList<>()

        for (Character c : charSet) {
            result.add(c.toString())
        }

        return result
    }

}
