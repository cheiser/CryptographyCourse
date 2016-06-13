import java.io.BufferedReader;
import java.io.FileReader;
import java.math.BigInteger;

public class AttackRSA {

	public static void main(String[] args) {
		String filename = "input.txt";
		BigInteger[] N = new BigInteger[3];
		BigInteger[] e = new BigInteger[3];
		BigInteger[] c = new BigInteger[3];
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			for (int i = 0; i < 3; i++) {
				String line = br.readLine();
				String[] elem = line.split(",");
				N[i] = new BigInteger(elem[0].split("=")[1]);
				e[i] = new BigInteger(elem[1].split("=")[1]);
				c[i] = new BigInteger(elem[2].split("=")[1]);
			}
			br.close();
		} catch (Exception err) {
			System.err.println("Error handling file.");
			err.printStackTrace();
		}
		BigInteger m = recoverMessage(N, e, c);
		System.out.println("Recovered message: " + m);
		System.out.println("Decoded text: " + decodeMessage(m));


		/////////////////////////////
//		int mods[] = {3, 4, 5};
//		int equalMods[] = {2, 2, 1};
//		int mods[] = {3, 5, 7};
//		int equalMods[] = {2, 3, 2};
		// int mods[] = {2, 3, 4, 5, 6, 7};
		// int equalMods[] = {1, 1, 1, 1, 1, 0}; this is equivalent to the system below because
		// lcm(2, 3, 4, 5, 6, 7) == 60
//		int mods[] = {7, 60};
//		int equalMods[] = {0, 1};
//		int[] result = CryptoLibExtended.chineseRemainderTheorem(mods, equalMods);
//		System.out.println("result[0]: " + result[0] + " result[1]: " + result[1]);
//
//		BigInteger val1 = new BigInteger("-9");
//		BigInteger val2 = new BigInteger("823");
//		BigInteger[] result2 = CryptoLibExtended.EEABI(val1, val2);
//		BigInteger resultMI = CryptoLibExtended.ModInvBI(val1, val2);
//		System.out.println("GCD of " + val1 + " and " + val2 + " is " + result2[0]);
//		System.out.println("modular inverse of " + val1 + " and " + val2 + " is " + resultMI);
	}

	public static String decodeMessage(BigInteger m) {
		return new String(m.toByteArray());
	}

	/**
	 * Tries to recover the message based on the three intercepted cipher texts.
	 * In each array the same index refers to same receiver. I.e. receiver 0 has
	 * modulus N[0], public key d[0] and received message c[0], etc.
	 *
	 * @param N
	 *            The modulus of each receiver.
	 * @param e
	 *            The public key of each receiver (should all be 3).
	 * @param c
	 *            The cipher text received by each receiver.
	 * @return The same message that was sent to each receiver.
	 */
	private static BigInteger recoverMessage(BigInteger[] N, BigInteger[] e,
			BigInteger[] c) {
		// e*d = 1 (mod y(N)) , y(N) == phi of N, i.e. N1, N2, N3
		// c = m ^ e (mod y(N)), m = c ^ d (mod y(N))
		// c1 = m^3 (mod N1)
		// c2 = m^3 (mod N2)
		// c3 = m^3 (mod N3)

		// m^3 = c1 (mod N1)
		// m^3 = c2 (mod N2)
		// m^3 = c3 (mod N3)
		// m = cbrt(m^3)


		BigInteger[] result = CryptoLibExtended.chineseRemainderTheoremBI(N, c);
		// System.out.println("result[0]: " + result[0] + " result[1]: " + result[1]);

		return CubeRoot.cbrt(result[0]);
	}

}
