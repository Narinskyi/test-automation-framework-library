package com.onarinskyi.gui;

import com.onarinskyi.annotations.ui.PageComponentClass;
import com.onarinskyi.driver.WebDriverDecorator;
import org.openqa.selenium.By;
import org.openqa.selenium.support.pagefactory.ByChained;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Scope;

@PageComponentClass
@Scope("prototype")
public abstract class AbstractPageComponent {

    @Autowired
    protected WebDriverDecorator driver;

    protected By locator;

    protected By ancestor;

    public By getLocator() {
        return locator;
    }

    public By getAncestor() {
        return ancestor;
    }

    public <T extends AbstractPageComponent> T withAncestor(By ancestor) {
        this.ancestor = ancestor;
        return (T) this;
    }

    protected ByChained chained(By... bys) {
        //if ancestor is not passed, it is assumed that it's html tag
        if (bys[0] == null) {
            bys[0] = By.cssSelector("html");
        }
        return new ByChained(bys);
    }
}
