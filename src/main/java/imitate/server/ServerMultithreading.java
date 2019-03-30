package imitate.server;

import imitate.javax.*;
import imitate.utils.xmlUtils;

import java.io.*;
import java.net.Socket;
import java.util.Map;

public class ServerMultithreading implements Runnable{
//    确保此处开辟的线程是之前的流对象，将之前的流对象作为参数传入
       private Socket clientSocket;
       public ServerMultithreading(Socket clientSocket){ this.clientSocket=clientSocket;}


    @Override
    public void run() {
        InputStreamReader reader=null;
        BufferedReader BFread=null;
        String filePath=null;
        try {
//            接收客户端消息
            reader =new InputStreamReader(clientSocket.getInputStream());
            BFread= new BufferedReader(reader);

//


            //     /file/a?username=aaaa&gender=1&interest=soccer



            String urlLine=BFread.readLine();//获得请求行

            //       /file/index.html
            String Path = urlLine.split(" ") [1];//截取URI

//            /file/a?username=aaaa&gender=1&interest=soccer&interest=music&interest=sleep

            //获得响应流对象
            PrintWriter writer=new PrintWriter(clientSocket.getOutputStream());
            //如果网页为静态网页读取本地的静态网页，向浏览器端写
            if(Path.endsWith(".html")||Path.endsWith(".htm")){
                  dealWithStaticPage(Path,writer);
            }else{//如果请求执行java代码（servlet）
                //
                //
                //
                //       /file/a?username=aaaa&gender=1&interest=soccer&interest=music&interest=sleep

//                解析url内的参数
                Request request=new ObjectRequest(Path);
//                获得url中请求信息     /file/a
                Path = Path.split("[?]")[0];
                String appName=  Path.split("[/]")[1];//   file  获得应用
                //获得当前应用的web.xml解析结果
                Map<String, String> appMap = xmlUtils.servletMaps.get(appName);
                //获得应用名后的地址   /a
                String urlPath=Path.substring(1+appName.length());
//               在解析结果中通过url获得对应的类路径
                String urlValue = appMap.get(urlPath);

                 Servlet servlet=null;
               //查看缓存中是否有该servlet实例，有的话直接取出来
                if(ServletCache.cacheContains(urlValue)){
                   servlet = ServletCache.getFromCache(urlValue);
                }else{//没有的话创建实例，并添加到缓存中
                 //   利用反射创建类对象
                     Object obj = Class.forName(urlValue).newInstance();
                     servlet=(Servlet)obj;
                     ServletCache.addServletToCache(urlValue,servlet);
                }
                System.out.println(servlet);
//
                //获得了url的对应类对象，但是并不知道应该执行这个对象的什么方法
                //让这个类实现servlet接口，获得类对象后可以调用接口的方法，实现类在重写接口方法时调用自己的方法

                //使用面向接口的方式编程就可以不用知道接口的具体实现类是哪个，可以用Object来接收接口类
                //再向上转型，强转为接口类

//                将响应流对象封装在响应对象中
                Response response =new ResponseObject();
                response.setWriter(writer);
//               执行类对象的service方法，方法参数中封装了请求对象和响应对象
                servlet.service(response,request);
            }
//
          }catch (IOException | ClassNotFoundException e){
            e.printStackTrace(); } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        }

        if (reader!=null){
            try {
                reader.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (BFread!=null){
            try {
                BFread.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

       }

    /**
     * 处理静态页面
     * @param filePath   静态页面的路径
     * @param writer    响应流对象
     */
    public void dealWithStaticPage(String filePath, PrintWriter writer) {
//        /file/index.html

       String pagePath = filePath.substring(1);  //   file/index.html
        //读取本地的网页
        try {
//            获取路径对应的本地流
            BufferedReader fileReader = new BufferedReader(new FileReader("src/main/webapp/WEB-INF/" + filePath));
//          读取本地的网页保存在  StringBuffer
            StringBuffer html = new StringBuffer();
            //拼接请求行(拼接的时候记得换行)
            html.append("HTTP/1.1 200 OK\n");
            html.append("Content-Type:text/html;charset=utf-8\n\n");
            String info=null;
            while ((info=fileReader.readLine())!=null){
                 html.append(info);
            }
            //强制刷新
            writer.print(html);
            writer.flush();
        } catch (IOException e) {
            //404找不到资源
            StringBuilder errorMsg=new StringBuilder();
            errorMsg.append("HTTP/1.1 200 OK\n");
            errorMsg.append("Content_Type:text/html;charset=utf-8\n\n");
            errorMsg.append("<html>");
            errorMsg.append("<head>");
            errorMsg.append("<meta Content-Type:text/html;charset=utf-8");
            errorMsg.append("</head>");
            errorMsg.append("<body>");
            errorMsg.append("HTTP 404 NOT FOUND");
            errorMsg.append("</body>");
            errorMsg.append("</html>");
            writer.print(errorMsg);
            e.printStackTrace();
            e.printStackTrace();
        }

    }
}


