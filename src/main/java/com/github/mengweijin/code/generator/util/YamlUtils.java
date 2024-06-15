package com.github.mengweijin.code.generator.util;

import org.dromara.hutool.core.io.file.FileUtil;
import org.dromara.hutool.core.regex.ReUtil;
import org.dromara.hutool.core.text.StrUtil;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author mengweijin
 */
public class YamlUtils {

    /**
     * groupId: @project.groupId@
     */
    private static final String DELIMITER_TEMPLATE_REGEX = "@\\S+@";

    public static <T> T loadAs(File file, Class<T> type) {
        List<String> lineList = FileUtil.readLines(file, StandardCharsets.UTF_8);
        List<String> lineCollect = lineList.stream().map(line -> {
            if (ReUtil.contains(DELIMITER_TEMPLATE_REGEX, line)) {
                return StrUtil.subBefore(line, StrUtil.AT, false) + "'" + StrUtil.AT +
                        StrUtil.subAfter(line, StrUtil.AT, false) + "'";
            }
            return line;
        }).collect(Collectors.toList());
        String joined = String.join("\r\n", lineCollect);
        Yaml yaml = new Yaml();
        return yaml.loadAs(joined, type);
    }

}
