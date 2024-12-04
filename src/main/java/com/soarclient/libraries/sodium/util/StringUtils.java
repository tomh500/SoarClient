package com.soarclient.libraries.sodium.util;

import com.google.common.collect.UnmodifiableIterator;
import com.soarclient.libraries.sodium.client.gui.options.Option;
import com.soarclient.libraries.sodium.client.gui.options.OptionPage;

import java.util.ArrayList;
import java.util.List;

public class StringUtils {
	public static int levenshteinDistance(String s1, String s2) {
		int m = s1.length();
		int n = s2.length();
		int[][] dp = new int[m + 1][n + 1];
		int i = 0;

		while (i <= m) {
			dp[i][0] = i++;
		}

		i = 0;

		while (i <= n) {
			dp[0][i] = i++;
		}

		for (int ix = 1; ix <= m; ix++) {
			for (int j = 1; j <= n; j++) {
				int cost = s1.charAt(ix - 1) == s2.charAt(j - 1) ? 0 : 1;
				dp[ix][j] = Math.min(dp[ix - 1][j] + 1, Math.min(dp[ix][j - 1] + 1, dp[ix - 1][j - 1] + cost));
			}
		}

		return dp[m][n];
	}

	public static List<Option<?>> fuzzySearch(List<OptionPage> pages, String userInput, int maxDistance) {
		List<Option<?>> result = new ArrayList();
		String[] targetWords = userInput.toLowerCase().split("\\s+");

		for (OptionPage page : pages) {
			UnmodifiableIterator var7 = page.getOptions().iterator();

			while (var7.hasNext()) {
				Option<?> option = (Option<?>) var7.next();
				String sentence = option.getName().toLowerCase();
				boolean containsAllWords = true;

				for (String word : targetWords) {
					boolean containsWord = false;

					for (String sentenceWord : sentence.toLowerCase().split("\\s+")) {
						int distance = levenshteinDistance(word, sentenceWord);
						if (distance <= maxDistance) {
							containsWord = true;
							break;
						}

						if (sentenceWord.startsWith(word)) {
							containsWord = true;
							break;
						}
					}

					if (!containsWord) {
						containsAllWords = false;
						break;
					}
				}

				if (containsAllWords) {
					result.add(option);
				}
			}
		}

		return result;
	}
}
