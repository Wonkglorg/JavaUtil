package com.wonkglorg.util.file;

import com.wonkglorg.util.files.FileUtils;
import com.wonkglorg.util.web.WebUtil;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static com.wonkglorg.util.console.ConsoleUtil.printr;
import static org.junit.jupiter.api.Assertions.assertEquals;

class FileTest {
	private static final Map<String, Boolean> fileMap = new HashMap<>();

	static {
		fileMap.put("https://www.google.com", false);
		fileMap.put("https://www.google.com/index.html", true);
		fileMap.put("https://www.google.com/index.htm", true);
		fileMap.put("https://cdn.jsdelivr.net/gh/twitter/twemoji@latest/assets/svg/1f62d.svg", true);
		fileMap.put("https://cdn.jsdelivr.net/gh/twitter/twemoji@latest/assets/svg/1f62d", false);


	}

	@Test
	void canIdentifyLinks() {
		for (Map.Entry<String, Boolean> entry : fileMap.entrySet()) {
			boolean result = WebUtil.doesLinkPointToFile(entry.getKey());
			assertEquals(entry.getValue(), result, "Link " + entry.getKey() + " caused an error.");
		}
	}

	@Test
	void canSanitizeFileNames() {
		String fileName = "file?name";
		String sanitized = FileUtils.sanitizeFileName(fileName);
		assertEquals("file_name", sanitized, "Sanitized file name is incorrect.");
	}

	@Test
	void downloadTextFormatted() throws InterruptedException {
		for (int i = 0; i < 10; i++) {
			printr(WebUtil.formattedProgress(i, i / 3, i / 8));
			Thread.sleep(500);
		}

	}

	@Test
	void createShortCutFile() throws MalformedURLException {

		var result = WebUtil.getUrlInfo("https://www.youtube.com");
		printr(result);

		Path path = Path.of("C:\\Users\\Wonkglorg\\Desktop");
		FileUtils.createShortCutFile(path, result.websiteName(), result.url());
	}

	@Test
	void canObtainExtensionFromValidFile() {
		String extension = FileUtils.getExtension(new File("test.exe"));

		assertEquals("exe", extension);
	}

	@Test
	void canChangeExtensionFromValidFile() {
		File file = new File("test.exe");
		String extension = FileUtils.getExtension(file);
		assertEquals("exe", extension);
		File newFile = FileUtils.changeExtension(file, "zip");
		assertEquals("zip", FileUtils.getExtension(newFile));
	}
}
