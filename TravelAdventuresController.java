package com.codecademy.plants.controllers;

import com.codecademy.plants.entities.Adventure;
import com.codecademy.plants.repositories.AdventureRepository;

import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

import java.util.List;
import java.util.Optional;
import java.lang.Iterable;

@RestController
@RequestMapping("/traveladventures")
public class TravelAdventuresController {

  private final AdventureRepository adventureRepository;

  public TravelAdventuresController(AdventureRepository adventureRepo) {
    this.adventureRepository = adventureRepo;
  }

  @GetMapping()
  public Iterable<Adventure> getAllAdventures() {
    Iterable<Adventure> adventures = this.adventureRepository.findAll();
    return adventures;
  }

  @GetMapping("/bycountry/{country}")
  public List<Adventure> getByCountry(@PathVariable String country) {
    return this.adventureRepository.findByCountry(country);
  }

  @GetMapping("/bystate")
  public List<Adventure> getByState(@RequestParam String state) {
    return this.adventureRepository.findByState(state);   
  }

  @PostMapping()
  public void createNewAdventure(@RequestBody Adventure adventure) {
    Integer id = adventure.getId();
    Optional<Adventure> optionalAdventure = this.adventureRepository.findById(id);
    if (optionalAdventure.isPresent()) {
      throw new ResponseStatusException(HttpStatus.CONFLICT, "Adventure already exists");
    }
    else {
      this.adventureRepository.save(adventure);
    }
  }

  @PutMapping("/{id}")
  public Adventure updateAdventure(@PathVariable Integer id, @RequestBody Adventure adventure) {
    Optional<Adventure> optionalAdventure = this.adventureRepository.findById(id);
    if (optionalAdventure.isPresent()) {
      Adventure adventureToUpdate = optionalAdventure.get();
      adventureToUpdate.setBlogCompleted(!(adventure.getBlogCompleted()));
      Adventure updatedAdventure = this.adventureRepository.save(adventureToUpdate);
      return updatedAdventure;
    }
    else {
      throw new ResponseStatusException(HttpStatus.NOT_FOUND);
    }
  }

  @DeleteMapping("/{id}")
  public void deleteAdventure(@PathVariable Integer id) {
    Optional<Adventure> optionalAdventure = this.adventureRepository.findById(id);
    if (optionalAdventure.isPresent()) {
      Adventure adventureToDelete = optionalAdventure.get();
      this.adventureRepository.delete(adventureToDelete);
    } 
  }

  
}
