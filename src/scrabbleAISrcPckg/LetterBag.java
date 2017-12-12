package scrabbleAISrcPckg;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

public final class LetterBag {

    private static LetterBag ourInstance;
    private static final LinkedList<Character> letters = new LinkedList<>(); //linked list allows for easy random removal
    static Map<Character,Integer> letterScoreMappings = new HashMap<>();
    static {
        letterScoreMappings.put('a', 1);letterScoreMappings.put('b', 3);
        letterScoreMappings.put('c', 3);letterScoreMappings.put('d', 2);
        letterScoreMappings.put('e', 1);letterScoreMappings.put('f', 4);
        letterScoreMappings.put('g', 2);letterScoreMappings.put('h', 4);
        letterScoreMappings.put('i', 1);letterScoreMappings.put('j', 8);
        letterScoreMappings.put('k', 5);letterScoreMappings.put('l', 1);
        letterScoreMappings.put('m', 3);letterScoreMappings.put('n', 1);
        letterScoreMappings.put('o', 1);letterScoreMappings.put('p', 3);
        letterScoreMappings.put('q', 10);letterScoreMappings.put('r', 1);
        letterScoreMappings.put('s', 1);letterScoreMappings.put('t', 1);
        letterScoreMappings.put('u', 1);letterScoreMappings.put('v', 4);
        letterScoreMappings.put('w', 4);letterScoreMappings.put('x', 8);
        letterScoreMappings.put('y', 4);letterScoreMappings.put('z', 10);
        letterScoreMappings.put('A', 1);letterScoreMappings.put('B', 3);
        letterScoreMappings.put('C', 3);letterScoreMappings.put('D', 2);
        letterScoreMappings.put('E', 1);letterScoreMappings.put('F', 4);
        letterScoreMappings.put('G', 2);letterScoreMappings.put('H', 4);
        letterScoreMappings.put('I', 1);letterScoreMappings.put('J', 8);
        letterScoreMappings.put('K', 5);letterScoreMappings.put('L', 1);
        letterScoreMappings.put('M', 3);letterScoreMappings.put('N', 1);
        letterScoreMappings.put('O', 1);letterScoreMappings.put('P', 3);
        letterScoreMappings.put('Q', 10);letterScoreMappings.put('R', 1);
        letterScoreMappings.put('S', 1);letterScoreMappings.put('T', 1);
        letterScoreMappings.put('U', 1);letterScoreMappings.put('V', 4);
        letterScoreMappings.put('W', 4);letterScoreMappings.put('X', 8);
        letterScoreMappings.put('Y', 4);letterScoreMappings.put('Z', 10);
    }

    // Fill letter bag
    private LetterBag() {
        // 1 point letters
        addInitialLetters('A', 9);
        addInitialLetters('I', 9);
        addInitialLetters('E', 12);
        addInitialLetters('O', 8);
        addInitialLetters('N', 6);
        addInitialLetters('R', 6);
        addInitialLetters('T', 6);
        addInitialLetters('L', 4);
        addInitialLetters('S', 4);
        addInitialLetters('U', 4);
        // 2 point letters
        addInitialLetters('D', 4);
        addInitialLetters('G', 3);
        // 3 point letters
        addInitialLetters('B', 2);
        addInitialLetters('C', 2);
        addInitialLetters('M', 2);
        addInitialLetters('P', 2);
        // 4 point letters
        addInitialLetters('F', 2);
        addInitialLetters('H', 2);
        addInitialLetters('V', 2);
        addInitialLetters('W', 2);
        addInitialLetters('Y', 2);
        // 5 point letter
        addInitialLetters('K', 1);
        // 8 point letters
        addInitialLetters('J', 1);
        addInitialLetters('X', 1);

        addInitialLetters('Q',  1);
        addInitialLetters('Z',  1);
        // leaving out blanks for now
        // addInitialLetters('_', 0, 2); // '_' represents a blank letter square
    }

    private void addInitialLetters(Character character, int numberOfOccurrences) {

        if (isBagEmpty()) {letters.addFirst(character);
            for (int i = 0; i < numberOfOccurrences-1; i++) {
                letters.add(character);}
        }
        else {
            for (int i = 0; i < numberOfOccurrences; i++) {
                letters.add(character);}
        }
    }


    static String getRandomFromBagAsString(){
        Random random = new Random();
        if (letters.size() > 0) {
            int randint = random.nextInt(letters.size());
            return letters.remove(randint).toString();
        }
        else return null;
    }

    private static boolean isBagEmpty() {
        return letters.isEmpty();
    }

    static void createInstance() {
        if (ourInstance == null)
            ourInstance = new LetterBag();
    }

    public static int numberOfRemainingLetters(){
        return letters.size();
    }

    static void addLetter(Character letterTile) {
        letters.add(letterTile);
    }
}
