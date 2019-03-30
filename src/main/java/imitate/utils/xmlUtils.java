package imitate.utils;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;

import org.dom4j.io.SAXReader;

import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class xmlUtils {
    //    用于保存应用和应用的web.xml  Map<String,String>内保存的为web.xml解析 可以理解为web.xml
    public static Map<String, Map<String, String>> servletMaps = new HashMap<>();

    /**
     * 将服务器中所有的web工程与对应的web.xml解析映射封装在map中
     *
     * @param AppNames
     */
    public static void parse(String[] AppNames) throws DocumentException {
        for (String name : AppNames) {
            Map<String, String> map = parseServlet(name);
            servletMaps.put(name, map);
        }
    }


    /**
     * 解析web工程对应的web.xml,并把url,servlet-class封装到map中
     *
     * @param appName javaWeb工程名
     * @return 封装了url, servlet-class的map
     * @throws DocumentException
     */
    private static Map<String, String> parseServlet(String appName) throws DocumentException {
//        用于保存servlet-name，servlet-class
        Map<String, String> servletInfo = new HashMap<>();
        //获取web.xml的路径
        SAXReader reader = new SAXReader();
        InputStream stream = xmlUtils.class.getClassLoader().getResourceAsStream(appName + "/web.xml");
        Document document = reader.read(stream);
        //找到各个servlet标签   使用xpath的语法
        List<Element> list = document.selectNodes("/web-app/servlet");
//         迭代获得每一个servlet
        for (Element ele : list) {
//                 获得servlet下的servlet-name，并获得标签内的值
            Element name = (Element) ele.selectSingleNode("servlet-name");
            String nameValue = name.getStringValue();
//             获得servlet下的servlet-class，并获得标签内的值
            Element servletClass = (Element) ele.selectSingleNode("servlet-class");
            String classValue = servletClass.getStringValue();
//                 将servlet-name和servlet-class的值纳入map中
            servletInfo.put(nameValue, classValue);
        }
//        用于保存servlet-name,url
        Map<String, String> servletMapping = new HashMap<>();
        List<Element> nodesList = document.selectNodes("/web-app/servlet-mapping");
        for (Element ele : nodesList) {
            Element servletName = (Element) ele.selectSingleNode("servlet-name");
            Element url = (Element) ele.selectSingleNode("url");
            String nameValue = servletName.getStringValue();
            String urlValue = url.getStringValue();
            servletMapping.put(nameValue, urlValue);
        }
        //       创建map用于放置url和servlet-class的值
        Map<String, String> servletMap = new HashMap<>();
        Set<String> nameInfo = servletInfo.keySet();
        for (String name : nameInfo) {
            String servletName = servletInfo.get(name);
            String servletUrl = servletMapping.get(name);
            servletMap.put(servletUrl, servletName);
        }
        return servletMap;
    }


}

