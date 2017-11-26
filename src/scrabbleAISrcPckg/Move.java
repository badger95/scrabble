package scrabbleAISrcPckg;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

class Move {

    private LetterContainer[] containersOfWord;
    private String word;
    private Map<LetterContainer, LetterContainer> moveMapping = new HashMap<>();

    Move(List<LetterContainer> letterContainerList, Map<LetterContainer, LetterContainer> moveMapping) {
        containersOfWord = new LetterContainer[letterContainerList.size()];
        StringBuilder sb = new StringBuilder();
        this.moveMapping = moveMapping;
        int i = 0;
        for (LetterContainer l : letterContainerList) {
            sb.append(l.getText());
            containersOfWord[i] = l;
            i++;
        }
        word = sb.toString();
    }

    Move(Set<LetterContainer> letterContainerList, Map<LetterContainer,LetterContainer> moveMapping) {
        containersOfWord = new LetterContainer[letterContainerList.size()];
        StringBuilder sb = new StringBuilder();
        this.moveMapping = moveMapping;
        int i = 0;
        for (LetterContainer l : letterContainerList) {
            sb.append(l.getText());
            containersOfWord[i] = l;
            i++;
        }
        word = sb.toString();
    }

    Move(LetterContainer letter) {
        word = letter.getText();
    }

    LetterContainer[] getContainersOfWord() {
        return containersOfWord;
    }

    String getWordAsString() {
        return word;
    }

    public Map<LetterContainer, LetterContainer> getMoveMapping() {
        return moveMapping;
    }

    public void setMoveMapping(Map<LetterContainer, LetterContainer> moveMapping) {
        this.moveMapping = moveMapping;
    }
}
