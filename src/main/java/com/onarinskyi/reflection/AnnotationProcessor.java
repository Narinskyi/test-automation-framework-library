package com.onarinskyi.reflection;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.stereotype.Component;

import static org.springframework.util.ReflectionUtils.doWithFields;

@Component
public class AnnotationProcessor implements BeanPostProcessor {

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        this.inject(bean);
        return bean;
    }

    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        update(bean);
        return bean;
    }

    private void inject(Object bean) {
        doWithFields(bean.getClass(), new FindByFieldCallback(bean));
    }

    private void update(Object bean) {
        doWithFields(bean.getClass(), new PageComponentFieldCallback(bean));
        doWithFields(bean.getClass(), new PageComponentClassCallback(bean));
    }
}
