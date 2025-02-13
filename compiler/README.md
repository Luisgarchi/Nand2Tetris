# Tokenizer

Obviously we need to "tokenize" 1) special symbols, 2) keywords, 3) integer constants, 4) string constants, and 5) identifiers.

However we need to implicitly also handle 1) white spaces, and 2) comments.

- White spaces can either represent:
    - Indentation
    - The end of a token
    - A space inside a string constant

- White spaces that are not in a string constant should represent token boundaries

- ReadLine into class.



# JackTokenizer pseudo code
.HashMoreTokens(){
    Checks to see if there are any tokens in the cache
}

.Advance(){
    
}