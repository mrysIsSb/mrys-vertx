package top.mrys.vertx.demo.controller;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author mrys
 * @date 2021/7/19
 */
public class Test3 {

  private static ReentrantLock lock = new ReentrantLock();
  private static Condition condition = lock.newCondition();
  private static Integer i = 0;

  public static void main(String[] args) throws InterruptedException {
    Runnable runnable = () -> {
      try {
        lock.lock();
        while (i < 100) {
          condition.signalAll();
          System.out.println(Thread.currentThread().getName() + ":" + ++i);
          condition.await(1,TimeUnit.SECONDS);
        }
      } catch (InterruptedException e) {
        e.printStackTrace();
      } finally {
        lock.unlock();
      }
    };
    Thread thread1 = new Thread(runnable);
    thread1.start();
    TimeUnit.SECONDS.sleep(2);
    Thread thread2 = new Thread(runnable);
    thread2.start();
    thread1.join();
    thread2.join();
  }

}
