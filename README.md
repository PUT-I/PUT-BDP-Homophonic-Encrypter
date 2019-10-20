# PUT BDP Homophonic Encrypter

## Application Description
This application is used to encrypt data with homophonic cipher.
Application allows to generate and save encryption and encrypt and decrypt data (entered manually or loaded from file).

### Key Generation
Key generation consists of two view. In first view you enter public text (you can load it from file) to be used in generating key.
Key generation uses text to calculate character frequency and fit key to it.
In second view you can view frequency charts for public and encrypted texts, edit key and save it to file as JSON.
Key editing is secured to not to input character that is already in key.

### Encryption/Decryption
In encryption view you have several options. First you need to load encryption key (key status should show that key is loaded)
and then enter encrypted/public text (you can load both from file) and then you can decrypt/encrypt it.
As in key generation view, you can show frequency charts (public and encrypted text areas must be filled).
You can save encrypted text to file by entering filename in text field positioned beside file save button.

## Algorithm
Homophonic cipher is based on simple rule - for one letter in public text alphabet there is one or more letters in encryption key.
When encrypting text one random letter from encryption key is picked from encryption key to substitute letter from public text.
The more frequent a letter is in given text, the more letters should be assigned to it in encryption key. This improves cipher
immunity against character frequency analysis.

### Example
```
Encryption key:
a -> { b, c, d }
b -> { e }
l -> { f, g }
i -> { h }
\s -> {_}

| Public text | Encrypted text |
| ali baba    | cgh_ebed       |
```

### Implementation
Key is generated on latin alphabet extended by letters from public text passed as example text of given language.
Cipher uses letters with ASCII codes 33-126 and 173-254. To calculate amount of characters to be assigned for given character
in public text, key generator calculates average frequency of characters in public text and picks random number from 1 to amount of
remaining unused cipher alphabet character divided by remaining amount of public unused public text alphabet characters.
After picking random number of cipher characters to be assigned to public text character, generator checks if frequency of character
is higher than average. If it so, then number of characters to be assigned to it is multiplied by 2 and character frequency divided
by average frequency is added to it. After that random characters from cipher alphabet are assigned to character from public text alphabet.
To decrease size of public text alphabet, and therefore increasing cipher effectivness, generator turns whole public text to lower case characters.

## Enviroment
- Written using JetBrains IntelliJ
- Written in Groovy
- Compiled using Maven Groovy Compiler, Batch and Shade
- Requires Java 11 or newer to run
