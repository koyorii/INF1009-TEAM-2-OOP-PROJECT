package io.github.some_example_name.lwjgl3;

import java.util.List;

//inerface allows other classes to access the Entity list without knowing what is inside EntityManager
public interface getEntityList {
    public List<Entity> getEntities();
}
