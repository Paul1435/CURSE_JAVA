package org.crock.winterschool.userintetrface;

import org.crock.winterschool.kitchen.Cook;
import org.crock.winterschool.kitchen.Dish;

import java.util.Set;

public interface ManagingCooks {
    /**
     * @param cook - сотрудник для увольнения
     */
     void dismissCook(Cook cook);

    /**
     * @param cook   - сотрудник для принятия
     * @param dishes -  список его блюд
     */
     void hireCook(Cook cook, Set<Dish> dishes);

}
