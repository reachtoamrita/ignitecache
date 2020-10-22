package com.example.ignitecacheinmemory.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.multicast.TcpDiscoveryMulticastIpFinder;
import org.apache.ignite.springdata22.repository.config.EnableIgniteRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.ignitecacheinmemory.dto.HttpRequestData;


@Configuration
@EnableIgniteRepositories(value = "com.example.ignitecacheinmemory.*")
public class IgniteCacheConfiguration
{
    @Bean
    public Ignite igniteInstance() {
        return Ignition.start(igniteConfiguration());
    }

    @Bean
    public IgniteConfiguration igniteConfiguration() {
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        igniteConfiguration.setIgniteInstanceName("requestDataInstance");
        //igniteConfiguration.setClientMode()
        igniteConfiguration.setPeerClassLoadingEnabled(true);
        igniteConfiguration.setLocalHost("127.0.0.1");
        igniteConfiguration.setDiscoverySpi(tcpDiscoverySpi());
        igniteConfiguration.setCommunicationSpi(tcpCommunicationSpi());
        igniteConfiguration.setCacheConfiguration(cacheConfiguration());
        return igniteConfiguration;
    }

    @Bean
    public TcpDiscoverySpi tcpDiscoverySpi() {
        TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
        TcpDiscoveryMulticastIpFinder ipFinder = new TcpDiscoveryMulticastIpFinder();
        ipFinder.setAddresses(Collections.singletonList("127.0.0.1:47500..47509"));
        tcpDiscoverySpi.setIpFinder(ipFinder);
        tcpDiscoverySpi.setLocalPort(47500);
        // Changing local port range. This is an optional action.
        tcpDiscoverySpi.setLocalPortRange(9);
        //tcpDiscoverySpi.setLocalAddress("localhost");
        return tcpDiscoverySpi;
    }

    @Bean
    public TcpCommunicationSpi tcpCommunicationSpi() {
        TcpCommunicationSpi communicationSpi = new TcpCommunicationSpi();
        communicationSpi.setLocalAddress("localhost");
        communicationSpi.setLocalPort(48100);
        communicationSpi.setSlowClientQueueLimit(1000);
        return communicationSpi;
    }

    @Bean
    public CacheConfiguration[] cacheConfiguration()
    {
        List<CacheConfiguration> cacheConfigurations = new ArrayList<>();
        // Defining and creating a new cache to be used by Ignite Spring Data repository.
        CacheConfiguration cacheConfiguration = new CacheConfiguration("HttpRequestDataCache");
        // Setting SQL schema for the cache.
        cacheConfiguration.setIndexedTypes(Long.class, HttpRequestData.class);
        cacheConfigurations.add(cacheConfiguration);
        return cacheConfigurations.toArray(new CacheConfiguration[cacheConfigurations.size()]);
    }
}
