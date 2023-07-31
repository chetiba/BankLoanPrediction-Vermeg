package com.vermeg.chtiba.controllers;


import java.util.List;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.vermeg.chtiba.entities.Client;
import com.vermeg.chtiba.entities.authRequest;
import com.vermeg.chtiba.entities.authResponse;
import com.vermeg.chtiba.entities.registerRequest;
import com.vermeg.chtiba.repositories.ClientRepository;
import com.vermeg.chtiba.services.IService;
import com.vermeg.chtiba.services.Serviice;

@RestController
@RequestMapping({"/auth"})
@CrossOrigin(origins = {"*"}, maxAge = 3600L)
public class AppController {
    @Autowired
    private final ClientRepository cr;

    @Autowired
    private final Serviice s;

    @Autowired
    private final IService is;

    public AppController(ClientRepository cr, Serviice s, IService is) {
        this.cr = cr;
        this.s = s;
        this.is = is;
    }

    @PostMapping({"/Clients"})
    public Client createClient(@RequestBody Client client) {
        return this.is.createClient(client);
    }

    @GetMapping({"/Clients"})
    public List<Client> getAllClients() {
        return this.is.getAllClients();
    }

    @GetMapping({"/Clients/{id}"})
    public Optional<Client> getClientById(@PathVariable Long id) {
        return this.is.getClientById(id);
    }

    @DeleteMapping({"/Clients/{id}"})
    public void deleteClientById(@PathVariable Long id) {
        this.is.deleteClientById(id);
    }

    @PostMapping({"/login"})
    public ResponseEntity<authResponse> login(@RequestBody authRequest loginRequest) {
        try {
            String token = this.s.login(loginRequest.getEmail(), loginRequest.getPwd());
            Client authenticatedUser = this.s.getUserByEmail(loginRequest.getEmail());
            authResponse response = new authResponse();
            response.setToken(token);
            response.setId_client(Math.toIntExact(authenticatedUser.getId_client().longValue()));
            response.setWelcome("Welcome, " + authenticatedUser.getFirstname());
            response.setMessage("Authentication successful");
            response.setFirstname(authenticatedUser.getFirstname());
            response.setCin(authenticatedUser.getCin());
            return ResponseEntity.ok(response);
        } catch (AuthenticationException e) {
            authResponse errorResponse = new authResponse();
            errorResponse.setErrorMessage("Invalid email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
        }
    }

    @PostMapping({"/register"})
    public ResponseEntity<String> register(@RequestBody registerRequest registerRequest1) {
        try {
            String email = registerRequest1.getEmail();
            String password = registerRequest1.getPwd();
            String firstname = registerRequest1.getFirstname();
            String lastname = registerRequest1.getLastname();
            String cin = registerRequest1.getCin();
            String address = registerRequest1.getAddress();
            String phonenumber = registerRequest1.getPhonenumber();
            if (this.cr.existsByEmail(email))
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User with this email already exists");
            this.s.register(email, password, firstname, lastname, cin, address, phonenumber);
            return ResponseEntity.ok("User registered successfully!");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @PostMapping({"/logout/{id}"})
    public ResponseEntity<String> logout(@PathVariable Long id) {
        try {
            this.s.logout(id);
            return ResponseEntity.ok("User logged out successfully!");
        } catch (UsernameNotFoundException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }
}
