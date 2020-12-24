import cn.hutool.core.io.FileUtil;
import cn.hutool.core.lang.JarClassLoader;
import org.beetl.core.Configuration;
import org.beetl.core.GroupTemplate;
import org.beetl.core.Template;
import org.beetl.core.resource.StringTemplateResourceLoader;

import java.io.File;
import java.io.IOException;

public class BeetlTest {

    public static void main(String[] args) {
        File file = FileUtil.file(
                "C:\\Users\\admin\\.m2\\repository\\org\\apache\\tomcat\\embed\\tomcat-embed-core\\9.0.41\\tomcat-embed-core-9.0.41.jar");
        JarClassLoader jarClassLoader = JarClassLoader.loadJar(file);
        Thread.currentThread().setContextClassLoader(jarClassLoader);
        // 1. do business use JarClassLoader

        // 2. render beetl template
        executeBeetl();
    }

    public static void executeBeetl(){
        try {
            // 这里出错：TAG.incdlueJSP= org.beetl.ext.jsp.IncludeJSPTag,javax.servlet.http.HttpServletRequest
            Configuration cfg = Configuration.defaultConfiguration();

            GroupTemplate groupTemplate = new GroupTemplate(new StringTemplateResourceLoader(), cfg);
            Template template = groupTemplate.getTemplate("Print name=AAAAA");
            System.out.println(template.render());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
