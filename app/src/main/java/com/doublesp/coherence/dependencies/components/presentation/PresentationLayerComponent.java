package com.doublesp.coherence.dependencies.components.presentation;

import com.doublesp.coherence.dependencies.components.domain.DomainLayerComponent;
import com.doublesp.coherence.dependencies.modules.presentation.MainActivityModule;
import com.doublesp.coherence.interfaces.scopes.PresentationLayerScope;

import dagger.Component;

/**
 * Created by pinyaoting on 11/11/16.
 */

@PresentationLayerScope
@Component(dependencies = DomainLayerComponent.class)
public interface PresentationLayerComponent {

    MainActivitySubComponent newListCompositionActivitySubComponent(
            MainActivityModule activityModule);

}
