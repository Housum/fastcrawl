package com.hotusm.fastcrawl.parser;

import com.hotusm.fastcrawl.common.container.LinkBucket;
import com.hotusm.fastcrawl.fetch.ATag;
import com.hotusm.fastcrawl.util.HtmlAnalysisUtil;
import org.apache.commons.lang3.StringUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * 解析网页中的a标签 并且将其存到中央库中
 */
public class ATagParserWork implements ParserWork{

    private static final Logger LOGGER= LogManager.getLogger(ATagParserWork.class);

    private static final String TAG="a";
    private static final String ATTR="href";


    //这里是保存解析过的A标签
    private volatile ParsedData aTagParsedData;

    //存放a标签
    private volatile LinkBucket linkBucket;

    //检验和编码url的
    private ValidateTag validateTag;

    public ATagParserWork(LinkBucket linkBucket,ParsedData aTagParsedData,
                          ValidateTag validateTag){
        this.linkBucket=linkBucket;
        this.aTagParsedData=aTagParsedData;
        this.validateTag=validateTag;

    }
    public Map<Object, Object> doParser(String html) {
        Map<String,String> tags= HtmlAnalysisUtil.selectAttrAndText(TAG,ATTR,html);
        if(tags==null){
            return null;
        }

        ATag aTag=null;
        for(Map.Entry<String,String> entry:tags.entrySet()){
            synchronized (aTagParsedData){
                if(aTagParsedData.isParsed(validateTag.encodeHref(entry.getKey()))){
                    if(LOGGER.isDebugEnabled()){
                        LOGGER.debug(String.format("a 标签不满足条件. 是否已经验证 %s",true));
                    }
                    continue;
                }else{
                    aTagParsedData.addData(validateTag.encodeHref(entry.getKey()));
                }
            }
            if(!validateTag.validate(validateTag.encodeHref(entry.getKey()))
                    ||StringUtils.isEmpty(entry.getKey())){
                if(LOGGER.isDebugEnabled()){
                    LOGGER.debug(String.format("a标签不满足条件.验证结果 %s  是否为空 %s",
                            validateTag.validate(validateTag.encodeHref(entry.getKey()))
                    ,StringUtils.isEmpty(entry.getKey())));
                }
                continue;
            }
            aTag=new ATag(entry.getValue(),validateTag.encodeHref(entry.getKey()));
            try {
                linkBucket.push(aTag);
            }catch (Exception e){
                LOGGER.error("push error,",e);
                Thread.currentThread();
            }
        }
        return null;
    }
}
