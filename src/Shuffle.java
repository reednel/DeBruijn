import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * A collection of tools to manipulate even-length de Bruijn sequences.
 * 
 * @author reednel
 */
public final class Shuffle {

	private Shuffle() {}
	
	/**
	 * Check if, for each sequence in the given file, and each rotation of that
	 * sequence, it in/out shuffles back to itself.
	 * 
	 * @param A First bit of file name
	 * @param n next bit of file name
	 */
	public static void bigShuffleToSelf(String A, int n) {

		String filePath = A.length() + "-" + n + ".txt";

		try {

			BufferedReader bufferedReader = new BufferedReader(new FileReader(filePath));
			String line = bufferedReader.readLine();

			for (int i = 0; line != null; i++) {

				allRotShuffleToSelf(line, i);

				line = bufferedReader.readLine();

			}

			bufferedReader.close();

		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	/**
	 * Performs a perfect out shuffle, following the formula (length k, index i): if
	 * (i < k/2): i -> 2i; if (i >= k/2): i -> 2i - k + 1
	 * 
	 * @param str the set to be shuffled
	 * @return the shuffled set, as a String
	 */
	public static String outShuffle(String str) {

		// Only accept even strings
		if (str.length() % 2 != 0)
			return null;

		int n = str.length() / 2;
		char[] newStr = new char[str.length()];
		String retStr = "";

		// Handle first half of domain
		for (int i = 0; i < n; i++)
			newStr[2 * i] = str.charAt(i);

		// Handle second half of domain
		for (int i = n; i < 2 * n; i++)
			newStr[(2 * i) - (2 * n) + 1] = str.charAt(i);

		// Convert from char Array to String
		for (int i = 0; i < newStr.length; i++)
			retStr += newStr[i];

		return retStr;

	}

	/**
	 * Performs a perfect in shuffle, following the formula (length k, index i): if
	 * (i < k/2): i -> 2i + 1; if (i >= k/2): i -> 2i - k
	 * 
	 * @param str the set to be shuffled
	 * @return the shuffled set, as a String
	 */
	public static String inShuffle(String str) {

		// Only accept even strings
		if (str.length() % 2 != 0)
			return null;

		int n = str.length() / 2;
		char[] newStr = new char[str.length()];
		String retStr = "";

		// Handle first half of domain
		for (int i = 0; i < n; i++)
			newStr[(2 * i) + 1] = str.charAt(i);

		// Handle second half of domain
		for (int i = n; i < 2 * n; i++)
			newStr[(2 * i) - (2 * n)] = str.charAt(i);

		// Convert from char Array to String
		for (int i = 0; i < newStr.length; i++)
			retStr += newStr[i];

		return retStr;

	}

	/**
	 * Checks if the inputed string would retain its order after being out-shuffled
	 * O(n) for true return
	 * 
	 * @param str the string to be checked
	 * @return true if the string would out-shuffle to its original order, false
	 *         otherwise
	 */
	public static boolean outShufflesToSelf(String str) {

		int n = str.length() / 2;

		// Handle first half of domain
		for (int i = 0; i < n; i++)
			if (str.charAt(i) != (str.charAt(2 * i)))
				return false;

		// Handle second half of domain
		for (int i = n; i < 2 * n; i++)
			if (str.charAt(i) != (str.charAt((2 * i) - (2 * n) + 1)))
				return false;

		return true;

	}

	/**
	 * Checks if the inputed string would retain its order after being in-shuffled
	 * O(n) for true return
	 * 
	 * @param str the string to be checked
	 * @return true if the string would in-shuffle to its original order, false
	 *         otherwise
	 */
	public static boolean inShufflesToSelf(String str) {

		int n = str.length() / 2;

		// Handle first half of domain
		for (int i = 0; i < n; i++)
			if (str.charAt(i) != (str.charAt((2 * i) + 1)))
				return false;

		// Handle second half of domain (shit ain't transitive)
		for (int i = n; i < 2 * n; i++)
			if (str.charAt(i) != (str.charAt((2 * i) - (2 * n))))
				return false;

		return true;

	}

	/**
	 * Rotate a specified string right a specified amount O(1)
	 * 
	 * @param str the string to be rotated
	 * @param rot the amount to rotate by
	 * @return the string, rotated the specified amount
	 */
	public static String rotateRight(String str, int rot) {

		int cutPoint = str.length() - (rot % str.length()); // adjusts for rotations > str length
		String retStr = "";

		retStr = str.substring(cutPoint) + str.substring(0, cutPoint);

		return retStr;

	}

	/**
	 * Rotate a specified string left a specified amount O(1)
	 * 
	 * @param str the string to be rotated
	 * @param rot the amount to rotate by
	 * @return the string, rotated the specified amount
	 */
	public static String rotateLeft(String str, int rot) {

		int cutPoint = rot % str.length(); // adjusts for rotations > str length
		String retStr = "";

		retStr = str.substring(cutPoint) + str.substring(0, cutPoint);

		return retStr;

	}

	/**
	 * Checks to see if rotated versions of the given string shuffle (in or out)
	 * back to themselves, prints results
	 * 
	 * @param str the string to be checked
	 * @param j   the sequence number as generated by the DBGraph class
	 */
	public static void allRotShuffleToSelf(String str, int j) {

		String rot;

		for (int i = 0; i < str.length(); i++) {

			rot = rotateRight(str, i);

			if (outShufflesToSelf(rot))
				System.out.println(j + ": " + rot + " - OUT");

			if (inShufflesToSelf(rot))
				System.out.println(j + ":" + rot + " - IN");

		}

	}

	/**
	 * Checks to see if rotated versions of the given string shuffle, then rotate
	 * back to themselves, prints results
	 * 
	 * Performs the following operation, specifically: rotate right i characters >
	 * shuffle > rotate left i characters (back to rotated) > compare to original
	 * rotation
	 * 
	 * Note: The left rotate only gets it back to the state of the right rotate, not
	 * back to the original string.
	 * 
	 * @param str the string to be checked
	 */
	public static void allRotShuffleShiftToSelf(String str) {

		System.out.println(
				"\nFor the sequence " + str + ", \nthe following shifts \"shuffle-shift\" back to themselves:");

		for (int i = 0; i < str.length(); i++) {

			System.out.print(i + " - ");

			if (rotateLeft(outShuffle(rotateRight(str, i)), 2 * i).equals(str))
				System.out.print(" OUT");

			if (rotateLeft(inShuffle(rotateRight(str, i)), 2 * i).equals(str))
				System.out.print(" IN");

			System.out.println();

		}

	}

}
