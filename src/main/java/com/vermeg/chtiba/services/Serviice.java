package com.vermeg.chtiba.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.vermeg.chtiba.config.JwtService;
import com.vermeg.chtiba.entities.Client;
import com.vermeg.chtiba.repositories.ClientRepository;
@RequiredArgsConstructor
@Service
public class Serviice implements IService {
    @Autowired
    private final PasswordEncoder pe;

    @Autowired
    private final ClientRepository cr;

    @Autowired
    private final JwtService jwtService;

    @Autowired
    private final AuthenticationManager authenticationManager;


    public Client createClient(Client client) {
        String encodedPassword = this.pe.encode(client.getPwd());
        client.setPwd(encodedPassword);
        return (Client)this.cr.save(client);
    }

    public List<Client> getAllClients() {
        return this.cr.findAll();
    }

    public Optional<Client> getClientById(Long id) {
        return this.cr.findById(id);
    }

    public void deleteClientById(Long id) {
        this.cr.deleteById(id);
    }

    public String login(String email, String pwd) {
        try {
            Client client = this.cr.findByEmail1(email);
            if (client != null && this.pe.matches(pwd, client.getPwd())) {
                User user = new User(client.getEmail(), client.getPwd(), Collections.emptyList());
                String token = this.jwtService.generateToken((UserDetails)user);
                client.setToken(token);
                this.cr.save(client);
                return token;
            }
            throw new AuthenticationException("Invalid email or password") {

            };
        } catch (UsernameNotFoundException e) {
            e.printStackTrace();
            throw new AuthenticationException("Invalid email or password") {

            };
        }
    }

    public void register(String email, String pwd, String firstname, String lastname, String cin, String address, String phonenumber) {
        if (this.cr.existsByEmail(email))
            throw new IllegalArgumentException("Client with this email already exists");
        String encryptedPassword = this.pe.encode(pwd);
        Client client = new Client();
        client.setEmail(email);
        client.setPwd(encryptedPassword);
        client.setFirstname(firstname);
        client.setLastname(lastname);
        client.setCin(cin);
        client.setAddress(address);
        client.setPhonenumber(phonenumber);
        this.cr.save(client);
    }

    public void logout(Long id) {
        Optional<Client> ClientOptional = this.cr.findById(id);
        if (ClientOptional.isEmpty())
            throw new UsernameNotFoundException("Client with this id not found");
        Client Client = ClientOptional.get();
        Client.setToken(null);
        this.cr.save(Client);
    }

    public Client getUserByEmail(String email) {
        return this.cr.findByEmail1(email);
    }
}
