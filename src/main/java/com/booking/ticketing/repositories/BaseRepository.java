package com.booking.ticketing.repositories;

import com.booking.ticketing.configurations.InMemDBConfig;
import com.booking.ticketing.models.Identifable;
import com.booking.ticketing.repositories.exceptions.CollectionNotFoundException;
import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.core.type.filter.AssignableTypeFilter;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

@Component
@Slf4j
public class BaseRepository<T extends Identifable> {
    @Autowired
    private InMemDBConfig inMemDBConfig;
    private AtomicReference<Map<Class<?>, Map<String, T>>> collections = new AtomicReference<>(new HashMap<>());

    public void registerCollection(Class<?> classname) {
        collections.updateAndGet(existingCollections -> {
            if (!existingCollections.containsKey(classname)) {
                log.info(String.format("Registering Collection for %s", classname));
                existingCollections.put(classname, new HashMap<>());
            }
            return existingCollections;
        });
    }

    @PostConstruct
    public void autoRegisterCollections() {
        ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);

        scanner.addIncludeFilter(new AssignableTypeFilter(Identifable.class));

        String basePackage = inMemDBConfig.getRegistry();
        scanner.findCandidateComponents(basePackage).forEach(beanDefinition -> {
            try {
                Class<?> clazz = Class.forName(beanDefinition.getBeanClassName());
                registerCollection(clazz);
                log.debug("Registered collection for class: {}", clazz.getSimpleName());
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        });
    }

    public T save(T obj, Class<?> classname) {
        return collections.updateAndGet(existingCollections -> {
            Map<String, T> classCollection = existingCollections.get(classname);
            if (classCollection == null) {
                throw new CollectionNotFoundException(String.format("Collection is not registered for class %s", classname));
            }

            String id = obj.getId() != null ? obj.getId() : UUID.randomUUID().toString();
            if (classCollection.containsKey(id)) {
                obj.setUpdatedAt(Instant.now());
            } else {
                obj.setId(id);
                obj.setCreatedAt(Instant.now());
                obj.setUpdatedAt(Instant.now());
            }
            classCollection.put(id, obj);
            return existingCollections;
        }).get(classname).get(obj.getId());
    }

    public Optional<T> findById(String id, Class<?> className) {
        return Optional.ofNullable(collections.get().get(className).get(id));
    }

    public List<T> findByIds(Collection<String> ids, Class<?> className){
        List<T> result = new LinkedList<>();
        ids.forEach(id -> {
            result.add(collections.get().get(className).get(id));
        });
        return result;
    }

    public Collection<T> findAll(Class<?> classname) {
        return collections.get().getOrDefault(classname, Collections.emptyMap()).values();
    }
}
