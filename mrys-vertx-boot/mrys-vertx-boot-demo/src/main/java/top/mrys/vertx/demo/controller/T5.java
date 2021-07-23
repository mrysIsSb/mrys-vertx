package top.mrys.vertx.demo.controller;

/**
 * @author mrys
 * @date 2021/7/19
 */
public interface T5 {

  public static void main(String[] args) {
    Queue queue = new Queue();
    queue.add(new Node());
    queue.add(new Node());
    queue.add(new Node());
    queue.reversal();
    System.out.println(1);
  }

}

class Queue {

  public Node head;

  /**
   * 末尾添加
   *
   * @author mrys
   */
  public Queue add(Node node) {
    if (head == null) {
      head = node;
      return this;
    }
    Node current = head;
    Node next=current;
    while (current != null) {
      next=current;
      current = current.next;
    }
    next.next = node;
    return this;
  }

  /**
   * 反转
   *
   * @author mrys
   */
  public Queue reversal() {
    if (head == null) {
      return this;
    }
    if (head.next == null) {
      return this;
    }
    Node prev = null;
    Node current = head;
    while (current != null) {
      Node next = current.next;
      current.next = prev;
      prev = current;
      current = next;
    }
    head = prev;
    return this;
  }

}

class Node {

  public Node next;
  public Object val;
}
