package imitate.javax;

public interface Request {
    /**
     * 根据参数名获得唯一的参数值
     * @param parameter 参数名
     * @return  参数值
     */
    public String getParameter(String parameter);

    /**
     * 根据参数名获得多个参数值
     * @param parameter 参数名
     * @return 过个参数值封装为数组
     */
    public String[] getParameters(String parameter);
}
