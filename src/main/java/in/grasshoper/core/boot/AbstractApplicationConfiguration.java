package in.grasshoper.core.boot;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.apache.catalina.connector.Connector;
import org.apache.commons.io.FileUtils;
import org.apache.coyote.http11.Http11NioProtocol;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.flyway.FlywayDataSource;
import org.springframework.boot.context.embedded.EmbeddedServletContainerFactory;
import org.springframework.boot.context.embedded.tomcat.TomcatEmbeddedServletContainerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.web.servlet.DispatcherServlet;


@EnableTransactionManagement(proxyTargetClass = true)
@EnableJpaRepositories("in.grasshoper.*")
@EnableJpaAuditing(auditorAwareRef = "auditorAwareImpl")
@EnableAsync
@ComponentScan(basePackages = { "in.grasshoper.*" })
@ImportResource({ "classpath*:META-INF/spring/appContext.xml" })
@EnableAutoConfiguration
public abstract class AbstractApplicationConfiguration {
	@Bean
    public EmbeddedServletContainerFactory servletContainer() {
        TomcatEmbeddedServletContainerFactory tomcat = new TomcatEmbeddedServletContainerFactory();
        tomcat.setContextPath(getContextPath());
        tomcat.addAdditionalTomcatConnectors(createSslConnector());
        return tomcat;
    }

	
	private String getContextPath() {
        return "/grasshoper-core";
    }
	
	@Bean   
    public DispatcherServlet dispatcherServlet() {
        DispatcherServlet ds = new DispatcherServlet();
        ds.setThrowExceptionIfNoHandlerFound(true);
        return ds;
    }
	
	protected Connector createSslConnector() {
        Connector connector = new Connector("org.apache.coyote.http11.Http11NioProtocol");
        Http11NioProtocol protocol = (Http11NioProtocol) connector.getProtocolHandler();
        try {
            File keystore = getFile(getKeystore());
            File truststore = keystore;
            connector.setScheme("https");
            connector.setSecure(true);
            connector.setPort(getHTTPSPort());
            protocol.setSSLEnabled(true);
            protocol.setKeystoreFile(keystore.getAbsolutePath());
            protocol.setKeystorePass(getKeystorePass());
            protocol.setTruststoreFile(truststore.getAbsolutePath());
            protocol.setTruststorePass(getKeystorePass());
            // ? protocol.setKeyAlias("apitester");
            return connector;
        } catch (IOException ex) {
            throw new IllegalStateException("can't access keystore: [" + "keystore" + "] or truststore: [" + "keystore" + "]", ex);
        }
	}
	protected int getHTTPSPort() {
        // TODO This shouldn't be hard-coded here, but configurable
        return 8443;
    }

    protected String getKeystorePass() {
        return "massslkey123";
    }

    protected Resource getKeystore() {
        return new ClassPathResource("/keystore.jks");
    }
    
    public File getFile(Resource resource) throws IOException {
        try {
            return resource.getFile();
        } catch (IOException e) {
            // Uops.. OK, try again (below)
        }

        try {
            URL url = resource.getURL();
            /**
             * // If this creates filenames that are too long on Win, // then
             * could just use resource.getFilename(), // even though not unique,
             * real risk prob. min.bon String tempDir =
             * System.getProperty("java.io.tmpdir"); tempDir = tempDir + "/" +
             * getClass().getSimpleName() + "/"; String path = url.getPath();
             * String uniqName = path.replace("file:/", "").replace('!', '_');
             * String tempFullPath = tempDir + uniqName;
             **/
            // instead of File.createTempFile(prefix?, suffix?);
            File targetFile = new File(resource.getFilename());
            long len = resource.contentLength();
            if (!targetFile.exists() || targetFile.length() != len) { // Only
                                                                      // copy
                                                                      // new
                                                                      // files
                FileUtils.copyURLToFile(url, targetFile);
            }
            return targetFile;
        } catch (IOException e) {
            // Uops.. erm, give up:
            throw new IOException("Cannot obtain a File for Resource: " + resource.toString(), e);
        }

    }

}
