package ee.smkv.scheduler.utils;

import java.util.Arrays;
import java.util.List;

public class StringUtils {

  public static final String LINE_SEPARATOR = System.getProperty("line.separator");

  public static List<String> splitLines(String source){
    return Arrays.asList(source.split(LINE_SEPARATOR));
  }
}
