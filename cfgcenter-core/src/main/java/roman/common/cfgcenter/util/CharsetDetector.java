package roman.common.cfgcenter.util;

import org.mozilla.intl.chardet.nsDetector;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class CharsetDetector {

    /**
     * 获取编码
     * @throws IOException
     */
    public static String detectCharset(byte[] bytes) throws IOException {
        return detectCharset(new ByteArrayInputStream(bytes));
    }

    /**
     * 获取编码
     * @throws IOException
     */
    public static String detectCharset(InputStream is) throws IOException {
        nsDetector detector = new nsDetector();
        byte[] buf = new byte[1024];
        int len;
        while ((len = is.read(buf, 0, buf.length)) != -1) {
            // Check if the stream is only ascii.
            if(detector.isAscii(buf, len))
                return "ASCII";
            // DoIt if non-ascii and not done yet.
            if(detector.DoIt(buf, len, false)){
                String[] probableCharsets = detector.getProbableCharsets();
                return probableCharsets[0];
            }
        }
        return null;
    }
}
