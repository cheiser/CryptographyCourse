import java.io.BufferedReader;
import java.io.FileReader;

import javax.xml.bind.DatatypeConverter;

//9111103512
public class CBCXor {

	public static void main(String[] args) {
		String filename = "input.txt";
		byte[] first_block = null;
		byte[] encrypted = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(filename));
			first_block = br.readLine().getBytes();
			encrypted = DatatypeConverter.parseHexBinary(br.readLine());
			br.close();
		} catch (Exception err) {
			System.err.println("Error handling file.");
			err.printStackTrace();
			System.exit(1);
		}
		String m = recoverMessage(first_block, encrypted);
		System.out.println("Recovered message: " + m);
		
//		byte[] arr1 = new byte[] { 1, 0, 1, 0, 1, 1 };
//		byte[] arr2 = new byte[] { 1, 1, 0, 1, 0, 1 };
//		byte[] xored = xorArrays(arr1, arr2);
//		System.out.print(" arr1: ");
//		printByteArray(arr1);
//		System.out.print("\n arr2: ");
//		printByteArray(arr2);
//		System.out.print("\nxored: ");
//		printByteArray(xored);
//		System.out.println("\nthe key: ");
//		printByteArray(findKey(first_block, encrypted));
	}

	/**
	 * Recover the encrypted message (CBC encrypted with XOR, block size = 12).
	 * 
	 * @param first_block
	 *            We know that this is the value of the first block of plain
	 *            text.
	 * @param encrypted
	 *            The encrypted text, of the form IV | C0 | C1 | ... where each
	 *            block is 12 bytes long.
	 */
	private static String recoverMessage(byte[] first_block, byte[] encrypted) {
		byte[] key = findKey(first_block, encrypted);
		byte[] decrypted = decryptMessage(encrypted, key);
		return new String(decrypted);
	}
	
	/**
	 * Decrypts the encrypted text using the given key
	 * @param encrypted the encrypted message
	 * @param key the key used to encrypt the message
	 * @return the plaintext (the decryption) of the encrypted message
	 */
	private static byte[] decryptMessage(byte[] encrypted, byte[] key){
		byte[] decrypted = new byte[encrypted.length - key.length]; // we won't have iv hence - key.length
		byte[] cipheri = new byte[key.length];
		byte[] cipheriminus1 = new byte[key.length];
		byte[] messagei;
		
		System.arraycopy(encrypted, 0, cipheriminus1, 0, key.length);
		for(int i = key.length; i < encrypted.length; i += key.length){
			System.arraycopy(encrypted, i, cipheri, 0, key.length);
			
			messagei = xorArrays(key, xorArrays(cipheri, cipheriminus1));
			System.arraycopy(messagei, 0, decrypted, i-key.length, messagei.length);
			
			System.arraycopy(cipheri, 0, cipheriminus1, 0, cipheri.length);
		}
		
		return decrypted;
	}
	
	/**
	 * Finds the key used to create the encrypted text
	 * @param first_block the first known block of the encrypted data
	 * @param encrypted the encrypted message
	 * @return the key used to encrypt the message
	 */
	private static byte[] findKey(byte[] first_block, byte[] encrypted){
		byte[] iv = new byte[first_block.length];
		byte[] first_cipher = new byte[first_block.length];
		byte[] key = new byte[first_block.length];
		System.arraycopy(encrypted, 0, iv, 0, iv.length); // get the first IV block from the cipher text
		System.arraycopy(encrypted, iv.length, first_cipher, 0, iv.length); // get the cipher of first_block
		
		// Ci = k + (Mi + Ci-1)
		// k = Ci + (Mi + Ci-1), Ci is the first cipher block, Mi is the message for the first cipher block,
		// Ci-1 is in this case the IV and k is naturally the key.
		key = xorArrays(first_cipher, xorArrays(first_block, iv));
		
		return key;
	}
	
	/**
	 * XORs the two byte arrays with each other and returns the resulting array
	 * @param arr1 one of the byte arrays
	 * @param arr2 the other of the byte arrays
	 * @return the two byte arrays XORed (arr1 XOR arr2)
	 */
	private static byte[] xorArrays(byte[] arr1, byte[] arr2) {
//		if(arr1.length != arr2.length)
//			throw new IndexOutOfBoundsException("different sized arrays");
		byte[] xored = new byte[arr1.length];
		
		for(int i = 0; i < arr1.length; i++){
			xored[i] = (byte) (arr1[i] ^ arr2[i]);
		}
		
		return xored;
	}
	
	private static void printByteArray(byte[] arr){
		for(int i = 0; i < arr.length; i++){
			System.out.print(arr[i] + " ");
		}
	}
}
