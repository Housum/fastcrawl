package com.hotusm.fastcrawl.parser;

/**
 * 检验超链接是否满足
 */
public interface ValidateTag {

    /**
     * 检验url是否合法
     * @param url
     * @return
     */
    boolean validate(String url);

    /**
     *将url转义成合法的
     * 比如 绝对路径相对路径这种的
     * @return
     */
    String encodeHref(String href);
}
