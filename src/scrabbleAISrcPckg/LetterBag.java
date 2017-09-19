package scrabbleAISrcPckg;
import java.util.LinkedList;
import java.util.Random;

/**
 * Created by DELL660 on 7/11/2017.
 */
public final class LetterBag {

    private static LetterBag ourInstance;
    private static final LinkedList<Letter> letters = new LinkedList<>(); //linked list allows for easy random removal

    // Fill letter bag
    private LetterBag() {
        // 1 point letters
        addInitialLetters('A', 1 , 9);
        addInitialLetters('I', 1 , 9);
        addInitialLetters('E', 1 , 12);
        addInitialLetters('O', 1 , 8);
        addInitialLetters('N', 1 , 6);
        addInitialLetters('R', 1 , 6);
        addInitialLetters('T', 1 , 6);
        addInitialLetters('L', 1 , 4);
        addInitialLetters('S', 1 , 4);
        addInitialLetters('U', 1 , 4);
        // 2 point letters
        addInitialLetters('D', 2 , 4);
        addInitialLetters('G', 2 , 3);
        // 3 point letters
        addInitialLetters('B', 3 , 2);
        addInitialLetters('C', 3 , 2);
        addInitialLetters('M', 3 , 2);
        addInitialLetters('P', 3 , 2);
        // 4 point letters
        addInitialLetters('F', 4 , 2);
        addInitialLetters('H', 4 , 2);
        addInitialLetters('V', 4 , 2);
        addInitialLetters('W', 4 , 2);
        addInitialLetters('Y', 4 , 2);
        // 5 point letter
        addInitialLetters('K', 5 , 1);
        // 8 point letters
        addInitialLetters('J', 8 , 1);
        addInitialLetters('X', 8 , 1);

        addInitialLetters('Q', 10, 1);
        addInitialLetters('Z', 10, 1);
        // leaving out blanks for now
        // addInitialLetters('_', 0, 2); // '_' represents a blank letter square
    }

    private void addInitialLetters(Character character, int value, int numberOfOccurrences) {

        if (isBagEmpty()) {letters.addFirst(new Letter(value, character));
            for (int i = 0; i < numberOfOccurrences-1; i++) {
                letters.add(new Letter(value, character));}
        }
        else {
            for (int i = 0; i < numberOfOccurrences; i++) {
                letters.add(new Letter(value, character));}
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

}
