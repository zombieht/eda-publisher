package com.yunwutech.mercury.eda.publisher.config;


import com.yunwutech.mercury.eda.publisher.client.MpsFeignClient;
import com.yunwutech.mercury.eda.publisher.service.EdaTemplate;
import feign.Client;
import feign.Contract;
import feign.Feign;
import feign.codec.Decoder;
import feign.codec.Encoder;
import io.github.jhipster.security.uaa.LoadBalancedResourceDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.cloud.netflix.feign.FeignClientsConfiguration;
import org.springframework.cloud.security.oauth2.client.feign.OAuth2FeignRequestInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.security.oauth2.client.DefaultOAuth2ClientContext;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

@Configuration
@Import(FeignClientsConfiguration.class)
public class EdaEventAutoConfigure {


    @Autowired
    private MpsFeignClient mpsFeignClient;
    private Decoder decoder;
    private Encoder encoder;
    private Client client;
    private Contract contract;
    private LoadBalancedResourceDetails loadBalancedResourceDetails;

    @Bean
    @ConditionalOnMissingBean
    public EdaTemplate msgService() {
        return new EdaTemplate();
    }

    @Autowired
    public EdaEventAutoConfigure(Decoder decoder, Encoder encoder, Client client, Contract contract, LoadBalancedResourceDetails loadBalancedResourceDetails) {
        this.decoder = decoder;
        this.encoder = encoder;
        this.client = client;
        this.contract = contract;
        this.loadBalancedResourceDetails = loadBalancedResourceDetails;
    }

    @Bean
    public MpsFeignClient fb() {
        MpsFeignClient mpsFeignClient = Feign.builder().client(client)
                .encoder(encoder)
                .decoder(decoder)
                .contract(contract)
                .requestInterceptor(new OAuth2FeignRequestInterceptor(new DefaultOAuth2ClientContext(), loadBalancedResourceDetails))
                .target(MpsFeignClient.class, "http://MPS");
        return mpsFeignClient;
    }


    @Autowired
    DataSource dataSource;

    @PostConstruct
    private void create() {

        Connection conn = null;
        Statement stmt = null;
        try {
            conn = dataSource.getConnection();
            stmt = conn.createStatement();


            String sql = "CREATE TABLE IF NOT EXISTS `eda_event` (\n" +
                    "  `id` bigint(20) NOT NULL AUTO_INCREMENT,\n" +
                    "  `created_by` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,\n" +
                    "  `created_date` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,\n" +
                    "  `last_modified_by` varchar(50) COLLATE utf8_unicode_ci DEFAULT NULL,\n" +
                    "  `last_modified_date` timestamp NULL DEFAULT NULL,\n" +
                    "  `from_system` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,\n" +
                    "  `has_read` bit(1) DEFAULT NULL,\n" +
                    "  `identitier` bigint(20) DEFAULT NULL,\n" +
                    "  `message_content` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,\n" +
                    "  `message_type` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,\n" +
                    "  `notice` int(11) DEFAULT NULL,\n" +
                    "  `received` bigint(20) DEFAULT NULL,\n" +
                    "  `send_time` timestamp NULL DEFAULT NULL,\n" +
                    "  `sender` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,\n" +
                    "  `to_system` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL,\n" +
                    "  `guid` varchar(255) COLLATE utf8_unicode_ci DEFAULT NULL unique,\n"+
                    "  PRIMARY KEY (`id`)\n" +
                    ")";

            stmt.executeUpdate(sql);

        } catch (SQLException se) {

            se.printStackTrace();
        } catch (Exception e) {

            e.printStackTrace();
        } finally {

            try {
                if (stmt != null)
                    conn.close();
            } catch (SQLException se) {
            }
            try {
                if (conn != null)
                    conn.close();
            } catch (SQLException se) {
                se.printStackTrace();
            }
        }

    }
}
