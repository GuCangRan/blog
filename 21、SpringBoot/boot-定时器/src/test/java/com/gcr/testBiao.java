package com.gcr;

import org.junit.jupiter.api.Test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by GuaiWenWo on 2021/3/1 14:09
 */
public class testBiao {

    @Test
    public void searchTable() {
        String strPath = "E:\\E000--项目文档存储\\001-项目集合\\2019-09-06-回龙观心理热线java版本\\设计源码\\UltraCRM_crm_hlg\\UltraCRM\\src\\main\\java\\com\\CallThink";
//        File file = new File(strPath);
//        File[] files = file.listFiles();
//        for (File file1 : files) {
//            System.out.println(file.getAbsolutePath());
//            System.out.println(file.getName());
//        }
        checkJavaClass(strPath);

    }

    /**
     * 递归整个java文件目录
     *
     * @param dir
     */
    private void checkJavaClass(String dir) {
        File f = new File(dir);
        File fs[] = f.listFiles();

        if (fs != null) {
            for (int i = 0; i < fs.length; i++) {
                File currenFile = fs[i];
                if (currenFile.isFile()) {
                    String fileName = currenFile.getName();
                    //System.out.println(currenFile.getPath());
                    String suffix = fileName.substring(fileName.lastIndexOf(".") + 1);
                    //如果是java文件,则进行处理
                    if (suffix.equals("java")) {
                        //renderDoc(currenFile.getPath());
                        //System.out.println(currenFile.getPath());
                        readFileByLine(currenFile.getPath());
                    }
                } else {
                    //System.out.println(currenFile.getAbsolutePath());
                    checkJavaClass(currenFile.getAbsolutePath());
                }
            }
        }
    }


    /*  * 按行读取文件
     * @param strFile
     */
    public List<String> readFileByLine(String strFile) {
        List<String> lstParam = new ArrayList<>();
        try {
            File file = new File(strFile);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String strLine = null;
            // int lineCount = 1;
            while (null != (strLine = bufferedReader.readLine())) {

                //正则去匹配所有哈希中需要解析的内容  暂未过滤处理
                //if (strLine.indexOf("\") > 0) {
                //System.out.println(strLine);
                Pattern p = Pattern.compile("\"(.*?)\"");
                Matcher m = p.matcher(strLine);
                while (m.find()) {
                    //System.out.println(m.group());
                    //lstParam.add(m.group().replaceAll("\"", ""));
                    String strResult = m.group().replaceAll("\"", "");
                    // System.out.println(strResult);
                    if (strResult.startsWith("CRM") || strResult.startsWith("CTS"))
                        System.out.println(m.group());
                }
                //}

            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return lstParam;
    }
}
