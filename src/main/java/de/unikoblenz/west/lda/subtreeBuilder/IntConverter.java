package de.unikoblenz.west.lda.subtreeBuilder;

import java.util.Map;
import java.util.HashMap;

public class IntConverter {

	// 426-bit base (10 + 2*26 + 21 + 343)
	private static char[] map = new char[] {
		'0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
		'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
		'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
		'U', 'V', 'W', 'X', 'Y', 'Z', 'a', 'b', 'c', 'd',
		'e', 'f', 'g', 'h', 'i', 'j', 'k', 'l', 'm', 'n',
		'o', 'p', 'q', 'r', 's', 't', 'u', 'v', 'w', 'x',
		'y', 'z',
		'!', '"', '§', '$', '%', '&', '[', ']', '{', '~',
		'}', '?', '#', '+', '-', ',', ';', '_', '<', '>', 
		'|', 
		'Æ', 'Ð', 'Ǝ', 'Ə', 'Ɛ', 'Ɣ', 'Ĳ', 'Ŋ', 'Œ', 'ẞ', 
		'Þ', 'Ƿ', 'Ȝ', 'æ', 'ð', 'ǝ', 'ə', 'ɛ', 'ɣ', 'ĳ', 
		'ŋ', 'œ', 'ĸ', 'ſ', 'ß', 'þ', 'ƿ', 'ȝ', 'Ą', 'Ɓ', 
		'Ç', 'Đ', 'Ɗ', 'Ę', 'Ħ', 'Į', 'Ƙ', 'Ł', 'Ø', 'Ơ', 
		'Ş', 'Ș', 'Ţ', 'Ț', 'Ŧ', 'Ų', 'Ư', 'Y', '̨', 'Ƴ', 
		'ą', 'ɓ', 'ç', 'đ', 'ɗ', 'ę', 'ħ', 'į', 'ƙ', 'ł', 
		'ø', 'ơ', 'ş', 'ș', 'ţ', 'ț', 'ŧ', 'ų', 'ư', 'y', 
		'̨', 'ƴ', 'Á', 'À', 'Â', 'Ä', 'Ǎ', 'Ă', 'Ā', 'Ã', 
		'Å', 'Ǻ', 'Ą', 'Æ', 'Ǽ', 'Ǣ', 'Ɓ', 'Ć', 'Ċ', 'Ĉ', 
		'Č', 'Ç', 'Ď', 'Ḍ', 'Đ', 'Ɗ', 'Ð', 'É', 'È', 'Ė', 
		'Ê', 'Ë', 'Ě', 'Ĕ', 'Ē', 'Ę', 'Ẹ', 'Ǝ', 'Ə', 'Ɛ', 
		'Ġ', 'Ĝ', 'Ǧ', 'Ğ', 'Ģ', 'Ɣ', 'á', 'à', 'â', 'ä', 
		'ǎ', 'ă', 'ā', 'ã', 'å', 'ǻ', 'ą', 'æ', 'ǽ', 'ǣ', 
		'ɓ', 'ć', 'ċ', 'ĉ', 'č', 'ç', 'ď', 'ḍ', 'đ', 'ɗ', 
		'ð', 'é', 'è', 'ė', 'ê', 'ë', 'ě', 'ĕ', 'ē', 'ę', 
		'ẹ', 'ǝ', 'ə', 'ɛ', 'ġ', 'ĝ', 'ǧ', 'ğ', 'ģ', 'ɣ', 
		'Ĥ', 'Ḥ', 'Ħ', 'I', 'Í', 'Ì', 'İ', 'Î', 'Ï', 'Ǐ', 
		'Ĭ', 'Ī', 'Ĩ', 'Į', 'Ị', 'Ĳ', 'Ĵ', 'Ķ', 'Ƙ', 'Ĺ', 
		'Ļ', 'Ł', 'Ľ', 'Ŀ', 'ʼ', 'N', 'Ń', 'N', '̈', 'Ň', 
		'Ñ', 'Ņ', 'Ŋ', 'Ó', 'Ò', 'Ô', 'Ö', 'Ǒ', 'Ŏ', 'Ō', 
		'Õ', 'Ő', 'Ọ', 'Ø', 'Ǿ', 'Ơ', 'Œ', 'ĥ', 'ḥ', 'ħ', 
		'ı', 'í', 'ì', 'i', 'î', 'ï', 'ǐ', 'ĭ', 'ī', 'ĩ', 
		'į', 'ị', 'ĳ', 'ĵ', 'ķ', 'ƙ', 'ĸ', 'ĺ', 'ļ', 'ł', 
		'ľ', 'ŀ', 'ŉ', 'ń', 'n', '̈', 'ň', 'ñ', 'ņ', 'ŋ', 
		'ó', 'ò', 'ô', 'ö', 'ǒ', 'ŏ', 'ō', 'õ', 'ő', 'ọ', 
		'ø', 'ǿ', 'ơ', 'œ', 'Ŕ', 'Ř', 'Ŗ', 'Ś', 'Ŝ', 'Š', 
		'Ş', 'Ș', 'Ṣ', 'ẞ', 'Ť', 'Ţ', 'Ṭ', 'Ŧ', 'Þ', 'Ú', 
		'Ù', 'Û', 'Ü', 'Ǔ', 'Ŭ', 'Ū', 'Ũ', 'Ű', 'Ů', 'Ų', 
		'Ụ', 'Ư', 'Ẃ', 'Ẁ', 'Ŵ', 'Ẅ', 'Ƿ', 'Ý', 'Ỳ', 'Ŷ', 
		'Ÿ', 'Ȳ', 'Ỹ', 'Ƴ', 'Ź', 'Ż', 'Ž', 'Ẓ', 'ŕ', 'ř', 
		'ŗ', 'ſ', 'ś', 'ŝ', 'š', 'ş', 'ș', 'ṣ', 'ß', 'ť', 
		'ţ', 'ṭ', 'ŧ', 'þ', 'ú', 'ù', 'û', 'ü', 'ǔ', 'ŭ', 
		'ū', 'ũ', 'ű', 'ů', 'ų', 'ụ', 'ư', 'ẃ', 'ẁ', 'ŵ', 
		'ẅ', 'ƿ', 'ý', 'ỳ', 'ŷ', 'ÿ', 'ȳ', 'ỹ', 'ƴ', 'ź', 
		'ż', 'ž', 'ẓ'
	};
	static Map<Character, Integer> mapReversed;
	public static void initMapReversed() {
		mapReversed = new HashMap<>();
		for (int i = 0; i < map.length; i++)
			mapReversed.put(map[i], i);
	}
	
	public static String encode(int i) {
		if (i < 0)
			return null; // not implemented for negative numbers
		String res = "";
		if (i == 0)
			return "" + map[0];
		long l = i;
		while (l > 0) { // encoded least-to-most significant
			int val = (int)(l % map.length);
			l = l / map.length;
			res += map[val];
		}
		return res;
	}

	public static int decode(String s) {
		if (mapReversed == null)
			initMapReversed();
		int res = 0;
		for (int i = s.length() - 1; i >= 0; i--) { // go in reverse to mirror encoding
			char ch = s.charAt(i);
			int val = mapReversed.get(ch);
			res = (res * map.length) + val;
		}
		return res;
	}

	public static void main(String[] args) {
		int[] test = new int[] {1, 12, 123, 1234, 12345, 123456, 1234567, 12345678, 123456789, 1000000000, 2147483647};
		for (int t: test) {
			String encoded = encode(t);
			int decoded = decode(encoded);
			System.out.println("Value: " + t + ", Encoded: " + encoded);// + ", Decoded: " + decoded);
			if (t != decoded)
				System.out.println("Failed decoding! Decoded: " + decoded);
		}
	}
}
