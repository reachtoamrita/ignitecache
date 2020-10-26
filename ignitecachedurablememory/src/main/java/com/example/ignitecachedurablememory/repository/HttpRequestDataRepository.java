package com.example.ignitecachedurablememory.repository;

import org.apache.ignite.springdata22.repository.IgniteRepository;
import org.apache.ignite.springdata22.repository.config.RepositoryConfig;

import com.example.ignitecachedurablememory.dto.HttpRequestData;


@RepositoryConfig(cacheName = "HttpRequestDataCache")
public interface HttpRequestDataRepository extends IgniteRepository<HttpRequestData, Long>
{

    HttpRequestData findByRequestId(Long requestId);

    /*@Query("SELECT p.* FROM Person p WHERE age > ?")
    List<Person> selectPersonsByAge(int age);*/
}
