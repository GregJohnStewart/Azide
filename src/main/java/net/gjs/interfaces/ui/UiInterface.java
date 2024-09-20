package net.gjs.interfaces.ui;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import net.gjs.interfaces.RestInterface;

public abstract class UiInterface extends RestInterface {

    protected abstract Template getPageTemplate();

    protected TemplateInstance getDefaultPageSetup(){
        return this.getPageTemplate()
                .data("user", this.requireAndGetEntity());
    }
}
