package com.kc.authenticator.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.kc.authenticator.model.TempDev;
import com.kc.authenticator.repository.TempDevRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class TempDevService {

    @Autowired
    private TempDevRepository tempDevRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    public List<TempDev> getAllDevs() {
        return tempDevRepository.findAll();
    }

    public TempDev getByDevEmail(String email) {
        return tempDevRepository.findByDevEmail(email);
    }

    public TempDev getDevById(String id) {
        Optional<TempDev> devOptional = tempDevRepository.findById(id);
        return devOptional.orElse(null);
    }

    public TempDev editDevById(String id, TempDev updatedDev) {
        Optional<TempDev> devOptional = tempDevRepository.findById(id);
        if (devOptional.isPresent()) {
            TempDev existingDev = devOptional.get();
            // Update tempDev fields with new values
            existingDev.setFirstName(updatedDev.getFirstName());
            existingDev.setLastName(updatedDev.getLastName());
            existingDev.setDevEmail(updatedDev.getDevEmail());
            existingDev.setPhone(updatedDev.getPhone());
            // Save the changes to the database
            return tempDevRepository.save(existingDev);
        } else {
            return null; // Return null if tempDev with the given ID does not exist
        }
    }

    public TempDev saveTempDev(TempDev tempDev) {
        // If there's an existing developer with the same email, set its ID to override
        TempDev existingDev = tempDevRepository.findByDevEmail(tempDev.getDevEmail());
        if (existingDev != null) {
            tempDev.setId(existingDev.getId());
        }

        // Encrypt the password before saving
        if (tempDev.getPassword() != null && !tempDev.getPassword().isEmpty()) {
            tempDev.setPassword(passwordEncoder.encode(tempDev.getPassword()));
        }

        return tempDevRepository.save(tempDev);
    }

    public void deleteDev(String id) {
        tempDevRepository.deleteById(id);
    }
}
