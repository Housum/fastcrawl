package com.hotusm.fastcrawl.parser;

/**
 */
public class ParserTest {

    /*@Test
    public void testParser() throws Exception{
        FileInputStream fileInputStream=new FileInputStream("C:\\html\\httpsbookdoubancom.html");
        StringBuilder sb=new StringBuilder();
        byte[] b=new byte[2014];
        while (fileInputStream.read(b)!=-1){
            sb.append(new String(b,"UTF-8"));
        }
        Document doc = Jsoup.parse(sb.toString());
       *//* Elements elements=doc.select("a");
        if(elements!=null&&elements.size()>0){
          //Element element= elements.get(0);
          //System.out.println(element.text());
            for(Element element:elements){
               //System.out.println("href:"+element.attr("href")+" name:"+element.text());
              String url=additionTag(element.attr("href"));
              if(StringUtils.isNoneBlank(url)){
                  System.out.println(url);
              }
            }
        }*//*

      List<String> list=  HtmlAnalysisUtil.selectAttr("a","href",sb.toString());
    }

    //如果url是以/开头的 需要增加完整的名称 domain+url  如果不是以http或者https 开头又不是/开头 那么
    //认为是相对路径  old-url+'/'+url
    private String additionTag(String url) throws Exception{
        if(StringUtils.isBlank(url)){
            return "";
        }

        List<String> suffix=new ArrayList<String>(Arrays.asList(ConfigUtil.getValue("suffix.exclude").split(",")));
        if(validate(suffix,url)){
            return "";
        }
        String domain=ConfigUtil.getValue("domain");
        String host=ConfigUtil.getValue("host");
        if(url.startsWith("https")){
            url= url.replaceFirst("https","http");
        }
        if(url.startsWith("/")){
            url= domain+url.substring(1,url.length());
        }
        if(url.startsWith("www")){
            url= "http://"+url;
        }
        if(url.startsWith(host)){
            url= url.replaceFirst(host,domain);
        }
        if(!url.startsWith("http")){
            url= ConfigUtil.getValue("domain")+url;
        }

        return url;//encodeUrlCh(url);
    }

    private boolean validate(List<String> suffix,String url){

        for (String s:suffix){
            if(url.endsWith(s)){
                return true;
            }
        }
        return false;
    }
    public static String encodeUrlCh (String url){
        URI uri;
        try {
            uri = new URI(url,false);
            return uri.toString();
        } catch (URIException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Test
    public void testChinses() {

        System.out.println("11".matches("[\\u4e00-\\u9fa5]"));
        System.out.println("7868768你".matches("[\\u4e00-\\u9fa5]+"));
        Pattern pattern = Pattern.compile("[\\u4e00-\\u9fa5]+");
        System.out.println(pattern.matcher("7868768你").matches());
    }


    @Test
    public void testParser1(){
        final LinkBucket linkBucket= new QueueLinkBucket<ATag>();
        final PageData pageData=new PageDataImpl();
        final ParserWork parserWork=new TestParserWork();
        ParsedData parsedData=new ATagParsedData();
        new TestAbstractParser(pageData,parsedData,linkBucket,parserWork).load();
    }
*/
    public static void main(String[] args) throws Exception{

//             //a链接的中央仓库
//             LinkBucket linkBucket= new QueueLinkBucket<ATag>();
//             //下载好的网页,但是没有解析的
//             PageData pageData=new PageDataImpl();
//             // 解析的工作单元
//             ParserWork parserWork=new TestParserWork();
//             // 解析过的中央仓库
//             ParsedData parsedData=new ATagParsedData();
//             //被丢弃的
//             DiscardData discardData=new DiscardDataImpl();
//             //验证标签
//             ValidateTag validateTag=new ValidateATag();
//             //
//             ParserWork aTagParserWork=new ATagParserWork(linkBucket,parsedData,validateTag);
//
//             //存放一个root
//             linkBucket.push(new ATag("BLOGS","http://www.cnblogs.com/"));
//             DelayAndRetryLoad load=new DelayAndRetryLoadImpl(linkBucket,pageData,discardData);
//             load.delayLoad();
//
//             TestAbstractParser parser= new TestAbstractParser();
//
//             parser.setaTagParserWork(aTagParserWork);
//             parser.setPageData(pageData);
//             parser.setPageParserWork(parserWork);
//
//             parser.load();
//
//            synchronized (ParserTest.class){
//            try{
//                ParserWork.class.wait();
//            }catch (Exception e){
//
//            }
//            }
    }

}