package com.example.testcontroller2;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author: lintao
 * @date: 2024/9/20 13:45
 * @description:
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class MyNum {
    private Integer flag;
    private Integer up;
    private Integer down;
    private Integer integer;


    public boolean isInt(){
        return this.flag==1;
    }

    public boolean isDouble(){
        return this.flag==0;
    }

    public void setInt(){
        this.flag=1;
    }

    public void setDouble(){
        this.flag=0;
    }


    public MyNum(Integer integer){
        this.integer=integer;
        this.flag=1;
    }

    public MyNum(Integer up,Integer down){
        this.up=up;
        this.down=down;
        this.flag=0;
    }


    public void operator(MyNum myNum,String operator){
        switch (operator){
            case "+": add(myNum);break;
            case "-": del(myNum);break;
            case "*": mul(myNum);break;
            case "/": div(myNum);break;
        }

    }

    public void  gcd (){
        if (this.isDouble()) {
            int gcd = gcd(this.getUp(), this.getDown());
            this.setDown(this.getDown() / gcd);
            this.setUp(this.getUp() / gcd );
        }
    }

    private int gcd(int num1,int num2){
        if (num1%num2==0) return num2;
        return gcd(num2,num1%num2);
    }


    public void add(MyNum num){
        if (num.isInt()&&this.isInt() ){
            this.integer += num.getInteger();
        }else if (num.isDouble() && this.isDouble()){
            this.up = this.up * num.down + this.down *num.up;
            this.down = this.down * num.down;
        }else if (num.isDouble() && this.isInt() ){
            this.setDouble();
            this.up = this.integer * num.getDown() + num.getUp();
            this.integer=null;
            this.down = num.getDown();
        }else{
            this.up = this.up + num.getInteger()*this.down;
        }
    }


    public void del(MyNum num){
        if (num.isInt()&&this.isInt() ){
            this.integer -= num.getInteger();
        }else if (num.isDouble() && this.isDouble()){
            this.up = this.up * num.down - this.down *num.up;
            this.down = this.down * num.down;
        }else if (num.isDouble() && this.isInt() ){
            this.setDouble();
            this.up = this.integer * num.getDown() - num.getUp();
            this.integer=null;
            this.down = num.getDown();
        }else{
            this.up = this.up - num.getInteger()*this.down;
        }
    }



    public void mul(MyNum num){
        if (num.isInt()&&this.isInt() ){
            this.integer *= num.getInteger();
        }else if (num.isDouble() && this.isDouble()){

            this.up = this.up * num.up;
            this.down = this.down * num.down;
        }else if ( num.isDouble() && this.isInt() ){
            this.setDouble();

            this.up = this.integer* num.up;
            this.integer=null;
            this.down = num.getDown();
        }else{
            this.up = this.up *num.getInteger();
        }
    }



    public void div(MyNum num){
        if (num.isInt()&&this.isInt() ){
            this.setDouble();
            this.up= this.integer;
            this.down=num.getInteger();
            this.integer=null;

        }else if (num.isDouble() && this.isDouble()){

            this.up = this.up * num.down ;
            this.down = this.down * num.up;

        }else if (num.isDouble() && this.isInt() ){

            this.setDouble();

            this.up = this.integer * num.getDown();
            this.integer=null;
            this.down = num.getUp();

        }else{
            this.down = this.down * num.getInteger();
        }
    }

    public boolean check() {
        if (this.isInt()) return this.integer<0;
        return this.up<0||this.down<0;
    }

    public boolean checkDiv() {
        return this.up>this.down;
    }


    public String getAnswer(){
        String ans = "";
        if (this.isInt()) {
            ans = String.valueOf(this.getInteger());
        } else if (this.isDouble() && this.getDown() == 1) {
            ans = String.valueOf(this.getUp());
        } else if (this.isDouble() && this.getUp() == 0) {
            ans = String.valueOf(0);
        } else {
            int i = this.getUp() / this.getDown();
            if (i != 0) ans = i + "'";
            ans = ans + (this.getUp() % this.getDown()) + "/" + this.getDown();
        }
        return ans;
    }

}
