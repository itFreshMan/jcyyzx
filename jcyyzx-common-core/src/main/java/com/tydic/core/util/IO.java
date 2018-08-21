package com.tydic.core.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.nio.charset.Charset;

import static com.tydic.core.util.Strings.EMPTY;
import static com.tydic.core.util.Strings.INSTANTIATION_PROHIBITED;
import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * Utility to convert a <code>java.io.InputStream</code> into a byte array,
 * or into a <code>java.lang.String</code> using specific charset encoding.
 *
 * @author Guang YANG
 * @version 1.0
 */
public final class IO {

  private static final Logger LOGGER = LoggerFactory.getLogger(IO.class);

  private IO() {
    throw new AssertionError(INSTANTIATION_PROHIBITED);
  }

  /**
   * Convert a <code>java.io.InputStream</code> into a <code>java.lang.String</code> using UTF-8.<br/>
   * If it fails to convert, or giving InputStream is <code>null</code>, an empty String will be returned.<br/>
   * Once converted, the InputStream is consumed and cannot be read again.
   */
  public static String stringify(InputStream in) {
    return IO.stringify(in, UTF_8);
  }

  /**
   * Convert a <code>java.io.InputStream</code> into a <code>java.lang.String</code> using giving charset encoding.<br/>
   * If it fails to convert, or either giving InputStream or encoding name is <code>null</code>, an empty String will be returned.<br/>
   * Once converted, the InputStream is consumed and cannot be read again.
   */
  public static String stringify(InputStream in, String encoding) {
    if (in == null) {
      return EMPTY;
    }
    try {
      return IO.stringify(in, Charset.forName(encoding));
    } catch (Exception e) {
      LOGGER.debug(e.getMessage());
      return EMPTY;
    }
  }

  /**
   * Convert a <code>java.io.InputStream</code> into a <code>java.lang.String</code> using giving charset encoding.<br/>
   * If it fails to convert, or either giving InputStream or charset encoding is <code>null</code>, an empty String will be returned.<br/>
   * Once converted, the InputStream is consumed and cannot be read again.
   */
  public static String stringify(InputStream in, Charset encoding) {
    if (in == null) {
      return EMPTY;
    }
    try {
      return new String(IO.read(in), encoding);
    } catch (Exception e) {
      LOGGER.debug(e.getMessage());
      return EMPTY;
    }
  }

  /**
   * Convert a <code>java.io.InputStream</code> into a byte array.<br/>
   * If it fails to convert, or either giving InputStream is <code>null</code>, a byte array with a length equals 0 will be returned.<br/>
   * Once converted, the InputStream is consumed and cannot be read again.
   */
  public static byte[] read(InputStream in) {
    try {
      ByteArrayOutputStream buffer = new ByteArrayOutputStream();
      int nRead;
      byte[] data = new byte[1024];
      while ((nRead = in.read(data, 0, data.length)) != -1) {
        buffer.write(data, 0, nRead);
      }
      buffer.flush();
      return buffer.toByteArray();
    } catch (Exception e) {
      LOGGER.debug(e.getMessage());
      return new byte[0];
    }
  }

}
