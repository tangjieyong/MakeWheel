package imitate.javax;

import java.util.HashMap;
import java.util.Map;

public class ObjectRequest implements Request {
    //    imfo?username=tangjieyong&gender=1&interest=soccer&interest=music&interest=sleep
   Map<String,String[]>map=new HashMap<>();

   //要求在加载这个类的时候就要解析url,因此放在构造方法中
    public ObjectRequest(String requestUrl){
       //url是否包含参数，如果不包含参数就没有必要解析url的参数部分
       if(requestUrl.contains("?")){
           String[] parts = requestUrl.split("[?]");
           if(parts.length>1){//排除这种情况 a?
               //获取参数部分
            String parameterPart=parts[1];//username=tangjieyong&gender=1&interest=soccer&interest=music&interest=sleep
           //单一参数还是多个参数
               if(parameterPart.contains("&")){
                   //多个参数 {username=tangjieyong,gender=1}
                   String[] parameters = parameterPart.split("&");
                   for (String par:parameters){//username=tangjieyong
                       String[] pars = par.split("=");
                       if(map.containsKey(pars[0])){//map中包含这个值
                          //找到map中该key对应的数组
                           String[] values = map.get(pars[0]);
                           //创建一个新的数组，规模是之前数组加1
                           String[]newValues=new String[values.length+1];
                           //把原数组的元素复制到新数组中
                           System.arraycopy(values,0,newValues,0,values.length);
                           //判断待加入新数组的元素是否为空
                           if(pars.length>1){////这个值不为空
                             newValues[newValues.length-1]=pars[1];
                           }else{//这个值为空
                               newValues[newValues.length-1]="";
                           }
                           map.put(pars[0],newValues);
                       }else{//map中不包含这个值  username=tangjieyong
                           if(pars.length>1){//这个值不为空
                               map.put(pars[0],new String[]{pars[1]});
                           }else{//这个值为空
                               map.put(pars[0],new String[]{""});
                           }

                       }

                   }
               }else{  //单一参数  username=tangjieyong
                   String[] singleParameters = parameterPart.split("=");
                   if (singleParameters.length>1){//单一参数不为空
                         map.put(singleParameters[0],new String[]{singleParameters[1]});
                   }else{//单一参数为空
                      map.put(singleParameters[0],new String[]{""});
                   }

               }
           }
       }

       }

    /**
     * 获得url中参数对应的单个值
     * @param name 参数名
     * @return 参数值
     */
       public String getParameter(String name){
           String[] values = map.get(name);
           return (values!=null ||values.length!=0)? values[0]:null;
       }

    /**
     * 获得url中参数对应多个值
     * @param name 参数名
     * @return  多个参数值
     */
       public String[] getParameters(String name){
             return  map.get(name);
       }
   }



