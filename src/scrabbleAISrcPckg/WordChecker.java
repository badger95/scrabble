
package scrabbleAISrcPckg;

import java.util.HashMap;
import java.util.Map;

class WordChecker {

    private TrieNode root;
    private static Map<Integer, Character> intToCharMap = new HashMap<>();


    WordChecker() {
        intToCharMap.put(0,'a');
        intToCharMap.put(1,'b');
        intToCharMap.put(2,'c');
        intToCharMap.put(3,'d');
        intToCharMap.put(4,'e');
        intToCharMap.put(5,'f');
        intToCharMap.put(6,'g');
        intToCharMap.put(7,'h');
        intToCharMap.put(8,'i');
        intToCharMap.put(9,'j');
        intToCharMap.put(10,'k');
        intToCharMap.put(11,'l');
        intToCharMap.put(12,'m');
        intToCharMap.put(13,'n');
        intToCharMap.put(14,'o');
        intToCharMap.put(15,'p');
        intToCharMap.put(16,'q');
        intToCharMap.put(17,'r');
        intToCharMap.put(18,'s');
        intToCharMap.put(19,'t');
        intToCharMap.put(20,'u');
        intToCharMap.put(21,'v');
        intToCharMap.put(22,'w');
        intToCharMap.put(23,'x');
        intToCharMap.put(24,'y');
        intToCharMap.put(25,'z');
        root = new TrieNode();
    }

    void insert(String word) {
        TrieNode parent = root;
        word = word.toLowerCase();
        for (int i = 0; i < word.length(); i++) {
            int index = word.charAt(i) - 'a';
            if (parent.children[index] == null) {
                TrieNode temp = new TrieNode();
                parent.children[index] = temp;
                parent = temp;
            } else {
                parent = parent.children[index];
            }
        }

        parent.endOfWord = true;
    }

    TrieNode search(String s) {
        s = s.toLowerCase();

        TrieNode parent = root;
        for (int i = 0; i < s.length(); i++) {
            int index = s.charAt(i) - 'a';
            if (parent.children[index] != null) {
                parent = parent.children[index];
            } else {
                return null;
            }
        }

        if (parent == root)
            return null;
        return parent;
    }

    boolean startsWith(String prefix) {
        TrieNode p = search(prefix);
        return p != null;
    }

    TrieNode getRoot() {
        return root;
    }

    static char toChar(int j) {
        return intToCharMap.get(j);
    }

    class TrieNode {
        TrieNode[] children;
        boolean endOfWord;

        TrieNode() {
            this.children = new TrieNode[26];
        }
    }
}
