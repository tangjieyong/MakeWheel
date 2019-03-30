package imitate.javax;


import java.io.PrintWriter;

public interface Response {
   public PrintWriter getWriter();
   public void setWriter(PrintWriter writer);
}
