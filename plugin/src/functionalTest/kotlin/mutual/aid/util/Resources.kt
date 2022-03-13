package mutual.aid.util

import java.io.BufferedReader
import java.io.InputStream

object Resources {
  @JvmStatic
  fun Any.textResource(resourcePath: String): String {
    return streamFromResource(resourcePath).bufferedReader().use(BufferedReader::readText)
  }

  @JvmStatic
  fun Any.streamFromResource(resourcePath: String): InputStream {
    return javaClass.classLoader.getResourceAsStream(resourcePath)
      ?: error("No resource at '$resourcePath'")
  }
}
