/**
 *  用LinkedList实现队列
 *
 *  队列和栈区别：队列先进先出，栈先进后出。
 *
 * author douglas
 * Created by douglas on 1/21/16.
 */

package com.douglas.ticketmanager;

import java.util.*;
import java.util.LinkedList;
import java.util.Queue;


public class ClientQueue<T> {
    private Queue<T> storage = new LinkedList<T>();

    /** 将指定的元素插入队尾 */
    public String offer(T v) {
        storage.offer(v);
        return null;
    }

    /** 检索，但是不移除队列的头，如果此队列为空，则返回 null */
    public T peek() {
        return storage.peek();
    }

    /** 检索，但是不移除此队列的头 */
    /** 此方法与 peek 方法的惟一不同是，如果此队列为空，它会抛出一个异常 */
    public T element() {
        return storage.element();
    }

    /** 检索并移除此队列的头，如果队列为空，则返回 null */
    public T poll() {
        return storage.poll();
    }

    /** 检索并移除此队列的头 */
    /** 此方法与 poll 方法的不同在于，如果此队列为空，它会抛出一个异常 */
    public T remove() {
        return storage.remove();
    }

    /** 队列是否为空 */
    public boolean empty() {
        return storage.isEmpty();
    }

    /** 打印队列元素 */
    public String toString() {
        return storage.toString();
    }

    /*
    public static void main(String[] args) {
        ClientQueue stack=new ClientQueue<String>();
        stack.offer("Cilenta");
        stack.offer("Cilentb");
        stack.offer("Cilentc");

        //[a, b, c]
        System.out.println(stack.toString());

        Object obj=stack.peek();
        // a--[a, b, c]
        System.out.println(obj+"--"+stack.toString());
        obj=stack.element();
        // a--[a, b, c]
        System.out.println(obj+"--"+stack.toString());
        obj=stack.poll();
        //a--[b, c]
        System.out.println(obj+"--"+stack.toString());
        obj=stack.remove();
        //b--[c]
        System.out.println(obj+"--"+stack.toString());
        //false
        System.out.println(stack.empty());
    }
    */

/*
        public class BigPlate {

            public void putClient(Object Client) {
                try {
                    queue.put(Client);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Put Client");
            }

            public Object getClient() {
                Object Client = null;
                try {
                    Client = queue.take();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Get Client");
                return Client;
            }

            static class AddThread extends Thread {
                private BigPlate plate;
                private Object Client = new Object();

                public AddThread(BigPlate plate) {
                    this.plate = plate;
                }

                public void run() {
                    plate.putClient(Client);
                }
            }

            static class GetThread extends Thread {
                private BigPlate plate;

                public GetThread(BigPlate plate) {
                    this.plate = plate;
                }

                public void run() {
                    plate.getClient();
                }
            }
        }
*/
}
