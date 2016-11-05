package nl.inekevermeulen.coffee.counter.Controllers;

import nl.inekevermeulen.coffee.counter.Constants.CoffeeType;
import nl.inekevermeulen.coffee.counter.Model.Coffee;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/coffee")
public class CoffeeController {

    private static long idCounter = 1L;
    private static List<Coffee> coffeeList = makeCoffee();

    private static List<Coffee> makeCoffee() {

        //todo:get coffee from db

        List<Coffee> coffeeList = new ArrayList<>();

        Coffee livanto = new Coffee();
        livanto.setId(idCounter);
        livanto.setName("Livanto");
        livanto.setType(CoffeeType.ESPRESSO);
        livanto.setAmount(1);
        livanto.setRating(4);
        idCounter++;

        Coffee linizio = new Coffee();
        linizio.setId(idCounter);
        linizio.setName("Linizio");
        linizio.setType(CoffeeType.LUNGO);
        linizio.setAmount(1);
        linizio.setRating(4);
        idCounter++;

        Coffee arpeggio = new Coffee();
        arpeggio.setId(idCounter);
        arpeggio.setName("Arpeggio");
        arpeggio.setType(CoffeeType.RISTRETTO);
        arpeggio.setAmount(3);
        arpeggio.setRating(3);
        idCounter++;

        Coffee cappucino = new Coffee();
        cappucino.setId(idCounter);
        cappucino.setName("Cappucino");
        cappucino.setType(CoffeeType.WITH_MILK);
        cappucino.setAmount(1000);
        cappucino.setRating(5);
        idCounter++;

        coffeeList.add(livanto);
        coffeeList.add(linizio);
        coffeeList.add(arpeggio);
        coffeeList.add(cappucino);

        return coffeeList;
    }

    @RequestMapping(method = RequestMethod.GET)
    @CrossOrigin
    public List<Coffee> getCoffee() {
        return coffeeList;
    }

    @RequestMapping(method = RequestMethod.GET, path = "/{id}")
    @CrossOrigin
    public Coffee getCoffee(@PathVariable Long id) {
        Coffee selectedCoffee = null;

        for(Coffee coffee : coffeeList) {
            if(id.equals(coffee.getId())) {
                selectedCoffee = coffee;
            }
        }

        if(selectedCoffee == null) {
            throw new CoffeeNotFoundException(id);
        }

        return selectedCoffee;
    }

    @RequestMapping(method = RequestMethod.POST)
    @CrossOrigin
    public Coffee addCoffee(@RequestBody Coffee newCoffee) {
        newCoffee.setId(this.idCounter);
        coffeeList.add(newCoffee);

        return newCoffee;
    }

    @RequestMapping(method = RequestMethod.PUT)
    @CrossOrigin
    public Coffee updateCoffee(@RequestBody Coffee updatedCoffee) {
        Long id = updatedCoffee.getId();
        int index = 0;

        for(Coffee coffee : coffeeList) {
            if(id.equals(coffee.getId())) {
                index = coffeeList.indexOf(coffee);
            }
        }

        coffeeList.set(index, updatedCoffee);
        return updatedCoffee;
    }

    @RequestMapping(method = RequestMethod.DELETE, path = "/{id}")
    @CrossOrigin
    public ResponseEntity<?> deleteCoffee(@PathVariable Long id) {
        for(Coffee coffee : coffeeList) {
            if(id.equals(coffee.getId())) {
                coffeeList.remove(coffee);
            }
        }

        return new ResponseEntity<>(HttpStatus.OK);
    }


    @ResponseStatus(HttpStatus.NOT_FOUND)
    class CoffeeNotFoundException extends RuntimeException {

        public CoffeeNotFoundException(Long id) {
            super("Could not find coffee with id '" + id + "'.");
        }
    }
}
