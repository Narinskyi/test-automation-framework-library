package com.onarinskyi.reflection;

import com.onarinskyi.annotations.ui.PageComponentClass;
import com.onarinskyi.gui.AbstractPageComponent;
import org.openqa.selenium.By;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public class PageComponentClassCallback implements ReflectionUtils.FieldCallback {

    private Object bean;

    PageComponentClassCallback(Object bean) {
        this.bean = bean;
    }

    @Override
    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
        if (!bean.getClass().isAnnotationPresent(PageComponentClass.class)) {
            return;
        }

        if (((AbstractPageComponent) (bean)).getLocator() != null) {
            return;
        }

        if (!field.getName().contains("locator")) {
            return;
        }

        field.setAccessible(true);
        field.set(bean, getByLocatorOf(bean));
    }

    private By getByLocatorOf(Object bean) {
        PageComponentClass findByAnnotation = bean.getClass().getAnnotation(PageComponentClass.class);

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
