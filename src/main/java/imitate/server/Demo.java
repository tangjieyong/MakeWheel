package imitate.server;

import imitate.javax.Request;
import imitate.javax.Response;
import imitate.javax.Servlet;

import java.io.PrintWriter;

public class Demo  implements Servlet {
    public void test(){
        System.out.println("方法执行......");
    }



    @Override
    public void service(Response response, Request requestObject) {
        String username = requestObject.getParameter("username");
        String gender = requestObject.getParameter("gender");
        PrintWriter out = response.getWriter();
        StringBuilder res=new StringBuilder();
        res.append("HTTP/1.1 200 OK\n");
        res.append("Content-Type:text/html;charset=utf-8\n\n");
        res.append("<html>");
        res.append("<head>");
        res.append("<meta Content-Type:text/html;charset=utf-8");
        res.append("<body>");
        res.append("姓名："+username);
        res.append("性别："+gender);
        res.append("/<body>");
        res.append("</head>");
        res.append("</html>");

        out.print(res);
        out.flush();

    }
}
