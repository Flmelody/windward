package org.flmelody.core.context;

import org.flmelody.core.WindwardRequest;
import org.flmelody.core.WindwardResponse;

/**
 * @author esotericman
 */
public class EnhancedWindwardContext extends AbstractWindwardContext{
    public EnhancedWindwardContext(WindwardRequest windwardRequest, WindwardResponse windwardResponse) {
        super(windwardRequest, windwardResponse);
    }
}
