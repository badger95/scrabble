
package scrabbleAISrcPckg;

class WordChecker {
    private TrieNode root;

    WordChecker() {
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

    private TrieNode search(String s) {
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

    boolean isAWordInDictionary(String word) {
        TrieNode parent = search(word);
        return parent != null && parent.endOfWord;
    }

    boolean startsWith(String prefix) {
        TrieNode p = search(prefix);
        return p != null;
    }

    TrieNode getRoot() {
        return root;
    }

    class TrieNode {
        TrieNode[] children;
        boolean endOfWord;

        TrieNode() {
            this.children = new TrieNode[26];
        }
    }
}
