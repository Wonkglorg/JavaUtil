package com.wonkglorg.util.file;

import com.wonkglorg.util.files.FileUtils;
import com.wonkglorg.util.web.WebUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class FileTest {
    private static final Map<String, Boolean> fileMap = new HashMap<>();

    static {
        fileMap.put("https://www.google.com", false);
        fileMap.put("https://www.google.com/index.html", false);
        fileMap.put("https://www.google.com/index.htm", false);
        fileMap.put("https://cdn.jsdelivr.net/gh/twitter/twemoji@latest/assets/svg/1f62d.svg", true);
        fileMap.put("https://cdn.jsdelivr.net/gh/twitter/twemoji@latest/assets/svg/1f62d", false);


    }

    @Test
    public void canIdentifyLinks() {


        for (Map.Entry<String, Boolean> entry : fileMap.entrySet()) {
            boolean result = WebUtil.doesLinkPointToFile(entry.getKey());
            Assertions.assertEquals(entry.getValue(), result, "Link " + entry.getKey() + " caused an error.");
        }
    }

    @Test
    public void canSanitizeFileNames() {
        String fileName = "file?name";
        String sanitized = FileUtils.sanitizeFileName(fileName);
        Assertions.assertEquals("file_name", sanitized, "Sanitized file name is incorrect.");
    }
}
