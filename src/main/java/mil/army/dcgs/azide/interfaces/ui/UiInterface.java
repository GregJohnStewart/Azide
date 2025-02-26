package mil.army.dcgs.azide.interfaces.ui;

import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import jakarta.inject.Inject;
import lombok.AccessLevel;
import lombok.Getter;
import mil.army.dcgs.azide.config.ClassificationBannerConfig;
import mil.army.dcgs.azide.interfaces.RestInterface;

public abstract class UiInterface extends RestInterface {

    @Inject
    @Getter(AccessLevel.PROTECTED)
    ClassificationBannerConfig classificationBannerConfig;

    protected abstract Template getPageTemplate();

    protected TemplateInstance getDefaultPageSetup(Template template){
        return template
                .data("user", null)
                .data("profile", null)
                .data("classificationBanner", this.classificationBannerConfig);
    }

    protected TemplateInstance getDefaultPageSetup(){
        return this.getDefaultPageSetup(this.getPageTemplate());
    }

    protected TemplateInstance getDefaultAuthPageSetup(Template template){
        return this.getDefaultPageSetup(template)
            .data("user", this.getAccessToken())
            .data("profile", this.getFullProfile());
    }

    protected TemplateInstance getDefaultAuthPageSetup(){
        return this.getDefaultAuthPageSetup(this.getPageTemplate());
    }
}
