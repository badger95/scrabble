package scrabbleAISrcPckg;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Random;

/**
 * Created by DELL660 on 7/11/2017.
 */
public final class LetterBag {

    private static LetterBag ourInstance;
    private static final LinkedList<Letter> letters = new LinkedList<>(); //linked list allows for easy random removal
    static Map<Character,Integer> letterScoreMappings = new HashMap<>();
    static {
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

        if (isBagEmpty()) {letters.addFirst(new Letter(character));
            for (int i = 0; i < numberOfOccurrences-1; i++) {
                letters.add(new Letter(character));}
        }
        else {
            for (int i = 0; i < numberOfOccurrences; i++) {
                letters.add(new Letter(character));}
        }
    }


    static Letter getRandomFromBag(){
        Random random = new Random();
        int randint = random.nextInt(letters.size());
        return letters.remove(randint);
    }

    public static boolean isBagEmpty() {
        return letters.isEmpty();
    }

    public static int numberOfRemainingLetters(){
        return letters.size();
    }

    public static LetterBag getInstance() {
        if (ourInstance == null)
            ourInstance = new LetterBag ();
        return ourInstance;
    }

    public static void addLetter(Letter letterTile) {
        letters.add(letterTile);
    }

    public int getLettersScore(String letter) {
           return letterScoreMappings.get(letter.toCharArray()[0]);
    }
}
