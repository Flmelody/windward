package org.flmelody.core.context;

import org.flmelody.core.WindwardRequest;
import org.flmelody.core.WindwardResponse;

/**
 * @author esotericman
 */
public class TestContext extends SimpleWindwardContext{
    public TestContext(WindwardRequest windwardRequest, WindwardResponse windwardResponse) {
        super(windwardRequest, windwardResponse);
    }
}
