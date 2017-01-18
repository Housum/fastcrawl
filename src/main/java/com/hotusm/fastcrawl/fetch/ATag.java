package com.hotusm.fastcrawl.fetch;

/**
 * A 标签
 */
public final class ATag implements Tag{

    private String href;
    private String name;

    public ATag() {
    }

    public ATag(String name,String href) {
        this.href = href;
        this.name = name;
    }

    public String getHref() {
        return href;
    }

    public void setHref(String href) {
        this.href = href;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    /**
     * 调试使用
     * @return
     */
    @Override
    public String toString() {
        return "ATag{" +
                "href='" + href + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
