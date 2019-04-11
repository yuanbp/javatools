package com.chieftain.watchdir;
 
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
 
public class MyWatchService {
 
    public static void main(String[] args) throws Exception {
        String propFileName = "/Users/chieftain/retainfiles/tmp/recv"; //要监控的文件目录
        //因为是线程安全的所以可以放入ThreadPool中使用
        ExecutorService cachedThreadPool = Executors.newFixedThreadPool(1);
        cachedThreadPool.execute(new FileWatchTask(propFileName));
    }
 
}
