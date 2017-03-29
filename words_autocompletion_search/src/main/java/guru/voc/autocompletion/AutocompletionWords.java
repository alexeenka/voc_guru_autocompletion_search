package guru.voc.autocompletion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Singleton thread-safe class to help search string (start with) from list of string
 *
 * Created by aalexeenka on 29.03.2017.
 */
@SuppressWarnings("WeakerAccess")
public class AutocompletionWords {

  private static AutocompletionWords instance = new AutocompletionWords();

  private AutocompletionWords() {
  }

  public static AutocompletionWords getInstance() {
    return instance;
  }

  private List<String> words = new ArrayList<>();

  public void newValues(List<String> words) {
    Collections.sort(words);

    synchronized (AutocompletionWords.class) {
      this.words = Collections.unmodifiableList(words);
    }
  }

  public List<String> getWords() {
    return words;
  }

  public int getSize() {
    return words.size();
  }

  public final static int MAX_FIND_WORD = 10;

  /**
   * Binary search for part of word.
   *
   * Return sorted result.
   *
   * @param findPart findPart
   *
   * @return index of match word
   */
  public List<String> findWords(final String findPart) {
    return AutocompletionWordsUtils.findWords(words, findPart, MAX_FIND_WORD);
  }

}
