package de.unikoblenz.west.lda.input;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.GZIPInputStream;

public class Reader {

	// static Pattern regex = Pattern.compile("m/('.*?'|\".*?\"|\\S+)/g");
	static Pattern regex = Pattern.compile("[^\\s\"']+|\"([^\"]*)\"|'([^']*)'");

	public static void main(String[] args) throws FileNotFoundException, IOException {

		InputReduction reduce = new InputReduction(2000, 1000);

		// String dir =
		// "C://Users//Lukas//workspace//Java//DataMining//ReduceMap//";

		String file = "C://Users//Lukas//workspace//Java//DataMining//ReduceMap//data.nq-0.gz";

		BufferedReader in = new BufferedReader(new InputStreamReader(new GZIPInputStream(new FileInputStream(file))));

		String[] content;
		String line;
		int lines = 0;

		while ((line = in.readLine()) != null) {
			List<String> matchList = new ArrayList<String>();
			Matcher regexMatcher = regex.matcher(line);
			// line = Unescaper.unescape_perl_string(line);
			// content = line.split("\\s+");
			while (regexMatcher.find()) {
				if (regexMatcher.group(1) != null) {
					// Add double-quoted string without the quotes
					matchList.add(regexMatcher.group(1).replace("\n", "").trim());
				} else if (regexMatcher.group(2) != null) {
					// Add single-quoted string without the quotes
					matchList.add(regexMatcher.group(2).replace("\n", "").trim());
				} else {
					// Add unquoted word
					matchList.add(regexMatcher.group().replace("\n", "").trim());
				}
			}
			if (lines > 1000)
				break;
			System.out.println(matchList.get(0));
			System.out.println(matchList.get(1));
			System.out.println(matchList.get(2));
			reduce.consume(matchList.get(0), matchList.get(1), matchList.get(2));
			lines++;
		}

		/*
		 * TIntArrayList res = reduce.getReducedInput(); int i = 0; while (i <
		 * 40) { System.out.print(res.get(i) + " "); System.out.print(res.get(i
		 * + 1) + " "); System.out.print(res.get(i + 2) + " ");
		 * System.out.print(res.get(i + 3) + " "); System.out.println(); i = i +
		 * 4; }
		 */

		for (DisjointSet set : reduce.getSets()) {
		}
		System.out.println(reduce.getSets().size());

		reduce.close();
		in.close();

	}
}
