package org.azgl.lbms.services;

import org.azgl.lbms.model.Loan;
import org.azgl.lbms.model.User;
import org.azgl.lbms.repository.LoanRepository;
import org.azgl.lbms.repository.UserRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final LoanRepository loanRepository;

    public UserService(UserRepository userRepository, LoanRepository loanRepository) {
        this.userRepository = userRepository;
        this.loanRepository = loanRepository;
    }

    public void addUser(User user) {
        userRepository.save(user); // Using the save() method from JpaRepository
    }

    //read
    public Optional<User> searchUser(String name){
        Optional<User> uopt = userRepository.findByUsername(name);
        if(uopt.isEmpty()){
            System.out.println("User not found");
        } else {
            System.out.println(uopt.get());
        }
        return uopt;
    }

    public Optional<User> searchUser(UUID uID){
        Optional<User> uopt = userRepository.findById(uID);
        if(uopt.isEmpty()){
            System.out.println("User not found");
        } else {
            System.out.println(uopt.get());
        }
        return uopt;
    }
    public List<User> getAllUsers(){
        return userRepository.findAll();
    }
    public List<Loan> displayLoans(String name){
        Optional<User> uopt = userRepository.findByUsername(name);
        if (uopt.isPresent()) {
            User user = uopt.get();
            List<Loan> loans = loanRepository.findByUser(user);
            return loans;
            //  add method to display a list of loans
        } else {
            System.out.println("No User found");
            return new ArrayList<Loan>();
        }
    }
    public List<Loan> displayLoans(UUID uID){
        Optional<User> uopt = userRepository.findById(uID);
        if (uopt.isPresent()) {
            User user = uopt.get();
            List<Loan> loans = loanRepository.findByUser(user);
            return loans;
            //  add method to display a list of loans
        } else {
            System.out.println("No User found");
            return new ArrayList<Loan>();
        }
    }

    //update
    public void updateUser(User u){
        userRepository.save(u);
    }

    //delete
    public void deleteUser(UUID uID){
        userRepository.deleteById(uID);
    }
    public void deleteUser(String name){
        Optional<User> uopt = userRepository.findByUsername(name);
        uopt.ifPresent(user -> userRepository.deleteById(user.getuID()));
    }
}
