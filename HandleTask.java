
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.time.LocalDateTime;


public class HandleTask implements Runnable{
    //源文件地址
    private File file = null;
    //目标文件地址
    private File dest = null;
    //进程名称
    private String nameStr = "";

    @Override
    public void run() {
        try{
            Thread.currentThread().setName(nameStr);
            System.out.println("Time:"+LocalDateTime.now() +"任务名"+nameStr+"-----"+"线程名："+Thread.currentThread().getName());
            //处理视频文件，修改字符。
            FileInputStream in = new FileInputStream(file);
            String[] filePart = file.getName().split("\\.");
            File dest1 = new File(dest.toString(),filePart[0]+"_copy."+filePart[1]);
            FileOutputStream out = new FileOutputStream(dest1);
            byte[] fileData = new byte [61858764];
            int lineLength = in.read(fileData);
            out.write(fileData,3,fileData.length-3);
            in.read(fileData);
            while (lineLength !=-1){
                out.write(fileData);
                lineLength =  in.read(fileData);
            }
            in.close();
            out.close();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void setDest(File dest){
        this.dest = dest;
    }
    //设置线程名
    public void  setThreadName(String name){
        this.nameStr = name;
    }
    // LinkedBlockingQueue 
    public HandleTask(File file) {
        this.file = file;
    }
}
