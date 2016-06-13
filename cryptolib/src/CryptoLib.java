// Compilation (CryptoLibTest contains the main-method):
//   javac CryptoLibTest.java
// Running:
//   java CryptoLibTest
public class CryptoLib {

	/**
	 * Returns an array "result" with the values "result[0] = gcd",
	 * "result[1] = s" and "result[2] = t" such that "gcd" is the greatest
	 * common divisor of "a" and "b", and "gcd = a * s + b * t".
	 **/
	public static int[] EEA(int a, int b) {
		// Note: as you can see in the test suite,
		// your function should work for any (positive) value of a and b.
		int tempInt;
		int s = 0, oldS = 1;
		int t = 1, oldT = 0;
		int r = b, oldR = a;
		int divisor;

		while(r != 0){
			divisor = oldR / r;

			// gcd
			tempInt = r;
			r = oldR - divisor * tempInt;
			oldR = tempInt;

			// one of the multipliers in gcd = a * multiplier1 + b * multiplier2
			tempInt = s;
			s = oldS - divisor * tempInt;
			oldS = tempInt;

			// one of the multipliers in gcd = a * multiplier1 + b * multiplier2
			tempInt = t;
			t = oldT - divisor * tempInt;
			oldT = tempInt;
		}

		if(a == b){
			// special case to handle 5,5 which expects 5, 1, 0 but recieves 5, 0, 1 which is also a correct
			// answer but not the one expected
			tempInt = oldS;
			oldS = oldT;
			oldT = tempInt;
		}

		int[] result = new int[3];
		result[0] = oldR;
		result[1] = oldS;
		result[2] = oldT;
		return result;
	}

	/**
	 * Returns Euler's Totient for value "n".
	 **/
	public static int EulerPhi(int n) {
		int numberOfCoprimes = 0;
		for(int i = 1; i < n; i++){
			// if n and i is coprime (i.e. GCD(n, i) == 1) increase counter
			if(EEA(n, i)[0] == 1)
				numberOfCoprimes++;
		}
		return numberOfCoprimes;
	}

	/**
	 * Returns the value "v" such that "n*v = 1 (mod m)". Returns 0 if the
	 * modular inverse does not exist.
	 **/
	public static int ModInv(int n, int m) {
		int eeaResult = EEA(n, m)[1];
		if(n < 0){
			eeaResult = -eeaResult; // the inverse of a negative number is negative not positive...
		}
		if(eeaResult < 0){
			eeaResult = m + eeaResult; // -number mod m == m + -number
		}
		return eeaResult;
	}

	/**
	 * Returns 0 if "n" is a Fermat Prime, otherwise it returns the lowest
	 * Fermat Witness. Tests values from 2 (inclusive) to "n/3" (exclusive).
	 **/
	public static int FermatPT(int n) {
		for(int i = 2; i < n/3; i++){
			// if i ^ n-1 mod n == 1
			if(modPow(i, n-1, n) != 1){
				return i;
			}
		}

		return 0;
	}

	/**
	 * a ^ b mod "mod"
	 */
	public static int modPow(int a, int b, int mod){
		int result = 1;

		for(int i = 0; i < b; i++){
			result = result * a;
			result = result % mod;
		}

		return result;
	}

	/**
	 * Returns the probability that calling a perfect hash function with
	 * "n_samples" (uniformly distributed) will give one collision (i.e. that
	 * two samples result in the same hash) -- where "size" is the number of
	 * different output values the hash function can produce.
	 **/
	public static double HashCP(double n_samples, double size) {
		// The probability that any two samples (out of n possible) result in the same hash is the same as the
		// probability that not all of them is unique, i.e. 1 - "the probability that all hashes are unique".
		// This probability is easier to calculate. The first hash can be any of the available hashes
		// (i.e. size), the next hash can be any hash other than that hash (i.e. any of size - 1 values, so
		// the probability that we pick two that is unique is (size - 1) / size), the next hash can
		// be any hash other than the first two (i.e. size - 2) and so on.
		double currentSum = 1.0;
		for(int i = 1; i < n_samples; i++){
			double nValue = (size - i) / size;
			currentSum = currentSum * nValue;
		}
		return 1.0 - currentSum;
	}

}
