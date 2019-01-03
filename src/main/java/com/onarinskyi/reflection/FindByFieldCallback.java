package com.onarinskyi.reflection;

import com.onarinskyi.annotations.ui.FindBy;
import org.openqa.selenium.By;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public class FindByFieldCallback implements ReflectionUtils.FieldCallback {

    private Object bean;

    FindByFieldCallback(Object bean) {
        this.bean = bean;
    }

    @Override
    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
        if (!field.isAnnotationPresent(FindBy.class)) {
            return;
        }

        field.setAccessible(true);
        field.set(bean, getByLocatorOf(field));
    }

    private By getByLocatorOf(Field field) {
        FindBy findByAnnotation = field.getAnnotation(FindBy.class);
        return findByAnnotation.id().isEmpty() ?
                findByAnnotation.name().isEmpty() ?
                        findByAnnotation.css().isEmpty() ?
                                findByAnnotation.xpath().isEmpty() ?
                                        findByAnnotation.text().isEmpty() ? null :
                                                By.xpath(String.format("//*[contains(text(), '%s')]", findByAnnotation.text())) :
                                        By.xpath(findByAnnotation.xpath()) :
                                By.cssSelector(findByAnnotation.css()) :
                        By.name(findByAnnotation.name()) :
                By.id(findByAnnotation.id());
    }
}
