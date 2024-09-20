package com.example.testcontroller2;

import java.math.BigDecimal;
import java.util.*;

/**
 * @author: lintao
 * @date: 2024/9/12 19:18
 * @description:
 */
public class Test {
    public static Random random = new Random();
    public static Integer n = null; // 十个题目
    public static Integer max = null; //最大值为十
    public static int t = 3;
    public static String[] strArr = new String[]{"+", "-", "*", "/"}; //算术运算符
    public static String[] KuoHao = new String[]{"(", ")"};

    public static int number = 0;

    public static void main(String[] args) {



        // 遍历命令行参数，匹配参数类型
        for (int i = 0; i < args.length; i += 2) {
            // System.out.println(args[i]);
            switch (args[i]) {
                case "-n":
                    System.out.println("数目参数" + args[i + 1]);
                    n = Integer.valueOf(args[i + 1]);
                    break;
                case "-r":
                    System.out.println("范围参数" + args[i + 1]);
                    max = Integer.valueOf(args[i + 1]);
                    break;
                case "-e":
                    System.out.println("题目文件" + args[i + 1]);
                    IOUtil.inputProblem = IOUtil.path + args[i + 1];
                    break;
                case "-a":
                    System.out.println("答案文件" + args[i + 1]);
                    IOUtil.inputAnswer = IOUtil.path + args[i + 1];
                    break;
            }
        }

        //如何接收参数
        //要求真分数就是 1'1/2
        //这个参数是生成多少个题目

        if (n!=null&&max!=null){
            number = Integer.valueOf(IOUtil.read(IOUtil.number));
            while (n> 0) {
                if (answer("")) {
                    n--;
                }
            }
        } else if (IOUtil.inputProblem!=null&& IOUtil.inputAnswer!=null ){
            //接下来是接收文件进行题目判别情况

            String inputProblem = IOUtil.read(IOUtil.inputProblem);
            String inputAnswer  = IOUtil.read(IOUtil.inputAnswer);

            System.out.println(inputAnswer);
            String[] AnsSplit = inputAnswer.split("\n");
            for (int i = 0; i < AnsSplit.length; i++) {
                AnsSplit[i] = AnsSplit[i].substring(AnsSplit[i].indexOf(".")+1);
            }

            List<Integer> right = new ArrayList<>();
            List<Integer> error = new ArrayList<>();

            String[] proSplit = inputProblem.split("\n");
            for (int i = 0; i < proSplit.length; i++) {
                String s = proSplit[i];
                int begin = s.indexOf(".")+1;
                int end = s.indexOf("=");
                s=  s.substring(begin,end);
                try {
                    MyNum compute = compute(s);
                    Integer num = Integer.valueOf(proSplit[i].substring(0, proSplit[i].indexOf(".")));
                    if (compute.getAnswer().equals(AnsSplit[i])) {
                        right.add(num);
                    }else {
                        error.add(num);
                    }
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
            String correct = "Correct: "+right.size()+" ";
            StringJoiner sj = new StringJoiner(",","(",")");
            for (int i = 0; i < right.size(); i++) {
                sj.add(String.valueOf(right.get(i)));
            }
            correct += sj.toString()+"\n";

            sj =new StringJoiner(",","(",")");
            String wrong = "Wrong: "+error.size()+" ";

            for (int i = 0; i < error.size(); i++) {
                sj.add(String.valueOf(error.get(i)));
            }
            wrong += sj.toString()+"\n";

            String an = correct + wrong;

            IOUtil.reWrite(an,IOUtil.saveAnswer);

            System.out.println(an);
        }



    }

    private static boolean answer(String s) {
        if (s==null||s.equals(""))s = createPromble();
        //以下是开启解决算式的方法
        s = change(s);
        MyNum compute = null;
        try {
            compute = compute(s);
        } catch (Exception e) {
            return false;
        }
        String ans = "";
        ans = compute.getAnswer();
        s =  number + "."+ s + " = \n";
        IOUtil.write(s,IOUtil.problem);
        IOUtil.write(number++ + "." + ans+"\n",IOUtil.answer);
        IOUtil.reWrite(String.valueOf(number),IOUtil.number);

        return true;
    }

    private static String createPromble() {
        StringJoiner stringBuilder = new StringJoiner(" ", " ", " ");
        String e0 = getNum(random.nextInt(2));
        stringBuilder.add(e0);
        for (int i = 0; i < t; i++) {
            String fuHao = strArr[random.nextInt(strArr.length)];
            String e1 = getNum(random.nextInt(2));

            if (fuHao.equals("/") && e1.equals("0")) {
                i--;
                continue;
            }

            stringBuilder.add(fuHao);
            stringBuilder.add(e1);
        }

        String s = stringBuilder.toString();
        //这里之前是生成算式
        return s;
    }

    private static MyNum compute(String s) throws Exception {
        // String s = e0.replaceAll(" ", "");
        // System.out.println(s);

        List<MyNum> list = new ArrayList<>();
        List<String> stringList = new ArrayList<>();
        for (String s1 : s.split(" ")) {
            if (s1.length() == 0 || s1.equals("")) continue;
            if (s1.equals("+") || s1.equals("*") || s1.equals("-") || s1.equals("/")) {
                stringList.add(s1);
                continue;
            }

            if (s1.contains("/")) {
                String[] split = s1.split("/");
                Integer up = Integer.valueOf(split[0]);
                Integer down = Integer.valueOf(split[1]);
                list.add(new MyNum(up, down));
            } else {
                list.add(new MyNum(Integer.valueOf(s1)));
            }
        }

        //这里直接计算可行？
        //两次for循环 一次乘除 一次加减
        int current = 0;
        for (int i = 0; i < stringList.size(); i++) {
            String s1 = stringList.get(i);
            if (s1.equals("*") || s1.equals("/")) {
                list.get(current).operator(list.remove(current + 1), s1);
                if (list.get(current).check()||(s1.equals("/")) && list.get(current).checkDiv()   ) {
                    throw new Exception();
                }
            } else {
                current++;
            }
        }

        current = 0;
        for (int i = 0; i < stringList.size(); i++) {
            String s1 = stringList.get(i);
            if (s1.equals("+") || s1.equals("-")) {
                list.get(current).operator(list.remove(current + 1), s1);
                if (list.get(current).check()) {
                    throw new Exception();
                }
            }
        }


        //将所有数字转化为加减乘除
        //我很好奇 括号怎么处理
        //0 * 2 * 1 * 5 / 4
        MyNum myNum = list.get(0);
        myNum.gcd();
        return myNum;

    }

    private static String change(String s) {
        //转化假分数
        for (int i = 0; i < s.length(); i++) {
            if (s.charAt(i) == '\'') {
                String temp = s.substring(i - 1, i + 4);
                char num1 = s.charAt(i - 1);
                char num2 = s.charAt(i + 1);
                char num3 = s.charAt(i + 3);
                Integer temp1 = Integer.valueOf(num1) - 48;
                Integer temp2 = Integer.valueOf(num2) - 48;
                Integer temp3 = Integer.valueOf(num3) - 48;
                s = s.replaceFirst(temp, (temp1 * temp3 + temp2) + "/" + temp3);
                i = -1;
            }
        }
        return s;
    }


    public static String getNum(int n) {
        if (n == 0) return getZhengFengShu();
        if (n == 1) return getInt();
        return null;
    }

    public static String getZhengFengShu(int num1, int num2) {
        int gcd = gcd(num1, num2);
        num1 = num1 / gcd;
        num2 = num2 / gcd;
        int i = num1 / num2;
        if (num1 % num2 == 0) return num1 / num2 + "";

        if (i == 0) {
            return num1 + "/" + num2;
        }
        return i + "'" + (num1 % num2) + "/" + num2;
    }


    public static String getZhengFengShu() {
        int num1 = random.nextInt(max);
        int num2 = random.nextInt(max - 1) + 1;
        return getZhengFengShu(num1, num2);
    }


    public static String getInt() {
        return String.valueOf(random.nextInt(max));
    }

    public static int gcd(int num1, int num2) {
        if (num1 % num2 == 0) return num2;
        return gcd(num2, num1 % num2);
    }


}
