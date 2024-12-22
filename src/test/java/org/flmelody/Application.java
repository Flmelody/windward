package org.flmelody;


import org.flmelody.core.Windward;
import org.flmelody.core.context.support.JsonWindwardContext;
import org.flmelody.support.EnhancedFunction;
import org.flmelody.util.JacksonUtil;


/**
 * @author esotericman
 */
public class Application {
    public static void main(String[] args) {
        Windward windward = Windward.setup();
        windward
                .get("/supplier", () -> "Hello World!")
                .get("/consumer", simpleWindwardContext -> {
                    simpleWindwardContext.writeString("This is a consumer");
                })
                // /name?name=esotericman
                .get("/name", (EnhancedFunction<JsonWindwardContext, String>)
                        jsonWindwardContext ->
                                JacksonUtil.toJson(String.format("{\"name\": \"%s\"}", (String) jsonWindwardContext.getRequestParameter("name"))));
        windward.run();
    }
}
