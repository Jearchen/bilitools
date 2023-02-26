
import java.io.File;
import java.time.LocalDateTime;
import java.util.LinkedList;
import java.util.List;

public class ExecMain {
    // 文件夹所有视频文件
    private static List<File> fileLinks = new LinkedList<File>();

    public static void recurseFile(File file) throws Exception {
        if (file.isFile() && file.toString().endsWith("mp4")) {
            fileLinks.add(file);
        } else if (file.isDirectory()) {
            File[] listFile = file.listFiles();
            for (int i = 0; i < listFile.length; i++) {
                recurseFile(listFile[i]);
            }
        }
    }
    public static void checkArgsValidate(String[] args) throws Exception{
        if(args.length>2){
            throw new Exception(String.format("请输出两个参数个数,格式:java %s %s",args[0],args[1]));
        }

        if((!new File(args[0]).isDirectory())||(!new File(args[1]).isDirectory()))
        {
            throw new Exception("请输入有效的目录");
        }
    }

    public static void main(String[] args) throws Exception {
        checkArgsValidate(args);
        File dirs = new File(args[0]);
        File dest = new File(args[1]);
        
        File[] files = dirs.listFiles();
        for (int i = 0; i < files.length; i++) {
            recurseFile(files[i]);
        }
        MultiThread mThreads = new MultiThread();
        mThreads.setDestFile(dest);
        mThreads.createThreadPoolExecutors();
        mThreads.Execute(fileLinks);
        Thread deamonThread = new Thread(()->{
            try {
                System.out.println("Time:"+LocalDateTime.now()+"开始检查线程池执行情况");
                while(true){
                    Thread.sleep(1000);
                    if(mThreads.checkIfCompleted())
                    {
                        System.out.println("Time:"+LocalDateTime.now()+"关闭守护线程");
                        mThreads.closeExecutors();
                        Thread.currentThread().interrupt();
                    }
                }
            } catch (InterruptedException e) {
                System.out.println("Time"+LocalDateTime.now()+"守护进程中断错误:"+e.getMessage());
            }
        });
        deamonThread.setDaemon(true);
        deamonThread.start();
    }
}
