package th.co.ais.dt;

import org.springframework.aot.hint.RuntimeHints;
import org.springframework.aot.hint.RuntimeHintsRegistrar;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class DtRuntimeHints implements RuntimeHintsRegistrar {

    @Override
    public void registerHints(RuntimeHints hints, ClassLoader classLoader) {
    	
    	log.info("QueueRuntimeHints  runnnnnnnnnnnnnnnnnnnAAAA------");
    	//see information https://docs.spring.io/spring-boot/docs/current/reference/html/native-image.html#native-image.advanced.custom-hints
        // 2 way can registerHint
    	 //  - 4.4. Custom Hints via this class
    	 //  - 4.3.1. Launch the Application Directly
    	
    	// Register method for reflection
        //Method method = ReflectionUtils.findMethod(MyClass.class, "sayHello", String.class);
        //hints.reflection().registerMethod(method, ExecutableMode.INVOKE);
        

        // Register resources
        //hints.resources().registerPattern("config/*.properties"); 

        // Register serialization
//        hints.serialization().registerType(net.sf.jasperreports.engine.base.JRBaseStyle.class);
//        hints.serialization().registerType(java.lang.Number.class);
//        hints.serialization().registerType(java.lang.Float.class);
//        hints.serialization().registerType(java.lang.Long.class);
//        hints.serialization().registerType(java.lang.Byte.class);
//        hints.serialization().registerType(java.awt.Color.class);
//        hints.serialization().registerType(java.lang.Double.class);
//        hints.serialization().registerType(java.lang.Short.class);
       
        // Register proxy
        //hints.proxies().registerJdkProxy(MyInterface.class);
    }

}
