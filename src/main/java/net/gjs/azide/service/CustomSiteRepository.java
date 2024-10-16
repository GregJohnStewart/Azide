package net.gjs.azide.service;

import io.quarkus.hibernate.orm.panache.PanacheRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.ws.rs.core.SecurityContext;
import net.gjs.azide.entities.model.CustomSite;
import net.gjs.azide.entities.model.Person;
import org.eclipse.microprofile.jwt.JsonWebToken;

import java.util.Optional;

@ApplicationScoped
public class CustomSiteRepository implements PanacheRepository<CustomSite> {

}