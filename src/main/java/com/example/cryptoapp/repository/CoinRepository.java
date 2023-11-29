package com.example.cryptoapp.repository;

import com.example.cryptoapp.dto.CoinDTO;
import com.example.cryptoapp.entity.Coin;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CoinRepository{

    private String INSERT = "";
    private String UPDATE = "";
    private String DELETE = "";

    private EntityManager entityManager;

    public CoinRepository(@Autowired EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public List<CoinDTO> getAllCoins(){
        String jpql = "SELECT new com.example.cryptoapp.dto.CoinDTO(c.name, SUM(c.quantity)) FROM Coin c GROUP BY c.name";
        TypedQuery<CoinDTO> query = entityManager.createQuery(jpql, CoinDTO.class);
        return query.getResultList();
    }

    @Transactional
    public Coin insert(Coin coin){
        entityManager.persist(coin);
        return coin;
    }

    @Transactional
    public Coin update(Coin coin){
        entityManager.merge(coin);
        return coin;
    }

    public List<Coin> getByName(String name){
        String jpql = "SELECT c FROM Coin c WHERE c.name like :name";
        TypedQuery<Coin> query = entityManager.createQuery(jpql, Coin.class);
        query.setParameter("name", "%" + name + "%");
        return query.getResultList();
    }

    public boolean delete(long id){
        Coin coin = entityManager.find(Coin.class, id);
        if(coin == null){
            throw new RuntimeException();
        } else {
            entityManager.remove(coin);
            return true;
        }
    }

}
