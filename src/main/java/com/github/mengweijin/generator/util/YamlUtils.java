package com.github.mengweijin.generator.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import com.github.mengweijin.generator.entity.ProjectInfo;
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
        File tempFile = FileUtil.file(ProjectInfo.TMP_DIR + file.getName());
        FileUtil.writeLines(lineCollect, tempFile, StandardCharsets.UTF_8);

        Yaml yaml = new Yaml();
        return yaml.loadAs(FileUtil.getInputStream(tempFile), type);
    }

}
