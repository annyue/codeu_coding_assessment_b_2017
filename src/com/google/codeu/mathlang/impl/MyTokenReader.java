// Copyright 2017 Google Inc.
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
// http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.

package com.google.codeu.mathlang.impl;

import java.io.IOException;

import com.google.codeu.mathlang.core.tokens.NameToken;
import com.google.codeu.mathlang.core.tokens.NumberToken;
import com.google.codeu.mathlang.core.tokens.StringToken;
import com.google.codeu.mathlang.core.tokens.SymbolToken;
import com.google.codeu.mathlang.core.tokens.Token;
import com.google.codeu.mathlang.parsing.TokenReader;

// MY TOKEN READER
//
// This is YOUR implementation of the token reader interface. To know how
// it should work, read src/com/google/codeu/mathlang/parsing/TokenReader.java.
// You should not need to change any other files to get your token reader to
// work with the test of the system.
public final class MyTokenReader implements TokenReader {
  private int pos;
  private String source;

  public MyTokenReader(String source) {
    // Your token reader will only be given a string for input. The string will
    // contain the whole source (0 or more lines).
    this.source = source;
    pos = 0;

  }

  @Override
  public Token next() throws IOException {
    // Most of your work will take place here. For every call to |next| you should
    // return a token until you reach the end. When there are no more tokens, you
    // should return |null| to signal the end of input.

    // If for any reason you detect an error in the input, you may throw an IOException
    // which will stop all execution.
    //at the end of input
    
    //end of input, return null
    if (pos >= source.length()) {
      return null;
    }

    String currPart = source.substring(pos);
    char currChar = currPart.charAt(0);

    if (currChar == ' ' || currChar == '\n') {
      pos++;
      return next();
    }
    
    if (currChar == ';') {
      pos++;
      return new SymbolToken(';');
    }
    //checking for symbol
    if (currChar == '+' || currChar == '-' || currChar == '=') {
      pos++;
      return new SymbolToken(currChar);
    }
    //checking for StringToken
    if (currChar == '\"') {
      int end = currPart.indexOf('\"', 1);
      //check if end quote is present in the string
      if (end != -1) {
        pos += end + 1;
        String result = currPart.substring(1, end);
        return new StringToken(result);
      } else {
        throw new IOException("Missing end quote");
      }
    }
    //checking for numbers
    if (Character.isDigit(currChar)) {
      String result = Character.toString(currChar);
      int i = 1;
      //iterate while the charAt(i) is a number or '.' and concatenate it onto the result
      for (; i < currPart.length()
          && ((Character.isDigit(currPart.charAt(i)) || currPart.charAt(i) == '.')); i++) {
        result += currPart.charAt(i);
      }
      double num = Double.parseDouble(result);
      pos += i;
      return new NumberToken(num);
    }

    String var = Character.toString(currChar);
    int i = 1;
    //iterate while the charAt(i) is a letter or a number and concatenate it onto var
    for (; Character.isAlphabetic(currPart.charAt(i))
        || Character.isDigit(currPart.charAt(i)); i++) {
      var += currPart.charAt(i);
    }
    pos += var.length();
    return new NameToken(var);
  }
}
