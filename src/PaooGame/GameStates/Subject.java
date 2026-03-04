package PaooGame.GameStates;

import java.util.ArrayList;
import java.util.List;

/// Interfața Subject definește metodele pentru gestionarea Observatorilor și pentru notificare lor.
public interface Subject {
    void addObserver(Observer observer);

}