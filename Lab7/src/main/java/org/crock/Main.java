package org.crock;

import org.crock.exceptions.IncorrectOrderStateException;
import org.crock.order.Order;
import org.crock.product.localproduct.CoffeeMachine;
import org.crock.product.localproduct.Dimensions;
import org.crock.product.localproduct.Product;
import org.crock.product.localproduct.Refrigerator;

public class Main {
    public static void main(String[] args) {
        Product[] products = new Product[]{new CoffeeMachine(19_200, new Dimensions(12), 2012),
                new Refrigerator(500_250, new Dimensions(13, 15, 6))};
        Order ord = null;
        try {
            ord = new Order("Paul", "89217897658", products);
        } catch (IncorrectOrderStateException e) {
            System.out.println(e.getMessage());
        }
        try {
            ord.getMessageClient();
        } catch (IncorrectOrderStateException e) {
            System.out.println(e.getMessage());
        }
        // Увидеть ошибку
        try {
            ord.getMessageClient();
        } catch (IncorrectOrderStateException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        //Статус после создания
        System.out.println("Состояние до сборки: " + ord.getStatus());
        // Принятие заказа
        try {
            ord.collectOrder();
        } catch (IncorrectOrderStateException e) {
            System.out.println(e.getMessage());
        }

        //статус после принятия
        System.out.println("Состояние после сборки: " + ord.getStatus());

        //Повторно собрать один и тот же заказ не получится
        try {
            ord.collectOrder();
        } catch (IncorrectOrderStateException e) {
            System.out.println("Ошибка: " + e.getMessage());
        }
        //Вывод шаблонного сообщения (В Сумме к оплате возможно будет выводиться "?", но это проблема вроде iDE)
        try {
            System.out.println(ord.getMessageClient());
            ord.closeOrder();
        } catch (IncorrectOrderStateException e) {
            System.out.println(e.getMessage());
        }

    }
}