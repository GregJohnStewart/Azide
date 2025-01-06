package mil.army.dcgs.azide.interfaces.ui;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import mil.army.dcgs.azide.interfaces.RestInterface;

public abstract class UiInterface extends RestInterface {

    protected abstract Template getPageTemplate();

    protected TemplateInstance getDefaultPageSetup(Template template){
        return template.instance();
    }

    protected TemplateInstance getDefaultPageSetup(){
        return this.getDefaultPageSetup(this.getPageTemplate());
    }

    protected TemplateInstance getDefaultAuthPageSetup(Template template){
        return template.data("user", this.getFullUser());
    }

    protected TemplateInstance getDefaultAuthPageSetup(){
        return this.getDefaultAuthPageSetup(this.getPageTemplate());
    }
}
