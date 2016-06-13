
public class TestMethods {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		int[] result = CryptoLib.EEA(10, 5);
		System.out.println("GCD: " + result[0] + " with quotients[s, t]: " + result[1] + ", " + result[2]);
		System.out.println("modInv: " + CryptoLib.ModInv(823, 9));
		System.out.println("modpow: " + CryptoLib.modPow(5, 8, 3));
	}

}
