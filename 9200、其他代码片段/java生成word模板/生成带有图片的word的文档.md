参考：https://blog.csdn.net/TurboAnho/article/details/97924175

生成带有图片的word的文档

```java

@RequestMapping(params ="getWordByDate",produces = "text/html;charset=UTF-8")
    @ResponseBody
    public String getWordByDate(String ImgInfo,String startDate,String endDate) throws IOException, ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        SimpleDateFormat sdfs = new SimpleDateFormat("yyyy年MM月");
        String result = null;
        try {
            //日期
            Date date = sdf.parse(startDate);
            String start = sdfs.format(date);
            Date dateEnd = sdf.parse(endDate);
            String end = sdfs.format(dateEnd);
            
            //模板路径
            String templ = "D://word.docx";
            //图片路径
            String wordPath  = "D://";
            
            Map<String, Object> datas = new HashMap<String, Object>(){{
                put("startDate", start);
                put("endDate", end);
                if(StringUtil.isNotEmpty(startDate) && StringUtil.isNotEmpty(endDate) ){
                    put("title",new TextRenderData(start+"至"+end+"文档报告"));
                }else{
                    put("title",new TextRenderData("文档报告"));
                }

               //插入图片  PictureRenderData对象  参数：  宽 高 图片文件地址
                put("ImgInfo", new PictureRenderData(520, 360, wordPath+"/"+ImgInfo));

               //插入各种文字段落  TextRenderData 
                put("info",new TextRenderData("000000","文档内容1"));

                ...............

                ..............

                 put("info......",new TextRenderData("000000","文档内容n........"));
            }};
            if(datas.size() > 1){
                //将生成的文件保存到指定目录下
                String filePath ="D://file";
                XWPFTemplate template = XWPFTemplate.create(templ);
                RenderAPI.render(template, datas);
                Date day=new Date();    
                SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss"); 
                  //输出文件
                result =df.format(day)+".docx";
                File outFile = new File(filePath+"/"+result);
                //如果输出目标文件夹不存在，则创建
                if (!outFile.getParentFile().exists()){
                    outFile.getParentFile().mkdirs();
                }
                FileOutputStream out = new FileOutputStream(outFile);
                template.write(out);
                out.flush();
                out.close();
                template.close();
            }
        }finally {
            
        }
        return result;
    }

```
生成word文档要有模板

例：word.docx

![](1.png)