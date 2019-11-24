package com.gunock.pod.cipher

class EncryptionKey {

    private Map<Character, Set<Character>> key

    EncryptionKey() {
        key = new TreeMap<>()
    }

    EncryptionKey(Map<Character, Set<Character>> key) {
        this.key = key
    }

    void put(Character k, Set<Character> value) {
        key.put(k, value)
    }

    Set<Character> get(Character k) {
        return key.get(k)
    }

    Set<Character> keySet() {
        return key.keySet()
    }

    boolean containsKey(Character c) {
        return key.containsKey(c)
    }

    Map<Character, Set<Character>> getKey() {
        return key
    }
}
