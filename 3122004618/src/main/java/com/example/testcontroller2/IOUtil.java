package com.example.testcontroller2;

import java.io.*;

/**
 * @author: lintao
 * @date: 2024/9/20 15:53
 * @description:
 */
public class IOUtil {
    public static String path = "src/main/java/com/example/text";
    public static String answer = "src/main/java/com/example/text/answer.txt";
    public static String problem = "src/main/java/com/example/text/problem.txt";
    public static String number = "src/main/java/com/example/text/number.txt";

    public static String inputProblem = "src/main/java/com/example/text/inputProblem.txt";
    public static String inputAnswer = "src/main/java/com/example/text/inputAnswer.txt";

    public static String saveAnswer = "src/main/java/com/example/text/Grade.txt";



    public static void write(String str,String file){

        try (BufferedWriter writer =new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file,true)))){
            writer.write(str);
        } catch (FileNotFoundException e) {
            File filedir = new File(path);
            filedir.mkdir();
            File file1 = new File(file);
            write(str,file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void reWrite(String str,String file){

        try (BufferedWriter writer =new BufferedWriter(new OutputStreamWriter(new FileOutputStream(file)))){
            writer.write(str);
        } catch (FileNotFoundException e) {
            File filedir = new File(path);
            filedir.mkdir();
            File file1 = new File(file);
            write(str,file);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public static String read(String file){
        //   BufferedWriter writer = null;
        StringBuilder stringBuilder = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(new FileInputStream(file)))){

            String s = null;
            while ((s=reader.readLine())!=null&&!"".equals(s)){
                stringBuilder.append(s+"\n") ;
            }

            return stringBuilder.toString();
        } catch (FileNotFoundException e) {
            System.out.println("读入文件不存在");
            System.exit(0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

}
