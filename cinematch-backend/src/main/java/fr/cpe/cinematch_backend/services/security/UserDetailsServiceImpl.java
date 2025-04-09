package fr.cpe.cinematch_backend.services.security;

import fr.cpe.cinematch_backend.entities.AppUser;
import fr.cpe.cinematch_backend.repositories.AppUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

public class UserDetailsServiceImpl implements UserDetailsService
{
    @Autowired
    private AppUserRepository userRepository = null;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException
    {
        Optional<AppUser> optional = userRepository.findByUsername(username);

        if (optional.isEmpty())
        {
            throw new UsernameNotFoundException(username);
        }

        return optional.get();
    }
}