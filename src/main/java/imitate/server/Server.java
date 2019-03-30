package imitate.server;

import imitate.utils.xmlUtils;
import org.dom4j.DocumentException;
import org.junit.Test;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;


public class Server {
    @Test
    public void receiveMsgFromClient(){
        try {
            //创建套接字绑定端口
            ServerSocket serverSocket=new ServerSocket(8080);
            //解析服务器中的应用与其应用中的web.xml
            String[]apps={"file"};
            xmlUtils.parse(apps);
            //监听该端口
            while (true){//让服务器一直监听(若服务器未被访问此处线程阻塞)
                Socket acceptSocket = serverSocket.accept();
                //服务器监听到访问动作的时候就会开辟一个线程来进行连接
                //为保证创建的线程基于当前的socket，将当前的socket作为参数传递给线程
                new Thread(new ServerMultithreading(acceptSocket)).start();
                //完成连接后再一次监听，因为服务器在没有监听到连接时一直是线程阻塞，所以new Thread()操作不会一直执行
            }
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }

    }
}
