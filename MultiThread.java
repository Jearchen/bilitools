
import java.io.File;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;


public class MultiThread {
    // cpu核心数
    private static int core = 6;
    // 有界队列容量
    private static int capcity = 12;
    // 空闲时间
    private static long keepAliveTime = 500;
    // 线程池
    private static ThreadPoolExecutor tps = null;
    //任务队列
    private static BlockingQueue<Runnable> linkQueues = new LinkedBlockingQueue<>();
    //目标目录
    private static File dest;

    //创建线程
    public void createThreadPoolExecutors() {
        linkQueues = new LinkedBlockingQueue<Runnable>();
        tps = new ThreadPoolExecutor(core, capcity, keepAliveTime, TimeUnit.MILLISECONDS, linkQueues);
    }

    public void setDestFile(File dst){
        dest = dst;
    }

    //执行线程
    public void Execute(List<File> files)  {
        for (File file : files) {
            HandleTask task = new HandleTask(file);
            task.setThreadName(file.getName());
            task.setDest(dest);
            tps.execute(task);
        }
    }

    public boolean checkIfCompleted(){
        // int size = tps.getPoolSize();
        int completedCount = (int)tps.getCompletedTaskCount();
        // int queueSize = linkQueues.size();
        int taskSize = (int)tps.getTaskCount();
        //完成数量为加入任务所有队列总数。 队列大小为待处理的任务数，为0即无代办任务。
        // System.out.println(String.format("线程池大小：%d,完成数量:%d,任务数量：%d,激活数量%d, 队列大小：%d", size,completedCount,tps.getTaskCount(),tps.getActiveCount(),queueSize));
        return completedCount == taskSize?true:false;
    }

    public int closeExecutors(){
        tps.shutdownNow();
        
        if(tps.isShutdown()){
            return 0;
        }else{
            return -1;
        }
    }
}
