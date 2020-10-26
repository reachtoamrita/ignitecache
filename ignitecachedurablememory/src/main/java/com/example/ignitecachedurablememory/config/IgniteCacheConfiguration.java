package com.example.ignitecachedurablememory.config;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.ignite.Ignite;
import org.apache.ignite.Ignition;
import org.apache.ignite.cache.CacheAtomicityMode;
import org.apache.ignite.cache.CacheMode;
import org.apache.ignite.cache.CacheWriteSynchronizationMode;
import org.apache.ignite.configuration.CacheConfiguration;
import org.apache.ignite.configuration.DataRegionConfiguration;
import org.apache.ignite.configuration.DataStorageConfiguration;
import org.apache.ignite.configuration.IgniteConfiguration;
import org.apache.ignite.configuration.WALMode;
import org.apache.ignite.spi.communication.tcp.TcpCommunicationSpi;
import org.apache.ignite.spi.discovery.tcp.TcpDiscoverySpi;
import org.apache.ignite.spi.discovery.tcp.ipfinder.vm.TcpDiscoveryVmIpFinder;
import org.apache.ignite.springdata22.repository.config.EnableIgniteRepositories;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.example.ignitecachedurablememory.dto.HttpRequestData;
import lombok.NonNull;

@Configuration
@EnableIgniteRepositories(value = "com.example.ignitecachedurablememory.*")
public class IgniteCacheConfiguration
{

    private final String PERSISTENT_FILE_PATH = "IgniteData";
    private final String DATA_CONFIG_NAME     = "default_dataregion";

    @Bean
    public Ignite igniteInstance()
    {
        Ignite ignite = Ignition.start(igniteConfiguration());
        ignite.cluster().active(true);
        return ignite;
    }

    @Bean
    public IgniteConfiguration igniteConfiguration()
    {
        IgniteConfiguration igniteConfiguration = new IgniteConfiguration();
        igniteConfiguration.setIgniteHome("/home/arani/itron/durable");
        igniteConfiguration.setPeerClassLoadingEnabled(true);
        igniteConfiguration.setDataStorageConfiguration(dataStorageConfiguration());
        igniteConfiguration.setCacheConfiguration(cacheConfigurations());
        igniteConfiguration.setClientMode(false);
        igniteConfiguration.setDiscoverySpi(tcpDiscoverySpi());
        igniteConfiguration.setCommunicationSpi(tcpCommunicationSpi());
        return igniteConfiguration;
    }

    @Bean
    public DataStorageConfiguration dataStorageConfiguration()
    {
        // DataStorage Configurations for Ignite Bean under creation
        DataStorageConfiguration dataStorageConfiguration = new DataStorageConfiguration();
        dataStorageConfiguration.setStoragePath(PERSISTENT_FILE_PATH + "/store");
        dataStorageConfiguration.setWalArchivePath(PERSISTENT_FILE_PATH + "/walArchive");
        dataStorageConfiguration.setWalPath(PERSISTENT_FILE_PATH + "/walStore");
        //@link https://apacheignite.readme.io/docs/write-ahead-log#section-wal-modes
        dataStorageConfiguration.setWalMode(WALMode.LOG_ONLY);
        dataStorageConfiguration.setDataRegionConfigurations(dataRegionConfiguration());
        dataStorageConfiguration.setDefaultDataRegionConfiguration
            (dataStorageConfiguration.getDefaultDataRegionConfiguration().setPersistenceEnabled(true));
        return dataStorageConfiguration;
    }

    @Bean
    public DataRegionConfiguration dataRegionConfiguration()
    {
        DataRegionConfiguration dataRegionConfiguration = new DataRegionConfiguration();
        dataRegionConfiguration.setName(DATA_CONFIG_NAME);
        dataRegionConfiguration.setPersistenceEnabled(true);
        return dataRegionConfiguration;
    }

    //Configuring TcpDiscoverySpi of ignite cluster
    //Ref :- https://apacheignite.readme.io/docs/tcpip-discovery
    @Bean
    public TcpDiscoverySpi tcpDiscoverySpi()
    {
        TcpDiscoverySpi tcpDiscoverySpi = new TcpDiscoverySpi();
        TcpDiscoveryVmIpFinder tcpDiscoveryVmIpFinder = new TcpDiscoveryVmIpFinder();
        tcpDiscoveryVmIpFinder.setAddresses(Collections.singleton("127.0.0.1"));
        tcpDiscoverySpi.setLocalPort(9170);
        tcpDiscoverySpi.setLocalPortRange(9);
        return tcpDiscoverySpi;
    }

    @Bean
    public TcpCommunicationSpi tcpCommunicationSpi()
    {
        TcpCommunicationSpi tcpCommunicationSpi = new TcpCommunicationSpi();
        //communicationSpi.setLocalAddress("localhost");
        tcpCommunicationSpi.setLocalPort(9170);
        tcpCommunicationSpi.setLocalPortRange(9);
        //communicationSpi.setSlowClientQueueLimit(1000);
        return tcpCommunicationSpi;
    }

    @Bean
    public CacheConfiguration[] cacheConfigurations()
    {
        List<CacheConfiguration> cacheConfigurations = new ArrayList<>();
        // Defining and creating a new cache to be used by Ignite Spring Data repository.
        CacheConfiguration requestDataCacheConfiguration = buildCacheConfiguration();
        requestDataCacheConfiguration.setName("HttpRequestDataCache");
        requestDataCacheConfiguration.setIndexedTypes(Long.class, HttpRequestData.class);
        cacheConfigurations.add(requestDataCacheConfiguration);
        return cacheConfigurations.toArray(new CacheConfiguration[cacheConfigurations.size()]);
    }

    @NonNull
    private CacheConfiguration buildCacheConfiguration()
    {
        CacheConfiguration cacheConfiguration = new CacheConfiguration();
        cacheConfiguration.setAtomicityMode(CacheAtomicityMode.ATOMIC);
        cacheConfiguration.setDataRegionName(DATA_CONFIG_NAME);
        cacheConfiguration.setCacheMode(CacheMode.REPLICATED);
        cacheConfiguration.setWriteSynchronizationMode(CacheWriteSynchronizationMode.FULL_SYNC);
        return cacheConfiguration;
    }
}
