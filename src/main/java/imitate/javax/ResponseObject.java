package imitate.javax;

import java.io.PrintWriter;

public class ResponseObject implements Response {
  private PrintWriter writer;

    @Override
    public PrintWriter getWriter() {
        return writer;
    }

    @Override
    public void setWriter(PrintWriter writer) {
       this.writer=writer;
    }
}
