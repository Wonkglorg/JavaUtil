package com.wonkglorg.util.file;

import com.wonkglorg.util.files.FileUtils;
import com.wonkglorg.util.web.WebUtil;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static com.wonkglorg.util.console.ConsoleUtil.printr;

public class FileTest {
    private static final Map<String, Boolean> fileMap = new HashMap<>();

    static {
        fileMap.put("https://www.google.com", false);
        fileMap.put("https://www.google.com/index.html", true);
        fileMap.put("https://www.google.com/index.htm", true);
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

    @Test
    public void downloadTextFormatted() throws InterruptedException {
        for (int i = 0; i < 10; i++) {
            printr(WebUtil.formattedProgress(i, i / 3, i / 8));
            Thread.sleep(500);
        }

    }

    @Test
    public void createShortCutFile() throws MalformedURLException {

        var result = WebUtil.getUrlInfo("https://www.youtube.com");
        printr(result);

        Path path = Path.of("C:\\Users\\Wonkglorg\\Desktop");
        FileUtils.createShortCutFile(path, result.websiteName(), result.url());
    }
}
