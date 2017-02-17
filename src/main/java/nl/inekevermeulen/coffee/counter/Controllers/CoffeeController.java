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
        // The data in this class in stubbed for demo purposes,
        // you should of course not use this in production
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

    /**
     * Get all coffee data
     * @return A list of {@link Coffee} objects
     */
    @RequestMapping(method = RequestMethod.GET)
    //@CrossOrigin is used to prevent CrossOrigin errors when talking to the service from another server,
    //but will make it possible for everyone to talk to this service, so this should be used carefully
    @CrossOrigin
    public List<Coffee> getCoffee() {
        return coffeeList;
    }

    /**
     * Get coffee by id
     *
     * @param id the coffee id, passed as a path variable in the url, i.e. '/coffee/2'
     * @return The selected {@link Coffee} object
     */
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

    /**
     * Add a new coffee
     * @param newCoffee A new {@link Coffee} object, passed along as the request body (in json)
     *                  of the POST request
     * @return The newly created coffee type with an id
     */
    @RequestMapping(method = RequestMethod.POST)
    @CrossOrigin
    public Coffee addCoffee(@RequestBody Coffee newCoffee) {
        newCoffee.setId(this.idCounter);
        coffeeList.add(newCoffee);

        return newCoffee;
    }

    /**
     * Update an existing coffee
     * @param updatedCoffee The updated {@link Coffee} object,
     *                      passed along as the request body of the PUT method
     * @return The updated coffee type
     */
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

    /**
     * Delete a coffee
     * @param id The id of the coffee
     * @return An HttpStatus indicating that the coffee was the deleted successfully
     */
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


    /**
     * This exception is thrown when the coffee cannot be found,
     * it returns an http status of the type 'not found' to the client
     */
    @ResponseStatus(HttpStatus.NOT_FOUND)
    class CoffeeNotFoundException extends RuntimeException {

        public CoffeeNotFoundException(Long id) {
            super("Could not find coffee with id '" + id + "'.");
        }
    }
}
