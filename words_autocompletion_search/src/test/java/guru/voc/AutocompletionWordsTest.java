package guru.voc;

import org.junit.Before;
import org.junit.Test;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ThreadLocalRandom;

import guru.voc.autocompletion.AutocompletionWords;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.core.StringStartsWith.startsWith;

/**
 * Test for autocompletion functionality.
 *
 * Created by aalexeenka on 29.03.2017.
 */
@SuppressWarnings("WeakerAccess")
public class AutocompletionWordsTest {

  private List<String> words = new ArrayList<>();

  @Before
  public void initTestData() {
    for (int i = 0; i < 10000; i++) {
      StringBuilder word = new StringBuilder();
      for (int j = 0, n = randomLength(); j < n; j++) {
        word.append(randomLetter());
      }
      words.add(word.toString());
    }

  }

  public static char randomLetter() {
    return (char) ThreadLocalRandom.current().nextInt('A', 'Z' + 1);
  }

  public static int randomLength() {
    return (char) ThreadLocalRandom.current().nextInt(4, 10);
  }

  public static int randomIndex(int count) {
    return (char) ThreadLocalRandom.current().nextInt(count);
  }


  @Test
  public void checkBaseGlobalVocFunctions() throws ExecutionException, InterruptedException {

    final AutocompletionWords instance = AutocompletionWords.getInstance();
    instance.newValues(words);
    final List<String> words = instance.getWords();

    // check work of search algorithm, for all words full search
    {
      final Instant startTime = Instant.now();
      for (int i = 0; i < words.size(); i++) {
        final String searchWord = words.get(randomIndex(words.size()));
        assertWord(instance, searchWord);
      }
      final Instant finishTime = Instant.now();
      System.out.println("Iterate all words, size: " + words.size() + ". Duration: " + Duration
              .between(startTime, finishTime).toMillis() + "ms");
    }

    // check 2 letter word search
    {
      final Instant startTime = Instant.now();
      for (int i = 0; i < words.size(); i++) {
        final String searchWord = words.get(randomIndex(words.size())).substring(0, 2);
        List<String> search = instance.findWords(searchWord);

        assertWord(instance, searchWord);
      }
      final Instant finishTime = Instant.now();
      System.out.println("Iterate 2 letter words. Duration: " + Duration.between(
              startTime,
              finishTime).toMillis() + "ms");
    }

    // check 3 letter word search
    {
      final Instant startTime = Instant.now();
      for (int i = 0; i < words.size(); i++) {
        final String searchWord = words.get(randomIndex(words.size())).substring(0, 3);
        List<String> search = instance.findWords(searchWord);

        assertWord(instance, searchWord);
      }
      final Instant finishTime = Instant.now();
      System.out.println("Iterate 3 letter words. Duration: " + Duration.between(
              startTime,
              finishTime).toMillis() + "ms");
    }

    // check alphabet
    {
      final Instant startTime = Instant.now();
      for (char alphabet = 'A'; alphabet <= 'Z'; alphabet++) {
        assertWord(instance, Character.toString(alphabet));
      }
      final Instant finishTime = Instant.now();
      System.out.println("Iterate alphabet: " + ('Z' - 'A' + 1) + ". Duration: " + Duration.between(
              startTime,
              finishTime).toMillis() + "ms");
    }

  }

  private void assertWord(AutocompletionWords instance, String searchWord) {
    final List<String> search = instance.findWords(searchWord);
    for (String iWord : search) {
      assertThat(iWord, startsWith(searchWord));
    }
    isNaturalOrdering(search);
  }

  private void isNaturalOrdering(List<String> search) {
    List<String> expected = new ArrayList<>(search);
    Collections.sort(expected);
    assertThat(search, contains(expected.toArray()));
  }

}
