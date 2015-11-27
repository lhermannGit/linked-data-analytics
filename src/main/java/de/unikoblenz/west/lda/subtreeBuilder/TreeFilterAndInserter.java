package de.unikoblenz.west.lda.subtreeBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TreeFilterAndInserter {

	static List<String> queriedPaths = new ArrayList<String>();
	static String parent = "";
	static String child = "";

	public void query(String path) {

		if (!path.contains("/")) {
			System.out.println("\n- Basisfall");

			for (String queried : queriedPaths) {
				String[] split = {};
				//String[] split = splitByPoint(queried);
				for (String splitQuery : split) {
					Matcher matcher = Pattern.compile("^" + "\\(*" + Pattern.quote(parent) + ".*").matcher(splitQuery);
					while (matcher.find()) {
						int pos = matcher.start() + parent.length();
						addChild(queried, child, pos);
						System.out.printf("-- %s -- %s an Position [%d,%d], Kind zum hinzufügen: %s%n", queried,
								matcher.group(), matcher.start(), matcher.end(), child);
					}
				}
			}
			System.out.println("- Ende");
		} else {
			System.out.println("-- Rekursionsfall");

			if (parent.isEmpty()) {
				child = path.substring(path.lastIndexOf("/") + 1);
				parent = path.substring(0, path.lastIndexOf("/"));
			}
			path = path.substring(0, path.lastIndexOf("/"));

			for (String queried : queriedPaths) {
				Matcher matcher = Pattern.compile(path + ".*").matcher(queried);
				while (matcher.find()) {
					System.out.printf("%n -- %s -- %s an Position [%d,%d], Kind zum hinzufügen: %s%n", queried,
							matcher.group(), matcher.start(), matcher.end(), child);
					int pos = matcher.start() + parent.length();
					addChild(queried, child, pos);
				}
			}
			query(path);
		}

	}

	private void addChild(String queried, String child, int pos) {
		System.out.print("--- AddChild started");
		String part1 = "";
		String part2 = "";
		String res = "";

		System.out.print(" --- Char at index "+pos+": "+queried.charAt(pos));
		switch (queried.charAt(pos)) {
		case '/':
			System.out.print(" --- Case '/'");
			part1 = queried.substring(0, pos + 2);
			part2 = queried.substring(pos + 2);
			res = part1 + "." + child + part2;

			break;

		case ' ':
			System.out.print(" --- Case ' '");
			part1 = queried.substring(0, pos);
			part2 = queried.substring(pos);
			res = part1 + "/" + child + part2;

			break;

		case ')':
			System.out.print(" --- Case ')'");
			part1 = queried.substring(0, pos);
			part2 = queried.substring(pos);
			res = part1 + "/" + child + part2;

			break;

		case '.':
			System.out.print(" --- Case '.'");
			part1 = queried.substring(0, pos);
			part2 = queried.substring(pos);
			res = part1 + "." + child + part2;

			break;

		/*case '(':

			System.out.println("---- Case '('");
			addChild(queried, child, index+1);

			break;*/

		default:
			System.out.println(" --- Default Case");
			/*part1 = queried.substring(0, index);
			part2 = queried.substring(index);
			res = part1 + child + "." + part2;*/
			addChild(queried, child, pos+1);
			break;

		}
		System.out.println(" --- Added: " + res);
		System.out.print(" --- AddChild finished");
	}

	public String[] splitByPoint(String queried) {
		System.out.println("-- Split started");

		List<Integer> dotIndices = new ArrayList<Integer>();
		int paranthesesOpenCnt = 0;
		int paranthesesCloseCnt = 0;

		char c;
		for (int i = 0; i <= queried.length() - 1; i++) {
			c = queried.charAt(i);
			// System.out.println("Current: " + queried.substring(i));
			if (paranthesesOpenCnt != 0 && paranthesesCloseCnt != 0 && paranthesesOpenCnt == paranthesesCloseCnt) {
				dotIndices.add(i);
				paranthesesCloseCnt = paranthesesOpenCnt = 0;
			} else if (c == '(' && c != '.')
				paranthesesOpenCnt++;
			else if (c == ')' && c != '.')
				paranthesesCloseCnt++;
		}

		int dotSize = dotIndices.size();
		String[] split = new String[dotSize + 1]; // one more path than dots
		int startIndex = 0;
		int currentIndex = 0;

		for (int i = 0; i <= dotSize; i++) {
			if (i != dotSize) {
				currentIndex = dotIndices.get(i);
				split[i] = queried.substring(startIndex, currentIndex);
				startIndex = currentIndex + 1;
			}

			else // add last path
				split[i] = queried.substring(startIndex);

			// System.out.println("Split[" + i + "]= " + split[i]);
		}
		System.out.println("-- Split finished");
		return split;
	}

	public static void main(String[] args) {
		String path = "2/2/2/2";
		queriedPaths.add("(1/1).2((1.(2/1))).3(1.2)");
		queriedPaths.add("1/1/1");
		queriedPaths.add("2((1.(2/1.2.3.4))).3(1/1/1/1)");
		queriedPaths.add("(1/1)");
		TreeFilterAndInserter tf = new TreeFilterAndInserter();
		tf.query(path);
	}
}
