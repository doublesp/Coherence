package com.doublesp.coherence.dependencies.components.presentation;

import com.doublesp.coherence.dependencies.components.domain.DomainLayerComponent;
import com.doublesp.coherence.dependencies.modules.presentation.MainActivityModule;
import com.doublesp.coherence.interfaces.scopes.PresentationLayerScope;

import dagger.Component;

@PresentationLayerScope
@Component(dependencies = DomainLayerComponent.class)
public interface PresentationLayerComponent {

    MainActivitySubComponent newListCompositionActivitySubComponent(
            MainActivityModule activityModule);

}
