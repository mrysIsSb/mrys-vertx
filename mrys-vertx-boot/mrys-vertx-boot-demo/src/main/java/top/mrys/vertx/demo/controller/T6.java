package top.mrys.vertx.demo.controller;

import java.util.HashMap;

/**
 * @author mrys
 * @date 2021/7/20
 */
public class T6 {

  public static void main(String[] args) {

    System.out.println(Integer.toBinaryString(Integer.rotateLeft(16,1)));
    int i=17;
    System.out.println(Integer.toBinaryString(Integer.bitCount(16)));
    if (Integer.bitCount(i) == 1) {
      System.out.println(Integer.toBinaryString(Integer.highestOneBit(i)));
    }else {
      System.out.println(Integer.toBinaryString(Integer.highestOneBit(i<<1)));

    }
    System.out.println(Integer.toBinaryString(Integer.highestOneBit(i)<<1));
    int n = i - 1;
    n |= n >>> 1;
    n |= n >>> 2;
    n |= n >>> 4;
    n |= n >>> 8;
    n |= n >>> 16;
    System.out.println(Integer.toBinaryString(n+1));
    System.out.println(Integer.toBinaryString(i));
    System.out.println(Integer.toBinaryString(-i));
    System.out.println(Integer.toBinaryString(Integer.lowestOneBit(i)));
    System.out.println(Integer.numberOfTrailingZeros(16));
    System.out.println(Integer.numberOfLeadingZeros(16));
  }

}
