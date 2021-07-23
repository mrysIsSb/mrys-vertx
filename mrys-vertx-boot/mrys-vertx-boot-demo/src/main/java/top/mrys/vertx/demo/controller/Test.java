package top.mrys.vertx.demo.controller;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PipedInputStream;
import java.io.RandomAccessFile;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;

/**
 * @author mrys
 * @date 2021/7/19
 */
public class Test {

  public static void main(String[] args) {
//    new RandomAccessFile()
//    new BufferedInputStream(new FileInputStream(""));
//    System.out.println(Arrays.toString("|你好".getBytes()));
//    System.out.println(Arrays.toString("|".getBytes()));
//    try (BufferedInputStream inputStream = new BufferedInputStream(new FileInputStream(""));){
//      byte[] bys = new byte[1024] ;
//      int len = 0 ;
//      while((len=inputStream.read(bys))!=-1) {
//        System.out.println(new String(bys, 0, len));  //通过使用平台的默认字符集解码指定的 byte 子数组，构造一个新的 String。
//      }
//
//
//    } catch (FileNotFoundException e) {
//      e.printStackTrace();
//    } catch (IOException e) {
//      e.printStackTrace();
//    }

//    Stream.generate(() -> {
//      return (Integer)null;
//    })
//        .map(new Function<Integer, Integer>() {
//      private Integer sum=0;
//      @Override
//      public Integer apply(Integer integer) {
//        if (integer == null) {
//          return null;
//        }
//        sum+=integer;
//        if (sum >= 10) {
//          int t=sum;
//          sum=0;
//          return t;
//        }
//        return 0;
//      }
//    }).ifilter(integer -> integer>0)
//        .peek(System.out::println)
//        .anyMatch(Objects::isNull);
//
  }
}
