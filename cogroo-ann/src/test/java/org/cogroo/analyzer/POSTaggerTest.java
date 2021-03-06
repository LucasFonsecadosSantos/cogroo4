/**
 * Copyright (C) 2012 cogroo <cogroo@cogroo.org>
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *         http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.cogroo.analyzer;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import opennlp.tools.postag.POSTaggerME;

import org.cogroo.analyzer.POSTagger;
import org.cogroo.text.Sentence;
import org.cogroo.text.Token;
import org.cogroo.text.impl.DocumentImpl;
import org.cogroo.text.impl.SentenceImpl;
import org.cogroo.text.impl.TokenImpl;
import org.junit.Before;
import org.junit.Test;


public class POSTaggerTest {
  private POSTagger tagger;
  POSTaggerME mockedTagger;

  @Before
  public void setUp() throws Exception {
    mockedTagger = mock(POSTaggerME.class);
    tagger = new POSTagger(mockedTagger);
  }

  @Test
  public void testAnalyze() throws FileNotFoundException {
    DocumentImpl document = new DocumentImpl();

    String text = "A menina pequena foi para o seu quarto .";
    document.setText(text);

    String[] textArray = text.split(" ");

    Sentence sentence = new SentenceImpl(0, 40, document);
    document.setSentences(Collections.singletonList(sentence));

    List<Token> tokens = createTokens(textArray);
    sentence.setTokens(tokens);

    String[] tags = { "art", "n", "adj", "v-fin", "prp", "art", "pron-det",
        "n", "punc" };

    double[] probs = { 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1, 0.1 };

    when(mockedTagger.tag(any(String[].class), any(Object[].class))).thenReturn(tags);
    when(mockedTagger.probs()).thenReturn(probs);
    tagger.analyze(document);

    assertEquals("art", document.getSentences().get(0).getTokens().get(0)
        .getPOSTag());
    assertEquals("n", document.getSentences().get(0).getTokens().get(1)
        .getPOSTag());
    assertEquals("adj", document.getSentences().get(0).getTokens().get(2)
        .getPOSTag());
    assertEquals("v-fin", document.getSentences().get(0).getTokens().get(3)
        .getPOSTag());
    assertEquals("prp", document.getSentences().get(0).getTokens().get(4)
        .getPOSTag());
    assertEquals("art", document.getSentences().get(0).getTokens().get(5)
        .getPOSTag());
    assertEquals("pron-det", document.getSentences().get(0).getTokens().get(6)
        .getPOSTag());
    assertEquals("n", document.getSentences().get(0).getTokens().get(7)
        .getPOSTag());
    assertEquals("punc", document.getSentences().get(0).getTokens().get(8)
        .getPOSTag());
  }

  private List<Token> createTokens(String[] textArray) {
    List<Token> tokens = new ArrayList<Token>();
    int ini = 0;

    for (int i = 0; i < textArray.length; i++) {
      TokenImpl tokenImpl = new TokenImpl(ini, ini + textArray[i].length(), textArray[i]);
      ini = ini + textArray[i].length() + 1;
      tokens.add(tokenImpl);
    }
    return tokens;
  }
}