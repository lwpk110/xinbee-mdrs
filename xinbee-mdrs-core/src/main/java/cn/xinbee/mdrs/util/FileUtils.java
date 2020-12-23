package cn.xinbee.mdrs.util;

import org.springframework.util.Assert;

public class FileUtils {

		public static String getNameFromFileName(String fileName) {
				Assert.hasText(fileName, "file name can't be null");
				return fileName.substring(0, fileName.lastIndexOf("."));
		}

}
