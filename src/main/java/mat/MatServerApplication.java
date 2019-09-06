package mat;

import liquibase.integration.spring.SpringLiquibase;
import mat.dao.audit.impl.AuditInterceptor;
import mat.hibernate.HibernateStatisticsFilter;
import org.hibernate.Hibernate;
import org.jasypt.encryption.pbe.StandardPBEStringEncryptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.jdbc.datasource.DriverManagerDataSource;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.hibernate5.support.OpenSessionInViewFilter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.support.OpenEntityManagerInViewFilter;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.Properties;

@SpringBootApplication
@EnableAutoConfiguration
@EnableTransactionManagement
@EnableJpaRepositories
@Configuration
@ComponentScan
@PropertySource("classpath:MAT.properties")
public class MatServerApplication {
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
	@Value("${spring.datasource.url}")
	private String datasourceUrl;

	@Value("${spring.datasource.driver-class-name}")
	private String dbDriverClassName;

	@Value("${spring.datasource.username}")
	private String dbUsername;

	@Value("${spring.datasource.password}")
	private String dbPassword;

	@Value("${ALGORITHM}")
	private String algorithm;

	@Value("${PASSWORDKEY}")
	private String passwordKey;

	@Bean
	public FilterRegistrationBean<HibernateStatisticsFilter> hibernateStatsFilter() {
		FilterRegistrationBean<HibernateStatisticsFilter> bean = new FilterRegistrationBean<>();

		bean.setFilter(new HibernateStatisticsFilter());
		bean.addUrlPatterns("/api/*");
		return bean;
	}


	@Bean
	public FilterRegistrationBean registerOpenEntityManagerInViewFilterBean() {
		FilterRegistrationBean registrationBean = new FilterRegistrationBean();
		OpenSessionInViewFilter filter = new OpenSessionInViewFilter();
		registrationBean.setFilter(filter);
		registrationBean.addUrlPatterns("/api/*");
		return registrationBean;
	}


	@Bean
	public DataSource dataSource() {
		final DriverManagerDataSource dataSource = new DriverManagerDataSource();

		dataSource.setDriverClassName(dbDriverClassName);
		dataSource.setUrl(datasourceUrl);
		dataSource.setUsername(dbUsername);
		dataSource.setPassword(dbPassword);

		return dataSource;
	}

	@Bean
	public StandardPBEStringEncryptor getStandardEncryptor() {
		StandardPBEStringEncryptor standardPBEStringEncryptor = new StandardPBEStringEncryptor();
		standardPBEStringEncryptor.setAlgorithm(algorithm);
		standardPBEStringEncryptor.setPassword(passwordKey);
		return standardPBEStringEncryptor;
	}

	@Bean
	public HibernateTransactionManager txManager(@Autowired LocalSessionFactoryBean sessionFactory){
		HibernateTransactionManager txManager = new HibernateTransactionManager();
		txManager.setSessionFactory(sessionFactory.getObject());
		return txManager;
	}


	@Bean
	public LocalSessionFactoryBean sessionFactory(@Autowired DataSource dataSource, @Autowired AuditInterceptor auditInterceptor){
		LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
		sessionFactory.setDataSource(dataSource);
		sessionFactory.setPackagesToScan("mat.model","mat.repository","mat.dao","mat.entity", "mat.hibernate");
		sessionFactory.setHibernateProperties(hibernateProperties());
		sessionFactory.setEntityInterceptor(auditInterceptor);
		return sessionFactory;
	}

	@Bean
	public LocalContainerEntityManagerFactoryBean entityManagerFactory(@Autowired DataSource dataSource, @Autowired AuditInterceptor auditInterceptor) {
		HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
		vendorAdapter.setGenerateDdl(false);

		LocalContainerEntityManagerFactoryBean factory = new LocalContainerEntityManagerFactoryBean();
		factory.setJpaVendorAdapter(vendorAdapter);
		//Add package to scan for entities.
		factory.setPackagesToScan("mat.model","mat.repository","mat.dao","mat.entity", "mat.hibernate");
		factory.setDataSource(dataSource);
		return factory;
	}

	private final Properties hibernateProperties() {
		Properties hibernateProperties = new Properties();
		hibernateProperties.setProperty("hibernate.enable_lazy_load_no_trans", "true");
		hibernateProperties.setProperty("hibernate.cache.region.factory_class", "org.hibernate.cache.ehcache.EhCacheRegionFactory");
		hibernateProperties.setProperty("hibernate.cache.use_query_cache","true");
		hibernateProperties.setProperty("hibernate.cache.use_second_level_cache","true");
		hibernateProperties.setProperty("hibernate.show_sql","false");
		hibernateProperties.setProperty("hibernate.default_batch_fetch_size","20");
		hibernateProperties.setProperty("hibernate.connection.release_mode","auto");
		hibernateProperties.setProperty("hibernate.cache.use_second_level_cache","true");
		hibernateProperties.setProperty("hibernate.dialect","org.hibernate.dialect.MySQL5Dialect");
		hibernateProperties.setProperty("entityInterceptor", "mat.dao.audit.impl.AuditInterceptor");
		return hibernateProperties;
	}

	@Bean
	public static PropertySourcesPlaceholderConfigurer placeHolderConfigurer() {
		PropertySourcesPlaceholderConfigurer ppc = new PropertySourcesPlaceholderConfigurer();
		Properties propertiesSource = new Properties();
		propertiesSource.setProperty("systemPropertiesMode", "2");
		ppc.setProperties(propertiesSource);
		return ppc;
	}

	@Bean
	public SpringLiquibase liquibase(@Autowired DataSource dataSource) {
		SpringLiquibase springLiquibase=new SpringLiquibase();
		springLiquibase.setDataSource(dataSource);
		springLiquibase.setChangeLog("classpath:/liquibase/changelog.xml");
		springLiquibase.setContexts("prod");
		return springLiquibase;
	}

	public static void main(String[] args) {
		SpringApplication.run(MatServerApplication.class, args);
	}
}
