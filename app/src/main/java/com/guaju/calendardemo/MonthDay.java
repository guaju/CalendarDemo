package com.guaju.calendardemo;

/**
 * Created by root on 17-7-12.
 */

public class MonthDay {

    public static int getMaxDayNum(int year,int month){
        int num=0;
        if (1==month||3==month
                ||5==month||7==month
                ||8==month||10==month
                ||12==month){
            num=31;
        }else if(month!=2){
            num=30;
        }else{
            if (year%100==0){
                if (year%4==0){
                    num=29;
                }
            }else if(year%4==0){
                num=29;
            }else{
            num=28;
            }
        }

       return num;
    }

}
