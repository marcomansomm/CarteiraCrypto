package com.example.cryptoapp.controller;

import com.example.cryptoapp.dto.CoinDTO;
import com.example.cryptoapp.entity.Coin;
import com.example.cryptoapp.repository.CoinRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.sql.Timestamp;
import java.util.List;

@RestController
@RequestMapping("/coins")
public class CoinController {

    @Autowired(required = true)
    private CoinRepository coinRepository;

    @GetMapping()
    public ResponseEntity<List<CoinDTO>> getCoins(){
        return new ResponseEntity<>(coinRepository.getAllCoins(), HttpStatus.OK);
    }

    @GetMapping("/consultas/{name}")
    public ResponseEntity<List<Coin>> getByName(@PathVariable String name){
        return new ResponseEntity<>(coinRepository.getByName(name), HttpStatus.OK);
    }

    @PostMapping("")
    public ResponseEntity<Coin> cadastrar(@RequestBody Coin coin){
        try{
            coin.setDateTime(new Timestamp(System.currentTimeMillis()));
            return new ResponseEntity<>(coinRepository.insert(coin), HttpStatus.CREATED);
        }catch(Exception e){
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        boolean response = coinRepository.delete(id);
        if(response){
            return new ResponseEntity<>(response, HttpStatus.OK);
        } else {
            return new ResponseEntity<>(HttpStatus.NO_CONTENT);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<Coin> update(@PathVariable long id, @RequestBody Coin coinAtualizado){
        coinAtualizado.setDateTime(new Timestamp(System.currentTimeMillis()));
        coinAtualizado.setId(id);
        return new ResponseEntity<>(coinRepository.update(coinAtualizado), HttpStatus.OK);
    }
}
