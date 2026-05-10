package th.co.ais.dt;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
//import th.co.ais.dt.util.JBossBackEndProperties;

@SpringBootApplication
//@EnableAutoConfiguration
//(exclude = { HibernateJpaAutoConfiguration.class })
//@ComponentScan(basePackages = "th.co.ais.dt.controller.impl;th.co.ais.dt.dao.hibernate;th.co.ais.dt.service.impl;th.co.ais.dt.properties")
//@EntityScan(basePackages = "th.co.ais.dt.dao.entity")
//@ImportRuntimeHints(DtRuntimeHints.class)
public class RestServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestServiceApplication.class, args);
		//JBossBackEndProperties.getInstance();
	}
}
