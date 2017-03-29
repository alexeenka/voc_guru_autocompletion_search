package guru.voc.autocompletion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Utility class to search through list of string. <br>
 * Complexity - log N. <br>
 *
 * List of words must be sorted with: <br>
 *
 * <pre>
 *   Collections.sort(string);
 * </pre>
 *
 *
 * Created by aalexeenka on 29.03.2017.
 */
@SuppressWarnings("WeakerAccess")
public final class AutocompletionWordsUtils {
  private AutocompletionWordsUtils() {
  }

  /**
   * Binary search for part of word.
   *
   * Return sorted result.
   *
   * @param findPart findPart
   * @return index of match word
   */
  public static List<String> findWords(final List<String> words,
                                       final String findPart,
                                       final int maxWordsCount) {
    if (findPart == null || findPart.trim().length() == 0) return new ArrayList<>();
    int findPartLength = findPart.length();

    int low = 0;
    int high = words.size() - 1;

    int resultIndex = -1;
    while (low <= high) {
      int mid = (low + high) / 2;
      String midVal = words.get(mid);

      String iPart = midVal.substring(0, Math.min(findPartLength, midVal.length()));
      int cmp = iPart.compareTo(findPart);

      if (cmp < 0) {
        low = mid + 1;
      } else if (cmp > 0) {
        high = mid - 1;
      } else {
        resultIndex = mid; // key found
        break;
      }
    }

    if (resultIndex == -1) {
      return new ArrayList<>();
    }

    // find other words
    List<String> bR = new ArrayList<>();
    List<String> fR = new ArrayList<>();
    bR.add(words.get(resultIndex));

    int backward = resultIndex - 1;
    int forward = resultIndex + 1;
    while (backward >= 0 || forward < words.size()) {
      if (backward >= 0) {
        final String backwardWord = words.get(backward);
        if (backwardWord.length() < findPartLength) {
          backward = -1;
        } else {
          final String wordPart = backwardWord.substring(0, findPartLength);
          if (findPart.equals(wordPart)) {
            bR.add(backwardWord);
            if ((bR.size() + fR.size()) == maxWordsCount) break;
            backward--;
          } else {
            backward = -1;
          }
        }
      }
      if (forward < words.size()) {
        final String forwardWord = words.get(forward);
        if (forwardWord.length() < findPartLength) {
          forward = words.size();
        } else {
          final String wordPart = forwardWord.substring(0, findPartLength);
          if (findPart.equals(wordPart)) {
            fR.add(forwardWord);
            if ((bR.size() + fR.size()) == maxWordsCount) break;
            forward++;
          } else {
            forward = words.size();
          }
        }
      }
    }
    // Merge two list
    Collections.reverse(bR);
    bR.addAll(fR);
    return bR;
  }

}
