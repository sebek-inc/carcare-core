package inc.sebec.carcare.core.config;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.github.cloudyrock.mongock.SpringMongock;
import com.github.cloudyrock.mongock.SpringMongockBuilder;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;

import inc.sebec.carcare.core.changelog.MongoChangeLog;


@Configuration
public class ApplicationConfig {
	@Value("${spring.data.mongodb.host}")
	private String dbHost;
	@Value("${spring.data.mongodb.port}")
	private Integer dbPort;
	@Value("${spring.data.mongodb.database}")
	private String dbName;
	@Value("${spring.data.mongodb.username:}")
	private String dbUsername;
	@Value("${spring.data.mongodb.password:}")
	private String dbPassword;

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public SpringMongock mongock() {
		MongoClient mongoclient = new MongoClient(new MongoClientURI(getMongoUri()));
		return new SpringMongockBuilder(mongoclient, dbName, MongoChangeLog.class.getPackage().getName())
				.setLockQuickConfig()
				.build();
	}

	private String getMongoUri() {
		var creds = StringUtils.isNotEmpty(dbUsername) && StringUtils.isNotEmpty(dbPassword)
					? dbUsername + ":" + dbPassword + "@"
					: "";
		return "mongodb://" + creds + dbHost + ":" + dbPort;
	}
}
