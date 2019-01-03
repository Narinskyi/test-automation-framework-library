package com.onarinskyi.reflection;

import com.onarinskyi.annotations.ui.PageComponent;
import com.onarinskyi.gui.AbstractPageComponent;
import org.openqa.selenium.By;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

public class PageComponentFieldCallback implements ReflectionUtils.FieldCallback {

    private Object bean;

    PageComponentFieldCallback(Object bean) {
        this.bean = bean;
    }

    @Override
    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
        if (!field.isAnnotationPresent(PageComponent.class)) {
            return;
        }

        if (((AbstractPageComponent)field.get(bean)).getAncestor() != null) {
            return;
        }

        field.setAccessible(true);
        field.set(bean, ((AbstractPageComponent)field.get(bean)).withAncestor(getByLocatorOf(field)));
    }

    private By getByLocatorOf(Field field) {
        PageComponent findByAnnotation = field.getAnnotation(PageComponent.class);

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
