package com.kc.authenticator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kc.authenticator.model.TempUser;
import com.kc.authenticator.repository.TempUserRepository;

import java.util.List;
import java.util.Optional;

@Service
public class TempUserService {

    @Autowired
    private TempUserRepository tempUserRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<TempUser> getAllUsers() {
        return tempUserRepository.findAll();
    }

    public TempUser getByEmail(String email) {
        return tempUserRepository.findByEmail(email);
    }

    public TempUser getById(String id) {
        return tempUserRepository.findById(id).orElse(null);
    }

    public TempUser getUserById(String id) {
        Optional<TempUser> userOptional = tempUserRepository.findById(id);
        return userOptional.orElse(null);
    }

    public TempUser editUserById(String id, TempUser updatedUser) {
        Optional<TempUser> userOptional = tempUserRepository.findById(id);
        if (userOptional.isPresent()) {
            TempUser existingUser = userOptional.get();
            // Update tempUser fields with new values
            existingUser.setFirstName(updatedUser.getFirstName());
            existingUser.setLastName(updatedUser.getLastName());
            existingUser.setEmail(updatedUser.getEmail());
            existingUser.setPhone(updatedUser.getPhone());
            // Save the changes to the database
            return tempUserRepository.save(existingUser);
        } else {
            return null; // Return null if tempUser with the given ID does not exist
        }
    }

    public TempUser saveTempUser(TempUser tempUser) {
        // If there's an existing user with the same email, set its ID to override
        TempUser existingUser = tempUserRepository.findByAppIdAndEmail(tempUser.getAppId(), tempUser.getEmail());
        if (existingUser != null) {
            tempUser.setId(existingUser.getId());
        }
        // Encrypt the password before saving
        if (tempUser.getPassword() != null && !tempUser.getPassword().isEmpty()) {
            tempUser.setPassword(passwordEncoder.encode(tempUser.getPassword()));
        }
        return tempUserRepository.save(tempUser);
    }

    public void deleteUser(String id) {
        tempUserRepository.deleteById(id);
    }
}
