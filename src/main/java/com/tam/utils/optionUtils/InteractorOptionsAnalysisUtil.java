package com.tam.utils.optionUtils;

import com.tam.model.InteractorTypeResource;

public class InteractorOptionsAnalysisUtil {
    public static boolean interactorIsWebAppOrBrowserOrGeneric(InteractorTypeResource type) {
        return type == InteractorTypeResource.BROWSER
                || type == InteractorTypeResource.WEB_APPLICATION
                || type == InteractorTypeResource.GENERIC_INTERACTOR;
    }

    public static boolean interactorMightAllowUserGeneratedContent(InteractorTypeResource type) {
        return type == InteractorTypeResource.GENERIC_INTERACTOR || type == InteractorTypeResource.WEB_APPLICATION;
    }
}
