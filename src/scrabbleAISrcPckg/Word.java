package scrabbleAISrcPckg;

import java.util.List;

class Word {

    private LetterContainer[] containersOfWord;
    private String word;

    Word(List<LetterContainer> letterContainerList) {
        containersOfWord = new LetterContainer[letterContainerList.size()];
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (LetterContainer l : letterContainerList) {
            sb.append(l.getText());
            containersOfWord[i] = l;
            i++;
        }
        word = sb.toString();
    }

    Word(LetterContainer letter) {
        word = letter.getText();
    }

    LetterContainer[] getContainersOfWord() {
        return containersOfWord;
    }

    String getWordAsString() {
        return word;
    }
}
