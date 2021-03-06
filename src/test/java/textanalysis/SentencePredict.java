package textanalysis;

import com.google.common.io.Files;
import org.junit.Test;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

/**
 * 测试不带权值匹配词典的方法（采用的是台湾大学的情感极性词典NTUSD ）
 * Created by sssd on 2017/7/19.
 */

public class SentencePredict {

    @Test
    public void outPrint()throws Exception{

        SentencePredcit sentencePredcit = new SentencePredcit();
        InputPath inputPath = new InputPath();

        int method = 2;
        String negPath = "C:\\Users\\sssd\\Desktop\\data\\NTUSD_negative_simplified.tx";
        String posPath = "C:\\Users\\sssd\\Desktop\\data\\NTUSD_positive_simplified.txt";
        inputPath.setNegPATH(negPath);
        inputPath.setPosPath(posPath);
        sentencePredcit.initTrain(method,inputPath);

        List<String> lines = Files.readLines(new File("C:\\Users\\sssd\\Desktop\\data\\train\\posdata.txt"), Charset.forName("UTF-8"));
        int posNum = 1;
        int posNumPre = 0;
        long posstartT = System.currentTimeMillis();
        for (String line : lines) {
            List preLabel = sentencePredcit.sensePredict(line);
//            System.out.println(posNum + " 预测值为："+preLabel.get(0));
            posNum++;
            if ((Double)preLabel.get(0) > 0.0){
                posNumPre++;
            }
        }
        long posendT = System.currentTimeMillis();
        System.out.println("正面文本准确率为："+ (double)posNumPre/(posNum-1));
        System.out.println(String.format("%d 条文本耗时 %d ms, 平均： %f ms/条", lines.size(), (posendT - posstartT), ((posendT - posstartT) / (float)posNum)));


        List<String> negLines = Files.readLines(new File("C:\\Users\\sssd\\Desktop\\data\\train\\negdata.txt"), Charset.forName("UTF-8"));
        int negNum = 1;
        int negNumPre = 0;
        for (String line : negLines) {
            List preLabel = sentencePredcit.sensePredict(line);
//            System.out.println(negNum + " 预测值为："+preLabel.get(0));
            negNum++;
            if ((Double)preLabel.get(0) < 0.0){
                negNumPre++;
            }
        }
        long negendT = System.currentTimeMillis();
        System.out.println("反面文本准确率为："+ (double)negNumPre/(negNum-1));
        System.out.println("预测总的文本时间为：" +(negendT- posstartT));
        System.out.println("平均预测一个文本时间为：" +(negendT- posstartT)/(posNum+negNum-2));
    }
}
