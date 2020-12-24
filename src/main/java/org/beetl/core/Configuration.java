/*
 [The "BSD license"]
 Copyright (c) 2011-2020  闲大赋 (李家智)
 All rights reserved.

 Redistribution and use in source and binary forms, with or without
 modification, are permitted provided that the following conditions
 are met:
 1. Redistributions of source code must retain the above copyright
     notice, this list of conditions and the following disclaimer.
 2. Redistributions in binary form must reproduce the above copyright
     notice, this list of conditions and the following disclaimer in the
     documentation and/or other materials provided with the distribution.
 3. The name of the author may not be used to endorse or promote products
     derived from this software without specific prior written permission.

 THIS SOFTWARE IS PROVIDED BY THE AUTHOR ``AS IS'' AND ANY EXPRESS OR
 IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES
 OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED.
 IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY DIRECT, INDIRECT,
 INCIDENTAL, SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT
 NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,
 DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY
 THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
 (INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF
 THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package org.beetl.core;

import org.beetl.android.text.TextUtils;
import org.beetl.core.text.HtmlTagConfig;
import org.beetl.core.text.PlaceHolderDelimeter;
import org.beetl.core.text.ScriptDelimeter;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

/**
 * 模板配置，核心文件之一
 *
 * @author xiandafu
 */
public class Configuration {

    /** 模板字符集 */
    String charset = "UTF-8";
    /** 模板占位起始符号 */
    String placeholderStart = "${";
    /** 模板占位结束符号 */
    String placeholderEnd = "}";

    String placeholderStart2 = null;
    /** 模板占位结束符号 */
    String placeholderEnd2 = null;

    /** 控制语句起始符号 */
    String statementStart = "<%";
    /** 控制语句结束符号 */
    String statementEnd = "%>";

    /** 控制语句起始符号 */
    String statementStart2 = null;
    /** 控制语句结束符号 */
    String statementEnd2 = null;

    /** html tag 标示符号 */
    String htmlTagFlag = "#";
    /** 是否允许html tag，在web编程中，有可能用到html tag，最好允许 */
    boolean isHtmlTagSupport = false;
    /** HTML标签开始符号 */
    String htmlTagStart;
    /** HTML标签结束符号 */
    String htmlTagEnd;

    /** 是否允许直接调用class */
    boolean nativeCall = false;
    /** 输出模式，默认是字符集输出，改成byte输出提高性能 */
    boolean directByteOutput = false;
    /** 严格mvc应用，只有变态的的人才打开此选项 */
    boolean isStrict = false;

    /** 是否忽略客户端的网络异常 */
    boolean isIgnoreClientIOError = true;

    /** 错误处理类 */
    String errorHandlerClass = "org.beetl.core.ConsoleErrorHandler";

    /** html 绑定的属性，如&lt;aa var="customer"> */
    String htmlTagBindingAttribute = "var";
    /** {@link org.beetl.core.text.DefaultAttributeNameConvert} */
    String htmlTagAttributeConvert = "org.beetl.core.text.DefaultAttributeNameConvert";

    /** 类搜索的包名列表 */
    Set<String> pkgList = new HashSet<>();

    /** 渲染web 前执行的代码，需要实现WebRenderExt接口，如果为空，则不做操作 */
    String webAppExt = null;

    /** html方法和html标签是否使用特殊的定界符，如模板使用简介的@和回车,html 标签和html tag使用<%%> */
    boolean hasFunctionLimiter = false;
    String functionLimiterStart = null;
    String functionLimiterEnd = null;

    // 关于引擎的设置

    /** {@code String engine = "org.beetl.core.DefaultTemplateEngine";} */
    String engine = "org.beetl.core.FastRuntimeEngine";
    String nativeSecurity = "org.beetl.core.DefaultNativeSecurityManager";
    String resourceLoader = "org.beetl.core.resource.ClasspathResourceLoader";

    // 扩展资源
    Map<String, String> fnMap = new HashMap<>();
    Map<String, String> fnPkgMap = new HashMap<>();

    Map<String, String> formatMap = new HashMap<>();
    Map<String, String> defaultFormatMap = new HashMap<>(0);
    Set<String> generalVirtualAttributeSet = new HashSet<>();
    Map<String, String> virtualClass = new HashMap<>();
    Map<String, String> tagFactoryMap = new HashMap<>();
    Map<String, String> tagMap = new HashMap<>();
    /** 资源loader配置 */
    Map<String, String> resourceMap = new HashMap<>();

    /** 缓冲数组长度不能小于 256 */
    private static final int BUFFER_MIN_SIZE = 256;
    /** 缓冲数组 */
    int bufferSize = 4096;
    int bufferNum = 64;

    /** 模板是否整体使用安全输出功能，如果是，则不存在的值返回空，而不是报错 */
    boolean safeOutput = false;

    public static final String DELIMITER_PLACEHOLDER_START = "DELIMITER_PLACEHOLDER_START";
    public static final String DELIMITER_PLACEHOLDER_END = "DELIMITER_PLACEHOLDER_END";
    public static final String DELIMITER_STATEMENT_START = "DELIMITER_STATEMENT_START";
    public static final String DELIMITER_STATEMENT_END = "DELIMITER_STATEMENT_END";
    public static final String DELIMITER_PLACEHOLDER_START2 = "DELIMITER_PLACEHOLDER_START2";
    public static final String DELIMITER_PLACEHOLDER_END2 = "DELIMITER_PLACEHOLDER_END2";
    public static final String DELIMITER_STATEMENT_START2 = "DELIMITER_STATEMENT_START2";
    public static final String DELIMITER_STATEMENT_END2 = "DELIMITER_STATEMENT_END2";
    public static final String NATIVE_CALL = "NATIVE_CALL";
    public static final String IGNORE_CLIENT_IO_ERROR = "IGNORE_CLIENT_IO_ERROR";
    public static final String DIRECT_BYTE_OUTPUT = "DIRECT_BYTE_OUTPUT";
    public static final String TEMPLATE_ROOT = "TEMPLATE_ROOT";
    public static final String TEMPLATE_CHARSET = "TEMPLATE_CHARSET";
    public static final String ERROR_HANDLER = "ERROR_HANDLER";
    public static final String MVC_STRICT = "MVC_STRICT";
    public static final String WEBAPP_EXT = "WEBAPP_EXT";
    public static final String HTML_TAG_SUPPORT = "HTML_TAG_SUPPORT";
    public static final String HTML_TAG_FLAG = "HTML_TAG_FLAG";
    public static final String HTML_TAG_ATTR_CONVERT = "HTML_TAG_ATTR_CONVERT";
    public static final String IMPORT_PACKAGE = "IMPORT_PACKAGE";
    public static final String ENGINE = "ENGINE";
    public static final String NATIVE_SECUARTY_MANAGER = "NATIVE_SECUARTY_MANAGER";
    public static final String RESOURCE_LOADER = "RESOURCE_LOADER";
    public static final String HTML_TAG_BINDING_ATTRIBUTE = "HTML_TAG_BINDING_ATTRIBUTE";
    public static final String BUFFER_SIZE = "buffer.maxSize";
    public static final String BUFFER_NUM = "buffer.num";
    public static final String SAFE_OUTPUT = "SAFE_OUTPUT";

    @Retention(RetentionPolicy.SOURCE)
    public @interface PropertiesKey {
    }

    /** 配置文件 */
    Properties ps = null;

    DelimeterHolder pd = null;
    DelimeterHolder sd = null;
    HtmlTagHolder tagConf = null;

    ClassLoader classLoader = Thread.currentThread().getContextClassLoader() != null
            ? Thread.currentThread().getContextClassLoader()
            : Configuration.class.getClassLoader();

    /**
     * 创建一个新的 Configuration 实例
     *
     * @return 一个新的 Configuration 实例
     * @throws IOException 配置文件不存在等情况
     */
    public static Configuration defaultConfiguration() throws IOException {
        return new Configuration();
    }

    /**
     * 构造方法
     *
     * @throws IOException 文件不存在等情况
     */
    public Configuration() throws IOException {
        initDefault();
    }

    public Configuration(ClassLoader classLoader) throws IOException {
        this.classLoader = classLoader;
        initDefault();
    }

    /**
     * 构造方法
     *
     * @param ps 配置文件
     * @throws IOException 文件不存在等情况
     */
    public Configuration(Properties ps) throws IOException {
        initDefault();
        parseProperties(ps);
    }

    public void build() {
        buildDelimeter();
    }

    private void buildDelimeter() {
        if (TextUtils.isBlank(placeholderStart) || TextUtils.isBlank(placeholderEnd)) {
            throw new IllegalArgumentException("占位符不能为空");
        }

        if (this.placeholderStart2 != null) {
            if (TextUtils.isBlank(placeholderStart2) || TextUtils.isBlank(placeholderEnd2)) {
                throw new IllegalArgumentException("定义了2对占位符配置，但占位符2不能为空");
            }
            pd = new DelimeterHolder(placeholderStart.toCharArray(), placeholderEnd.toCharArray(),
                    placeholderStart2.toCharArray(), placeholderEnd2.toCharArray());
        } else {
            pd = new DelimeterHolder(placeholderStart.toCharArray(), placeholderEnd.toCharArray());
        }

        if (TextUtils.isBlank(statementStart)) {
            throw new IllegalArgumentException("定界符起始符号不能为空");
        }
        if (this.statementStart2 != null) {
            if (TextUtils.isBlank(statementStart2)) {
                throw new IllegalArgumentException("定义了2对定界符配置，但定界符起始符号不能为空");
            }
            sd = new DelimeterHolder(statementStart.toCharArray(),
                    statementEnd != null ? statementEnd.toCharArray() : null,
                    statementStart2.toCharArray(),
                    statementEnd2 != null ? statementEnd2.toCharArray() : null);
        } else {
            sd = new DelimeterHolder(statementStart.toCharArray(),
                    statementEnd != null ? statementEnd.toCharArray() : null);
        }

        tagConf = new HtmlTagHolder(htmlTagStart, htmlTagEnd, htmlTagBindingAttribute, isHtmlTagSupport);
    }

    private void initDefault() throws IOException {
        resetHtmlTag();
        // 总是添加这俩个
        pkgList.add("java.util.");
        pkgList.add("java.lang.");
        // beetl默认
        ps = new Properties();
        ps.load(Configuration.class.getResourceAsStream("/org/beetl/core/beetl-default.properties"));
        parseProperties(ps);
        // 应用默认
        // 有问题，在eclipse环境下
        InputStream ins = Configuration.class.getResourceAsStream("/beetl.properties");
        if (ins != null) {
            ps.clear();
            ps.load(ins);
            parseProperties(ps);
        }
    }

    private void resetHtmlTag() {
        htmlTagStart = "<" + htmlTagFlag;
        htmlTagEnd = "</" + htmlTagFlag;
    }

    public void add(File file) throws IOException {
        Properties ps = new Properties();
        ps.load(new FileReader(file));
        parseProperties(ps);
    }

    public void add(String resourceAsStreamPath) throws IOException {
        Properties ps = new Properties();
        ps.load(Configuration.class.getResourceAsStream(resourceAsStreamPath));
        parseProperties(ps);
    }

    /**
     * 解析配置文件到类字段中
     *
     * @param ps 配置文件的内容
     */
    protected void parseProperties(Properties ps) {
        Set<Map.Entry<Object, Object>> set = ps.entrySet();
        for (Map.Entry<Object, Object> entry : set) {
            String key = (String) entry.getKey();
            String value = (String) entry.getValue();
            setValue(key, value == null ? null : value.trim());
        }
    }

    protected void setValue(String key, String value) {
        if (key.equalsIgnoreCase(TEMPLATE_CHARSET)) {
            this.charset = value;
        } else if (key.equalsIgnoreCase(DELIMITER_PLACEHOLDER_START)) {
            this.placeholderStart = value;
        } else if (key.equalsIgnoreCase(DELIMITER_PLACEHOLDER_END)) {
            this.placeholderEnd = value;
        } else if (key.equalsIgnoreCase(DELIMITER_STATEMENT_START)) {
            this.statementStart = value;
        } else if (key.equalsIgnoreCase(DELIMITER_STATEMENT_END)) {
            this.statementEnd = (TextUtils.isEmpty(value) || TextUtils.equals(value, "null")) ? null : value;
        } else if (key.equalsIgnoreCase(DELIMITER_PLACEHOLDER_START2)) { // 第二组定界符号
            this.placeholderStart2 = value;
        } else if (key.equalsIgnoreCase(DELIMITER_PLACEHOLDER_END2)) {
            this.placeholderEnd2 = value;
        } else if (key.equalsIgnoreCase(DELIMITER_STATEMENT_START2)) {
            this.statementStart2 = value;
        } else if (key.equalsIgnoreCase(DELIMITER_STATEMENT_END2)) {
            this.statementEnd2 = (TextUtils.isBlank(value) || TextUtils.equals(value, "null")) ? null : value;
        } else if (key.equalsIgnoreCase(NATIVE_CALL)) {
            this.nativeCall = Boolean.parseBoolean(value);
        } else if (key.equalsIgnoreCase(SAFE_OUTPUT)) {
            this.safeOutput = Boolean.parseBoolean(value);
        } else if (key.equalsIgnoreCase(IGNORE_CLIENT_IO_ERROR)) {
            this.isIgnoreClientIOError = Boolean.parseBoolean(value);
        } else if (key.equalsIgnoreCase(DIRECT_BYTE_OUTPUT)) {
            this.directByteOutput = Boolean.parseBoolean(value);
        } else if (key.equalsIgnoreCase(ERROR_HANDLER)) {
            this.errorHandlerClass = (TextUtils.isBlank(value) || value.equals("null")) ? null : value;
        } else if (key.equalsIgnoreCase(WEBAPP_EXT)) {
            this.webAppExt = TextUtils.isEmpty(value) ? null : value;
        } else if (key.equalsIgnoreCase(MVC_STRICT)) {
            this.isStrict = Boolean.parseBoolean(value);
        } else if (key.equalsIgnoreCase(HTML_TAG_SUPPORT)) {
            this.isHtmlTagSupport = Boolean.parseBoolean(value);
        } else if (key.equalsIgnoreCase(HTML_TAG_ATTR_CONVERT)) {
            if (!TextUtils.isBlank(value)) {
                this.htmlTagAttributeConvert = value;
            }
        } else if (key.equalsIgnoreCase(HTML_TAG_FLAG)) {
            this.htmlTagFlag = value;
            resetHtmlTag();
        } else if (key.equalsIgnoreCase(HTML_TAG_BINDING_ATTRIBUTE)) {
            this.htmlTagBindingAttribute = value;
        } else if (key.equalsIgnoreCase(IMPORT_PACKAGE)) {
            String[] strs = value.split(";");
            this.pkgList.addAll(Arrays.asList(TextUtils.split(value, ",")));
        } else if (key.equalsIgnoreCase(ENGINE)) {
            this.engine = value;
        } else if (key.equalsIgnoreCase(NATIVE_SECUARTY_MANAGER)) {
            this.nativeSecurity = value;
        } else if (key.equalsIgnoreCase(RESOURCE_LOADER)) {
            this.resourceLoader = value;
        } else if (key.equalsIgnoreCase(RESOURCE_LOADER)) {
            this.resourceLoader = value;
        } else if (key.equalsIgnoreCase(BUFFER_SIZE)) {
            this.bufferSize = Integer.parseInt(value);
            if (bufferSize < BUFFER_MIN_SIZE) {
                throw new IllegalStateException("GLOBAL." + BUFFER_SIZE + " 配置不能小于" + BUFFER_MIN_SIZE);
            }
        } else if (key.equalsIgnoreCase(BUFFER_NUM)) {
            this.bufferNum = Integer.parseInt(value);
        } else {
            // 扩展
            if (key.startsWith("fn.") || key.startsWith("FN.")) {
                String fn = checkValue(value);
                if (fn != null) {
                    this.fnMap.put(getExtName(key), fn);
                }
            } else if (key.startsWith("fnp.") || key.startsWith("FNP.")) {
                String fnPkg = checkValue(value);
                if (fnPkg != null) {
                    this.fnPkgMap.put(getExtName(key), fnPkg);
                }
            } else if (key.startsWith("ft.") || key.startsWith("FT.")) {
                String format = checkValue(value);
                if (format != null) {
                    this.formatMap.put(getExtName(key), format);
                }
            } else if (key.startsWith("ftc.") || key.startsWith("FTC.")) {
                String defaultFormat = checkValue(value);
                if (value != null) {
                    this.defaultFormatMap.put(getExtName(key), defaultFormat);
                }
            } else if (key.startsWith("virtual.") || key.startsWith("VIRTUAL.")) {
                String virtual = checkValue(value);
                if (virtual != null) {
                    this.virtualClass.put(getExtName(key), virtual);
                }
            } else if (key.startsWith("general_virtual.") || key.startsWith("GENERAL_VIRTUAL.")) {
                this.generalVirtualAttributeSet.addAll(Arrays.asList(TextUtils.split(value, ";")));
            } else if (key.startsWith("tag.") || key.startsWith("TAG.")) {
                String tag = checkValue(value);
                if (tag != null) {
                    this.tagMap.put(getExtName(key), tag);
                }
            } else if (key.startsWith("tagf.") || key.startsWith("TAGF.")) {
                this.tagFactoryMap.put(getExtName(key), value);
            } else if (key.startsWith("resource.") || key.startsWith("RESOURCE.")) {
                this.resourceMap.put(getExtName(key), value);
            }
        }

    }

    protected String checkValue(String value) {
        String[] vals = TextUtils.split(value, ",");
        if (vals.length == 1) {
            return value;
        }
        String cls = vals[1];
        try {
            // 如果此类不存在，则不加入配置
            Class.forName(cls, false, classLoader);
        } catch (ClassNotFoundException e) {
            return null;
        }
        return vals[0];
    }

    private static String getExtName(String key) {
        return key.substring(key.indexOf(".") + 1);
    }

    // =================================================================================================================
    //                                          getter / setter 方法
    // =================================================================================================================

    public String getCharset() {
        return charset;
    }

    public String getPlaceholderStart() {
        return placeholderStart;
    }

    public void setPlaceholderStart(String placeholderStart) {
        this.placeholderStart = placeholderStart;
    }

    public String getPlaceholderEnd() {
        return placeholderEnd;
    }

    public void setPlaceholderEnd(String placeholderEnd) {
        this.placeholderEnd = placeholderEnd;
    }

    public String getStatementStart() {
        return statementStart;
    }

    public void setStatementStart(String statementStart) {
        this.statementStart = statementStart;
    }

    public String getStatementEnd() {
        return statementEnd;
    }

    public void setStatementEnd(String statementEnd) {
        this.statementEnd = statementEnd;
    }

    public String getPlaceholderStart2() {
        return placeholderStart2;
    }

    public void setPlaceholderStart2(String placeholderStart2) {
        this.placeholderStart2 = placeholderStart2;
    }

    public String getPlaceholderEnd2() {
        return placeholderEnd2;
    }

    public void setPlaceholderEnd2(String placeholderEnd2) {
        this.placeholderEnd2 = placeholderEnd2;
    }

    public String getStatementStart2() {
        return statementStart2;
    }

    public void setStatementStart2(String statementStart2) {
        this.statementStart2 = statementStart2;
    }

    public String getStatementEnd2() {
        return statementEnd2;
    }

    public void setStatementEnd2(String statementEnd2) {
        this.statementEnd2 = statementEnd2;
    }

    public String getHtmlTagFlag() {
        return htmlTagFlag;
    }

    public void setHtmlTagFlag(String htmlTagFlag) {
        this.htmlTagFlag = htmlTagFlag;
    }

    public DelimeterHolder getPlaceHolderDelimeter() {
        return pd;
    }

    public DelimeterHolder getScriptDelimeter() {
        return sd;
    }

    public HtmlTagHolder getTagConf() {
        return tagConf;
    }

    public void setTagConf(HtmlTagHolder tagConf) {
        this.tagConf = tagConf;
    }

    public boolean isHtmlTagSupport() {
        return isHtmlTagSupport;
    }

    public void setHtmlTagSupport(boolean isHtmlTagSupport) {
        this.isHtmlTagSupport = isHtmlTagSupport;
    }

    public boolean isNativeCall() {
        return nativeCall;
    }

    public void setNativeCall(boolean nativeCall) {
        this.nativeCall = nativeCall;
    }

    public boolean isDirectByteOutput() {
        return directByteOutput;
    }

    public void setDirectByteOutput(boolean directByteOutput) {
        this.directByteOutput = directByteOutput;
    }

    public boolean isStrict() {
        return isStrict;
    }

    public void setStrict(boolean isStrict) {
        this.isStrict = isStrict;
    }

    public String getHtmlTagStart() {
        return htmlTagStart;
    }

    public void setHtmlTagStart(String htmlTagStart) {
        this.htmlTagStart = htmlTagStart;
    }

    public String getHtmlTagEnd() {
        return htmlTagEnd;
    }

    public void setHtmlTagEnd(String htmlTagEnd) {
        this.htmlTagEnd = htmlTagEnd;
    }

    public String getHtmlTagBindingAttribute() {
        return htmlTagBindingAttribute;
    }

    public void setHtmlTagBindingAttribute(String htmlTagBindingAttribute) {
        this.htmlTagBindingAttribute = htmlTagBindingAttribute;
    }

    public String getHtmlTagAttributeConvert() {
        return htmlTagAttributeConvert;
    }

    public void setHtmlTagAttributeConvert(String htmlTagAttributeConvert) {
        this.htmlTagAttributeConvert = htmlTagAttributeConvert;
    }

    public void setCharset(String charset) {
        this.charset = charset;
    }

    public Set<String> getPkgList() {
        return pkgList;
    }

    public void addPkg(String pkg) {
        this.pkgList.add(pkg.concat("."));
    }

    public void setPkgList(Set<String> pkgList) {
        this.pkgList = pkgList;
    }

    public String getEngine() {
        return engine;
    }

    public void setEngine(String engine) {
        this.engine = engine;
    }

    public String getNativeSecurity() {
        return nativeSecurity;
    }

    public void setNativeSecurity(String nativeSecurity) {
        this.nativeSecurity = nativeSecurity;
    }

    public String getWebAppExt() {
        return webAppExt;
    }

    public void setWebAppExt(String webAppExt) {
        this.webAppExt = webAppExt;
    }

    public boolean isIgnoreClientIOError() {
        return isIgnoreClientIOError;
    }

    public void setIgnoreClientIOError(boolean isIgnoreClientIOError) {
        this.isIgnoreClientIOError = isIgnoreClientIOError;
    }

    public String getErrorHandlerClass() {
        return errorHandlerClass;
    }

    public void setErrorHandlerClass(String errorHandlerClass) {
        this.errorHandlerClass = errorHandlerClass;
    }

    public Map<String, String> getFnMap() {
        return fnMap;
    }

    public void setFnMap(Map<String, String> fnMap) {
        this.fnMap = fnMap;
    }

    public Map<String, String> getFnPkgMap() {
        return fnPkgMap;
    }

    public void setFnPkgMap(Map<String, String> fnPkgMap) {
        this.fnPkgMap = fnPkgMap;
    }

    public Map<String, String> getFormatMap() {
        return formatMap;
    }

    public void setFormatMap(Map<String, String> formatMap) {
        this.formatMap = formatMap;
    }

    public Map<String, String> getDefaultFormatMap() {
        return defaultFormatMap;
    }

    public void setDefaultFormatMap(Map<String, String> defaultFormatMap) {
        this.defaultFormatMap = defaultFormatMap;
    }

    public Set<String> getGeneralVirtualAttributeSet() {
        return generalVirtualAttributeSet;
    }

    public void setGeneralVirtualAttributeSet(Set<String> generalVirtualAttributeSet) {
        this.generalVirtualAttributeSet = generalVirtualAttributeSet;
    }

    public Map<String, String> getVirtualClass() {
        return virtualClass;
    }

    public void setVirtualClass(Map<String, String> virtualClass) {
        this.virtualClass = virtualClass;
    }

    public Map<String, String> getTagFactoryMap() {
        return tagFactoryMap;
    }

    public void setTagFactoryMap(Map<String, String> tagFactoryMap) {
        this.tagFactoryMap = tagFactoryMap;
    }

    public Map<String, String> getTagMap() {
        return tagMap;
    }

    public void setTagMap(Map<String, String> tagMap) {
        this.tagMap = tagMap;
    }

    public String getProperty(String name) {
        return this.ps.getProperty(name);
    }

    public String getResourceLoader() {
        return resourceLoader;
    }

    public void setResourceLoader(String resourceLoader) {
        this.resourceLoader = resourceLoader;
    }

    public Map<String, String> getResourceMap() {
        return resourceMap;
    }

    public void setResourceMap(Map<String, String> resourceMap) {
        this.resourceMap = resourceMap;
    }

    public Properties getPs() {
        return ps;
    }

    public void setPs(Properties ps) {
        this.ps = ps;
    }

    public boolean isSafeOutput() {
        return safeOutput;
    }

    public void setSafeOutput(boolean safeOutput) {
        this.safeOutput = safeOutput;
    }

    public static class HtmlTagHolder {
        String htmlTagStart = "<#";
        String htmlTagEnd = "</#";
        String htmlTagBindingAttribute = "var";
        boolean support = true;

        public HtmlTagHolder() {
            //默认
        }

        public HtmlTagHolder(String htmlTagStart, String htmlTagEnd, String htmlTagBindingAttribute, boolean support) {
            this.htmlTagStart = htmlTagStart;
            this.htmlTagEnd = htmlTagEnd;
            this.htmlTagBindingAttribute = htmlTagBindingAttribute;
            this.support = support;
        }

        public boolean isSupport() {
            return support;
        }

        public HtmlTagConfig create() {
            return new HtmlTagConfig(htmlTagStart, htmlTagEnd, htmlTagBindingAttribute);
        }

    }

    public static class DelimeterHolder {
        char[] start;
        char[] end;
        char[] start1;
        char[] end1;

        public DelimeterHolder(char[] start, char[] end) {
            this.start = start;
            this.end = end;
        }

        public DelimeterHolder(char[] start, char[] end, char[] start1, char[] end1) {
            this.start = start;
            this.end = end;
            this.start1 = start1;
            this.end1 = end1;
        }

        public PlaceHolderDelimeter createPhd() {
            return start1 == null
                    ? new PlaceHolderDelimeter(start, end)
                    : new PlaceHolderDelimeter(start, end, start1, end1);
        }

        public ScriptDelimeter createSd() {
            return start1 == null
                    ? new ScriptDelimeter(start, end)
                    : new ScriptDelimeter(start, end, start1, end1);
        }

    }

}
