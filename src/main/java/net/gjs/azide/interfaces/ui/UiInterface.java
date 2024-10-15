package net.gjs.azide.interfaces.ui;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import net.gjs.azide.interfaces.RestInterface;

public abstract class UiInterface extends RestInterface {

    protected abstract Template getPageTemplate();

    protected TemplateInstance getDefaultPageSetup(Template template){
        return template
                .data("user", this.getFullUser());
    }

    protected TemplateInstance getDefaultPageSetup(){
        return this.getDefaultPageSetup(this.getPageTemplate());
    }
}
