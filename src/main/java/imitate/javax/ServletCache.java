package imitate.javax;

import java.util.HashMap;
import java.util.Map;

public class ServletCache {

    //用于保存servlet名称和servlet实例
    private static Map<String,Servlet> map=new HashMap<>();


    /**
     * 通过servlet名称获得servlet实例
     * @param servletName
     * @return
     */
    public static Servlet getFromCache(String servletName){
        if(cacheContains(servletName))
             return  map.get(servletName);
        throw new IllegalArgumentException("not contain");
    }

    /**
     * 返回该servlet是否存在于缓存中
     * @param servletName
     * @return
     */
    public  static boolean cacheContains(String servletName) {
        return  map.containsKey(servletName);
    }

    /**
     * 将servlet添加到缓存中
     * @param servletName
     * @param
     */
    public static void addServletToCache(String servletName,Servlet servlet){
          map.put(servletName,servlet);
    }


}
