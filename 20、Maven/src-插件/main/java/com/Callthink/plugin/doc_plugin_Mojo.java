package com.Callthink.plugin;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.plugins.annotations.ResolutionScope;
import org.apache.maven.project.MavenProject;

//import com.ToneThink.ctsTools.myUtility.myFile;
//import com.ToneThink.ctsTools.myUtility.myString;

/**
 * 插件入口类
 * @author GuaiWenWo
 * @param <MavenProject>
 *
 */
@Mojo(name = "doc", 
defaultPhase=LifecyclePhase.PACKAGE,
requiresDependencyResolution=ResolutionScope.TEST)
public class doc_plugin_Mojo extends AbstractMojo {

    //读取配置文件属性
	//当前应用名称
    @Parameter(property = "application")
    private String application;

    //当前应用版本号
    @Parameter(property = "version")
    private String version;
    
    //当前应用根目录
    @Parameter(property = "sourceFolderPath")
    private String sourceFolderPath;

    //生成文档目录
    @Parameter(property = "outFolderPath")
    private String outFolderPath;

    @Parameter(defaultValue = "${project}", readonly = true, required = true)
    private  MavenProject project;
    
    //生成文档所在路径
    String m_DocPath="";
    
    public void execute() throws MojoExecutionException, MojoFailureException {

        System.out.println(application);
        System.out.println(version);
        System.out.println(sourceFolderPath);
        System.out.println(outFolderPath);
        
        //检测生成文件的路径是否存在
        m_DocPath=outFolderPath+"//"+application+"-"+version+".md";
        checkPath(outFolderPath,m_DocPath);

       checkJavaClass(sourceFolderPath);
    }
    
    /**
     * 递归整个java文件目录
     * @param dir
     */
    private void checkJavaClass(String dir) {
    	  File f =  new File(dir) ;
          File fs[] = f.listFiles() ;

          if(fs!= null){
              for( int i= 0 ;i<fs. length ;i++){
                  File  currenFile = fs[i] ;
                  if(currenFile.isFile()){
                      String fileName = currenFile.getName() ;
                      //System.out.println(currenFile.getPath());
                      String suffix = fileName.substring(fileName.lastIndexOf( ".") +  1) ;
                      //如果是java文件,则进行处理
                      if(suffix.equals( "java")){
                    	  renderDoc(currenFile.getPath());
                      }
                  } else{
                      //System.out.println(currenFile.getAbsolutePath());
                	  checkJavaClass(currenFile.getAbsolutePath()) ;
                  }
              }
          }
	}
    
    /**
     * 生成文档
     * @param strAbsolutePath java类所在磁盘绝对路径
     */
    public void renderDoc(String strAbsolutePath) {

//    	//直接读取类文件进行生成文档
    	List<String> lstParam=readFileByLine(strAbsolutePath);
    	String strPathName=replacePath(strAbsolutePath).replace("com.CallThink.", "");
    	//创建MD文档格式
    	if (lstParam.size()>0) {
			WriteString(m_DocPath, "# "+strPathName);
			
			WriteString(m_DocPath, "- QueryString，统一使用 & 连接键值对的方式");
			WriteString(m_DocPath, "|参数|说明|备注|");
			WriteString(m_DocPath, "|-|-|-|");
			for (String strValue : lstParam) {
				WriteString(m_DocPath, "|"+strValue+"|||");
			}
		}

    	//检测注解去生成文档,暂未做任何解析,目前已经调试通过
		String strPath=replacePath(strAbsolutePath);
		System.out.println("执行注解检测:"+strPath);
		parseDocAPI(strPath);
	}

    /**
     * 按行读取文件
     * @param strFile
     */
    public List<String> readFileByLine(String strFile){
    	List<String> lstParam=new ArrayList<>();
        try {
            File file = new File(strFile);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(file));
            String strLine = null;
           // int lineCount = 1;
            while(null != (strLine = bufferedReader.readLine())){
              
             	//正则去匹配所有哈希中需要解析的内容  暂未过滤处理
                if (strLine.indexOf("Functions.ht_Get_strValue")>0) {	
              	  Pattern p=Pattern.compile("\"(.*?)\"");
                    Matcher m=p.matcher(strLine);
                    while(m.find()){
                        //System.out.println(m.group());
                    	lstParam.add(m.group().replaceAll("\"", ""));
                    }           	  
  			   }
            	
            }
        }catch(Exception e){
            e.printStackTrace();
        }
        
        return lstParam;
    }
    
    
    
    /**
     * 将绝对路径更改可反射路径
     * 例如: E:\\UltraCRM_callback_test\\UltraCRM\\src\\main\\java\\com\\CallThink\\ut_survey\\survey_task_select.java
     *          替换为: com.CallThink.ut_survey.survey_task_select
     * @param strAbsolutePath
     * @return
     */
    private  String replacePath(String strAbsolutePath) {
		String strPath=strAbsolutePath.replace("\\", ".");
		int index=strPath.indexOf("com.CallThink");
		strPath=strPath.substring(index);
		//去除后缀 .java  此处未做容错
		strPath=strPath.substring(0, strPath.length()-5);
		return strPath;
	}
    
    /**
     * 反射类上的注解
     * @param strPath 格式例如: com.CallThink.ut_callback.callback_plan_list
     */
    private void parseDocAPI(String strPath) {
        try {
        	 //System.out.println(project.getArtifact().getFile().getAbsolutePath());   //反射路径
        	//反射类上的注解
			Class  concreteClass = getClassLoader(project).loadClass(strPath);
			Annotation[] classAN=concreteClass.getAnnotations();
			if (concreteClass.isAnnotationPresent(docAPI .class)) {  //检测类上是否包含此注解,有注解进行处理
				 docAPI myClassAnnotation =(docAPI) concreteClass.getAnnotation(docAPI .class);
				 //打印注解书写内容
				 System.out.println("类注解:"+myClassAnnotation.apiDesc() + "+" + myClassAnnotation.apiValue());	
			}
			
			//反射方法上注解	
			 Method[] methods= concreteClass.getDeclaredMethods();  
			 for (int i = 0; i < methods.length; i++) {
				if (methods[i].isAnnotationPresent(docAPI .class)) {
					 docAPI myClassAnnotation =(docAPI) methods[i].getAnnotation(docAPI .class);
					 //打印注解书写内容
					 System.out.println("方法注解:"+methods[i].getName()+" "+myClassAnnotation.apiDesc() + "+" + myClassAnnotation.apiValue());	
				}
				
			}
			 
						
		} catch (Exception e) {
			e.printStackTrace();
			System.out.println("出现异常"+e.getMessage());
		}
	}
    
    /**
     * 自定义类loader加载器
     * @param project
     * @return
     */
	private ClassLoader getClassLoader(MavenProject project)
    {
        try
        {
        	// 所有的类路径环境
            List classpathElements = project.getCompileClasspathElements();
            
            classpathElements.add( project.getBuild().getOutputDirectory() );
            classpathElements.add( project.getBuild().getTestOutputDirectory() );
            // 转为 URL 数组
            URL urls[] = new URL[classpathElements.size()];
            for ( int i = 0; i < classpathElements.size(); ++i )
            {
                urls[i] = new File( (String) classpathElements.get( i ) ).toURL();
            }
            // 自定义类加载器
            return new URLClassLoader( urls, this.getClass().getClassLoader() );
        }
        catch ( Exception e )
        {
            getLog().debug( "Couldn't get the classloader." );
            return this.getClass().getClassLoader();
        }
    }
    
	/**
	 * 文件中追加内容
	 * @param strFilePath
	 * @param strMsg
	 */
	private void WriteString(String strFilePath,String strMsg) {
        BufferedWriter sw = null;
		try {
			sw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(strFilePath, true), "UTF-8"));
			sw.write(strMsg);
			sw.write("\r\n");  //换行
		} catch (IOException ex) {
		
		} finally {
			try {
				sw.close();
			} catch (IOException ex) {
			}
		}
	}
	
	/**
	 * 检测路径是否存在
	 * @param stroutPath  路径
	 * @param strFilePath 文件路径
	 */
	private void checkPath(String stroutPath,String strFilePath) {
		//检测路径
		File fileDir = new File(stroutPath);
         if(!fileDir.exists()) {
        	 fileDir.mkdirs();//创建目录
         }
         
         //检测文件
         File fileName = new File(strFilePath); //如果存在删除
         if(fileName .exists()) {
        	 fileName.delete();
         }
         //创建新的文件
         try {
        	 fileName.createNewFile();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
 
}